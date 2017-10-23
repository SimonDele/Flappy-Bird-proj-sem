package Main;
import java.util.Random;

import Controleur.Checker;
import Modele.Jeu;
import Vue.Fenetre;
import Vue.PJeu;
import ia.Genetic;

public class Main {
	// statics : dimensions and random
	public static Random rand = new Random();
	public static int DIMX;
	public static int DIMY;
	private static boolean isAI = true;
	private static int delay;
	private static int sizePop;
	// main method (the reason we're here at all)
	public static void main(String[] args) {
		DIMX = 1000;
		DIMY = 600;
		delay = 15;
		sizePop = 3000;
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
		if(isAI) {
			loopAI(jeu,window,genetic,saut);
		}else {
			loopPlayer(jeu, saut, window, checker);
		}
	}
	private static void loopPlayer(Jeu jeu, boolean[] saut, Fenetre window, Checker checker) {
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
	private static void loopAI(Jeu jeu, Fenetre window, Genetic genetic, boolean[] saut) {
		
		// Game loop
				while(true) { // for now, while true
					if(genetic.generationDead()) {
						jeu = new Jeu(Main.DIMX, Main.DIMY, sizePop);
						genetic = new Genetic(jeu, sizePop);
						//window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,jeu)); //Marche pas...
						window.dispose();
						window = new Fenetre(Main.DIMX,Main.DIMY,jeu);
					}
					// Model updating
					jeu.update(saut);
					// Display updating 
					(window.getPjeu()).repaint();
					// Control ...?
					saut = genetic.getJump(); 
					
					// Delaying (we're only humans, afterall)
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		
	}

}
