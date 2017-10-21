package Vue;

import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import Controleur.LaunchAI;
import Controleur.LaunchSoloMode;

public class Menu extends JDialog{
	JButton buttonLaunchAI;
	JButton play;
	JFormattedTextField inputSizePop;
	
	public Menu(JFrame parent) {
		super(parent, "Menu", true);
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		//this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//Create buttons
		buttonLaunchAI = new JButton("Launch AI");
		play = new JButton("Play alone");
		
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
	    formatter.setOverwriteMode(true);
		inputSizePop = new JFormattedTextField(formatter);
		inputSizePop.setMaximumSize(new Dimension(100,30));
		//Add listeners
		buttonLaunchAI.addActionListener(new LaunchAI(this, inputSizePop));
		play.addActionListener(new LaunchSoloMode(this));

		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.PAGE_AXIS));
		JPanel AI = new JPanel();
		AI.setLayout(new BoxLayout(AI,BoxLayout.LINE_AXIS));
		AI.setAlignmentX(LEFT_ALIGNMENT);
		AI.add(buttonLaunchAI);
		AI.add(inputSizePop);
		this.getContentPane().add(AI);
		this.getContentPane().add(play);
		this.setVisible(true);
	}
}
