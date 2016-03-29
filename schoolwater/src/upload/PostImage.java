package upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class PostImage {
	/**
	 * 提交图片给图库
	 * @param image
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String doUpload(File f , String token){
		//贴图库数据加密请求
		String url = PathConfig.getProperty("tie.tu.ku.post.api");
		
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpPost httppost = new HttpPost(url);  
		
		FileBody bin = new FileBody(f); 
        MultipartEntity reqEntity = new MultipartEntity(); //关键
        try {
        	reqEntity.addPart("file", bin); 
			reqEntity.addPart("Token", new StringBody(token));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        try{
	        httppost.setEntity(reqEntity);
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();  
	        
	        StringBuffer buffer = new StringBuffer();   
	        if (entity != null) {              
	            //start 读取整个页面内容  
	            InputStream is = entity.getContent();  
	            BufferedReader in = new BufferedReader(new InputStreamReader(is));   
	            
	            String line = "";  
	            while ((line = in.readLine()) != null) {  
	                buffer.append(line);  
	            }   
	        } 
	        return buffer.toString();
        }catch (Exception e){
        	e.printStackTrace();
        }
        return "";
	}
	
	public static void main(String []args) throws ClientProtocolException, IOException{
		String token = Token.createToken(new Date().getTime()+180, 1142528 , "{\"height\":\"h\",\"width\":\"w\",\"s_url\":\"url\"}");
		String result = PostImage.doUpload(new File("H:\\picture\\3.jpg"), token);
		System.out.println(result);
	}
	
}
