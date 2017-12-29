package ia.sel;

import ia.Individual;

/**
 * The interface that guides every Selection class. Only consists of one method to breed the old generation to make the new one.
 */
public interface Selection {
	/**
	 * The method called by Genetic to update his population after the previous one died. Calls the crossover method and some mutation.
	 * @param population the population to breed to create the new population
	 * @param mutAmpl the amplitude of the mutations
	 * @param mutProba the probability for a mutation to happen
	 * @return the new, hopefully better population
	 */
	public Individual[] select(Individual[] population, double mutAmpl, double mutProba);
}