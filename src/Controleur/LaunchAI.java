package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowStateListener;

import Main.Main;
import Vue.Menu;
public class LaunchAI implements ActionListener {
	

	private Menu menu;
	
	public LaunchAI(Menu menu) {
		this.menu = menu;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Main.isAI = true;
		menu.dispose();
	}		
	


}