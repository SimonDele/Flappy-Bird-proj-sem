package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Main.Main;
import Modele.Jeu;
import Vue.Fenetre;
import ia.Genetic;
public class LaunchAI implements ActionListener {
	
	Jeu jeu;
	Fenetre window;
	Genetic genetic;
	boolean[] saut;
	
	public LaunchAI(Jeu jeu, Fenetre window, Genetic genetic, boolean[] saut) {
		this.jeu = jeu;
		this.window = window;
		this.genetic = genetic;
		this.saut = saut;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Main.loopAI(jeu, window, genetic, saut);	
	}		
	


}
