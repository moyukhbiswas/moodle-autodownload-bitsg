import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextProcessing {

	public static HashMap<String,String> getCourses(String response)
	{
		String s="(<div id=\"course-)([0-9][0-9][0-9][0-9])(\" class=\"box coursebox\"><div class=\"course_title\"><h2 class=\"title\"><a title=\")([^\"]*)(\")";
		Pattern p=Pattern.compile(s);
		HashMap<String,String> courses=new HashMap<String,String>();
		Matcher m=p.matcher(response);
		while(m.find())
		{
			courses.put(m.group(2),m.group(4));
		}
		return courses;
	}
	
	public static String getPDFLink(String response)
	{
		String s="(<param name=\"src\" value=\")([^\"]*)(\" />)";
		Pattern p=Pattern.compile(s);
		Matcher m=p.matcher(response);
		if(m.find())
		{
			return m.group(2);
		}
		return "";
	}
	
	public static HashMap<String,String> getFiles(String response)
	{
		String s="(<li class=\"activity [a-z]* modtype_[a-z]*\" id=\"module-)([0-9][0-9][0-9][0-9][0-9])(\"><div class=\"mod-indent\"><div class=\"activityinstance\"><a class=\"\" onclick=\"\" href=\"http://10.1.1.242/moodle/mod/[a-z]*/view.php.id=[0-9][0-9][0-9][0-9][0-9]\"><img src=\"http://10.1.1.242/moodle/theme/image.php)([^\"]*)(\" class=\"iconlarge activityicon\" alt=\"[a-zA-Z]*\" /><span class=\"instancename\">)([^<]*)(<)";
//		String s="(<li class=\"activity resource modtype_resource\" id=\"module-)([0-9][0-9][0-9][0-9][0-9])";
//		String s="(\"http://10.1.1.242/moodle/mod/resource/view.php.id=[0-9][0-9][0-9][0-9][0-9]\"><img src=\"http://10.1.1.242/moodle/theme/image.php)([^\"]*)";
//		String s="(\" class=\"iconlarge activityicon\" alt=\"[a-zA-Z]*\" /><span class=\"instancename\">)([^<]*)(<)";
//		System.out.println(s);
		Pattern p=Pattern.compile(s);
		HashMap<String,String> files=new HashMap<String,String>();
		Matcher m=p.matcher(response);
		while(m.find())
		{
			String id=m.group(2);
			String image=m.group(4);
			String name=m.group(6);
			Pattern space=Pattern.compile(" ");
			Matcher space_m=space.matcher(name);
			name=space_m.replaceAll("_");
			if(image.indexOf("folder") != -1)
			{
				files.put(id,name+".folder");
			}
			else if(image.indexOf("powerpoint") != -1)
			{
				files.put(id,name+".ppt");
			}
			else if(image.indexOf("pdf") != -1)
			{
				files.put(id,name+".pdf");
			}
			else if(image.indexOf("mpeg") != -1)
			{
				files.put(id,name+".mp4");
			}
			else if(image.indexOf("document") != -1)
			{
				files.put(id,name+".docx");
			}
			else if(image.indexOf("calc") != -1)
			{
				files.put(id, name+".xlsx");
			}
			else if(image.indexOf("forum")!=-1)
			{
				files.put(id, name+".forum");
			}
			else if(image.indexOf("feedback")!=-1)
			{
				continue;
			}
			else
			{
				files.put(id,name);
			}
			
//			System.out.println("Found");
//			System.out.println(m.group(1));
//			System.out.println(m.group(2));
//			System.out.println(m.group(4));
//			System.out.println(m.group(6));
			//courses.put(m.group(2),m.group(4));
		}
		return files;
	}
	
	public static HashMap<String,String> getFilesInFolder(String response)
	{	
			String s="(<a href=\")(http://10.1.1.242/moodle/pluginfile.php/[0-9]*/mod_folder/content/0/[^\\?]*\\?forcedownload=1)(\"><span class=\"fp-icon\"><img alt=\")([^\"]*)(\" class=\"smallicon\")";
//			System.out.println(s);
//			System.out.println(response);
//			String s="(<span class=\"fp-filename-icon\"><a href=\")(http://10.1.1.242/moodle/pluginfile.php/86436/mod_folder/content/0/NetworkingConcepts%20Part%20A.pdf\\?forcedownload=1)(\">)";
			Pattern p=Pattern.compile(s);
			Matcher m=p.matcher(response);
			HashMap<String,String> files=new HashMap<String,String>();
			while(m.find())
			{
				files.put(m.group(4),m.group(2));
				//return m.group(2);
			}
//			System.out.println(files);
		return files;
	}
	
	public static HashMap<String,String> getPosts(String response)
	{
//		System.out.println(response);
		String s="(<tr class=\"discussion r[0-9]+\"><td class=\"topic starter\"><a href=\"http://10.1.1.242/moodle/mod/forum/discuss.php\\?d=)([0-9][0-9][0-9][0-9][0-9])(\">)([^<]*)(</a></td>)";
//		System.out.println(s);
		String s1="(<td class=\"lastpost\"><a href=\"http://10.1.1.242/moodle/user/view.php\\?id=[0-9][0-9][0-9][0-9]&amp;course=[0-9][0-9][0-9][0-9]\">)([^<]*)(</a><br /><a href=\"http://10.1.1.242/moodle/mod/forum/discuss.php\\?d=)([0-9][0-9][0-9][0-9][0-9])(\">)([^<]*)(</a></td>)";
//		System.out.println(s1);
		Pattern p=Pattern.compile(s);
		Pattern p1=Pattern.compile(s1);
		Matcher m=p.matcher(response);
		Matcher m1=p1.matcher(response);
		HashMap<String,String> forumPostTitle=new HashMap<String,String>();
		while(m.find())
		{
			forumPostTitle.put(m.group(2),m.group(4));
//			System.out.println(m.group(2)+":\t\t"+m.group(4));
		}
		while(m1.find())
		{
//			System.out.println(m1.group(4)+": Posted by \t\t"+m1.group(2)+" at \t"+m1.group(6));
		}
		
		return forumPostTitle;
	}
}
