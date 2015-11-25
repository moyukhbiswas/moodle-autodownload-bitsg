import views.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class MoodleDownloadGUI {
	public static FileChoosePanel filePanel;
	public static LoginPanel loginPanel;
	public static ConsoleAndProgressPanel consolePanel;
	
	public static JFrame mainFrame;
	
	public static FileWriter fw;
	public static String username="";
	public static String password="";
	public static String filepath="";
	
	public static void main(String args[])throws Exception{
		
		mainFrame=new JFrame("Autodownload Client For Moodle");
		filePanel=new FileChoosePanel();
		loginPanel=new LoginPanel();
		consolePanel=new ConsoleAndProgressPanel();
		
		mainFrame.setContentPane(loginPanel);
		
		loginPanel.jButton1.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				username= loginPanel.usernameBox.getText();
				password= new String(loginPanel.passwordBox.getPassword());
				
			try{
				if(LoginHandler.login(username, password)){
					/*Uncomment this if you want to run the Daemon process ever.
					 * try {
						fw=new FileWriter("loginDetails");
						fw.write(username);
						fw.write("\n");
						fw.write(password);
						fw.write("\n");
						fw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
//				System.out.println(username+password);
				//TODO: Write Code for validating login ID and Password
				mainFrame.setContentPane(filePanel);
				mainFrame.setTitle("Select where to download");
				mainFrame.setSize(600,650);
//				mainFrame.revalidate();
				mainFrame.repaint();
				}else{
					InvalidUsername inuser= new InvalidUsername();
					inuser.setVisible(true);
					
				}
			}catch(Exception exc){}
			}
			
			
			
		});
		
		filePanel.jFileChooser1.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				filepath= filePanel.jFileChooser1.getSelectedFile().getAbsolutePath();
				/*
				 * Uncomment this if you wanna run the Daemon process ever.
				 * try {
					fw=new FileWriter("loginDetails",true);
					fw.write(filepath);
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				
				System.out.println(filepath);
				mainFrame.setContentPane(consolePanel);
				mainFrame.setTitle("Downloading!!");
				mainFrame.validate();
				mainFrame.repaint();
				new Thread(new Runnable(){
					
					public void run(){
						try{
							MainClass.executeMain(username, password, filepath+"\\", consolePanel.jTextArea1);
						}catch(Exception exc){}
				}
					
				}).start();
				
			}
			
		});
		
		consolePanel.jButton1.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				System.exit(0);
				
				
			}
			
			
		});
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(450, 600);
		mainFrame.setVisible(true);
		
	}
	
}
