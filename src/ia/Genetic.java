package ia;

import java.util.ArrayList;
import ia.dna.DNA;
import ia.sel.Selection;
import model.Game;
import model.Obstacle;
import model.Whale;

/**
 * The class managing any approach for the Genetic algorithm. Its main field is the population which is an array of {@link Individual Individuals}. It can run no matter the DNA implementation chosen for the Individuals.
 */
public class Genetic {
	// Population-related attributes
	/**
	 * Number of individuals in the population
	 */
	private int sizePop;
	/**
	 * Array of Individuals modelling the population
	 */
	private Individual[] population;
	/**
	 * Number of the current generation
	 */
	public static int GENERATION;
	/**
	 * Class to provide visual information on the welfare of the generations
	 */
	public static InfoGenetic infoGenetic;
	
	// mutation-related variables
	/**
	 * Mutation Amplitude value at generation 0
	 */
	private double mutationAtZero;
	/**
	 * The probability of a mutation occuring
	 */
	private double mutProba;
	/**
	 * Whether the mutation amplitude should decrease over time (generations) or not
	 */
	private boolean decrease;
	
	/**
	 * The selection method chosen by the user to be used by the genetic Algorithm
	 */
	private Selection selector;
	
	// Game's attributes
	/**
	 * Instance of the game's whales (pointer), to prevent too much data flow
	 */
	private Whale[] whales;
	/**
	 * Instance of the game's obstacles (pointer), to prevent too much data flow
	 */
	private ArrayList<Obstacle> obstacles;
	
	// Optimizer for apt DNA type
	/**
	 * Number of frames per action allowed to the Individuals. In the end, never used in practice : too difficult for AIs
	 * @deprecated
	 */
	private int framesPerAction;

	/**
	 * Main Genetic Constructor. Initializes his attributes based on the inputs given.
	 * @param game the game to play on. The values of whale and obstacle are passed by reference.
	 * @param sizePop the size of the population to apply the algorithm on
	 * @param dnaImpl the Class to use as the implementation of DNA for the Individuals of the population.
	 * @param selector the selection method to use to select the next generation
	 * @param framesPerAction the number of frames per action allowed to the AI chosen by the user
	 */
	public Genetic(Game game, int sizePop, Class<? extends DNA> dnaImpl, Selection selector, double mutationAtZero, boolean decrease, int framesPerAction) {
		// Initialization of the info, genetic and game's attributes
		Genetic.infoGenetic = new InfoGenetic(GENERATION);
		Genetic.GENERATION = 0;
		this.sizePop = sizePop;
		this.whales = game.getBirds();
		this.obstacles = game.getObstacles();
		this.framesPerAction = framesPerAction;
		/// Hyperparameters :
		// Mutation
		this.mutationAtZero = mutationAtZero;
		this.mutProba = 0.2;
		this.decrease = decrease;
		// Selection
		this.selector = selector;
		
		// Population intitialization
		this.population = new Individual[sizePop];
		for(int i=0; i<sizePop; i++) {
			this.population[i] = new Individual(dnaImpl);
		}
	}
	
	/**
	 * Checks whether the population died (each individual died).
	 * @return a boolean = (if every individual is dead)
	 */
	public boolean generationDead() {
		boolean isDead = true;
		int i = 0;
		while(isDead && i < sizePop ) {
			isDead = whales[i].isDead();
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
		infoGenetic.update(++GENERATION, whales);
		// update the population's values of fitness
		for (int i = 0; i < sizePop; i++) {
			population[i].setFitness(whales[i].getFitness());
		}
		// update game's attributes

		whales = game.getBirds();
		obstacles = game.getObstacles();
		
		population = selector.select(population, mutationDecrease(decrease), mutProba);
	}

	/**
	 * Asks each individual of the population whether to jump
	 * @return an array of booleans for the decision of each individual
	 */
	public boolean[] getJumps() {
		boolean[] jumps = new boolean[sizePop];
		if(obstacles.size() > 0) {
			for (int i = 0; i < sizePop; i++) {
				jumps[i] = population[i].decideJump(obstacles.get(0), whales[i]);
			}
		}		
		return jumps;
	}
	
	/**
	 * Getter for the size of the population
	 * @return the size of the population
	 */
	public int getSizePop() {return sizePop;}	
	/**
	 * Getter for the number of frames per Action chosen by the user for his DNA type
	 * @return the number of frames per action associated with this run
	 */
	public int getFramesPerAction() {return framesPerAction;}
	
	/**
	 * The amplitude of the mutation, which can decrease as an exponential over time (generations). Used within crossover
	 * @return the ampiltude of the mutation, given the generation number
	 */
	private double mutationDecrease(boolean decrease) { 
		if (decrease) {
			// parameters for building an exponential passing through two given points and above a threshold
			double valueAtFifty = 0.0001;
			double minValue = 0; // different form valAt0 : where you converge to
			
			// just solving equations
			double alpha = mutationAtZero - minValue;
			double beta = -(1/50f)*Math.log((valueAtFifty-minValue)/alpha);
			return (alpha*Math.exp(-Genetic.GENERATION*beta)+minValue);
		} else {
			return mutationAtZero;
		}
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
}
