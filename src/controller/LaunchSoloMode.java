package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mainPkg.Main;
import view.Menu;

public class LaunchSoloMode implements ActionListener{
	

	private Menu menu;
	
	public LaunchSoloMode(Menu menu) {
		this.menu = menu;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Main.isAI = false;
		menu.dispose();
	}		
	

}