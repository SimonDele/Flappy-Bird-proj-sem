package Main;
import java.util.Random;

import Modele.*;
import Vue.*;
import Controleur.*;

public class Main {
	// statics : dimensions and random
	public static Random rand = new Random();
	public static int DIMX;
	public static int DIMY;
	
	// main method (the reason we're here at all)
	public static void main(String[] args) {
		DIMX = 1000;
		DIMY = 600;
		int delay = 15;
		
		// Game generation (initial state)
		Jeu jeu = new Jeu(Main.DIMX, Main.DIMY);
		boolean saut = true;
		Checker checker = new Checker();
		
		// Window creation
		Fenetre window = null;
		try {
			window = new Fenetre(Main.DIMX, Main.DIMY,jeu);
			window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Game loop
		while(!jeu.end()) { // for now, while true
			
			// Model upating
			jeu.update(saut);
			// Display updating 
			(window.getPjeu()).repaint();
			// Control ...?
			saut = checker.getJump();
			
			// Delaying (we're only humans, afterall)
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
