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
	private ArrayList<Individual> pop;
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
		pop = new ArrayList<Individual>();
		Individual staticCreator = new Individual(0,0);
		for(int i=0; i<sizePop;i++) {
			pop.add(new Individual(2*Jeu.DIMY/Individual.MESHSIZE, Obstacle.MINDIST/Individual.MESHSIZE+1));
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
		int[] fitnesses = new int[birds.length];
		for (int i = 0; i < birds.length; i++) {
			fitnesses[i] = birds[i].getScore();
		}
		pop = selection(fitnesses);
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
	
	private ArrayList<Individual> selection(int[] fitnesses){
		ArrayList<Individual> meltingPot = new ArrayList<Individual>();
		int minfitness = fitnesses[0];
		int maxfitness = fitnesses[0];
		for (int i = 1; i < fitnesses.length; i++) {
			if(minfitness > fitnesses[i]) {
				minfitness = fitnesses[i];
			}
			if(maxfitness < fitnesses[i]) {
				maxfitness = fitnesses[i];
			}
		}
		if (minfitness < 0) {
			for (int i = 0; i < fitnesses.length; i++) {
				fitnesses[i]-=minfitness -1;
			}
			maxfitness -=minfitness -1;
		}

//		for (int i = 0; i < fitnesses.length; i++) {
//			for(int j = 0; j < fitnesses[i]/maxfitness*100; j++) {
//				meltingPot.add(pop.get(i));
//			}
//		}
		for (int i = 0; i < fitnesses.length; i++) {
			for(int j = 0; j < Math.pow(1+fitnesses[i]/(float)maxfitness,9); j++) {
				meltingPot.add(pop.get(i));
			}
		}
		ArrayList<Individual> newPop = new ArrayList<Individual>();
		for(int i = 0; i < sizePop; i++) {
			Individual parentA = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			Individual parentB = meltingPot.get(Main.rand.nextInt(meltingPot.size()));
			newPop.add(crossover(parentA.getGenes(), parentB.getGenes(), parentA.getNRow(), parentA.getNCol()));
		}
		//Arrays.sort(fitnesses);
//		for(int i = 0; i < sizePop - (int)(sizePop*0.9); i++) {
//			int iMax = 0;
//			int maxFitness = fitnesses[0];
//			for(int j = 0; j < sizePop; j++) {
//				if(maxFitness < fitnesses[i]) {
//					maxFitness = fitnesses[i];
//					iMax = j;
//				}
//			}
//			
//			newPop.add(pop.get(iMax));
//			fitnesses[iMax] = minfitness;
//		}

		return newPop;
	}
	
	private Individual crossover(Boolean[][] genesA, Boolean[][] genesB, int nrow, int ncol) {
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
		return new Individual(newGenes, nrow, ncol);
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
