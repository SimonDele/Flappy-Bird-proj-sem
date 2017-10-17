package Main;
import java.util.Random;

import Modele.*;
import Vue.*;
import ia.Genetic;
import Controleur.*;

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
		
		int N;
		if(isAI) {
			Genetic genetic = new Genetic();
			N = genetic.getSizePop();
		}else {
			N = 1;
		}
		
		// Game generation (initial state)
		Jeu jeu = new Jeu(Main.DIMX, Main.DIMY, N);
		
		
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
		boolean[] saut = new boolean[N];
		for(int i=0; i<N; i++) {
			saut[i] = true;
		}
		
		// Game loop
		while(!jeu.end()) { // for now, while true
			
			// Model upating
			jeu.update(saut);
			// Display updating 
			(window.getPjeu()).repaint();
			// Control
			if(isAI) {
				//saut = genetic.getJump(); 
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

}
