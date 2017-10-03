package Vue;
import java.awt.Dimension;

import javax.swing.JFrame;
import Modele.*;

public class Fenetre extends JFrame {
	
	public static int DIMX;
	public static int DIMY;
	private String title;
	private PJeu pjeu;
	private Jeu jeu;

	public Fenetre(){
		// Variables
		DIMX = 1000;
		DIMY = 600;
		title = new String("Flappy Bird");
		jeu = new Jeu();
		
		// Window initialisation
		
		this.setMinimumSize(new Dimension(DIMX,DIMY));
		this.setTitle(title);
	    this.setLocationRelativeTo(null);               
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    pjeu = new PJeu(DIMX, DIMY);
		this.setContentPane(pjeu);
		this.setVisible(true);
		go();
			
	}
	
	public PJeu getPjeu() {
		return pjeu;
	}

	public void setPjeu(PJeu pjeu) {
		this.pjeu = pjeu;
	}
	
	private void go() {
		int delay = 15;
		while(!jeu.end()) { // for now since jeu isn't updated it's a 'while true'
			pjeu.repaint();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
