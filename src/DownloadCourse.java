import javax.swing.JTextArea;


public class DownloadCourse extends Thread
{
	String courseFolder;
	String courseID;
	JTextArea console;
	public DownloadCourse(String courseFolder,String courseID, JTextArea console)
	{
		this.courseFolder=courseFolder;
		this.courseID=courseID;
		this.console=console;
	}
	public void run()
	{
		try {
			System.out.println("Downloading Course : \t"+courseFolder);
			console.setText(console.getText()+"Downloading Course : \t"+courseFolder+"\n");
			console.repaint();
			MainClass.downloadCoursePage(courseFolder,courseID, console);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}