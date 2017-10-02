package Modele;

import java.util.ArrayList;

import Main.Main;
import Vue.Fenetre;

public class Jeu {
	private Bird bird;
	private ArrayList<Obstacle> obstacles;
	
	public Jeu() {
		bird = new Bird(Fenetre.DIMY/2);
		// creating list
		obstacles = new ArrayList<Obstacle>();
		// instanciating. Be careful : if multiple, ASCENDING X order !	
		obstacles.add(new Obstacle(Main.rand.nextInt(Fenetre.DIMY),Fenetre.DIMX));
	}
	
	public void update(boolean saut) {
		// updating bird 
		bird.update(saut);
		
		// updating obstacles and potentially deleting (returns deleting boolean)
		boolean destroy = false;
		for(int i=0; i<obstacles.size();i++) {
			destroy = obstacles.get(i).update() || destroy;
		}
		if (destroy) {
			obstacles.remove(0);
			obstacles.add(new Obstacle(Main.rand.nextInt(Fenetre.DIMY),Fenetre.DIMX));
		}
	}
	
	public boolean end() {
		return ((bird.getPosY() < bird.getSize()) || (bird.getPosY() > Fenetre.DIMY - bird.getSize())) ;
	}

	

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
	
//
//	}

}
