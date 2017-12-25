package ia;

import java.util.ArrayList;
import java.util.Arrays;

import Main.Main;
import Modele.Bird;
import Modele.Game;
import Modele.Obstacle;

/**
 * The mother abstract class to every approach for the Genetic algorithm. Its main field is the population which is an array of (abstract) {@link Individual Individuals}. From there we can generalize the Algorithm's behaviour no matter the ADN type chosen, based on the tasks an Individual will always be able to carry out (ie jump).
 */
public abstract class Genetic {
	// Population-related attributes
	protected int sizePop;
	protected Individual[] population;
	public static int GENERATION;
	public static InfoGenetic infoGenetic;
	// mutation-related variables
	protected double mutationAtZero;
	protected double mutProba;

	// Game's attributes
	protected Bird[] birds;
	protected ArrayList<Obstacle> obstacles;
	
	/**
	 * Checks whether the population died (each individual died).
	 * @return a boolean = (if every individual is dead)
	 */
	public boolean generationDead() {
		boolean isDead = true;
		int i = 0;
		while(isDead && i < sizePop ) {
			isDead = birds[i].isDead();
			i++;
		}
		return isDead;
	}
	
	/**
	 * When the generation died, update the display, update game info and select
	 * @param game the new, current game
	 */
	public void update(Game game) {
		// Update the info onscreen
		infoGenetic.update(GENERATION++, birds);
		// update the population's values of fitness
		for (int i = 0; i < sizePop; i++) {
			population[i].setFitness(birds[i].getScore());
		}
		birds = game.getBirds();
		obstacles = game.getObstacles();
		
		// MOST IMPORTANT OF ALL, THE SELECTION. (in subclass)
//		population = someSelection();
	}
	
	/**
	 * Getter for the size of the population
	 * @return the size of the population
	 */
	public int getSizePop() {return sizePop;}	
	
	/**
	 * Asks each individual of the population whether to jump
	 * @return an array of booleans for the decision of each individual
	 */
	public boolean[] getJump() {
		boolean[] jumps = new boolean[sizePop];
		if(obstacles.size() > 0) {
			for (int i = 0; i < sizePop; i++) {
				jumps[i] = population[i].decideJump(obstacles.get(0), birds[i]);
			}
		}		
		return jumps;
	}
	/**
	 * The amplitude of the mutation, which can decrease as an exponential over time (generations). Used within crossover
	 * @return the ampiltude of the mutation, given the generation number
	 */
	protected double mutationDecrease(boolean descrease) { 
		if (descrease) {
			// parameters for building an exponential passing through two given points and above a threshold
			double valueAtFifty = 0.0001;
			double minValue = 0; // different form valAt0 : where you converge to
			
			// just solving equations
			double alpha = mutationAtZero - minValue;
			double beta = -(1/50f)*Math.log((valueAtFifty-minValue)/alpha);
			return (alpha*Math.exp(-GeneticNN.GENERATION*beta)+minValue);
		} else {
			return mutationAtZero;
		}
	}	
	
	/**
	 * The crossover method which crosses the genes of two parents to output an offspring, with some mutation. Abstract, for it depends on the ADN type.
	 * @param parent1 the first parent to get an offspring out of
	 * @param parent2 the second parent to get an offspring out of
	 * @return the offspring as a combination of the parent's genes + mutations
	 */
	protected abstract Individual crossover(Individual parent1, Individual parent2) throws IllegalArgumentException;
	/**
	 * The mutation the individual can undergo, if it is not done directly within crossover (last case is more optimized but sometimes unapplicable). Abstract, for it depends on the ADN type.
	 * @param indiv the original Individual which to mutate
	 * @return the mutated individual
	 */
	protected Individual mutate(Individual indiv, double mutAmpl) {
		indiv.mutate(mutAmpl, mutProba);
		return indiv; // mutated
	}
	
	// ALL DIFFERENT SELECTION METHODS
	/**
	 * The original, linear selection method with heavy melting pot
	 * @return the new, selected population as crossover of (hopefully) the best individuals
	 */
	protected Individual[] selection(){
		ArrayList<Individual> meltingPot = new ArrayList<Individual>();
		int minfitness = population[0].getFitness();
		int maxfitness = population[0].getFitness();
		for (int i = 1; i < sizePop; i++) {
			if(minfitness > population[i].getFitness()) {
				minfitness = population[i].getFitness();
			}
			if(maxfitness < population[i].getFitness()) {
				maxfitness = population[i].getFitness();
			}
		}
		if (minfitness < 0) {
			for (int i = 0; i < sizePop; i++) {
				population[i].setFitness(population[i].getFitness()-minfitness+1);
			}
			maxfitness -=minfitness -1;
		}

		for (int i = 0; i < sizePop; i++) {
			for(int j = 0; j < population[i].getFitness()/(float)maxfitness*100; j++) {
				meltingPot.add(population[i]);
			}
		}
		Individual[] newPop = new Individual[sizePop];
		for(int i = 0; i < sizePop; i++) {
			Individual parentA = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			Individual parentB = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			newPop[i] = crossover(parentA, parentB);
		}

		return newPop;
	}
	
