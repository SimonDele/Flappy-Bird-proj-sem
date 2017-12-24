package ia;

import java.util.ArrayList;
import java.util.Arrays;

import Main.Main;
import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class GeneticNN {

	private int sizePop;
	private IndividualNN[] population;
	public static int GENERATION;
	public static InfoGenetic infoGenetic;
	// hyperparameters
	private double mutationProba;
	private double parentProba;
	private double initMinWeight;
	private double initMaxWeight;
	private double selectPower;
	private double rankSelectProba;
	private double mutationAtZero;
	private double keeper;
	private double bestCross;
	// Game's attributes
	private Bird[] birds;
	private ArrayList<Obstacle> obstacles;
	
	public GeneticNN(Jeu jeu, int sizePop) {
		infoGenetic = new InfoGenetic(GENERATION);
		this.sizePop = sizePop;
		birds = jeu.getBirds();
		obstacles = jeu.getObstacles();
		GENERATION = 0;
		
		/// Hyperparameters :
		// Mutation
		mutationAtZero = 0.05; // Amplitude. "zero" bcs it can evolve over time (not now)
		mutationProba = 0.1; 
		// NN Weights
		initMinWeight = -0.5;
		initMaxWeight = 0.5;
		// Selection & Crossover
		parentProba = 0.5;
		rankSelectProba = 0.2;
		selectPower = 5;
		// For the "video selection" - not finished
		keeper = 0.01;
		bestCross = 0.2;
		
		population = new IndividualNN[sizePop];
		int[] hidden = {6};
		for(int i=0; i<sizePop; i++) {
			try {
				population[i] = new IndividualNN(2,1,hidden,initMinWeight,initMaxWeight);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
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
		population = rankSelectionKeeper();
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
	
	private IndividualNN[] selection(){
		ArrayList<IndividualNN> meltingPot = new ArrayList<IndividualNN>();
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
				meltingPot.add(population[i]);
			}
		}
		IndividualNN[] newPop = new IndividualNN[sizePop];
		for(int i = 0; i < sizePop; i++) {
			IndividualNN parentA = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			IndividualNN parentB = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			newPop[i] = crossover(parentA.getNN(), parentB.getNN());
		}

		return newPop;
	}
	
	private IndividualNN[] functionalSelection(){
		double[] fitnesses = new double[sizePop];
		double[] cumulateFitnesses = new double[sizePop];
		double minfitness = population[0].getFitness();
		double sum = 0;
		for (int i = 1; i < sizePop; i++) {
			// apply desired function
			fitnesses[i] = identity(population[i].getFitness());
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
			
		IndividualNN[] newPop = new IndividualNN[sizePop];
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			IndividualNN parentA = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			IndividualNN parentB = population[cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble())];
			newPop[i]=(crossover(parentA.getNN(), parentB.getNN()));
		}
		
		return newPop;
	}
	
	public int cumulativeIndex(double[] cumulative, double e) {
		int i=-1;
		while (cumulative[++i] < e);
		return i;
	}
	
	public double identity(double x) {
		return x;
	}
	public double polynomial(double x, double pow) {
		return Math.pow(Math.max(0, x), pow);
	}
	
	private IndividualNN[] rankSelection(){
		Arrays.sort(population);
		IndividualNN[] newPop = new IndividualNN[sizePop];
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			IndividualNN parentA = population[rankSelecter(sizePop, rankSelectProba)];
			IndividualNN parentB = population[rankSelecter(sizePop, rankSelectProba)];
			newPop[i] = crossover(parentA.getNN(), parentB.getNN());
		}
		return newPop;
	}
	
	private IndividualNN[] rankSelectionKeeper(){
		Arrays.sort(population);
		IndividualNN[] newPop = new IndividualNN[sizePop];
		for(int i = 0; i < sizePop; i++) {
			if (i < sizePop*keeper) {
				// keep 1% of the pop (the best)
				newPop[i] = population[i];
			} else {
				// now, grabbing parent corresponding to best ranks
				IndividualNN parentA = population[rankSelecter(sizePop, rankSelectProba)];
				IndividualNN parentB = population[rankSelecter(sizePop, rankSelectProba)];
				newPop[i] = crossover(parentA.getNN(), parentB.getNN());
			}
		}
		return newPop;
	}
	
	private IndividualNN[] videoSelection() {
		Arrays.sort(population);
		IndividualNN[] newPop = new IndividualNN[sizePop];
		for (int i = 0; i < sizePop; i++) {
			if (i<sizePop*keeper) {
				newPop[i] = population[i];
			} else if (i < sizePop*(keeper + bestCross)) {
				newPop[i] = crossover(newPop[0].getNN(),newPop[1].getNN());
			} else {
				newPop[i] = crossover(newPop[Main.rand.nextInt((int)(sizePop*keeper))].getNN(), 
						  			  newPop[Main.rand.nextInt((int)(sizePop*keeper))].getNN());
			}
			newPop[i].mutate(mutationAtZero);	

		}
		
		return newPop;
	}
	
	private static int rankSelecter(int size, double proba) {
		int index = 0;
		while (!(Main.rand.nextDouble() < proba) && (index++ < size -2));
		return index;
	}
	
	private IndividualNN crossover(NeuralNet nn1, NeuralNet nn2) {
		NeuralNet newNN = nn1;
		for (int layer = 0; layer < newNN.getWeights().length; layer++) {
			for (int i = 0; i < newNN.getWeights()[layer].getRowDimension(); i++) {
				// weights loop
				for (int j = 0; j < newNN.getWeights()[layer].getColumnDimension(); j++) {
					if (Main.rand.nextFloat() < parentProba) {
						newNN.getWeights()[layer].set(i,j,nn2.getWeights()[layer].get(i,j));
					}
					if (Main.rand.nextFloat() < mutationProba) {
						newNN.getWeights()[layer].set(i,j,newNN.getWeights()[layer].get(i,j) 
								+ (Main.rand.nextDouble() - 0.5)*2*mutationAmplitude());
					}
				}
			}
		}
		return new IndividualNN(newNN);
	}

	private double mutationAmplitude() { 
		// parameters for building an exponential passing through two given points and above a threshold

		double valueAtFifty = 0.05;
		double minValue = 0; // different form valAt0 : where you converge to
		
		// just solving equations
//		double alpha = mutationAtZero - minValue;
//		double beta = -(1/50f)*Math.log((valueAtFifty-minValue)/alpha);
//		return (alpha*Math.exp(-GeneticNN.GENERATION*beta)+minValue);
		return mutationAtZero;
	}	

}
