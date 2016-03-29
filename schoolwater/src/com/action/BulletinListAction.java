package com.action;

import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;

import staticData.StaticString;
import bean.SystemNotice;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class BulletinListAction extends ActionSupport{
	private int pageNu=1;//Ĭ�ϵ�1ҳ
	private int pageTag;//�����һҳ-0����һҳ-1	
	private String oIdfirst;
	private String oIdlast;
	public static final int pageSize=10;//һҳ����ʾ��֪ͨ����
	public String getOIdfirst() {
		return oIdfirst;
	}
	public String getOIdlast() {
		return oIdlast;
	}
	public void setOIdfirst(String oIdfirst) {
		this.oIdfirst = oIdfirst;
	}
	public void setOIdlast(String oIdlast) {
		this.oIdlast = oIdlast;
	}
	public int getPageNu() {
		return pageNu;
	}
	public void setPageNu(int pageNu) {
		this.pageNu = pageNu;
	}
	
	public int getPageTag() {
		return pageTag;
	}
	public void setPageTag(int pageTag) {
		this.pageTag = pageTag;
	}	
	
	//��һҳ��һҳ��Ӧ����
	public String execute() throws Exception {
		ServletActionContext.getRequest().setAttribute("oIdfirst", oIdfirst);
		ServletActionContext.getRequest().setAttribute("oIdlast", oIdlast);
		ServletActionContext.getRequest().setAttribute("pageNu", pageNu);
		ServletActionContext.getRequest().setAttribute("pageTag", pageTag);
		return SUCCESS;
	}	
	
	
}