	/**
	 * The better version of the original selection with cumulative index, where we can choose a (convex, increasing) function to ensure the choice of the best individuals.
	 * @return the new, hopefully more performant population.
	 */
	protected Individual[] functionalSelection(int pow){
		double[] fitnesses = new double[sizePop];
		double[] cumulateFitnesses = new double[sizePop];
		double minfitness = population[0].getFitness();
		double sum = 0;
		for (int i = 1; i < sizePop; i++) {
			// apply desired function
			fitnesses[i] = RealFunctions.polynomial(population[i].getFitness(), pow);
			if(minfitness > fitnesses[i]) {
				minfitness = fitnesses[i];
			}
			sum += fitnesses[i];
		}
		
		if (minfitness < 0) { // case you have to lift everyone up
			sum -= minfitness*sizePop;
			for (int i = 0; i < sizePop; i++) {
				fitnesses[i] -= minfitness; // lift every value >= 0
				fitnesses[i] /= sum; // probabilitification 
				if (i==0) {
					cumulateFitnesses[i] = fitnesses[i];
				} else {
					cumulateFitnesses[i] = fitnesses[i] + cumulateFitnesses[i-1];
				}
			}
		} else { // already above, just probabilitize it
			for (int i = 0; i < sizePop; i++) {
				fitnesses[i] /= sum;
				if (i==0) {
					cumulateFitnesses[i] = fitnesses[i];
				} else {
					cumulateFitnesses[i] = fitnesses[i] + cumulateFitnesses[i-1];
				}
			}
		}
		// now we have an array of increasing values in ]0,1]
			
		Individual[] newPop = new Individual[sizePop];
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			Individual parentA = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			Individual parentB = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			newPop[i]=(crossover(parentA, parentB));
		}
		return newPop;
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
	
	/**
	 * Exponential selection, the most effective so far. Selects the individuals based on how well they did compared to the rest : the individuals are sorted, and starting from the first one, they are all given the probability rankSelectProba to be selected to breed; which means the ith indivual has probability rSP*(1-rSP)^i of being selected.
	 * @param rankSelectProba the probability for the current individual to be selected.
	 * @return the new, hopefully more performant population
	 */
	protected Individual[] rankSelection(double rankSelectProba){
		Arrays.sort(population);
		Individual[] newPop = new Individual[sizePop];
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			Individual parentA = population[rankSelecter(sizePop, rankSelectProba)];
			Individual parentB = population[rankSelecter(sizePop, rankSelectProba)];
			newPop[i] = crossover(parentA, parentB);
		}
		return newPop;
	}
	
	protected Individual[] rankSelectionKeeper(double keeper, double rankSelectProba){
		Arrays.sort(population);
		Individual[] newPop = new Individual[sizePop];
		for(int i = 0; i < sizePop; i++) {
			if (i < sizePop*keeper) {
				// keep 1% of the pop (the best)
				newPop[i] = population[i];
			} else {
				// now, grabbing parent corresponding to best ranks
				Individual parentA = population[rankSelecter(sizePop, rankSelectProba)];
				Individual parentB = population[rankSelecter(sizePop, rankSelectProba)];
				newPop[i] = crossover(parentA, parentB);
			}
		}
		return newPop;
	}
	
	// TODO finish implementing this based on the video
	protected Individual[] videoSelection(double keeper, double bestCross) {
		Arrays.sort(population);
		Individual[] newPop = new Individual[sizePop];
		for (int i = 0; i < sizePop; i++) {
			if (i<sizePop*keeper) {
				newPop[i] = population[i];
			} else if (i < sizePop*(keeper + bestCross)) {
				newPop[i] = crossover(newPop[0],newPop[1]);
			} else {
				newPop[i] = crossover(newPop[Main.rand.nextInt((int)(sizePop*keeper))], 
						  			  newPop[Main.rand.nextInt((int)(sizePop*keeper))]);
			}
			newPop[i].mutate(mutationDecrease(false), mutProba);	
		}
		return newPop;
	}
	
	/**
	 * Selects the index of the individual to act as a parent in the rankSelection() methods. Given the rankSelectionProba, it loops while no individual has been chosen by this probability. Please refer to {@link Genetic.rankSelection the rankSelection method}
	 * @param size the size of the population - to know where to stop , may no individual be chosen before the last
	 * @param proba the probability to select each individual once their turn has come (plain probability for the 0th individual to be selected)
	 * @return the index of the indiidual that will breed.
	 */
	protected static int rankSelecter(int size, double proba) {
		int index = 0;
		while (!(Main.rand.nextDouble() < proba) && (index++ < size -2));
		return index;
	}
	
	/**
	 * Debugging method : prints on console the fitnesses of every individual
	 */
	public void printPopFitness() {
		System.out.println("Population Fitnesses :");
		for (Individual indI : population) {
			System.out.println(indI.getFitness());
		}
	}
	
	/**
	 * Set of functions to be used by the functionalSelection method
	 */
	protected static class RealFunctions {
		protected static double identity(double x) {
			return x;
		}
		protected static double polynomial(double x, double pow) {
			return Math.pow(Math.max(0, x), pow);
		}
	}
}
