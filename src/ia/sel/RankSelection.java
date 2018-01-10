package ia.sel;

import java.util.Arrays;

import ia.Genetic;
import ia.Individual;
import mainPkg.Main;

/**
 * Exponential selection, the most effective so far. Selects the individuals based on how well they did compared to the rest : the individuals are sorted, and starting from the first one, they are all given the probability rankSelectProba to be selected to breed; which means the ith indivual has probability rSP*(1-rSP)^i of being selected.
 */
public class RankSelection implements Selection {
	protected double rankSelectProba;
	
	/**
	 * Constructor giving an empirically good value (0.05) to the attribute.
	 */
	public RankSelection() {
		this.rankSelectProba = 0.05;
	}
	
	/**
	 * Simply gives to the attribute the value of the parameter.
	 * @param rankSelectProba the parameter to set the attribute value to.
	 */
	public RankSelection(double rankSelectProba) {
		this.rankSelectProba = rankSelectProba;
	}
	
	@Override
	public Individual[] select(Individual[] population, double mutAmpl, double mutProba) {
		int sizePop = population.length;
		Arrays.sort(population);
		Individual[] newPop = new Individual[sizePop];
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			Individual parentA = population[rankSelecter(sizePop, rankSelectProba)];
			Individual parentB = population[rankSelecter(sizePop, rankSelectProba)];
			newPop[i] = parentA.crossover(parentB, mutAmpl, mutProba);
		}
		return newPop;
	}

	/**
	 * Selects the index of the individual to act as a parent in the rankSelection() methods. Given the rankSelectionProba, it loops while no individual has been chosen by this probability. Please refer to {@link Genetic.rankSelection the rankSelection method}
	 * @param size the size of the population - to know where to stop , may no individual be chosen before the last
	 * @param proba the probability to select each individual once their turn has come (plain probability for the 0th individual to be selected)
	 * @return the index of the individual that will breed.
	 */
	protected static int rankSelecter(int size, double proba) {
		int index = 0;
		while (!(Main.rand.nextDouble() < proba) && (index++ < size -2));
		return index;
	}	
}
