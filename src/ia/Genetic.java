package ia;

import java.util.ArrayList;
import java.util.Arrays;

import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;
import Main.Main;

public class Genetic {
	// Genetic's attributes
	private int sizePop;
	private IndividualBool[] population;
	public static int GENERATION;
	public static InfoGenetic infoGenetic;
	int selectPower;
	double rankSelectProba;

	// Game's attributes
	private Bird[] birds;
	private ArrayList<Obstacle> obstacles;
	
	public Genetic(Jeu jeu, int sizePop) {
		infoGenetic = new InfoGenetic(GENERATION);
		this.sizePop = sizePop;
		birds = jeu.getBirds();
		obstacles = jeu.getObstacles();
		GENERATION = 0;
		selectPower = 4;
		rankSelectProba = 0.1;
		population = new IndividualBool[sizePop];
		for(int i=0; i<sizePop; i++) {
			population[i] = new IndividualBool(2*Jeu.DIMY, Obstacle.MINDIST); 
		}
	}
	
	public boolean generationDead() {
		boolean isDead = true;
		int i = 0;
		while(isDead && i < sizePop ) {
			isDead = birds[i].isDead();
			i++;
		}
		return isDead;
			
	}
	public void update(Jeu jeu) {
		infoGenetic.update(GENERATION++, birds);
		for (int i = 0; i < sizePop; i++) {
			population[i].setFitness(birds[i].getScore());
		}
		population = rankSelection();
		birds = jeu.getBirds();
		obstacles = jeu.getObstacles();
		
	}
	public int getSizePop() {
		return sizePop;
	}	
	
	public boolean[] getJump() {
		boolean[] jumps = new boolean[sizePop];
		
		for (int i = 0; i < sizePop; i++) {
			if(obstacles.size() > 0) {
				jumps[i] = population[i].decideJump(obstacles.get(0), birds[i]);
			}

		}
		return jumps;
	}
	
	private ArrayList<IndividualBool> selection(){
		ArrayList<Integer> meltingPot = new ArrayList<Integer>();
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
			for(int j = 0; j < Math.pow(1+population[i].getFitness()/(float)maxfitness,selectPower); j++) {
				meltingPot.add(i);
			}
		}
		ArrayList<IndividualBool> newPop = new ArrayList<IndividualBool>();
		for(int i = 0; i < sizePop; i++) {
			IndividualBool parentA = population[meltingPot.get(Main.rand.nextInt(meltingPot.size()))];
			IndividualBool parentB = population[meltingPot.get(Main.rand.nextInt(meltingPot.size()))];
			newPop.add(crossover2(parentA.getGenes(), parentB.getGenes(), parentA.getNRow(), parentA.getNCol()));
		}

		return newPop;
	}
	
	private ArrayList<IndividualBool> functionalSelection(){
		double[] fitnesses = new double[sizePop];
		double[] cumulateFitnesses = new double[sizePop];
		double minfitness = population[0].getFitness();
		double sum = 0;
		for (int i = 1; i < sizePop; i++) {
			// apply desired function
			fitnesses[i] = identity(population[i].getFitness()); 
			fitnesses[i] = polynomial(population[i].getFitness(), selectPower);
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
			
		ArrayList<IndividualBool> newPop = new ArrayList<IndividualBool>();
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			IndividualBool parentA = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			IndividualBool parentB = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			newPop.add(crossover(parentA.getGenes(), parentB.getGenes(), parentA.getNRow(), parentA.getNCol()));
		}
		
		return newPop;
	}
	
	private IndividualBool[] rankSelection(){
		Arrays.sort(population);
		IndividualBool[] newPop = new IndividualBool[sizePop];
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			IndividualBool parentA = population[rankSelecter(sizePop, rankSelectProba)];
			IndividualBool parentB = population[rankSelecter(sizePop, rankSelectProba)];
			newPop[i] = crossover(parentA.getGenes(), parentB.getGenes(), parentA.getNRow(), parentA.getNCol());
		}
		return newPop;
	}
	
	private static int rankSelecter(int size, double proba) {
		int index = 0;
		while (!(Main.rand.nextDouble() < proba) && (index++ < size -1));
		return index;
	}
	
	public int cumulativeIndex(double[] cumulative, double e) {
		int i=-1;
		while (cumulative[++i] < e);
		return i;
	}
	
	// Functions for functional Selection
	public double identity(double x) {
		return x;
	}
	public double polynomial(double x, double pow) {
		return Math.pow(Math.max(0, x), pow);
	}
	
	private IndividualBool crossover(Boolean[][] genesA, Boolean[][] genesB, int nrow, int ncol) {
		Boolean[][] newGenes = new Boolean[nrow][ncol];
		for(int i = 0; i < nrow;  i++) {
			for(int j = 0; j < ncol ; j++) {
				if(Main.rand.nextFloat() < 0.5f) {
					newGenes[i][j] = genesA[i][j];
				}else {
					newGenes[i][j] = genesB[i][j];
				}
				if (Main.rand.nextFloat() < mutationProba()) {
					newGenes[i][j] = !newGenes[i][j];
				}
			}
		}
		return new IndividualBool(newGenes, nrow, ncol);
	}
	private IndividualBool crossover2(Boolean[][] genesA, Boolean[][] genesB, int nrow, int ncol) {
		Boolean[][] newGenes = genesA;
		for(int i = 0; i < nrow;  i++) {
			for(int j = 0; j < ncol ; j++) {
				if(Main.rand.nextFloat() < 0.5f) {
					newGenes[i][j] = genesB[i][j];
				}
				if (Main.rand.nextFloat() < mutationProba()) {
					newGenes[i][j] = !newGenes[i][j];
				}
			}
		}
		return new IndividualBool(newGenes, nrow, ncol);
	}
	
	private double mutationProba() { 
		// parameters for building an exponential passing through two given points and above a threshold
		double valueAtZero = 0.05;
		double valueAtFifty = 0.0001;
		double minValue = 0; // different form valAt0
		
		// just solving equations
		double alpha = valueAtZero - minValue;
		double beta = -(1/50f)*Math.log((valueAtFifty-minValue)/alpha);
		return (alpha*Math.exp(-Genetic.GENERATION*beta)+minValue);
	}
	
	/*
	 * 
	 * mutation
	 * 
	 */
	
}
