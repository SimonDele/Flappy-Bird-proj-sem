package ia;

import java.util.ArrayList;

import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class QLearning {
	
	public IndividualQLearning individual;
	private Bird bird;
	private ArrayList<Obstacle> obstacles;
	
	// QLearning parameter
	private final int bonus = 70000;
	private final int malus = -50000;
	
	
	public QLearning(Jeu jeu) {
		IndividualQLearning staticCreator = new IndividualQLearning(0,0);
		individual = new IndividualQLearning(2*Jeu.DIMY/IndividualQLearning.MESHSIZE, Obstacle.MINDIST/IndividualQLearning.MESHSIZE+1);
		bird = jeu.getBirds()[0];
		obstacles = jeu.getObstacles();
	}
	
	public void setJeu(Jeu jeu) {
		bird = jeu.getBirds()[0];
		obstacles = jeu.getObstacles();
	}
	
	public void giveMalus() {
		individual.updateMesh(malus, obstacles.get(0), bird);
	}
	public void giveBonus() {
		individual.updateMesh(bonus, obstacles.get(0), bird);
	}
	
	
	public boolean[] getJump() {
		
		boolean[] jump = new boolean[1];
		
		if(obstacles.size() > 0) {
			jump[0] = individual.decideJump(obstacles.get(0), bird);			
		}else {
			jump[0] = false;
		}
		
		return jump;
	}
	
}
