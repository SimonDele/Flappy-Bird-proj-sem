package ia.sel;

import ia.Individual;
import ia.sel.rf.RealFunction;
import mainPkg.Main;

/**
 * Enhanced version of the very simple {@linkplain BasicSelection BasicSelection}. 
 * The melting-pot is replaced by the probabilistic cumulative array cumulateFitnesses.
 * The probability can now be chosen as a real function, defined in the class. (real functions should be positive, increasing convex ones, passing by (1,1) if possible)
 */
public class FunctionalSelection implements Selection {
	private RealFunction f;
	public FunctionalSelection(RealFunction f) {
		this.f = f;
	}
	
	/**
	 * Selects the index of the individual corresponding to a given, random double in ]0,1[ according to the cumulative array
	 * @param cumulative the array of increasing values in ]0,1] where the interval between two values ((i-1)th, ith) gives the probability for the selection of the ith individual
	 * @param e the random picker, which will most likely fall within the biggest (best individuals) region.
	 * @return the index of the corresponding individual
	 */
	protected int cumulativeIndex(double[] cumulative, double e) {
		int i=-1;
		while (cumulative[++i] < e);
		return i;
	}
	
	@Override
	public Individual[] select(Individual[] population, double mutAmpl, double mutProba) {
		int sizePop = population.length;
		double[] fitnesses = new double[sizePop];
		double[] cumulateFitnesses = new double[sizePop];
		double sum = 0;
		int maxfitness = population[0].getFitness();
		for (int i = 1; i < sizePop; i++) {
			if(maxfitness < population[i].getFitness())
				maxfitness = population[i].getFitness();
		}
		
		for (int i = 0; i < sizePop; i++) {
			// Apply function to reduced 
			fitnesses[i] =  f.applyTo(population[i].getFitness()/(double)maxfitness);
			sum += fitnesses[i];
		}
		// Generate cumuateFitnesses
		cumulateFitnesses[0] = fitnesses[0];
		for (int i = 1; i < sizePop; i++) {
			fitnesses[i] /= sum;
			cumulateFitnesses[i] = fitnesses[i] + cumulateFitnesses[i-1];
		}
		// now we have an array of sizePop increasing values in ]0,1]

		Individual[] newPop = new Individual[sizePop];
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			Individual parentA = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			Individual parentB = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			newPop[i] = parentA.crossover(parentB, mutAmpl, mutProba);
		}
		return newPop;
	}
}
