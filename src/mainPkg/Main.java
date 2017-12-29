package mainPkg;
import java.util.Random;
import controller.Checker;
import ia.Genetic;
import ia.dna.BoolArray;
import ia.dna.DNA;
import ia.dna.NeuralNet;
import ia.sel.FunctionalSelection;
import ia.sel.Selection;
import ia.sel.rf.*;
import model.Game;
import view.Fenetre;
import view.Menu;
import view.PJeu;

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
		new Menu(null);
		Game game = new Game(Main.DIMX, Main.DIMY, sizePop);
		
		// Genetic algo initialisation (with its DNA implementation and Selection)
		Genetic genetic = null;
		Class<? extends DNA> dnaType = null;
		int framesPerAction = 0;
		if(isAI) {
			if (isNN) {
				dnaType = NeuralNet.class;
				framesPerAction = 2;
			}
			else {
				dnaType = BoolArray.class;
				framesPerAction = 1;
			}
		}
		Selection selector = new FunctionalSelection(new RePOL(2));
		genetic = new Genetic(game, sizePop, dnaType, selector, framesPerAction);
		
		// (View) Window creation
		Fenetre window = null;
		try {
			window = new Fenetre(Main.DIMX, Main.DIMY,game);
			window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// (Controller) Checker creation
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
			loopGenetic(game,window,genetic,saut);
		} else {
			loopPlayer(game, saut, window, checker);
		}
	}
	
	/**
	 * Game loop for a human player. Repeats the sequence 'update game, display game, get inputs from controller' until the end of the game.
	 * @param game
	 * @param saut
	 * @param window
	 * @param checker
	 */
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
	
	/**
	 * Game loop for the Genetic algorithm, no matter the DNA implementation. Generates the next population when the old one died, else asks for input and gets the associated model and view reactions.
	 * @param game the current game the AI has to play on
	 * @param window the window on which to display the game and AI results
	 * @param genetic the genetic algorithm with the DNA chosen
	 * @param saut the array of jumps to change at each frame
	 */
	public static void loopGenetic(Game game, Fenetre window, Genetic genetic, boolean[] saut) {
		// Game loop
		int count = 0;
		while(true) { // for now, while true
			// Generation updating (if dead)
			if(genetic.generationDead()) {
				// game updating
				game = new Game(Main.DIMX, Main.DIMY, sizePop);
				genetic.update(game);
				// window updating
				if(enableView) window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,game));
				else 		   window.setPjeu(null);
				window.getDisplayInfoGenetic().updateInfo(); 
			}

			// Model updating
			game.update(saut);
			
			// Display updating 
			if(enableView) {
				if(window.getPjeu() == null) window.setPjeu(new PJeu(Main.DIMX,Main.DIMY,game));
				window.getPjeu().repaint();
			}
			
			// Control 
			if (count++ % genetic.getFramesPerAction() == 0) {
				saut = genetic.getJumps();
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
