package ia;

import Jama.Matrix;

public class NeuralNet {
	private int nbInput; // number of elements in the input vector
	private int nbOutput; // idem, output vector
	private int[] hidden; // number of neurones in each hidden layer
	private Matrix[] weights; // Matrix[i] are a layer's weights
	private Matrix[] biases; // list of biases vectors, matrix type for summing
	private double maxWeight;
	private double minWeight;
	
	public NeuralNet(int nbInput, int nbOutput, int[] hidden, int minWeight, int maxWeight) throws IllegalArgumentException {
		if (maxWeight<=minWeight) {
			throw new IllegalArgumentException("Input error : maxWeight <= minWeight");
		} else {
			this.nbInput = nbInput;
			this.nbOutput = nbOutput; 
			this.hidden = hidden; 
			weights = new Matrix[hidden.length+1]; 
			biases = new Matrix[weights.length];
			this.maxWeight = maxWeight;
			this.minWeight = minWeight;
			randomInitParameters();
		}
	}
	
	private void randomInitParameters() {
		// WEIGHTS
		// in column : the weights from the jth intput neuron
		// in line :   the weights for  the ith output neuron
		weights[0] = randomMatrix(hidden[0], nbInput); // case input
		for (int i = 1; i < hidden.length; i++) {
			weights[i] = randomMatrix(hidden[i], hidden[i-1]); // within hidden 
		}
		weights[hidden.length] = randomMatrix(nbOutput, hidden[hidden.length-1]); // output case
		// BIASES
		// use hidden for all but the last (in this case, nbOutput)
		for (int i = 0; i < hidden.length; i++) {
			biases[i] = randomMatrix(hidden[i],1);
		}
		biases[hidden.length] = randomMatrix(nbOutput, 1);
	}
	
	private Matrix randomMatrix(int nrow, int ncol) {
		Matrix randMat = null;
		randMat = Matrix.random(nrow, ncol);
		randMat.print(1, 10);
		randMat.timesEquals(maxWeight-minWeight);
		randMat.plusEquals(new Matrix(nrow,ncol,minWeight));		
		return randMat;
	}
	
	public Matrix propagation(Matrix input) throws IndexOutOfBoundsException {
		Matrix prevision = input;
		if (!((input.getRowDimension()==nbInput) && (input.getColumnDimension()==1))) {
			throw new IndexOutOfBoundsException("Wrong NeuralNet input length: " + input.getRowDimension() + 
					"\nRequired : " + nbInput);
		} else {
			for (int i = 0; i < weights.length; i++) {
				prevision = weights[i].times(prevision);
				prevision.plusEquals(biases[i]);
				activationFunction(prevision);
				prevision.print(1, 10);
			}
		}
		return prevision;
	}
	
	
	public static void activationFunction(Matrix m) {
		for (int i = 0; i < m.getRowDimension(); i++) {
			for (int j = 0; j < m.getColumnDimension(); j++) {
				m.set(i, j, sig(m.get(i, j)));
			}
		}
	}
	
	public static double sig(double x) {
		return 1/(1+Math.exp(-x));
	}
	
	public static double reLU(double x) {
		return Math.max(0, x);
	}
}
