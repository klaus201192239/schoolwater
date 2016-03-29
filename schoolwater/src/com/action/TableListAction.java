package com.action;

import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;

import staticData.StaticString;
import bean.TableInfo;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class TableListAction extends ActionSupport{
	private int pageNu=1;//Ĭ�ϵ�1ҳ
	private String noticeidL;//��¼һҳ�����һ��֪ͨ��ID
	private String noticeidF;//��¼һҳ�е�һ��֪ͨ��ID
	private ArrayList<Document> list=new ArrayList<Document>();//�洢mongodb�ĵ�
	private int pageTag;//�����һҳ-0����һҳ-1	
	
	public static final int pageSize=10;//һҳ����ʾ��֪ͨ����
	//��һҳ��Ӧ����
	public String pre() throws Exception {
		setPageNu(pageNu-1);
		
		BasicDBObject projection=new BasicDBObject();
		projection.put(StaticString.TableInfo_CreateTime, 1);
    	projection.put(StaticString.TableInfo_Name, 1);
    	projection.put(StaticString.TableInfo_id, 1);
    	projection.put(StaticString.TableInfo_OrganizationId, 1);
		
		TableInfo sn=new TableInfo();
		String orgaId=(String)ServletActionContext.getContext().getSession().get("Organization_id");
		sn.set_id(new ObjectId(getNoticeidF()));
		BasicDBObject query=CreateQueryFromBean.GtObj(sn);		
		BasicDBObject sort=new BasicDBObject();
		sort.put(StaticString.TableInfo_id, 1);		
		MongoCursor<Document> mc=DaoImpl.GetSelectCursor(TableInfo.class, query, sort, projection);
		Document document =null;
		while(mc.hasNext()){
			document=mc.next();
			if(orgaId.equals(document.get("OrganizationId").toString())){
				list.add(document);
				if(list.size()>=pageSize){
					break;
				}
			}
		}
		ServletActionContext.getRequest().setAttribute("data", list);
		ServletActionContext.getRequest().setAttribute("pageTag", pageTag);
		return SUCCESS;
	}
	//��һҳ��Ӧ����
	public String nex() throws Exception {
		setPageNu(pageNu+1);
		
		BasicDBObject projection=new BasicDBObject();
    	projection.put(StaticString.TableInfo_CreateTime, 1);
    	projection.put(StaticString.TableInfo_Name, 1);
    	projection.put(StaticString.TableInfo_id, 1);
    	projection.put(StaticString.TableInfo_OrganizationId, 1);
		
		TableInfo sn=new TableInfo();
		String orgaId=(String)ServletActionContext.getContext().getSession().get("Organization_id");		
		sn.set_id(new ObjectId(getNoticeidL()));
		BasicDBObject query=CreateQueryFromBean.LtObj(sn);
		
		BasicDBObject sort=new BasicDBObject();
		sort.put(StaticString.TableInfo_id, -1);
		MongoCursor<Document> mc=DaoImpl.GetSelectCursor(TableInfo.class, query, sort, projection);
		Document document =null;
		while(mc.hasNext()){
			document=mc.next();
			if(orgaId.equals(document.get("OrganizationId").toString())){
				list.add(document);
				if(list.size()>=pageSize){
					break;
				}
			}
		}
		ServletActionContext.getRequest().setAttribute("data", list);
		ServletActionContext.getRequest().setAttribute("pageTag", pageTag);
		return SUCCESS;
	}	
	
	public int getPageNu() {
		return pageNu;
	}
	public void setPageNu(int pageNu) {
		this.pageNu = pageNu;
	}
	public String getNoticeidL() {
		return noticeidL;
	}
	public void setNoticeidL(String noticeidL) {
		this.noticeidL = noticeidL;
	}
	public String getNoticeidF() {
		return noticeidF;
	}
	public void setNoticeidF(String noticeidF) {
		this.noticeidF = noticeidF;
	}
	public ArrayList<Document> getList() {
		return list;
	}
	public void setList(ArrayList<Document> list) {
		this.list = list;
	}
	public int getPageTag() {
		return pageTag;
	}
	public void setPageTag(int pageTag) {
		this.pageTag = pageTag;
	}
}
