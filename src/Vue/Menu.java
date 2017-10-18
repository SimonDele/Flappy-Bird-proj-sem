package Vue;

import javax.swing.JButton;
import javax.swing.JPanel;

import Controleur.LaunchAI;

public class Menu extends JPanel{
	JButton buttonLaunchAI;
	JButton play;
	
	public Menu(LaunchAI launchAI) {
		
		//Create buttons
		buttonLaunchAI = new JButton("Launch AI");
		play = new JButton("Play alone");
		
		//Add listeners
		buttonLaunchAI.addActionListener(launchAI);
		this.add(buttonLaunchAI);
		this.add(play);
	}
}
