package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.menu.DialogChooseAIType;
import view.menu.Menu;
public class LaunchAI implements ActionListener {
	private Menu menu;
	//private SpinnerNumberModel sizePop;
	
	public LaunchAI(Menu menu) {
		this.menu = menu;
		//sizePop = inputSizePop;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			menu.setIsAI(true);
			DialogChooseAIType chooseAIType = new DialogChooseAIType(menu, "Choose Type of AI", true, menu );
			chooseAIType.setVisible(true);
			//Main.sizePop =(int)(sizePop.getNumber());
			menu.dispose();			
	}		
}
