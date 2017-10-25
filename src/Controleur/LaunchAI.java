package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;
import javax.swing.SpinnerNumberModel;

import Main.Main;
import Vue.Menu;
public class LaunchAI implements ActionListener {
	

	private Menu menu;
	private SpinnerNumberModel sizePop;
	public LaunchAI(Menu menu, SpinnerNumberModel inputSizePop) {
		this.menu = menu;
		sizePop = inputSizePop;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
			Main.isAI = true;
			Main.sizePop =(int)(sizePop.getNumber());
			menu.dispose();			
		

	}		
	


}
