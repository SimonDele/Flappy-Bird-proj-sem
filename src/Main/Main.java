package Main;
import java.util.Random;

import javax.swing.JFrame;

import Controleur.Checker;
import Modele.Jeu;
import Vue.Fenetre;
import ia.Genetic;

public class Main {
	// statics : dimensions and random
	public static Random rand = new Random();
	public static int DIMX;
	public static int DIMY;
	private static boolean isAI = false;
	
	// main method (the reason we're here at all)
	public static void main(String[] args) {
		DIMX = 1000;
		DIMY = 600;
		int delay = 15;
		int sizePop = 1;
		// Game generation (initial state)
		Jeu jeu = new Jeu(Main.DIMX, Main.DIMY, sizePop);
		
		Genetic genetic = null;
		if(isAI) {
			genetic = new Genetic(jeu, sizePop);
		}

		
		
		// Window creation
		Fenetre window = null;
		try {
			window = new Fenetre(Main.DIMX, Main.DIMY,jeu);
			window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Checker checker = null;
		if(!isAI) {
			checker = new Checker(window.getPjeu());
		}
		boolean[] saut = new boolean[sizePop]; // for each frame we will have an array of boolean saying which bird will jump
		for(int i=0; i<sizePop; i++) {
			saut[i] = true; // initialisation at true so that they all start by jumping.
		}
		
		// Game loop
		while(!jeu.end()) { // for now, while true
			
			// Model updating
			jeu.update(saut);
			// Display updating 
			(window.getPjeu()).repaint();
			// Control ...?
			if(isAI) {
				saut = genetic.getJump(); 
			}else {
				saut[0] = checker.getJump();
			}
			
			
			// Delaying (we're only humans, afterall)
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private static void loopPlayer(Jeu jeu, boolean[] saut, Fenetre window, Checker checker) {
		int delay = 15;
		// Game loop
		while(!jeu.end()) { // for now, while true
			
			// Model updating
			jeu.update(saut);
			// Display updating 
			(window.getPjeu()).repaint();
			// Control
			saut[0] = checker.getJump();
			
			
			// Delaying (we're only humans, afterall)
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	private static void loopIA() {
		
	}

}
