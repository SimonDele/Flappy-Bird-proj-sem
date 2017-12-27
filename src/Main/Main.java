package Main;
import java.util.Random;

import Controleur.Checker;
import Modele.Game;
import Vue.Fenetre;
import Vue.Menu;
import Vue.PJeu;
import ia.GeneticBool;
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
		isNN = true;
		DIMX = 1000;
		DIMY = 600;
		delay = 15;
		// Game generation (initial state)
		@SuppressWarnings("unused")
		Menu menu = new Menu(null);
		
		Game game = new Game(Main.DIMX, Main.DIMY, sizePop);
		
		// Genetic algo initialisation
		GeneticBool geneticBool = null;
		GeneticNN geneticNN = null;
		if(isAI) {
			if (isNN) {
				geneticNN = new GeneticNN(game, sizePop);
			} else {
				geneticBool = new GeneticBool(game, sizePop);
			}
		}

		
		// Window creation
		Fenetre window = null;
		try {
			window = new Fenetre(Main.DIMX, Main.DIMY,game);
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
				loopAI(game,window,geneticNN,saut);
			} else {
				loopAI(game,window,geneticBool,saut);
			} 
		} else {
			loopPlayer(game, saut, window, checker);
		}
	}
	public static void loopPlayer(Game game, boolean[] saut, Fenetre window, Checker checker) {
		// Game loop
		while(!game.end()) { // for now, while true
			
			// Model updating
			game.update(saut);
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
	public static void loopAI(Game game, Fenetre window, GeneticBool geneticBool, boolean[] saut) {
		
		// Game loop
		while(true) { // for now, while true
			if(geneticBool.generationDead()) {
				game = new Game(Main.DIMX, Main.DIMY, sizePop);
				geneticBool.update(game);
				if(enableView) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,game));
				}else {
					window.setPjeu(null);
				}
				window.getDisplayInfoGenetic().updateInfo(); 
			}
			
			// Model updating
			game.update(saut);
			// Display updating 
			if(enableView) {
				if(window.getPjeu() == null) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,game));
				}
				(window.getPjeu()).repaint();	
			}
			// Control ...?
			saut = geneticBool.getJump(); 
			
			// Delaying (we're only humans, afterall)
			try {
				Thread.sleep(delay);
				} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void loopAI(Game game, Fenetre window, GeneticNN geneticNN, boolean[] saut) {
		int framesPerAction = 2;
		// Game loop
		while(true) { // for now, while true
			if(geneticNN.generationDead()) {
				game = new Game(Main.DIMX, Main.DIMY, sizePop);
				geneticNN.update(game);
				if(enableView) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,game));
				}else {
					window.setPjeu(null);
				}
				window.getDisplayInfoGenetic().updateInfo(); 
			}
			
			// Model updating
			game.update(saut);
			// Display updating 
			if(enableView) {
				if(window.getPjeu() == null) {
					window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,game));
				}
				(window.getPjeu()).repaint();	
			}
			// Control ...?
			if (Game.SCORE % framesPerAction == 0) {
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
