package ia;

import Main.Main;
import Modele.Game;

public class GeneticNN extends Genetic {
	// hyperparameters
	private double initMinWeight;
	private double initMaxWeight;
	private double selectPower;
	private double rankSelectProba;
	private double keeper;
	private double bestCross;
	private boolean decrease;
	
	public GeneticNN(Game game, int sizePop) {
		infoGenetic = new InfoGenetic(GENERATION);
		this.sizePop = sizePop;
		birds = game.getBirds();
		obstacles = game.getObstacles();
		GENERATION = 0;
		
		/// Hyperparameters :
		// Mutation
		mutationAtZero = 0.05; // Amplitude. "zero" bcs it can evolve over time
		mutProba = 0.1;
		decrease = true;
		// NN Weights
		initMinWeight = -0.75;
		initMaxWeight = 0.75;
		// Selection & Crossover
		rankSelectProba = 0.1;
		selectPower = 5; // useless copared to rank
		// For the "video selection" - not finished
		keeper = 0.01;
		bestCross = 0.2;
		
		// population initialization
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
	
	public void update(Game game) {
		super.update(game);
		population = rankSelection(rankSelectProba);
	}
	
	public IndividualNN crossover(Individual parent1, Individual parent2) throws IllegalArgumentException {
		NeuralNet newNN = null;
		if ((parent1 instanceof IndividualNN) && (parent2 instanceof IndividualNN)) {
			newNN = new NeuralNet(((IndividualNN) parent1).getNN()); // this constructor copies the shape
			// Layers loop :
			for (int layer = 0; layer < newNN.getWeights().length; layer++) {
				// Weight matrices loops :
				for (int i = 0; i < newNN.getWeights()[layer].getRowDimension(); i++) {
					for (int j = 0; j < newNN.getWeights()[layer].getColumnDimension(); j++) {
						// classical crossover : parent copying
						if (Main.rand.nextFloat() < 0.5f) {
							newNN.getWeights()[layer].set(i,j,((IndividualNN) parent1).getNN().getWeights()[layer].get(i,j));
						} else {
							newNN.getWeights()[layer].set(i,j,((IndividualNN) parent2).getNN().getWeights()[layer].get(i,j));
						} 
						// mutation
						if (Main.rand.nextFloat() < mutProba) {
							newNN.getWeights()[layer].set(i,j,newNN.getWeights()[layer].get(i,j) 
									+ (Main.rand.nextDouble() - 0.5)*2*mutationDecrease(this.decrease));
						}
					}
				}
			}
		} else {
			System.out.println("Parents aren't IndividualNN !");
			throw new IllegalArgumentException();
		}
		return new IndividualNN(newNN);
	}
}
