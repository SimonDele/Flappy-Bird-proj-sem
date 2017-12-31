package view.menu;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.LaunchAI;
import controller.LaunchSoloMode;
import ia.dna.DNA;

public class Menu extends JDialog implements WindowListener{
	JButton buttonLaunchAI;
	JButton play;

	private Boolean isAI;
	private Class<? extends DNA> dnaUsed;
	private int framesPerAction;
	private int sizePop = 1;
	
	public Menu(JFrame parent) {
		super(parent, "Menu", true);
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		
		
		//Background 
		try {
			BufferedImage background = ImageIO.read(this.getClass().getResource("ressources/background.png"));
			Image bd = background.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_AREA_AVERAGING);
			Graphics2D g2d = (Graphics2D) background.getGraphics();
			g2d.drawImage(bd, 0, 0, this);
			JLabel labBack = new JLabel(new ImageIcon(bd));
			this.setContentPane(labBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Font f = new Font("Serif", Font.PLAIN, 24);

		//Create buttons
		buttonLaunchAI = new JButton("Launch AI");
		buttonLaunchAI.setFont(f);
		
		play = new JButton("Play alone");
		play.setFont(f);
		
		//Add listeners
		buttonLaunchAI.addActionListener(new LaunchAI(this));
		play.addActionListener(new LaunchSoloMode(this));
		
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.LINE_AXIS));
		this.getContentPane().add(Box.createGlue());
		this.getContentPane().add(buttonLaunchAI);
		this.getContentPane().add(Box.createGlue());
		this.getContentPane().add(play);
		this.getContentPane().add(Box.createGlue());
		this.setVisible(true);
		this.repaint();
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
