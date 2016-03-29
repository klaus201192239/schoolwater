package com.action;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.bson.types.ObjectId;

import utils.FileUpload;
import utils.UtilCommon;

import com.opensymphony.xwork2.ActionSupport;

public class UploadUtilAction extends ActionSupport implements
		ServletResponseAware {
	private File fileField; // ��JSP��input���nameͬ��
	private File fileactField; // ��JSP��input���nameͬ��
	private String imageUrl;
	private String attachmentUrl;
	private String fileRealName;
	private HttpServletResponse response;
	// ��������õ��ļ���,��������File������+FileName
	// ��˴�Ϊ 'fileupload' + 'FileName' = 'fileuploadFileName'
	private String textfield; // �ϴ�����ѧУlogo������
	private String textactfield; // �ϴ����Ļlogo������

	public String upImg() throws IOException {
		ObjectId orgaId = (ObjectId)ServletActionContext.getContext().getSession().get("Organization_SchoolId");
		
		String extName = ""; // �����ļ���չ��
		String newFileName = ""; // �����µ��ļ���
		//String nowTimeStr = ""; // ���浱ǰʱ��
		PrintWriter out = null;
		SimpleDateFormat sDateFormat;
		Random r = new Random();
		String savePath = FileUpload.pictureWebUpload(fileField);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8"); // ��أ���ֹ�����ļ���������
		// ��ȡ��չ��
		if (textfield.lastIndexOf(".") >= 0) {
			extName = textfield.substring(textfield
					.lastIndexOf("."));
		}
		try {
			out = response.getWriter();
			newFileName = orgaId.toString() + ".schlogo" + extName; // �ļ��������������
			String filePath = savePath + newFileName;
			filePath = filePath.replace("\\", "/");
			//����ϴ����Ƿ���ͼƬ
			if (UtilCommon.checkIsImage(extName)) {
				FileUtils.copyFile(fileField, new File(filePath));
			} else {
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String upactImg() throws IOException {
		ObjectId orgaId = (ObjectId)ServletActionContext.getContext().getSession().get("Organization_SchoolId");
		String extName = ""; // �����ļ���չ��
		String newFileName = ""; // �����µ��ļ���
		//String nowTimeStr = ""; // ���浱ǰʱ��
		PrintWriter out = null;
		SimpleDateFormat sDateFormat;
		Random r = new Random();
		
		String savePath = FileUpload.pictureUpload(fileactField);
		
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8"); // ��أ���ֹ�����ļ���������
	
		// ��ȡ��չ��
		if (textactfield.lastIndexOf(".") >= 0) {
			extName = textactfield.substring(textactfield
					.lastIndexOf("."));
		}
		try {
			out = response.getWriter();
			newFileName = orgaId + ".actlogo" + extName; // �ļ��������������
			String filePath = savePath + newFileName;
			filePath = filePath.replace("\\", "/");
			//����ϴ����Ƿ���ͼƬ
			if (UtilCommon.checkIsImage(extName)) {
				FileUtils.copyFile(fileactField, new File(filePath));
			} else {
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public File getFileField() {
		return fileField;
	}

	public void setFileField(File fileField) {
		this.fileField = fileField;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public String getFileRealName() {
		return fileRealName;
	}

	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getTextfield() {
		return textfield;
	}

	public void setTextfield(String textfield) {
		this.textfield = textfield;
	}


	public String getTextactfield() {
		return textactfield;
	}


	public void setTextactfield(String textactfield) {
		this.textactfield = textactfield;
	}


	public File getFileactField() {
		return fileactField;
	}


	public void setFileactField(File fileactField) {
		this.fileactField = fileactField;
	}

	
    
}
