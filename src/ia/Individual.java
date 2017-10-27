package ia;

import Modele.Bird;
import Modele.Obstacle;

public abstract class Individual {
	protected int fitness;
	
	public abstract boolean decideJump(Obstacle obstacle, Bird bird);
	public int getFitness(){return fitness;}
	public void setFitness(int fitness) {this.fitness = fitness;}
}
