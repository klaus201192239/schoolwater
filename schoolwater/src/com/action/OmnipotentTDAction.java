package com.action;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;

import utils.Util;
import bean.TableContentColumn;
import bean.TableContentInfo;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class OmnipotentTDAction extends ActionSupport{
	private String oldvalues;
	private String newvalues;
	private String deletevalues;
	private String tableId;
	
	private ObjectId schoolId=(ObjectId)ServletActionContext.getContext().getSession().get("Organization_SchoolId");
	
	//���ӻ���µķ���,
	public void add() throws Exception{	
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		//����ֵ����newval������
		String [] newval = Util.DoGetString(newvalues).trim().split(",");
		
		if(oldvalues.equals("")){	//ִ�в������
			System.out.println("����");
			@SuppressWarnings("unchecked")
			ArrayList<Document> arrayList=(ArrayList<Document>)ServletActionContext.getContext().getSession().get("TableInfoColumnsllz");
			TableContentInfo tableContentInfo = new TableContentInfo();
			tableContentInfo.set_id(new ObjectId());
			tableContentInfo.setStuId(new ObjectId("000000000000000000000000"));	//���޸�
			tableContentInfo.setSchoolId(schoolId);
			tableContentInfo.setIdCard(newval[1]);
			tableContentInfo.setCreateTime(new Date());
			tableContentInfo.setTableId(new ObjectId(tableId));
			tableContentInfo.setTableType(1); //�������ݱ�
			
			ArrayList<TableContentColumn> tableContentColumns = new ArrayList<TableContentColumn>();		
			for (int i = 0; i < newval.length; i++) {
				TableContentColumn tableContentColumn = new TableContentColumn();
				tableContentColumn.setName(arrayList.get(i).getString("Name"));
				tableContentColumn.setContent(newval[i]);
				tableContentColumns.add(tableContentColumn);
			}
			tableContentInfo.setTableContentColumn(tableContentColumns);
			DaoImpl.InsertWholeBean(tableContentInfo);	
			response.getWriter().print("����ɹ�");
		}else{	//ִ�и��²���
			System.out.println("����");
			TableContentInfo tableContentInfo = new TableContentInfo();
			tableContentInfo.setSchoolId(schoolId);
			tableContentInfo.setTableId(new ObjectId(tableId));
			tableContentInfo.setIdCard(Util.DoGetString(oldvalues));
			
			BasicDBObject query = CreateQueryFromBean.EqualObj(tableContentInfo);
			
			BasicDBObject p = new BasicDBObject();
			
			MongoCursor<Document> cursor = DaoImpl.GetSelectCursor(TableContentInfo.class, query, p);
			while(cursor.hasNext()){
				System.out.println(cursor.next());
			}
			
			@SuppressWarnings("unchecked")
			ArrayList<Document> arrayList=(ArrayList<Document>)ServletActionContext.getContext().getSession().get("TableInfoColumnsllz");
			TableContentInfo contentInfo = new TableContentInfo();
			ArrayList<TableContentColumn> tableContentColumns = new ArrayList<TableContentColumn>();		
			for (int i = 0; i < newval.length; i++) {
				TableContentColumn tableContentColumn = new TableContentColumn();
				tableContentColumn.setName(arrayList.get(i).getString("Name"));
				tableContentColumn.setContent(newval[i]);
				tableContentColumns.add(tableContentColumn);
			}
			System.out.println(query.toString());
			contentInfo.setTableContentColumn(tableContentColumns);
			DaoImpl.update(query, contentInfo, false,true);	//���²��ɹ�
			response.getWriter().println("���³ɹ�");
		}	
	}
	
	//ɾ���ķ���
	public void delete() throws Exception{
		System.out.println("ɾ��");
		System.out.println("Ҫɾ����ֵ"+Util.DoGetString(deletevalues).trim());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		String [] deleteval =Util.DoGetString(deletevalues).trim().split(",");
		for (int i = 0; i < deleteval.length; i++) {
			TableContentInfo tableContentInfo = new TableContentInfo();
			tableContentInfo.setIdCard(deleteval[i]);
			tableContentInfo.setSchoolId(schoolId);
			tableContentInfo.setTableId(new ObjectId(tableId));
			BasicDBObject query = CreateQueryFromBean.EqualObj(tableContentInfo);
			DaoImpl.DeleteDocment(TableContentInfo.class, query);
		}					
		response.getWriter().print("ɾ���ɹ�");
	}

	public String getOldvalues() {
		return oldvalues;
	}

	public void setOldvalues(String oldvalues) {
		this.oldvalues = oldvalues;
	}

	public String getNewvalues() {
		return newvalues;
	}

	public void setNewvalues(String newvalues) {
		this.newvalues = newvalues;
	}

	public String getDeletevalues() {
		return deletevalues;
	}

	public void setDeletevalues(String deletevalues) {
		this.deletevalues = deletevalues;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public ObjectId getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(ObjectId schoolId) {
		this.schoolId = schoolId;
	}
	
	public static void main(String[] args) {
		
		
		TableContentInfo tableContentInfo = new TableContentInfo();
		tableContentInfo.setStuId(new ObjectId("5615efdedc257111d0400ff0"));
		
		BasicDBObject query=null;
		
		try {
			query = CreateQueryFromBean.EqualObj(tableContentInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		TableContentInfo contentInfo = new TableContentInfo();
		ArrayList<TableContentColumn> tableContentColumns = new ArrayList<TableContentColumn>();		
		for (int i = 0; i < 5; i++) {
			TableContentColumn tableContentColumn = new TableContentColumn();
			tableContentColumn.setName("����");
			tableContentColumn.setContent("dad"+i);
			tableContentColumns.add(tableContentColumn);
		}
		
		contentInfo.setTableContentColumn(tableContentColumns);
		
		
		try {
			DaoImpl.update(query, contentInfo, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		BasicDBObject p = new BasicDBObject();
		
		try{
			MongoCursor<Document> cursor = DaoImpl.GetSelectCursor(TableContentInfo.class, query, p);
			while(cursor.hasNext()){
				System.out.println(cursor.next());
			}
		}catch(Exception e){
			
		}
		
		
		
		
	}
	
	
}
