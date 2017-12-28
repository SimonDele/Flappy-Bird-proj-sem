package view;

import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import controller.LaunchAI;
import controller.LaunchSoloMode;

public class Menu extends JDialog{
	JButton buttonLaunchAI;
	JButton play;
	private SpinnerNumberModel  modelSizePop;
	private JSpinner spinnerSizePop;
	
	public Menu(JFrame parent) {
		super(parent, "Menu", true);
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		//this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//Create buttons
		buttonLaunchAI = new JButton("Launch AI");
		play = new JButton("Play alone");
		
		modelSizePop = new SpinnerNumberModel(1000,1,100000,10);
		spinnerSizePop = new JSpinner(modelSizePop);
		//Add listeners
		buttonLaunchAI.addActionListener(new LaunchAI(this, modelSizePop));
		play.addActionListener(new LaunchSoloMode(this));

		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.PAGE_AXIS));
		JPanel AI = new JPanel();
		AI.setLayout(new BoxLayout(AI,BoxLayout.LINE_AXIS));
		AI.setAlignmentX(LEFT_ALIGNMENT);
		AI.add(buttonLaunchAI);
		AI.add(spinnerSizePop);
		this.getContentPane().add(AI);
		this.getContentPane().add(play);
		this.setVisible(true);
	}
}
