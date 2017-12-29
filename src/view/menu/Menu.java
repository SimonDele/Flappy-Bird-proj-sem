package view.menu;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import controller.LaunchAI;
import controller.LaunchSoloMode;
import ia.DNA;

public class Menu extends JDialog implements WindowListener{
	JButton buttonLaunchAI;
	JButton play;

	private Boolean isAI;
	private Class<? extends DNA> dnaUsed;
	private int framesPerAction;
	private int sizePop;
	
	public Menu(JFrame parent) {
		super(parent, "Menu", true);
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.addWindowListener(this);
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//Create buttons
		buttonLaunchAI = new JButton("Launch AI");
		play = new JButton("Play alone");
		
		//Add listeners
		buttonLaunchAI.addActionListener(new LaunchAI(this));
		play.addActionListener(new LaunchSoloMode(this));

		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.LINE_AXIS));
		JPanel AI = new JPanel();
		AI.setLayout(new BoxLayout(AI,BoxLayout.LINE_AXIS));
		AI.setAlignmentX(LEFT_ALIGNMENT);
		AI.add(buttonLaunchAI);

		this.getContentPane().add(AI);
		this.getContentPane().add(play);
		this.setVisible(true);
		
	
		
	}

	public int getFramesPerAction() {
		return framesPerAction;
	}

	public void setFramesPerAction(int framesPerAction) {
		this.framesPerAction = framesPerAction;
	}

	public int getSizePop() {
		return sizePop;
	}

	public void setSizePop(int sizePop) {
		this.sizePop = sizePop;
	}

	public Boolean isAI() {
		return isAI;
	}
	public void setIsAI(boolean val) {
		isAI = val;
	}
	public Class<? extends DNA> getDnaUsed(){
		return dnaUsed;
	}
	public void setDnaUsed(Class<? extends DNA> dnaUsed) {
		this.dnaUsed = dnaUsed;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
