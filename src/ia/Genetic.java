package ia;

import java.util.ArrayList;

import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;
import Main.Main;

public class Genetic {
	// Genetic's attributes
	private int sizePop;
	private ArrayList<IndividualBool> pop;
	public static int GENERATION;
	public static InfoGenetic infoGenetic;
	// Game's attributes
	private Bird[] birds;
	private ArrayList<Obstacle> obstacles;
	
	public Genetic(Jeu jeu, int sizePop) {
		infoGenetic = new InfoGenetic(GENERATION);
		this.sizePop = sizePop;
		birds = jeu.getBirds();
		obstacles = jeu.getObstacles();
		GENERATION = 0;
		
		pop = new ArrayList<IndividualBool>();
		for(int i=0; i<sizePop; i++) {
			pop.add(new IndividualBool(2*Jeu.DIMY, Obstacle.MINDIST)); // the netting will be done later
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
	
	private ArrayList<IndividualBool> selection(){
		int power = 7;
		ArrayList<IndividualBool> meltingPot = new ArrayList<IndividualBool>();
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
		ArrayList<IndividualBool> newPop = new ArrayList<IndividualBool>();
		for(int i = 0; i < sizePop; i++) {
			IndividualBool parentA = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			IndividualBool parentB = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			newPop.add(crossover(parentA.getGenes(), parentB.getGenes(), parentA.getNRow(), parentA.getNCol()));
		}

		return newPop;
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
	
	private double mutationProba() { 
		// parameters for building an exponential passing through two given points and above a threshold
		double valueAtZero = 0.01;
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
