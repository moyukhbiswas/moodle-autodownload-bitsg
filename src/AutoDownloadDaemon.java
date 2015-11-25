import java.util.*;
import java.io.*;

/*
 * 
 * This is to poll every 5 minutes and check for dependencies and all. It relies on the credentials in loginDetails file and the
 * filemap.txt file for the names of the links it has downloaded. Make sure to have those.
 */
public class AutoDownloadDaemon{
	static HashMap<String,String> fileHashMap=new HashMap<String,String>();
	 static SystemTrayHandler sysTrayHandler=new SystemTrayHandler();
	 
	public static void populateHashMap()throws Exception{
		BufferedReader br=new BufferedReader(new FileReader("filemap.txt"));
		String s=br.readLine();
		while(s!=null){
			fileHashMap.put(s.split(",")[0], s.split(",")[1]);
			s=br.readLine();
		}
		br.close();
		
	}
	
	public static void downloadCoursePageIncomplete(String courseFolder, String courseID)throws Exception{
		System.out.println("Polling Request to:"+"http://10.1.1.242/moodle/course/view.php?id="+courseID);
		String coursepage=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/course/view.php?id="+courseID);
		HashMap<String,String> files=TextProcessing.getFiles(coursepage);
		
		for(String s: files.keySet()){
			if(files.get(s).indexOf(".folder")<0)
			{
				if(files.get(s).indexOf(".forum") >=0)
				{
					String response=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/mod/forum/view.php?id="+s);
					HashMap<String,String> forumPostTitles=TextProcessing.getPosts(response);
					for(String id : forumPostTitles.keySet())
					{
						String postURL="http://10.1.1.242/moodle/mod/forum/discuss.php?d="+id;
						if(!fileHashMap.containsKey(postURL))
						{
							String path=courseFolder+"\\Forum_Posts\\"+forumPostTitles.get(id).replace(':',' ')+".html";
							System.out.println("Downloading Post: \t"+path);
							String filePath=DownloadManager.downloadPost(LoginHandler.httpclient,postURL,path);
							fileHashMap.put(postURL,filePath);
							sysTrayHandler.displayMessage("New Post Download!", "New Download has been made:"+filePath);
						}
					}
				}
				else if(files.get(s).indexOf(".pdf") >= 0)
				{
					String response=LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/mod/resource/view.php?id="+s);
					String pdfFileURL=TextProcessing.getPDFLink(response);
					if(!fileHashMap.containsKey(pdfFileURL)){
						String filePath=DownloadManager.downloadFile(LoginHandler.httpclient,pdfFileURL,courseFolder+"\\"+files.get(s));
						fileHashMap.put(pdfFileURL, filePath);
						sysTrayHandler.displayMessage("New File Download!", "New Download has been made:"+filePath);
					}
				}
				else
				{
					String URL="http://10.1.1.242/moodle/mod/resource/view.php?id="+s;
					if(!fileHashMap.containsKey(URL)){
						String filePath=DownloadManager.downloadFile(LoginHandler.httpclient, URL, courseFolder+"\\"+files.get(s));
						fileHashMap.put(URL, filePath);
						sysTrayHandler.displayMessage("New File Download!", "New Download has been made:"+filePath);
					}
				}
			}
			else
			{
				new File(courseFolder+"\\"+files.get(s).substring(0,files.get(s).indexOf(".folder"))).mkdir();	
			}
		}
	}

	public static void runDaemon()throws Exception
	{
		for(;;)
		{
			Scanner sc=new Scanner(new FileReader("loginDetails"));
			String username=sc.nextLine();
			String password=sc.nextLine();
			String defaultuserpath=sc.nextLine()+"\\";
	//		System.out.println(username+"\t"+password+"\t"+defaultuserpath);
			sysTrayHandler.init("C:\\wamp\\www\\favicon.jpg");
			
			populateHashMap();
			LoginHandler.login(username, password);
			
			String coursespage= LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/my/");
			HashMap<String,String> courses= TextProcessing.getCourses(coursespage);
			
			for(String s: courses.keySet()){
				new File(defaultuserpath+courses.get(s)).mkdir();
			}
			
			for(String s: courses.keySet()){
				System.out.println("Sending");
			//	sendHTTPGetRequest("http://10.1.1.242/moodle/my");
				downloadCoursePageIncomplete(defaultuserpath+courses.get(s),s);
				
			}
			FileWriter fr=new FileWriter("filemap.txt");
			for(String s: fileHashMap.keySet()){
				fr.write(s+","+fileHashMap.get(s)+"\n");
				
			}
			fr.close();
			System.out.println("Iteration complete. Sleeping.");
			Thread.sleep(100000);
			sysTrayHandler.destroy();
			
			Thread.sleep(5*60*100);
		}
		
		
	}
	public static void main(String args[])throws Exception
	{
		for(;;)
		{
			Scanner sc=new Scanner(new FileReader("loginDetails"));
			String username=sc.nextLine();
			String password=sc.nextLine();
			String defaultuserpath=sc.nextLine()+"\\";
	//		System.out.println(username+"\t"+password+"\t"+defaultuserpath);
			sysTrayHandler.init("C:\\wamp\\www\\favicon.jpg");
			
			populateHashMap();
			LoginHandler.login(username, password);
			
			String coursespage= LoginHandler.sendHTTPGetRequest("http://10.1.1.242/moodle/my/");
			HashMap<String,String> courses= TextProcessing.getCourses(coursespage);
			
			for(String s: courses.keySet()){
				new File(defaultuserpath+courses.get(s)).mkdir();
						
			}
			
			
			for(String s: courses.keySet()){
				System.out.println("Sending");
			//	sendHTTPGetRequest("http://10.1.1.242/moodle/my");
				downloadCoursePageIncomplete(defaultuserpath+courses.get(s),s);
				
			}
			
			FileWriter fr=new FileWriter("filemap.txt");
			for(String s: fileHashMap.keySet()){
				fr.write(s+","+fileHashMap.get(s)+"\n");
				
			}
			fr.close();
			
			System.out.println("Iteration complete. Sleeping.");
			Thread.sleep(100000);
			sysTrayHandler.destroy();
			
			Thread.sleep(5*60*100);
		}
		
		
	}
}
