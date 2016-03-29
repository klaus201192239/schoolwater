package com.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;

import staticData.StaticString;
import bean.ActivityCallOver;
import bean.ActivityIntegral;
import bean.InActivity;
import bean.TableContentInfo;
import bean.TableInfo;
import bean.TeamInfo;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class ActivityDeleteAction extends ActionSupport {

	
	public void deleteAct() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		String activityId=(String)ServletActionContext.getContext().getSession().get("SelectedActivityId");
		ObjectId oid=new ObjectId(activityId);
		InActivity inActivity = new InActivity();
		inActivity.set_id(oid);
		BasicDBObject query = CreateQueryFromBean.EqualObj(inActivity);
		//ɾ���
		DaoImpl.DeleteDocment(InActivity.class, query);
		//ɾ������������������ı�
		TableInfo tableInfo = new TableInfo();
		tableInfo.setActivityId(oid);
		query = CreateQueryFromBean.EqualObj(tableInfo);
		BasicDBObject projection = new BasicDBObject();
		MongoCursor<Document> mCursor = DaoImpl.GetSelectCursor(TableInfo.class, query, projection);
		while(mCursor.hasNext()) {
			Document document = mCursor.next();
			ObjectId tableId = (ObjectId) document.get(StaticString.TableInfo_id);
			TableContentInfo tableContentInfo = new TableContentInfo();
			tableContentInfo.setTableId(tableId);
			BasicDBObject q = CreateQueryFromBean.EqualObj(tableContentInfo);
			DaoImpl.DeleteDocment(TableContentInfo.class, q);
		}
		DaoImpl.DeleteDocment(TableInfo.class, query);
		//ɾ���Ŷ���Ϣ
		
		TeamInfo teamInfo=new TeamInfo();
		teamInfo.setActivityId(oid);
		query = CreateQueryFromBean.EqualObj(teamInfo);
		DaoImpl.DeleteDocment(TeamInfo.class, query);
		
		//�����ߵ�����Ϣ
		ActivityCallOver acover=new ActivityCallOver();
		acover.setActivityId(oid);
		query = CreateQueryFromBean.EqualObj(acover);
		DaoImpl.DeleteDocment(ActivityCallOver.class, query);
		
		//��Ϊ����
		ActivityIntegral aIntegral=new ActivityIntegral();
		aIntegral.setActivityId(oid);
		query = CreateQueryFromBean.EqualObj(aIntegral);
		DaoImpl.DeleteDocment(ActivityIntegral.class, query);
		
		//ɾ��ͼƬ��������
		response.getWriter().print("true");
	}
}
