package Vue;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import Controleur.LaunchAI;
import Controleur.LaunchSoloMode;

public class Menu extends JDialog{
	JButton buttonLaunchAI;
	JButton play;
	
	public Menu(JFrame parent) {
		super(parent, "Menu", true);
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		//Create buttons
		buttonLaunchAI = new JButton("Launch AI");
		play = new JButton("Play alone");
		
		//Add listeners
		buttonLaunchAI.addActionListener(new LaunchAI(this));
		play.addActionListener(new LaunchSoloMode(this));
		
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(buttonLaunchAI);
		this.getContentPane().add(play);
		this.setVisible(true);
	}
}
