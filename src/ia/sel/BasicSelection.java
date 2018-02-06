package ia.sel;

import java.util.ArrayList;

import ia.Individual;
import mainPkg.Main;

/**
 * The most basic selection method. The probability to be selected to breed is directly proportionnal to your fitness compared to the population's. This is handled through a very computational-demanding 'melting-pot' to select the individuals.
 */
public class BasicSelection implements Selection {
	
	@Override
	public Individual[] select(Individual[] population, double mutAmpl, double mutProba) {
		int sizePop = population.length;
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
			newPop[i] = parentA.crossover(parentB, mutAmpl, mutProba);
		}

		return newPop;
	}

}
