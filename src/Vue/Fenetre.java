package Vue;
import java.awt.Dimension;

import javax.swing.JFrame;

import Controleur.LaunchAI;
import Modele.Jeu;

public class Fenetre extends JFrame {
	// Window dimensions
	public static int DIMX;
	public static int DIMY;
	
	// Window visuals
	private String title;
	private PJeu pjeu;

	// Constructor
	public Fenetre(int dimx, int dimy, Jeu jeu){
		// Variables
		Fenetre.DIMX = dimx;
		Fenetre.DIMY = dimy;
		title = new String("Flappy Bird");
		
		// Window initialisation
		this.setMinimumSize(new Dimension(DIMX,DIMY));
		this.setTitle(title);
	    this.setLocationRelativeTo(null);               
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    pjeu = new PJeu(DIMX, DIMY, jeu);
		this.setContentPane(pjeu);
		this.setVisible(true);
			
	}
	public void displayJeu() {
		this.setContentPane(pjeu);
	}
	// Getters and Setters
	public PJeu getPjeu() {
		return pjeu;
	}
	public void setPjeu(PJeu pjeu) {
		this.pjeu = pjeu;
	}
	
}
