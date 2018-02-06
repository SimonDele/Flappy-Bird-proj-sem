package view.game;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ia.Genetic;
import mainPkg.Main;
import model.Game;
public class Frame extends JFrame {
	// Window dimensions
	public static int DIMX;
	public static int DIMY;
	
	// Window visuals
	private String title;
	private PJeu pjeu;
	private DisplayInfoGenetic displayInfoGenetic;
	
	// Constructor
	public Frame(int dimx, int dimy, Game game){
		// Variables
		Frame.DIMX = dimx;
		Frame.DIMY = dimy;
		title = new String("Flappy Whale :3");
		
		// Window initialisation
		if(Main.isAI) {
			this.setMinimumSize(new Dimension(DIMX+350,DIMY));	
		}else {
			this.setMinimumSize(new Dimension(DIMX,DIMY));
		}
		
		this.setTitle(title);
	    this.setLocationRelativeTo(null);               
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    pjeu = new PJeu(DIMX, DIMY, game);
	    
	    JPanel allContent = new JPanel();
	    allContent.setLayout(new BorderLayout());
	    allContent.add(pjeu, BorderLayout.CENTER);
	    if(Main.isAI) {
			displayInfoGenetic = new DisplayInfoGenetic(Genetic.infoGenetic);
		    allContent.add(displayInfoGenetic, BorderLayout.EAST);    	
	    }

		this.setContentPane(allContent);
		this.setVisible(true);
		
			
	}
	public void displayJeu() {
		this.setContentPane(pjeu);
	}
	// Getters and Setters
	public PJeu getPjeu() {
		return pjeu;
	}
	public DisplayInfoGenetic getDisplayInfoGenetic() {
		return displayInfoGenetic;
	}
	public void setPjeu(PJeu pjeu) {
		if(this.pjeu != null) {
			this.remove(this.pjeu);	
		}
		this.pjeu = pjeu;
		if(this.pjeu != null) {
			this.add(pjeu);		
		}

		

	}
	
}
