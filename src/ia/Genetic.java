package ia;

import java.util.ArrayList;

import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class Genetic {
	// Genetic's attributes
	private int sizePop;
	private ArrayList<Individual> pop;
	public static int GENERATION;
	
	// Game's attributes
	private Bird[] birds;
	private ArrayList<Obstacle> obstacles;
	
	public Genetic(Jeu jeu, int sizePop) {
		this.sizePop = sizePop;
		
		birds = jeu.getBirds();
		obstacles = jeu.getObstacles();
		
		GENERATION = 0;
		pop = new ArrayList<Individual>();
		
		for(int i=0; i<sizePop;i++) {
			pop.add(new Individual(2*Jeu.DIMY/Obstacle.getSpeed(), Obstacle.MINDIST/Obstacle.getSpeed()+1));
		}
		
	}
	public boolean generationDead() {
		boolean isDead = true;
		int i = 0;
		while(isDead && i < sizePop ) {
			isDead = birds[i].isDead();
			i++;
		}
		return isDead;
			
	}
	public int getSizePop() {
		return sizePop;
	}	
	
	public boolean[] getJump() {
		boolean[] jumps = new boolean[sizePop];
		
		for (int i = 0; i < sizePop; i++) {
			jumps[i] = pop.get(i).decideJump(obstacles.get(0), birds[i]);
		}
		return jumps;
	}
	
	private ArrayList<Individual> selection(){
		return null;
	}
	
	private Individual crossover(Individual parentA, Individual parentB) {
		return null;
	}
	
	/*
	 * 
	 * mutation
	 * 
	 */
	
}
