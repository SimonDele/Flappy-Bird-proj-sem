package controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import mainPkg.Main;
	
public class Checker implements KeyListener {
	// Thing we're checking :
	private static boolean jump;
	
	// Constructor
	public Checker(JPanel pjeu) {
		jump = false;
		pjeu.setFocusable(true);
		pjeu.addKeyListener(this);
	}
	
	// Methods
	/// warning ! not a real get() ! (also sets jump to false)
	public boolean getJump() {
		boolean save = jump;
		jump = false;
		return save;
	}

	/// methods for key listening
	public void keyPressed(KeyEvent e) {
	    if(e.getKeyCode() == 32) { // Space pressing (for your futuristic clothes)
	    	jump = true;
	    }
	}
	@Override
	public void keyReleased(KeyEvent arg0) {	
	}
	@Override
	public void keyTyped(KeyEvent arg0) {	
	}
}
