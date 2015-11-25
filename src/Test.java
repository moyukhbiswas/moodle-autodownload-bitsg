import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test 
{
	public static void main(String args[]) throws Exception
	{
//		Scanner sc=new Scanner(new File("response"));
//		
//		String res=sc.nextLine();
//		System.out.println(res);
//		String regex="(<div class=\"posting fullpost\">)(.+?)</p>(<div class=\"attachedimages\">)";
//        Pattern p=Pattern.compile(regex);
//        Matcher m=p.matcher(res);
//        String text="";
//        if(m.find())
//        {
//        	text=m.group(2);
//        }
//        System.out.println(text);
//		File file=new File("E:\\MoodleDownloads1\\COMPILER CONSTRUCTION (CS F363,IS F342)\\Forum_Post\\a");
//		file.createNewFile();
//		String str="E:\\MoodleDownloads1\\COMPILER CONSTRUCTION (CS F363,IS F342)\\Forum_Post\\a";
////					E:\\MoodleDownloads1\\COMPILER CONSTRUCTION (CS F363,IS F342)\\Forum_Posts\\a
//		FileWriter fw=new FileWriter(file.getAbsoluteFile());
//		BufferedWriter bw=new BufferedWriter(fw);
//		bw.write("test");
//		bw.close();
		SystemTrayHandler sys=new SystemTrayHandler();
		sys.init("favicon.jpg");
		
		sys.displayMessage("HELLO!", "Tdfsfsdff");
		Thread.sleep(100000);
		sys.destroy();
	}
}
