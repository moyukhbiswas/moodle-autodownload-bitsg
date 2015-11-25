import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DownloadManager {

	public static String downloadFile(CloseableHttpClient httpclient, String URL, String completeFilePath){
		try{
			
			completeFilePath= completeFilePath.replace('/', '_');
			System.out.println("Attempting:"+ completeFilePath);
			
			System.out.println("Attempt URL:"+URL);
		 HttpGet httpget = new HttpGet(URL);
         
         CloseableHttpResponse response1 = httpclient.execute(httpget);
         Header contentType = response1.getEntity().getContentType();
         String filename="";
         for(Header h : Arrays.asList(response1.getHeaders("Content-Disposition")))
         {
        	 String s=h.getValue();
//        	 System.out.println(s);
        	 int start=s.indexOf("\"");
        	 int end=s.lastIndexOf("\"");
        	 filename=s.substring(start+1,end);
//        	 System.out.println(s.substring(start+1,end));
//        	 System.out.println(h);
         }
         String filePath = completeFilePath;
         if(!filename.equals(""))
         {
        	 int index=filePath.lastIndexOf('\\');
        	 filePath=filePath.substring(0,index+1)+filename.replace('/','_').replace(' ','_');
         }
         
//         System.out.println(filePath);
//         System.out.println("FileName: "+filename);
//         System.out.println("FileNameReplaced :"+filename.replace('/','_'));
//         System.out.println(Arrays.asList(response1.getAllHeaders()));
         InputStream is = response1.getEntity().getContent();
        
         FileOutputStream fos = new FileOutputStream(new File(filePath));
         int inByte;
         while((inByte = is.read()) != -1)
              fos.write(inByte);
         is.close();
         fos.close();
         response1.close();
         System.out.println("Downloaded:"+ filePath);
         return filePath;
         
		}catch(Exception e){
			return null;
			
		}
		
	}
	
	public static String downloadPost(CloseableHttpClient httpclient, String URL, String completeFilePath){
		try{
			
			completeFilePath= completeFilePath.replace('/', '_');
//			completeFilePath=completeFilePath.replace(':','\0');
			System.out.println("Attempting:"+ completeFilePath);
			System.out.println("Attempt URL:"+URL);
			String response=LoginHandler.sendHTTPGetRequest(URL);
//	        System.out.println(response);
	        String regex="(<div class=\"posting fullpost\">)(.+?)</p>(<div class=\"attachedimages\">)";
	        Pattern p=Pattern.compile(regex);
	        Matcher m=p.matcher(response);
	        String text="";
	        if(m.find())
	        {
	        	text=m.group(2);
	        }
	       
	        text+="</p>";
//	        System.out.println(text);
//	        System.out.println(completeFilePath);
	        FileOutputStream fos = new FileOutputStream(new File(completeFilePath));
	              
	        fos.write(text.getBytes());
	         
	         fos.close();
	         System.out.println("Downloaded:"+ completeFilePath);
	         return completeFilePath;
	         
			}catch(Exception e){
				return null;
				
			}
	}
	
	
}
