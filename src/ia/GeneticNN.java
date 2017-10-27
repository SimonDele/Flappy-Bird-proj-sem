package ia;

import java.util.ArrayList;

import Main.Main;
import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class GeneticNN {

	private int sizePop;
	private ArrayList<IndividualNN> pop;
	public static int GENERATION;
	public static InfoGenetic infoGenetic;
	private float mutationProba;
	// Game's attributes
	private Bird[] birds;
	private ArrayList<Obstacle> obstacles;
	
	public GeneticNN(Jeu jeu, int sizePop) {
		infoGenetic = new InfoGenetic(GENERATION);
		this.sizePop = sizePop;
		birds = jeu.getBirds();
		obstacles = jeu.getObstacles();
		GENERATION = 0;
		mutationProba = 0.2f;
		
		pop = new ArrayList<IndividualNN>();
		int[] hidden = {6};
		for(int i=0; i<sizePop; i++) {
			try {
				pop.add(new IndividualNN(3,1,hidden,-1,1)); // the netting will be done later
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
		}
		pop = selection();
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
			}

		}
		return jumps;
	}
	
	private ArrayList<IndividualNN> selection(){
		int power = 5;
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
				pop.get(i).setFitness(pop.get(i).getFitness()-minfitness-1);
			}
			maxfitness -=minfitness -1;
		}

		for (int i = 0; i < sizePop; i++) {
			for(int j = 0; j < Math.pow(1+pop.get(i).getFitness()/(float)maxfitness,power); j++) {
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
	
	private IndividualNN crossover(NeuralNet nn1, NeuralNet nn2) {
		NeuralNet newNN = nn1;
		for (int layer = 0; layer < newNN.getWeights().length; layer++) {
			for (int i = 0; i < newNN.getWeights()[layer].getRowDimension(); i++) {
				for (int j = 0; j < newNN.getWeights()[layer].getColumnDimension(); j++) {
					if (Main.rand.nextFloat() < 0.5) {
						newNN.getWeights()[layer].set(i,j,nn2.getWeights()[layer].get(i,j));
					}
					if (Main.rand.nextFloat() < mutationProba) {
						newNN.getWeights()[layer].set(i,j,newNN.getWeights()[layer].get(i,j) 
								+ Main.rand.nextGaussian()*mutationAmplitude());
					}
				}
			}
		}
		return new IndividualNN(newNN);
	}
	
	private double mutationAmplitude() { 
		// parameters for building an exponential passing through two given points and above a threshold
		double valueAtZero = 0.2;
		double valueAtFifty = 0.1;
		double minValue = 0; // different form valAt0
		
		// just solving equations
		double alpha = valueAtZero - minValue;
		double beta = -(1/50f)*Math.log((valueAtFifty-minValue)/alpha);
		return (alpha*Math.exp(-GeneticNN.GENERATION*beta)+minValue);
	}
	
	/*
	 * 
	 * mutation
	 * 
	 */
	

}
