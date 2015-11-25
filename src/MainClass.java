import java.net.URI;
import java.net.URL;
import java.util.List;
import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MainClass {
	static HashMap<String,String> fileHashMap=new HashMap<String,String>();
	
	public static void downloadCoursePage(String courseFolder, String courseID, JTextArea console)throws Exception{
		System.out.println("Polling Request to:"+"http://10.1.1.242/moodle/course/view.php?id="+courseID+"\n");
		console.setText(console.getText()+"Polling Request to:"+"http://10.1.1.242/moodle/course/view.php?id="+courseID+"\n");
		console.repaint();
		
		String coursepage=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/course/view.php?id="+courseID);
		HashMap<String,String> files=TextProcessing.getFiles(coursepage);
		System.out.println(files);
		Thread fileThreads[]=new Thread[files.keySet().size()];
		int index=0;
		for(String s: files.keySet()){
			if(files.get(s).indexOf(".folder")<0)
			{
//				if(files.get(s).indexOf(".pdf") < 0)
//				{
////					System.out.println("Downloading:"+files.get(s));
//					fileThreads[index++]=new DownloadFile(LoginHandler.httpclient, "http://10.1.1.242/moodle/mod/resource/view.php?id="+s, courseFolder+"\\"+files.get(s),fileHashMap);
////					String filePath=DownloadManager.downloadFile(LoginHandler.httpclient, "http://10.1.1.242/moodle/mod/resource/view.php?id="+s, courseFolder+"\\"+files.get(s));
////					fileHashMap.put("http://10.1.1.242/moodle/mod/resource/view.php?id="+s, filePath);
//				}
				if(files.get(s).indexOf(".forum") >= 0 )
				{
					String response=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/mod/forum/view.php?id="+s);
					HashMap<String,String> forumPostTitles=TextProcessing.getPosts(response);
					Thread forumPosts[]=new Thread[forumPostTitles.keySet().size()];
					int j=0;
					new File(courseFolder+"\\Forum_Posts").mkdir();
					for(String id : forumPostTitles.keySet())
					{
						String postURL="http://10.1.1.242/moodle/mod/forum/discuss.php?d="+id;
						forumPosts[j++]=new DownloadPost(LoginHandler.httpclient,postURL,courseFolder+"\\Forum_Posts\\"+forumPostTitles.get(id).replace(':',' ')+".html",fileHashMap);
//						String resp=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/mod/forum/discuss.php?id="+s);
					}
					for(Thread t : forumPosts)
					{
						t.start();
					}
					for(Thread t : forumPosts)
					{
						t.join();
					}
						
				}
				else if(files.get(s).indexOf(".pdf") >= 0)
				{
					String response=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/mod/resource/view.php?id="+s);
					String pdfFileURL=TextProcessing.getPDFLink(response);
					fileThreads[index++]=new DownloadFile(LoginHandler.httpclient,pdfFileURL,courseFolder+"\\"+files.get(s),fileHashMap, console);
//					String filePath=DownloadManager.downloadFile(LoginHandler.httpclient,pdfFileURL,courseFolder+"\\"+files.get(s));
//					fileHashMap.put("http://10.1.1.242/moodle/mod/resource/view.php?id="+s, filePath);
				}
				else
				{
					System.out.println("Downloading:"+files.get(s));
					console.setText(console.getText()+files.get(s)+"\n");
					console.repaint();
					fileThreads[index++]=new DownloadFile(LoginHandler.httpclient, "http://10.1.1.242/moodle/mod/resource/view.php?id="+s, courseFolder+"\\"+files.get(s),fileHashMap,console);
//					String filePath=DownloadManager.downloadFile(LoginHandler.httpclient, "http://10.1.1.242/moodle/mod/resource/view.php?id="+s, courseFolder+"\\"+files.get(s));
//					fileHashMap.put("http://10.1.1.242/moodle/mod/resource/view.php?id="+s, filePath);
				}
			}
			else
			{
				
				String folderName=files.get(s).substring(0,files.get(s).indexOf(".folder"));
				new File(courseFolder+"\\"+folderName).mkdir();
				String response=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/mod/folder/view.php?id="+s);
				Map<String,String> filesList=TextProcessing.getFilesInFolder(response);
				Thread filesInFolderThread[]=new Thread[filesList.keySet().size()];
				int i=0;
				for(String name : filesList.keySet())
				{
					
					filesInFolderThread[i++]=new DownloadFile(LoginHandler.httpclient,filesList.get(name),courseFolder+"\\"+folderName+"\\"+name,fileHashMap, console);
//					DownloadManager.downloadFile(httpclient,filesList.get(name),courseFolder+"\\"+folderName+"\\"+name);
				}
				
				for(Thread t : filesInFolderThread)
				{
					t.start();
				}
				for(Thread t : filesInFolderThread)
				{
					t.join();
				}
			}
		
		}
		for(int j=0;j<index;j++)
		{
			fileThreads[j].start();
		}
		for(int j=0;j<index;j++)
		{
			fileThreads[j].join();
		}	
		
	}
	
	public static void main(String args[])throws Exception{
		String username, password;
		String defaultuserpath="E:\\MoodleDownloads1\\";
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter username:");
		username=br.readLine();
		System.out.println("Enter password:");
		password=br.readLine();
		
		
		LoginHandler.login(username,password);
		
		String coursespage= LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/my/");
		HashMap<String,String> courses= TextProcessing.getCourses(coursespage);
		
		for(String s: courses.keySet()){
			new File(defaultuserpath+courses.get(s)).mkdir();		
		}
		
		Thread downloadCourseThread[]=new Thread[courses.keySet().size()];
		int index=0;
		for(String s: courses.keySet())
		{
			System.out.println("Sending");
		//	downloadCourseThread[index++]=new DownloadCourse(defaultuserpath+courses.get(s),s, console);
//			downloadCoursePage(defaultuserpath+courses.get(s),s);			
		}
		
		for(Thread t : downloadCourseThread)
		{
			t.start();
		}
		for(Thread t : downloadCourseThread)
		{
			t.join();
		}
		System.out.println(courses.toString());
		PrintWriter pw= new PrintWriter(new FileWriter("filemap.txt"));
		for(String s: fileHashMap.keySet()){
			
			pw.println(s+","+fileHashMap.get(s));
			
		}
		pw.close();
		LoginHandler.httpclient.close();
			
	}
	
	/*CLONE of the above method :P EXCUUUUUSE
	 * 
	 */
	
	public static void executeMain(String username, String password, String filepath, JTextArea console)throws Exception{
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

		
		
		LoginHandler.login(username,password);
		
		String coursespage= LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/my/");
		HashMap<String,String> courses= TextProcessing.getCourses(coursespage);
		
		for(String s: courses.keySet()){
			new File(filepath+courses.get(s)).mkdir();		
		}
		
		Thread downloadCourseThread[]=new Thread[courses.keySet().size()];
		int index=0;
		for(String s: courses.keySet())
		{
			System.out.println("Sending");
			downloadCourseThread[index++]=new DownloadCourse(filepath+courses.get(s),s, console);
//			downloadCoursePage(defaultuserpath+courses.get(s),s);			
		}
		
		for(Thread t : downloadCourseThread)
		{
			t.start();
		}
		for(Thread t : downloadCourseThread)
		{
			t.join();
		}
	//	progressbar.setValue(100);
		console.setText(console.getText()+courses.toString());
		console.repaint();
		System.out.println(courses.toString());
		PrintWriter pw= new PrintWriter(new FileWriter("filemap.txt"));
		for(String s: fileHashMap.keySet()){
			
			pw.println(s+","+fileHashMap.get(s));
			
		}
		pw.close();
	//	LoginHandler.httpclient.close();
		console.setText(console.getText()+"\nDOWNLOAD HAS FINISHED");
		console.repaint();
		System.exit(0);
		
		
			//AutoDownloadDaemon.runDaemon();
		
		
	/*	FinishedDownloadFrame finishedFrame= new FinishedDownloadFrame();
		
		finishedFrame.jButton2.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				//START DAEMON HERE
				
			
			}
			
			
		});
		
		finishedFrame.jButton3.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				System.exit(0);
				
				
			}
			
			
		});
		
		finishedFrame.setVisible(true);
		*/
	
	}
	 
	
}
