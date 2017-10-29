package Main;
import java.util.Random;

import Controleur.Checker;
import Modele.Jeu;
import Vue.Fenetre;
import Vue.Menu;
import Vue.PJeu;
import ia.Genetic;
import ia.GeneticNN;

public class Main {
	// statics : dimensions and random
	public static Random rand = new Random();
	public static int DIMX;
	public static int DIMY;
	public static boolean isAI; 
	public static boolean isNN;
	public static int delay;
	public static int sizePop = 1;
	public static boolean enableView;
	// main method (the reason we're here at all)
	public static void main(String[] args) {
		enableView = true;
		isNN = false;
		DIMX = 1000;
		DIMY = 600;
		delay = 15;
		// Game generation (initial state)
		@SuppressWarnings("unused")
		Menu menu = new Menu(null);
		
		Jeu jeu = new Jeu(Main.DIMX, Main.DIMY, sizePop);
		
		// Genetic algo initialisation
		Genetic genetic = null;
		GeneticNN geneticNN = null;
		if(isAI) {
			if (isNN) {
				geneticNN = new GeneticNN(jeu, sizePop);
			} else {
				genetic = new Genetic(jeu, sizePop);
			}
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
			if (isNN) {
				loopAI(jeu,window,geneticNN,saut);
			} else {
				loopAI(jeu,window,genetic,saut);
			} 
		} else {
			loopPlayer(jeu, saut, window, checker);
		}
	}
	public static void loopPlayer(Jeu jeu, boolean[] saut, Fenetre window, Checker checker) {
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
	public static void loopAI(Jeu jeu, Fenetre window, Genetic genetic, boolean[] saut) {
		
		// Game loop
		while(true) { // for now, while true
			if(genetic.generationDead()) {
				jeu = new Jeu(Main.DIMX, Main.DIMY, sizePop);
				genetic.update(jeu);
				if(enableView) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,jeu));
				}else {
					window.setPjeu(null);
				}
				window.getDisplayInfoGenetic().updateInfo(); 

			}
			// Model updating
			jeu.update(saut);
			// Display updating 
			if(enableView) {
				if(window.getPjeu() == null) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,jeu));
				}
				(window.getPjeu()).repaint();	
			}
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
	
	public static void loopAI(Jeu jeu, Fenetre window, GeneticNN geneticNN, boolean[] saut) {
		int framesPerAction = 3;
		// Game loop
		while(true) { // for now, while true
			if(geneticNN.generationDead()) {
				jeu = new Jeu(Main.DIMX, Main.DIMY, sizePop);
				geneticNN.update(jeu);
				if(enableView) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,jeu));
				}else {
					window.setPjeu(null);
				}
				window.getDisplayInfoGenetic().updateInfo(); 

			}
			// Model updating
			jeu.update(saut);
			// Display updating 
			if(enableView) {
				if(window.getPjeu() == null) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,jeu));
				}
				(window.getPjeu()).repaint();	
			}
			// Control ...?
			if (Jeu.SCORE % framesPerAction == 0) {
				saut = geneticNN.getJump(); 
			}
			
			// Delaying (we're only humans, afterall)
			try {
				Thread.sleep(delay);
				} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
