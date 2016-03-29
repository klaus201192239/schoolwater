package com.action;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.omg.CORBA.Request;

import staticData.StaticString;
import utils.Util;
import bean.InActivity;
import bean.TableContentColumn;
import bean.TableContentInfo;
import bean.TableInfo;
import bean.TableInfoColumn;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;

public class ActivityCreateT1 extends ActionSupport {

	private String oldvalues;
	private String newvalues;
	private String deletevalues;
	private String tableId;
	

	private String message;

	private String activity_id = (String)ServletActionContext.getContext()
			.getSession().get("SelectedActivityId");
	
	public String createOrNot() throws Exception{

		TableInfo tInfo1=new TableInfo();
		tInfo1.setActivityId(new ObjectId(activity_id));
		tInfo1.setType(0);
		BasicDBObject query=CreateQueryFromBean.EqualObj(tInfo1);
		Long num=(Long)DaoImpl.GetSelectCount(TableInfo.class, query);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		response.getWriter().print(num.toString());
		return null;
	}
	public String create() throws Exception {

		message = Util.DoGetString(message);
		String[] msg = message.split(";");

		System.out.println(message);

		// ��ѯ��ǰ��Ƿ��б�����

		InActivity inActivity = new InActivity();
		inActivity.set_id(new ObjectId(activity_id));
		BasicDBObject q = CreateQueryFromBean.EqualObj(inActivity);
		BasicDBObject p = new BasicDBObject();
		MongoCursor<Document> mc = DaoImpl.GetSelectCursor(InActivity.class, q,
				p);
		p.put(StaticString.InActivity_Name, 1);
		String inActivityName = null;
		if (mc.hasNext()) {
			Document d = mc.next();
			inActivityName = (String) d.get("Name");
			System.out.println(inActivityName);
		}
		TableInfo tableInfo = new TableInfo();
		tableInfo.set_id(new ObjectId());
		tableInfo.setName(new String(inActivityName + "������"));
		tableInfo.setActivityId(new ObjectId(activity_id));
		tableInfo.setType(0);
		String organizationId = (String) ServletActionContext.getContext()
				.getSession().get("Organization_id");
		tableInfo.setOrganizationId(new ObjectId(organizationId));
		tableInfo.setCreateTime(new Date());
		ArrayList<TableInfoColumn> list = new ArrayList<TableInfoColumn>();
		for (int i = 1; i < msg.length; i++) {
			TableInfoColumn tableInfoColumn = new TableInfoColumn();
			String[] sonMsg = new String[3];
			sonMsg = msg[i].split(",");

			tableInfoColumn.setName(sonMsg[0].trim());
			tableInfoColumn.setLength(Integer.parseInt(sonMsg[1].trim()));
			if (sonMsg[2].trim().equals("1")) {
				tableInfoColumn.setChoose(true);
			} else {
				tableInfoColumn.setChoose(false);
			}
			list.add(tableInfoColumn);

		}

		tableInfo.setTableInfoColumn(list);
		DaoImpl.InsertWholeBean(tableInfo);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		response.getWriter().print("���������ɹ�!");
		
		return null;
	}

