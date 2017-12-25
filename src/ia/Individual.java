package ia;

import Modele.Bird;
import Modele.Obstacle;

public abstract class Individual implements Comparable<Individual> {
	/**
	 * The individual's score in his environment
	 */
	protected int fitness;
	
	// main methods of an individual
	public abstract boolean decideJump(Obstacle obstacle, Bird bird);
	public abstract void mutate(double mutAmpl, double mutProba);

	// Getters and setters for the fitness
	public int getFitness(){return fitness;}
	public void setFitness(int fitness) {this.fitness = fitness;}
	
	/**
	 * Comparing method used for sorting the array
	 */
	public int compareTo(Individual ind) { // descending order
		return ind.getFitness() - this.fitness;
	}
}
