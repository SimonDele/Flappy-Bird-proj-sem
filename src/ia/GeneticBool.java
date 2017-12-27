package ia;

import Modele.Game;
import Modele.Obstacle;
import Main.Main;

public class GeneticBool extends Genetic {
	int selectPower;
	double rankSelectProba;
	
	public GeneticBool(Game game, int sizePop) {
		// Initialization of the info, genetic and game's attributes
		infoGenetic = new InfoGenetic(GENERATION);
		this.sizePop = sizePop;
		birds = game.getBirds();
		obstacles = game.getObstacles();
		GENERATION = 0;
		
		/// Hyperparameters :
		// Mutation
		mutationAtZero = 0.05;
		// Selection
		selectPower = 4;
		rankSelectProba = 0.1;
		
		// Population intitialization
		population = new IndividualBool[sizePop];
		for(int i=0; i<sizePop; i++) {
			population[i] = new IndividualBool(2*Game.DIMY, Obstacle.MINDIST); 
		}
	}

	public void update(Game game) {
		super.update(game);
		population = rankSelection(rankSelectProba);
	}

	@Override
	protected Individual crossover(Individual parent1, Individual parent2) throws IllegalArgumentException {
		Boolean[][] newGenes = null;
		if ((parent1 instanceof IndividualBool) && (parent2 instanceof IndividualBool)) {
			int nrow = ((IndividualBool)parent1).getNRow();
			int ncol = ((IndividualBool)parent1).getNCol();
			newGenes = new Boolean[nrow][ncol];
			for(int i = 0; i < nrow;  i++) {
				for(int j = 0; j < ncol ; j++) {
					if(Main.rand.nextFloat() < 0.5f) {
						newGenes[i][j] = ((IndividualBool)parent1).getGenes()[i][j];
					} else {
						newGenes[i][j] = ((IndividualBool)parent2).getGenes()[i][j];
					}
					if (Main.rand.nextFloat() < mutationDecrease(true)) {
						newGenes[i][j] = !newGenes[i][j];
					}
				}
			}
		} else {
			System.out.println("Parents are not Individual Bool !");
			throw new IllegalArgumentException();
		}
		return new IndividualBool(newGenes);
	}
	
	/*
	 * 
	 * mutation
	 * 
	 */
	
}
