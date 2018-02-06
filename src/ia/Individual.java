package ia;

import ia.dna.DNA;
import model.Obstacle;
import model.Whale;

/**
 * An Individual, in the sense of the Genetic Algorithm - hence a tuple (fitness, DNA). Implements Comparable to be sorted fitnesswise; else heavily relies on its DNA.
 */
public class Individual implements Comparable<Individual> {
	/**
	 * The individual's score in his environment
	 */
	protected int fitness;
	/**
	 * The individual's DNA, responsible for its behaviour.
	 */
	protected DNA dna;
	
	/**
	 * Sets the fitness to 0 and the dna to the given value
	 * @param dna for the field of the same name
	 */
	public Individual(DNA dna) {
		this.fitness = 0;
		this.dna = dna;
	}
	
	/**
	 * Uses the generic constructor of the Class passed in argument to initialize its DNA. For gen 0
	 * @param dnaImpl the Class to be used as DNA
	 */
	public Individual(Class<? extends DNA> dnaImpl) {
		this.fitness = 0;
		try {
			this.dna = dnaImpl.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.println("Error in instanciating the implementation of DNA");
			e.printStackTrace();
		}
	}

	// main methods of an individual
	/**
	 * Calls the Individual's DNA "decideJump" method, as the Individual's behaviour is completely determined by its DNA.
	 * @param obstacle the obstacle to dodge
	 * @param whale the bird the Individual is controlling
	 * @return a boolean = (whether to jump or not)
	 */
	public boolean decideJump(Obstacle obstacle, Whale whale) {
		return dna.decidejump(obstacle, whale);
	}
	/**
	 * Never used in practice, usually mutation are nested in crossover; but some Selection methods might require some function to mutate afterwards (e.g. "keepers")
	 * @param mutAmpl the amplitude of the mutation, when it makes sense
	 * @param mutProba the probability of the mutation, when it makes sense
	 * @deprecated
	 */
	public void mutate(double mutAmpl, double mutProba) {
		dna.mutate(mutAmpl, mutProba);
	}
	/**
	 * Breeding of an Individual with another, giving birth to an offsrping as some arrangement of their genes, with some mutations
	 * @param otherParent the parent to breed this one with
	 * @return the new Individual born from both parents
	 */
	public Individual crossover(Individual otherParent, double mutAmpl, double mutProba) {
		return new Individual(this.dna.crossover(otherParent.getDNA(), mutAmpl, mutProba));
	}

	// Getters and setters
	public int getFitness(){return fitness;}
	public void setFitness(int fitness) {this.fitness = fitness;}
	public DNA getDNA() {return this.dna;}
	
	/**
	 * Comparing method used for sorting the array
	 */
	public int compareTo(Individual ind) { // descending order
		return ind.getFitness() - this.fitness;
	}
}
