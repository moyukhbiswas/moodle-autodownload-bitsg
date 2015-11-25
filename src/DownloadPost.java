import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.impl.client.CloseableHttpClient;


public class DownloadPost extends Thread
{
	String URL;
	String path;
	CloseableHttpClient httpclient;
	Map<String,String> fileHashMap;
	public DownloadPost(CloseableHttpClient httpclient,String URL,String path,Map<String,String> fileHashMap)
	{
		this.httpclient=httpclient;
		this.path=path;
		this.URL=URL;
		this.fileHashMap=fileHashMap;
	}
	public void run()
	{
		try 
		{
			System.out.println("Downloading Post: \t"+path);
			String fileName=DownloadManager.downloadPost(httpclient,URL,path);
			fileHashMap.put(URL,fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}