package com.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.bson.types.ObjectId;

import staticData.StaticString;
import utils.FileUpload;
import bean.InActivity;
import bean.School;

import com.dao.CreateQueryFromBean;
import com.dao.DaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class ActivityUpdateAction extends ActionSupport {

	private String InActivityName;
	private String select;
	private Date deadLine;
	private Date startTime;
	private Date endTime;
	private String content;
	private String oldPictureUrl;
	private String oldAttachmentUrl;

	// �ϴ�ͼƬ
	private File filePicture;
	private String filePictureContentType;
	
	public String getFilePictureContentType() {
		return filePictureContentType;
	}

	public void setFilePictureContentType(String filePictureContentType) {
		this.filePictureContentType = filePictureContentType;
	}

	private String filePictureFileName;

	// �ϴ�����
	private File fileAttachment;
	private String fileAttachmentContentType;
	private String fileAttachmentFileName;
	
	private String attachmentSavePath;

	@Override
	public String execute() throws Exception {

		// ���ݻ���name���һ���ID
		ObjectId activityId = null;
		School school = new School();
		ObjectId schoolid = (ObjectId) ServletActionContext.getContext()
				.getSession().get("Organization_SchoolId");
		school.set_id(schoolid);
		BasicDBObject query = CreateQueryFromBean.EqualObj(school);
		BasicDBObject projection = new BasicDBObject();
		projection.put(StaticString.School_InActivityCategoty, 1);
		MongoCursor<Document> cursor = DaoImpl.GetSelectCursor(School.class,
				query, projection);
		ArrayList<Document> documents = null;
		while (cursor.hasNext()) {
			Document document = cursor.next();
			documents = (ArrayList<Document>) document.get("InActivityCategoty");
		}
		if (documents != null) {
			for (int i = 0; i < documents.size(); i++) {
				if (documents.get(i).getString("Name").equals(select)) {
					activityId = documents.get(i).getObjectId("_id");
					break;
				}
			}
		}

		InActivity newActivity = new InActivity();
		newActivity.setName(InActivityName);
		newActivity.setCategoryName(select);
		newActivity.setCategoryId(activityId);
		// ����û�����ѡ����ͼƬ
		if (filePicture != null) {
			if(filePictureContentType.equals("image/jpeg")||filePictureContentType.equals("image/png")
					||filePictureContentType.equals("image/bmp")){
				// ɾ��ԭ����ͼƬ��ֻ�к���,��Util.FileUpload��
				FileUpload.pictureDel(oldPictureUrl);
				// �����µ�ͼƬ
				String pictureSavePath = FileUpload.pictureUpload(filePicture);
				newActivity.setImgUrl(pictureSavePath);
			}else{
				addActionError("ͼƬ��ʽ��֧�֣�ֻ���ϴ�jpg,png,bmp���͵�ͼƬ");
				return INPUT;
			}
		}
		if (fileAttachment != null) {
			if(fileAttachmentContentType.equals("application/x-zip-compressed")){ //������
				// ɾ��ԭ�����ļ���ֻ�к�����Util.FileUpload��
				FileUpload.attachmentDel(oldAttachmentUrl);
				// �����µĸ���
				String attachmentSavePath = FileUpload.attachmentUpload(
						fileAttachment, fileAttachmentContentType,
						fileAttachmentFileName, getAttachmentSavePath());
				//�����¸�����url
				newActivity.setAttachmentUrl(attachmentSavePath);
				//�����¸�����name
				newActivity.setAttachmentName(fileAttachmentFileName);
			}else{
				addActionError("������ʽ��֧�֣�ֻ���ϴ�zip��ʽ�Ĵ���ļ�");
				return INPUT;
			}
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(1900, 1, 1, 0, 0, 0);
		if(deadLine == null){
			newActivity.setDeadLine(deadLine);
		} else{
			newActivity.setDeadLine(deadLine);
		}
		
		if (startTime != null && endTime != null) {
			newActivity.setRunTime(startTime.toString() + "~"
					+ endTime.toString());
		}
		newActivity.setContent(content);

		InActivity inActivity = new InActivity();
		String inActivityId =(String)ActionContext.getContext().getSession().get("SelectedActivityId");
		inActivity.set_id(new ObjectId(inActivityId));
		query = CreateQueryFromBean.EqualObj(inActivity);
		
		//update
		DaoImpl.update(query, newActivity, true);

		System.out.println("�ɹ���");

		return SUCCESS;
	}

	public String getInActivityName() {
		return InActivityName;
	}

	public void setInActivityName(String inActivityName) {
		InActivityName = inActivityName;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public File getFilePicture() {
		return filePicture;
	}

	public void setFilePicture(File filePicture) {
		this.filePicture = filePicture;
	}

	public File getFileAttachment() {
		return fileAttachment;
	}

	public void setFileAttachment(File fileAttachment) {
		this.fileAttachment = fileAttachment;
	}

	public String getFileAttachmentContentType() {
		return fileAttachmentContentType;
	}

	public void setFileAttachmentContentType(String fileAttachmentContentType) {
		this.fileAttachmentContentType = fileAttachmentContentType;
	}

	public String getFileAttachmentFileName() {
		return fileAttachmentFileName;
	}

	public void setFileAttachmentFileName(String fileAttachmentFileName) {
		this.fileAttachmentFileName = fileAttachmentFileName;
	}

	public String getOldPictureUrl() {
		return oldPictureUrl;
	}

	public void setOldPictureUrl(String oldPictureUrl) {
		this.oldPictureUrl = oldPictureUrl;
	}

	public String getAttachmentSavePath() {
		return ServletActionContext.getRequest().getRealPath(attachmentSavePath);
	}

	public void setAttachmentSavePath(String attachmentSavePath) {
		this.attachmentSavePath = attachmentSavePath;
	}

	public String getOldAttachmentUrl() {
		return oldAttachmentUrl;
	}

	public void setOldAttachmentUrl(String oldAttachmentUrl) {
		this.oldAttachmentUrl = oldAttachmentUrl;
	}

	public String getFilePictureFileName() {
		return filePictureFileName;
	}

	public void setFilePictureFileName(String filePictureFileName) {
		this.filePictureFileName = filePictureFileName;
	}

}
