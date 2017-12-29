package ia.sel;

import ia.Individual;

/**
 * Same as {@linkplain RankSelection RankSelection} but allows the user to keep a portion 'keeper' of the old population. Identical to mother class if keeper = 0.
 */
public class RankSelectionKeeper extends RankSelection {
	private double keeper;
	
	/**
	 * Constructor that ignores the 'keeper' parameter - so the class acts the same as its mother class {@link RankSelection}
	 * @param rankSelectProba
	 */
	public RankSelectionKeeper(double rankSelectProba) {
		super(rankSelectProba); 
		this.keeper = 0;
	}
	/**
	 * Constructor that ignores the 'keeper' parameter - so the class acts the same as its mother class {@link RankSelection}
	 * @param rankSelectProba
	 */
	public RankSelectionKeeper(double rankSelectProba, double keeper) {
		super(rankSelectProba);
		this.keeper = 0;
	}
	
	
	@Override
	public Individual[] select(Individual[] population, double mutAmpl, double mutProba) {
		Individual[] newPop = super.select(population, mutAmpl, mutProba);
		// The most optimized would be to put this bit of code in the loop but calling the super method is cleaner
		for (int i = 0; i < newPop.length*keeper; i++) {
			newPop[i] = population[i];
			newPop[i].mutate(mutAmpl, mutProba);
		}
		return newPop;
	}
}