	// �ڱ���������Ա ���޸����г�Ա��Ϣ ͨ�����水ť����
	public void add() throws Exception {

		System.out.println(tableId);
		System.out.println(oldvalues);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		ObjectId schoolId = (ObjectId) ServletActionContext.getContext()
				.getSession().get("Organization_SchoolId");
		// ��ȡ��ֵ����
		String[] newval = Util.DoGetString(newvalues).trim().split(",");

		@SuppressWarnings("unchecked")
		ArrayList<String> columNameList = (ArrayList<String>) ServletActionContext
				.getContext().getSession().get("TableInfoColumnName");// ��ȡ���ֶ���

		ArrayList<TableContentColumn> tableContentColumns = new ArrayList<TableContentColumn>();

		// oldvalues ����ѧ�� ��ֵ˵����Ҫ�޸ĵ����� û��˵��ֱ������µ�
		if (oldvalues.equals("0")) {// ��������Ŀ
			System.out.println("����");
			// �����µı���Ŀ
			TableContentInfo tableContentInfo = new TableContentInfo();
			tableContentInfo.set_id(new ObjectId());
			tableContentInfo.setStuId(new ObjectId("000000000000000000000000")); // ���޸�
			tableContentInfo.setSchoolId(schoolId);
			System.out.println(schoolId);
			tableContentInfo.setIdCard(newval[1]);
			tableContentInfo.setCreateTime(new Date());
			tableContentInfo.setTableId(new ObjectId(tableId));
			tableContentInfo.setTableType(0); // ������

			System.out.println(newval.length);
			for (int i = 0; i < newval.length; i++) {
				TableContentColumn tableContentColumn = new TableContentColumn();
				tableContentColumn.setName(columNameList.get(i));
				tableContentColumn.setContent(newval[i]);
				tableContentColumns.add(tableContentColumn);
			}
			TableContentColumn tableContentColumn = new TableContentColumn();
			tableContentColumn.setName("SignIn");//Ĭ��ֵΪ0 δǩ��
			tableContentColumn.setContent("0");
			tableContentColumns.add(tableContentColumn);
			tableContentInfo.setTableContentColumn(tableContentColumns);
			DaoImpl.InsertWholeBean(tableContentInfo);
			response.getWriter().print("��ӳɹ�");
		} else {
			// ����Ӧ�ļ�¼
			System.out.println("����");
			TableContentInfo tableContentInfo = new TableContentInfo();
			tableContentInfo.setSchoolId(schoolId);
			tableContentInfo.setTableId(new ObjectId(tableId));
			tableContentInfo.setIdCard(Util.DoGetString(oldvalues));

			BasicDBObject query = CreateQueryFromBean
					.EqualObj(tableContentInfo);

			BasicDBObject p = new BasicDBObject();

			MongoCursor<Document> cursor = DaoImpl.GetSelectCursor(
					TableContentInfo.class, query, p);
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
			TableContentInfo contentInfo = new TableContentInfo();
			for (int i = 0; i < newval.length; i++) {
				TableContentColumn tableContentColumn = new TableContentColumn();
				tableContentColumn.setName(columNameList.get(i));
				tableContentColumn.setContent(newval[i]);
				tableContentColumns.add(tableContentColumn);
			}

			System.out.println(query.toString());
			contentInfo.setTableContentColumn(tableContentColumns);
			DaoImpl.update(query, contentInfo, false, true); // ���²��ɹ�
			response.getWriter().println("���³ɹ�");
		}
	}

	// ɾ��
	public void delete() throws Exception {
		ObjectId schoolId = (ObjectId) ServletActionContext.getContext()
				.getSession().get("Organization_SchoolId");
		System.out.println("ɾ��");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		String[] deleteval = deletevalues.split(",");
		for (int i = 0; i < deleteval.length; i++) {
			System.out.println("ɾ����ֵ��" + deleteval[i]);
			TableContentInfo tableContentInfo = new TableContentInfo();
			tableContentInfo.setIdCard(deleteval[i]);
			tableContentInfo.setSchoolId(schoolId);
			System.out.println("schoolId:" + schoolId);
			tableContentInfo.setTableId(new ObjectId(tableId));
			System.out.println("tableId:" + tableId);
			BasicDBObject query = CreateQueryFromBean
					.EqualObj(tableContentInfo);

			DaoImpl.DeleteDocment(TableContentInfo.class, query);
		}
		response.getWriter().print("ɾ���ɹ�");
	}

