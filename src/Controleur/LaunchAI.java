package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;

import Main.Main;
import Vue.Menu;
public class LaunchAI implements ActionListener {
	

	private Menu menu;
	private JFormattedTextField sizePop;
	public LaunchAI(Menu menu, JFormattedTextField inputSizePop) {
		this.menu = menu;
		sizePop = inputSizePop;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!sizePop.getText().isEmpty()) {
			Main.isAI = true;
			Main.sizePop = Integer.parseInt(sizePop.getText());
			menu.dispose();			
		}

	}		
	


}
