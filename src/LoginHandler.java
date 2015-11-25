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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginHandler {
	static BasicCookieStore cookieStore = new BasicCookieStore();
    static CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore)
            .build();
  
    
    public static boolean login(String username, String password)throws Exception{
		try {
            HttpGet httpget = new HttpGet("http://10.1.1.242/");
            CloseableHttpResponse response1 = httpclient.execute(httpget);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get: " + response1.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response1.close();
            }

            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("http://10.1.1.242/moodle/login/index.php"))
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .build();
            CloseableHttpResponse response2 = httpclient.execute(login);
            try {
                HttpEntity entity = response2.getEntity();

                System.out.println("Login form get: " + response2.getStatusLine()); 
                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
                
                
                
            } finally {
                response2.close();
            }
            
            
            
        } finally {
        	String response=sendHTTPGetRequest("http://10.1.1.242/moodle/my/");
        	if(response.indexOf("Login here using your username and password")==-1)return true;
        	return false;
        }
		
		
	}
    
    public static String sendHTTPGetRequest(String url)throws Exception{
		HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response1 = httpclient.execute(httpget);
        
      //  System.out.println(response1.getEntity().toString()+ response1.getStatusLine());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        response1.close();
		return result.toString();
			
	}
    
    
}
