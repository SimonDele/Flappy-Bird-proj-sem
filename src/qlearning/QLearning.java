package qlearning;

import java.util.ArrayList;

import ia.InfoGenetic;
import model.Game;
import model.Obstacle;
import model.Whale;

public class QLearning {

	Whale[] whales;
	ArrayList<Obstacle> obstacles;
	QIndividual[] population;
	
	ArrayList[][] historyRollout;
	
	public static InfoGenetic infoGenetic;
	
	// Hyperparameter :
	int malus = 1;
	int bonus = -1;

	int rollout = 10;
	int irollout;
	
	public QLearning(Game game,  Class<? extends QDNA> dnaImpl) {
		this.whales = game.getBirds();
		this.obstacles = game.getObstacles();
		population = new QIndividual[whales.length];
		
		for (int i = 0; i < population.length; i++) {
			population[i] = new QIndividual(dnaImpl);
		}
		
		irollout = 0;
		historyRollout = new ArrayList[population.length][rollout];
		
		infoGenetic = new InfoGenetic(0);
	}
	 
	public void newGame(Game game) {
		
		for(int i=0; i<population.length; i++) {
			historyRollout[i][irollout] = this.population[i].history;
		}
		irollout += 1;
		if(irollout == rollout) {
			irollout = 0;
			
			
			for (int i = 0; i < population.length; i++) {
				//System.out.println(population[i].dna);
				//System.out.println(population[i].history.size());
				population[i].dna.applyMalus(historyRollout[i]);
				
				//System.out.println(population[i].dna);
				population[i].history = new ArrayList<Event>();
			}
			
			historyRollout = new ArrayList[population.length][rollout];
		}
//	
//		for (int i = 0; i < population.length; i++) {
//			//System.out.println(population[i].dna);
//			//System.out.println(population[i].history.size());
//			population[i].dna.applyMalus(population[i].history);
//			
//			//System.out.println(population[i].dna);
//			population[i].history = new ArrayList<Event>();
//		}		
//		
		
		
	
		
		// update game's attributes
		whales = game.getBirds();
		obstacles = game.getObstacles();
	
	}
	/**
	 * Checks whether the population died (each individual died).
	 * @return a boolean = (if every individual is dead)
	 */
	public boolean generationDead() {
		boolean isDead = true;
		int i = 0;
		while(isDead && i < whales.length) {
			isDead = whales[i].isDead();
			i++;
		}
		//if(isDead) System.out.println("generation dead");
		return isDead;
	}
	/**
	 * Asks each individual of the population whether to jump
	 * @return an array of booleans for the decision of each individual
	 */
	public boolean[] getJumps() {
		boolean[] jumps = new boolean[whales.length];
		if(obstacles.size() > 0) {
			for (int i = 0; i < whales.length; i++) {
				jumps[i] = population[i].decideJump(obstacles.get(0), whales[i]);
			}
		}		
		return jumps;
	}

	public void applyReward(boolean[] whalesNotDead) {
		System.out.println("apply reward");
		for(int i=0; i<whalesNotDead.length; i++) {
			if(whalesNotDead[i]) {
				population[i].dna.applyBonus(historyRollout[i]);
			}else {
				population[i].dna.applyMalus(historyRollout[i]);
			}
		}
		
	}
	
	public void newFrame() {
		for(int i=0; i<population.length;i++) {
			if(obstacles.size() > 0) {
				population[i].addActionToHistory(whales[i], obstacles.get(0));
			}
	
		}
	}
	
	
}
