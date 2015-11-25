import java.awt.*;
import java.io.File;
import java.io.*;

import javax.imageio.ImageIO;

public class SystemTrayHandler {
	 Image image;
	 TrayIcon trayIcon;
	 SystemTray tray;
	public void init(String iconpath){
		try{
		image= ImageIO.read(new File(iconpath));
		trayIcon= new TrayIcon(image);
		tray=SystemTray.getSystemTray();
		tray.add(trayIcon);
		}catch(Exception e){
			System.out.println("Error");
			
		}
	}
	
	public void displayMessage(String heading, String message){
		trayIcon.displayMessage(heading,message, TrayIcon.MessageType.INFO);	
		
	}
	
	public void destroy(){
		
		SystemTray.getSystemTray().remove(trayIcon);
		
	}
}
