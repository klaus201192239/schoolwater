package com.action;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;

import staticData.StaticString;
import utils.Util;
import bean.ActivityIntegral;
import bean.School;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class GradeTypeDao extends ActionSupport{
	private String activityName; //��ȡ�����
	private String timeStr;		//��ȡʱ���
	private int gradeInt;
	private String major;
	
	@Override
	public String execute() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		ObjectId acId =getActiId(); //��ȡ�ID
		
		//����ʱ��
		String [] time =timeStr.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.set( Integer.parseInt(time[0]), 0, 1, 0, 0, 0);
		Date date1 = calendar.getTime();
		calendar.set(Integer.parseInt(time[1]), 0, 1, 0, 0, 0);
		Date date2 = calendar.getTime();
		
		ArrayList<ActivityIntegral> integrals = new ArrayList<ActivityIntegral>();
		if("undefined".equals(Util.DoGetString(major))){ //��ͨ����Ա
			System.out.println("��ͨ����Ա");
			ActivityIntegral activityIntegral = new ActivityIntegral(); 
			activityIntegral.setYear(gradeInt);
			activityIntegral.setCategoryId(acId);
			String organizationId =(String)ServletActionContext.getContext().getSession().get("Organization_id");
			activityIntegral.setOrganizationId(new ObjectId(organizationId));
			BasicDBObject query = CreateQueryFromBean.EqualObj(activityIntegral);
			BasicDBObject projection = new BasicDBObject();
			MongoCursor<Document> cursor =DaoImpl.GetSelectCursor(ActivityIntegral.class, query, projection);	
			while(cursor.hasNext()){
				Document document = cursor.next();
				Date date=(Date)document.get("CreateTime");
				if(date.compareTo(date1)>=0&&date.compareTo(date2)<=0){
					ActivityIntegral activityIntegral1 = new ActivityIntegral();
					activityIntegral1.setName(document.getString("Name"));
					activityIntegral1.setIdCard(document.getString("IdCard"));
					activityIntegral1.setMajor(document.getString("Major"));
					activityIntegral1.setLevel(document.getString("Level"));
					activityIntegral1.setScope(document.getString("Scope"));
					activityIntegral1.setThingName(document.getString("ThingName"));
					activityIntegral1.setGrade(document.getDouble("Grade"));
					activityIntegral1.setRemark(document.getString("Remark"));
					integrals.add(activityIntegral1);
				}
			}
		}else{ //��������Ա
			ActivityIntegral activityIntegral = new ActivityIntegral(); 
			activityIntegral.setYear(gradeInt);
			activityIntegral.setCategoryId(acId);
			if(!"������רҵ".equals(Util.DoGetString(major))){
				activityIntegral.setMajor(Util.DoGetString(major));
			}
			ObjectId schoolId = (ObjectId)ServletActionContext.getContext().getSession().get("Organization_SchoolId");
			activityIntegral.setSchoolId(schoolId);
			BasicDBObject query = CreateQueryFromBean.EqualObj(activityIntegral);
			BasicDBObject projection = new BasicDBObject();
			MongoCursor<Document> cursor =DaoImpl.GetSelectCursor(ActivityIntegral.class, query, projection);	
			while(cursor.hasNext()){
				Document document = cursor.next();
				Date date=(Date)document.get("CreateTime");
				if(date.compareTo(date1)>=0&&date.compareTo(date2)<=0){
					ActivityIntegral activityIntegral1 = new ActivityIntegral();
					activityIntegral1.setName(document.getString("Name"));
					activityIntegral1.setIdCard(document.getString("IdCard"));
					activityIntegral1.setMajor(document.getString("Major"));
					activityIntegral1.setLevel(document.getString("Level"));
					activityIntegral1.setScope(document.getString("Scope"));
					activityIntegral1.setThingName(document.getString("ThingName"));
					activityIntegral1.setGrade(document.getDouble("Grade"));
					activityIntegral1.setRemark(document.getString("Remark"));
					integrals.add(activityIntegral1);
				}
			}
		}
		
		//����excel��
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet=workbook.createSheet();
		HSSFRow row=sheet.createRow(0);
		HSSFCell cell=null;
				
		cell=row.createCell(0); //��һ�еĵ�i����Ԫ��
		cell.setCellValue("���"); //���õ�Ԫ������
		sheet.setColumnWidth(0, 2*2*256);
		cell=row.createCell(1);
		cell.setCellValue("����");
		sheet.setColumnWidth(1, 2*2*256);
		cell=row.createCell(2);
		cell.setCellValue("ѧ��");
		sheet.setColumnWidth(2, 2*2*256);
		cell=row.createCell(3);
		cell.setCellValue("רҵ");
		sheet.setColumnWidth(3, 2*2*256);
		cell=row.createCell(4);
		cell.setCellValue("������");
		sheet.setColumnWidth(4, 4*2*256);
		cell=row.createCell(5);
		cell.setCellValue("�񽱼���");
		sheet.setColumnWidth(5, 4*2*256);
		cell=row.createCell(6);
		cell.setCellValue("������");
		sheet.setColumnWidth(6, 4*2*256);
		cell=row.createCell(7);
		cell.setCellValue("��û���");
		sheet.setColumnWidth(7, 4*2*256);
		cell=row.createCell(8);
		cell.setCellValue("��ע��Ϣ");
		sheet.setColumnWidth(8, 4*2*256);
				
		//׷������
		for(int i = 1 ; i<(integrals.size()+1) ; i++){
			HSSFRow nextRow=sheet.createRow(i);
			HSSFCell cell1= null;
			//����
			cell1=nextRow.createCell(0);
			cell1.setCellValue(i);
			cell1=nextRow.createCell(1);
			cell1.setCellValue(integrals.get(i-1).getName());
			cell1=nextRow.createCell(2);
			cell1.setCellValue(integrals.get(i-1).getIdCard());
			cell1=nextRow.createCell(3);
			cell1.setCellValue(integrals.get(i-1).getMajor());
			cell1=nextRow.createCell(4);
			cell1.setCellValue(integrals.get(i-1).getScope());
			cell1=nextRow.createCell(5);
			cell1.setCellValue(integrals.get(i-1).getLevel());
			cell1=nextRow.createCell(6);
			cell1.setCellValue(integrals.get(i-1).getThingName());
			cell1=nextRow.createCell(7);
			cell1.setCellValue(integrals.get(i-1).getGrade());
			cell1=nextRow.createCell(8);
			cell1.setCellValue(integrals.get(i-1).getRemark());
			}
			response= ServletActionContext.getResponse();
			response.setContentType("application/vnd.ms-excel"); //�ĳ����excel�ļ�
			response.setHeader("Content-disposition","attachment; filename=grade.xls");
			OutputStream output =response.getOutputStream();  
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);   	
			try {		
				workbook.write(bufferedOutPut);
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}finally{
				workbook.close();
				bufferedOutPut.close();
			}
			return null;
	}

	//���ݻ����������ֻ�ȡID
	@SuppressWarnings("unchecked")
	private ObjectId getActiId() throws Exception{
		ObjectId id= null;		
		School school = new School();
		school.set_id((ObjectId)ServletActionContext.getContext().getSession().get("Organization_SchoolId"));
		BasicDBObject query = CreateQueryFromBean.EqualObj(school);
		BasicDBObject projection = new BasicDBObject();
		projection.put(StaticString.School_InActivityCategoty, 1);
		MongoCursor<Document> cursor =DaoImpl.GetSelectCursor(School.class, query, projection);
		ArrayList<Document> arrayList = null;
		if(cursor.hasNext()){
			Document document =cursor.next();
			arrayList =	(ArrayList<Document>)document.get("InActivityCategoty");
		}	
		for (int i = 0; i < arrayList.size(); i++) {
			if(arrayList.get(i).getString("Name").equals(Util.DoGetString(activityName))){
				id=(ObjectId)arrayList.get(i).get("_id");
				break;
			}
		}		
		return id;
	}
	
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}


	public int getGradeInt() {
		return gradeInt;
	}


	public void setGradeInt(int gradeInt) {
		this.gradeInt = gradeInt;
	}


	public String getMajor() {
		return major;
	}


	public void setMajor(String major) {
		this.major = major;
	}
}
