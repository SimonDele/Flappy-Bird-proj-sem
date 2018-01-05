package ia;

import java.util.ArrayList;
import ia.dna.DNA;
import ia.sel.Selection;
import model.Game;
import model.Obstacle;
import model.Whale;

/**
 * The class managing any approach for the Genetic algorithm. Its main field is the population which is an array of {@link Individual Individuals}. TODO change : From there we can generalize the Algorithm's behaviour no matter the DNA type chosen, based on the tasks an Individual will always be able to carry out (ie jump).
 */
public class Genetic {
	// Population-related attributes
	private int sizePop;
	private Individual[] population;
	public static int GENERATION;
	public static InfoGenetic infoGenetic;
	
	// mutation-related variables
	private double mutationAtZero;
	private double mutProba;
	private boolean decrease;
	
	// Selection-related
	private Selection selector;
	
	// Game's attributes
	private Whale[] whales;
	private ArrayList<Obstacle> obstacles;
	
	// Optimizer for apt DNA type
	private int framesPerAction;

	/**
	 * Main Genetic Constructor. Initializes his attributes based on the inputs given.
	 * @param game the game to play on. The values of whale and obstacle are passed by reference.
	 * @param sizePop the size of the population to apply the algorithm on
	 * @param dnaImpl the Class to use as the implementation of DNA for the Individuals of the population.
	 * @param selector the selection method to use to select the next generation
	 * @param framesPerAction the number of frames per action allowed to the AI chosen by the user
	 */
	public Genetic(Game game, int sizePop, Class<? extends DNA> dnaImpl, Selection selector, int framesPerAction) {
		// Initialization of the info, genetic and game's attributes
		Genetic.infoGenetic = new InfoGenetic(GENERATION);
		Genetic.GENERATION = 0;
		this.sizePop = sizePop;
		this.whales = game.getBirds();
		this.obstacles = game.getObstacles();
		this.framesPerAction = framesPerAction;
		
		/// Hyperparameters :
		// Mutation
		this.mutationAtZero = 0.05;
		this.mutProba = 0.1;
		this.decrease = true;
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
