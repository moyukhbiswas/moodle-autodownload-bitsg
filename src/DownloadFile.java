import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import javax.swing.*;

class DownloadFile extends Thread
{
	String URL;
	String path;
	CloseableHttpClient httpclient;
	Map<String,String> fileHashMap;
	JTextArea console;
	public DownloadFile(CloseableHttpClient httpclient,String URL,String path,Map<String,String> fileHashMap, JTextArea console)
	{
		this.httpclient=httpclient;
		this.path=path;
		this.URL=URL;
		this.fileHashMap=fileHashMap;
		this.console=console;
	}
	public void run()
	{
		try 
		{
			System.out.println("Downloading : \t"+path);
			console.setText(console.getText()+"Downloading : \t"+path+"\n");
			console.repaint();
			String fileName=DownloadManager.downloadFile(httpclient,URL,path);
			fileHashMap.put(URL,fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}