	// ����excel
	@SuppressWarnings("unchecked")
	public void export() throws Exception {
		
		// ���ұ�ͷ
		TableInfo tableInfo = new TableInfo();
		tableInfo.set_id(new ObjectId(tableId));
		BasicDBObject query = CreateQueryFromBean.EqualObj(tableInfo);
		BasicDBObject projection = new BasicDBObject();
		projection.put("TableInfoColumn", 1);
		MongoCursor<Document> mc = DaoImpl.GetSelectCursor(TableInfo.class,
				query, projection);
		Document document = mc.next();
		// ��ȡ�����

		InActivity inActivity = new InActivity();
		inActivity.set_id(new ObjectId(activity_id));
		BasicDBObject query2 = CreateQueryFromBean.EqualObj(inActivity);
		BasicDBObject project2 = new BasicDBObject();
		project2.put(StaticString.InActivity_Name, 1);
		MongoCursor<Document> cur = DaoImpl.GetSelectCursor(InActivity.class,
				query2, project2);
		Document document2 = cur.next();

		String activityName = (String) document2
				.get(StaticString.InActivity_Name);

		System.out.println(activityName+"+++++++++++++++++++++++++++++++++++");
		// ��ȡ��ͷ
		ArrayList<String> columnName = new ArrayList<String>();
		ArrayList<Document> documents = (ArrayList<Document>) document
				.get("TableInfoColumn");
		columnName.clear();
		for (int i = 0; i < documents.size(); i++) {
			columnName.add((String) documents.get(i).get("Name"));
			System.out.println(documents.get(i).get("Name"));
		}
		// ��ȡ����

		TableContentInfo tableContentInfo = new TableContentInfo();
		tableContentInfo.setTableId(new ObjectId(tableId));
		BasicDBObject q = CreateQueryFromBean.EqualObj(tableContentInfo);
		BasicDBObject p = new BasicDBObject();
		p.put("TableContentColumn", 1);
		MongoCursor<Document> cursor = DaoImpl.GetSelectCursor(
				TableContentInfo.class, q, p);
		ArrayList<Document> tableContentInfos = new ArrayList<Document>();
		int test = 0;
		while (cursor.hasNext()) {
			tableContentInfos.add(cursor.next());

		}
		// ��ȡÿһ�� �����ݴ浽��ά����contextColumns��
		ArrayList<Document> tableContent = new ArrayList<Document>();
		ArrayList<ArrayList<String>> contextColumns = new ArrayList<ArrayList<String>>();

		contextColumns.clear();
		for (int i = 0; i < tableContentInfos.size(); i++) {
			tableContent.clear();
			tableContent = (ArrayList<Document>) tableContentInfos.get(i).get(
					"TableContentColumn");
			ArrayList<String> content = new ArrayList<String>();
			content.clear();
			for (int j = 0; j < columnName.size(); j++) {
				System.out.println(j);
				for (int k = 0; k < tableContent.size(); k++) {
					if (columnName.get(j).equals(
							(String) tableContent.get(k).get("Name"))) {
						content.add((String) tableContent.get(k).get("Content"));
						System.out.println(tableContent.get(k).get("Content"));
					}
				}
			}
			contextColumns.add(content);
		}

		for (int i = 0; i < contextColumns.size(); i++) {
			for (int j = 0; j < contextColumns.get(i).size(); j++) {
				System.out.println(contextColumns.get(i).get(j));
			}
		}
		// ����excel��
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = null;
		for (int i = 0; i < columnName.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(columnName.get(i));
			System.out.println(columnName.get(i));
		}
		System.out.println("test1");
		// ׷������
		for (int i = 1; i < contextColumns.size() + 1; i++) {
			System.out.println(contextColumns.size() + 1);
			HSSFRow nextRow = sheet.createRow(i);
			HSSFCell c = null;
			for (int j = 0; j < contextColumns.get(i - 1).size(); j++) {
				System.out.println(contextColumns.get(i - 1).size());
				c = nextRow.createCell(j);
				c.setCellValue(contextColumns.get(i - 1).get(j));
				System.out.println(contextColumns.get(i - 1).get(j));
			}
		}

		System.out.println("test2");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/vnd.ms-excel"); // �ĳ����excel�ļ�
		response.setHeader("Content-disposition", "attachment; filename="
				+ Util.DoGetExportName(activityName+"������") + ".xls");
		OutputStream output = response.getOutputStream();
		BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
		try {
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		} finally {
			workbook.close();
			bufferedOutPut.close();
		}

		System.out.println("test3");

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
}
