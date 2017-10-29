package ia;

import Modele.Bird;
import Modele.Obstacle;

public abstract class Individual implements Comparable<Individual> {
	protected int fitness;
	
	public abstract boolean decideJump(Obstacle obstacle, Bird bird);
	public int getFitness(){return fitness;}
	public void setFitness(int fitness) {this.fitness = fitness;}
	
	public int compareTo(Individual ind) { // descending order
		return ind.getFitness() - this.fitness;
	}
}
