package Modele;

import java.util.ArrayList;

import Main.Main;
import Vue.Fenetre;

public class Jeu {
	public Bird bird;
	private ArrayList<Obstacle> obstacles;
	public static int DIMY;
	public static int DIMX;
	
	public Jeu() {
		Jeu.DIMX = Main.DIMX;
		Jeu.DIMY = Main.DIMY;
		
		this.bird = new Bird(Jeu.DIMY/2);
		// creating list
		this.obstacles = new ArrayList<Obstacle>();
		// instanciating. Be careful : if multiple, ASCENDING X order !	
		this.obstacles.add(new Obstacle(Main.rand.nextInt(Jeu.DIMY),Jeu.DIMX));
		System.out.println(obstacles);
	}
	public ArrayList<Obstacle> getObstacles(){
		return obstacles;
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
			obstacles.add(new Obstacle(Main.rand.nextInt(Jeu.DIMY),Jeu.DIMX));
		}
	}
	
	public boolean end() {
		return ((bird.getPosY() < bird.getSize()) || (bird.getPosY() > Jeu.DIMY - bird.getSize())) ;
	}

	

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
	
//
//	}

}
