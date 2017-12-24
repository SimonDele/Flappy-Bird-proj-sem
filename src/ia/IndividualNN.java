package ia;

import Jama.Matrix;
import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class IndividualNN extends Individual {
	private NeuralNet nn;
	
	public IndividualNN(int nbInput, int nbOutput, int[] hidden, double minWeight, double maxWeight) {
		fitness = 0;
		try {
			nn = new NeuralNet(nbInput, nbOutput, hidden, minWeight, maxWeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public IndividualNN(NeuralNet nn) {
		this.nn = nn;
		fitness = 0;
	}
	
	public NeuralNet getNN() {return nn;}
	
	public boolean decideJump(Obstacle obstacle, Bird bird) throws IllegalArgumentException {
		// only coded for nbInput = 2 ^-^
		boolean decision = false;
		double[][] input = new double[nn.getNbInput()][1]; // will be a vector type Matrix
		if (nn.getNbInput() != 2) {
			throw new IllegalArgumentException();
		} else {
			input[0][0] = (bird.getPosY()-obstacle.getPosY())/(double)Jeu.DIMY + 0.5;
			input[1][0] = obstacle.getPosX()/(double)Jeu.DIMX;
			decision = nn.propagation(new Matrix(input)).get(0, 0) > 0.5;
		}
		return decision;
	}
	
	public void mutate(double mutAmpl) {
		nn = nn.add(NeuralNet.randomNNShape(nn, mutAmpl));
	}
}
