package ia;

import java.util.ArrayList;
import java.util.Arrays;

import Main.Main;
import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class GeneticNN {

	private int sizePop;
	private ArrayList<IndividualNN> pop;
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
		mutationAtZero = 0.1;
		mutationProba = 0.1;
		// NN Weights
		initMinWeight = -1;
		initMaxWeight = 1;
		// Selection
		parentProba = 0;
		rankSelectProba = 0.1;
		selectPower = 5;
		
		pop = new ArrayList<IndividualNN>();
		population = new IndividualNN[sizePop];
		int[] hidden = {6};
		for(int i=0; i<sizePop; i++) {
			try {
				pop.add(new IndividualNN(2,1,hidden,initMinWeight,initMaxWeight)); // the netting will be done later
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
			pop.get(i).setFitness(birds[i].getScore());
			population[i].setFitness(birds[i].getScore());
		}
		pop = functionalSelection();
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
				jumps[i] = pop.get(i).decideJump(obstacles.get(0), birds[i]);
				jumps[i] = population[i].decideJump(obstacles.get(0), birds[i]);
			}

		}
		return jumps;
	}
	
	private ArrayList<IndividualNN> selection(){
		ArrayList<IndividualNN> meltingPot = new ArrayList<IndividualNN>();
		int minfitness = pop.get(0).getFitness();
		int maxfitness = pop.get(0).getFitness();
		for (int i = 1; i < sizePop; i++) {
			if(minfitness > pop.get(i).getFitness()) {
				minfitness = pop.get(i).getFitness();
			}
			if(maxfitness < pop.get(i).getFitness()) {
				maxfitness = pop.get(i).getFitness();
			}
		}
		if (minfitness < 0) {
			for (int i = 0; i < sizePop; i++) {
				pop.get(i).setFitness(pop.get(i).getFitness()-minfitness+1);
			}
			maxfitness -=minfitness -1;
		}

		for (int i = 0; i < sizePop; i++) {
			for(int j = 0; j < Math.pow(1+pop.get(i).getFitness()/(float)maxfitness,selectPower); j++) {
				meltingPot.add(pop.get(i));
			}
		}
		ArrayList<IndividualNN> newPop = new ArrayList<IndividualNN>();
		for(int i = 0; i < sizePop; i++) {
			IndividualNN parentA = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			IndividualNN parentB = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			newPop.add(crossover(parentA.getNN(), parentB.getNN()));
		}

		return newPop;
	}
	
	private ArrayList<IndividualNN> functionalSelection(){
		double[] fitnesses = new double[sizePop];
		double[] cumulateFitnesses = new double[sizePop];
		double minfitness = pop.get(0).getFitness();
		double sum = 0;
		for (int i = 1; i < sizePop; i++) {
			// apply desired function
			fitnesses[i] = identity(pop.get(i).getFitness());
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
			
		ArrayList<IndividualNN> newPop = new ArrayList<IndividualNN>();
		for(int i = 0; i < sizePop; i++) {
			// now, grabbing parent corresponding to 
			IndividualNN parentA = pop.get(cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble()));
			IndividualNN parentB = pop.get(cumulativeIndex(cumulateFitnesses, Main.rand.nextDouble()));
			newPop.add(crossover(parentA.getNN(), parentB.getNN()));
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
	
	private static int rankSelecter(int size, double proba) {
		int index = 0;
		while (!(Main.rand.nextDouble() < proba) && (index < size -1)) {
			index++; // 
		}
		return index;
	}
	
	private IndividualNN crossover(NeuralNet nn1, NeuralNet nn2) {
		NeuralNet newNN = nn1;
		newNN.print();
		for (int layer = 0; layer < newNN.getWeights().length; layer++) {
			for (int i = 0; i < newNN.getWeights()[layer].getRowDimension(); i++) {
				// weights loop
				for (int j = 0; j < newNN.getWeights()[layer].getColumnDimension(); j++) {
					if (Main.rand.nextFloat() < parentProba) {
						newNN.getWeights()[layer].set(i,j,nn2.getWeights()[layer].get(i,j));
					}
					if (Main.rand.nextFloat() < mutationProba) {
						newNN.getWeights()[layer].set(i,j,newNN.getWeights()[layer].get(i,j) 
								+ Main.rand.nextGaussian()*mutationAmplitude());
					}
				}
				// biases
				if (Main.rand.nextFloat() < parentProba) {
					newNN.getBiases()[layer].set(i, 0, nn2.getBiases()[layer].get(i, 0));
				}
				if (Main.rand.nextFloat() < mutationProba) {
					newNN.getBiases()[layer].set(i,0,newNN.getBiases()[layer].get(i,0) 
							+ Main.rand.nextGaussian()*mutationAmplitude());
				}
			}
		}
		return new IndividualNN(newNN);
	}
	
	private double mutationAmplitude() { 
		// parameters for building an exponential passing through two given points and above a threshold

		double valueAtFifty = 0.05;
		double minValue = 0; // different form valAt0
		
		// just solving equations
		double alpha = mutationAtZero - minValue;
		double beta = -(1/50f)*Math.log((valueAtFifty-minValue)/alpha);
		//return (alpha*Math.exp(-GeneticNN.GENERATION*beta)+minValue);
		return mutationAtZero;
	}
	
	/*
	 * 
	 * mutation
	 * 
	 */
	

}
