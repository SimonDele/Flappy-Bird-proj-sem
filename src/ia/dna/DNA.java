package ia.dna;

import model.Obstacle;
import model.Whale;

/**
 * Interface that sums up the methods that will be asked to any type that wants to be used as genes for the Genetic algorithm.
 */
public interface DNA {
	/**
	 * Asks the DNA model whether to jump, based on the obstacle and the bird it controls
	 * @param obstacle the obstacle to dodge
	 * @param whale the bird associated with the DNA implementation
	 * @return a boolean = (if bird should jump)
	 */
	public boolean decidejump(Obstacle obstacle, Whale whale);
	/**
	 * Tweaks the DNA implementation as a mutation. Never used in practice : usually nested in crossover; some specific Selection methods might require this.
	 * @param mutAmpl the amplitude of the mutations to apply
	 * @param mutProba the probability for a mutation to happen
	 * @deprecated
	 */
	public void mutate(double mutAmpl, double mutProba);
	/**
	 * Merges two sets of DNA with some mutations to create an offspring
	 * @param otherDNA the DNA of the other parent, to merge with this instance of DNA.
	 * @throws IllegalArgumentException if the other parent is not of the same DNA
	 * @return the offspring as product of the breeding of the two DNAs
	 */
	public DNA crossover(DNA otherDNA, double mutAmpl, double mutProba) throws IllegalArgumentException;
}
