package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import upload.PostImage;
import upload.Token;

public class FileUpload {
	
	//�ϴ�����,����·��
	public static String attachmentUpload(File fileAttachment ,String fileAttachmentContentType,String fileAttachmentFileName,String attachmentSavePath ) throws IOException{
		//�ж��Ƿ���ڸ��ļ��У��������ڣ�����/upload�ļ���,�˴�������
		File f = new  File(attachmentSavePath);	
		if(!f.exists()){
			f.mkdir();
		}
		if(fileAttachment!=null){
			FileOutputStream fos=new FileOutputStream(f+"\\"+fileAttachmentFileName);
			FileInputStream fis=new FileInputStream(fileAttachment);
			byte [] buffer=new byte[1024];
			int len=0;
			while((len=fis.read(buffer))!=-1){
				fos.write(buffer,0,len);
			}
			fis.close();
			fos.close();
		}
		return f+"\\"+fileAttachmentFileName; //�ݴ���upload��
	}
	
	//�ϴ�ͼƬ������·��
	public static String pictureUpload(File filePicture) throws IOException{
		String zoomPath=filePicture.getParentFile()+"\\f"+filePicture.getName();
		File cutPicture = PictureZoom.cutImage(filePicture,new File(zoomPath));
		File zoomPicture = PictureZoom.resize(cutPicture, new File(zoomPath), 300, 0, 1f);		
		String token = Token.createToken(new Date().getTime()+180, 1142528 , "{\"height\":\"h\",\"width\":\"w\",\"s_url\":\"url\"}");
		String json = PostImage.doUpload(zoomPicture, token);
		String url=getUrl(json);
		System.out.println(zoomPicture.delete());
		return url;
	}
	
	//��վ����ͼ��
		public static String pictureWebUpload(File filePicture) throws IOException{ 
			String zoomPath=filePicture.getParentFile()+"\\f"+filePicture.getName();
			File cutPicture = PictureZoom.cutImageWeb(filePicture,new File(zoomPath));
			File zoomPicture = PictureZoom.resize(cutPicture, new File(zoomPath), 300, 0, 1f);		
			String token = Token.createToken(new Date().getTime()+180, 1142528 , "{\"height\":\"h\",\"width\":\"w\",\"s_url\":\"url\"}");
			String json = PostImage.doUpload(zoomPicture, token);
			String url=getUrl(json);
			zoomPicture.delete();
			return url;
		}
		
	
	private static String getUrl(String json){
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(json);
		return jsonObject.getString("url");
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File("H:\\picture\\4.jpg");
		String url =pictureUpload(file);
		System.out.println(url);
	}
	
	public static boolean pictureDel(String url) {
		//���urlΪnull
		if(url == null){
			
		}
		
		
		return true;
	}
	
	
	public static boolean attachmentDel(String url) {
		//���urlΪnull,˵���ûû�и��� 
		if(url == null) {
			
		}
		
		return true;
	}
	
}
