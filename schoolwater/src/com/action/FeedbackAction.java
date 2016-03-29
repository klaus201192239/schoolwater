package com.action;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;

import staticData.StaticString;
import bean.School;
import bean.Suggestion;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class FeedbackAction extends ActionSupport{
	private String content;

	public void fankui() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		ObjectId organization_SchoolId=(ObjectId)ServletActionContext.getContext().getSession().get("Organization_SchoolId");
		
		//��ѯ���ݿ⣬��ȡ��Ӧ��ѧУID
		School school = new School();
		school.set_id(organization_SchoolId);
		BasicDBObject ob=CreateQueryFromBean.EqualObj(school);
		
		BasicDBObject projection=new BasicDBObject();
		projection.put(StaticString.School_Name,1);
		MongoCursor<Document> mc = DaoImpl.GetSelectCursor(School.class,ob,projection);
		String schoolname=null;
		if(mc.hasNext()){
			Document d =mc.next();
			schoolname=(String)d.get("Name");
		}
		String manager_Name=(String)ServletActionContext.getContext().getSession().get("Manager_Name");
		Date d=new Date();
		//�������ݿ�
		Suggestion suggestion=new Suggestion();
		suggestion.set_id(new ObjectId());
		suggestion.setContent(getContent());
		suggestion.setReleaseTime(d);
		suggestion.setFrom("�����ˣ�"+schoolname+"	"+manager_Name);
		suggestion.setDealPerson(new ObjectId());//δ����֮ǰ��������һ��ֵ
		suggestion.setDealTime(d);//δ����֮ǰ���óɵ�ǰʱ��
		suggestion.setSolution("δ����");
		suggestion.setState(0);//δ����
		DaoImpl.InsertWholeBean(suggestion);
		response.getWriter().print("true");
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
