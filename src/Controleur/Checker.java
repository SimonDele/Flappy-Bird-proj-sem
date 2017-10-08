package Controleur;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Main.Main;

import javax.swing.JPanel;
	
public class Checker extends JPanel implements KeyListener {
	// Thing we're checking :
	private static boolean jump;
	
	// Constructor
	public Checker() {
		jump = false;
		this.setFocusable(true);
		// Must set the addKeyListener, but from whom to whom ?
	}
	
	// Methods
	/// warning ! not a real get() ! (also sets jump to false)
	public boolean getJump() {
		boolean save = jump;
		if (jump) {System.out.println("jump ! getJump");} // doesn't work :'(
		jump = false;
		return save;
	}

	/// methods for key listening
	public void keyPressed(KeyEvent e) {
	    if(e.getKeyCode() == 32) { // Space pressing (for your futuristic clothes)
	    	jump = true;
	    	System.out.println("jump ! keyPressed"); // won't work either :'(
	    }
	}
	@Override
	public void keyReleased(KeyEvent arg0) {	
	}
	@Override
	public void keyTyped(KeyEvent arg0) {	
	}
}
