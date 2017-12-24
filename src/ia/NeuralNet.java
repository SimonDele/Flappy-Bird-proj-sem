package ia;

import Jama.Matrix;

public class NeuralNet {
	private int nbInput; // number of elements in the input vector
	private int nbOutput; // idem, output vector
	private int[] hidden; // number of neurones in each hidden layer
	private Matrix[] weights; // Matrix[i] are a layer's weights
	private double maxWeight;
	private double minWeight;
	
	public NeuralNet(int nbInput, int nbOutput, int[] hidden, double minWeight, double maxWeight) throws IllegalArgumentException {
		if (maxWeight<=minWeight) {
			throw new IllegalArgumentException("Input error : maxWeight <= minWeight");
		} else {
			this.nbInput = nbInput;
			this.nbOutput = nbOutput; 
			this.hidden = hidden; 
			weights = new Matrix[hidden.length+1]; 
			this.maxWeight = maxWeight;
			this.minWeight = minWeight;
			randomInitParameters();
		}
	}
	
	public NeuralNet(Matrix[] w) {
		this.nbInput = w[0].getColumnDimension();
		this.nbOutput = w[w.length-1].getRowDimension();
		this.weights = w;
	}
	
	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(double minWeight) {
		this.minWeight = minWeight;
	}

	public int getNbInput() {
		return nbInput;
	}
	public int getNbOutput() {
		return nbOutput;
	}
	public int[] getHidden() {
		return hidden;
	}
	public Matrix[] getWeights() {
		return weights;
	}
	
	public int getNbLayers() {
		return 2+hidden.length;
	}

	private void randomInitParameters() {
		// WEIGHTS
		// in column : the weights from the jth intput neuron
		// in line :   the weights for  the ith output neuron
		weights[0] = randomMatrix(hidden[0], nbInput); // case input
		// BIASES
		for (int i = 1; i < hidden.length; i++) {
			weights[i] = randomMatrix(hidden[i], hidden[i-1]); // within hidden
		}
		weights[hidden.length] = randomMatrix(nbOutput, hidden[hidden.length-1]); // output case
	}
	
	public Matrix randomMatrix(int nrow, int ncol) {
		Matrix randMat = null;
		randMat = Matrix.random(nrow, ncol);
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
				activationFunction(prevision);
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
	
	public void print() {
		for (int i = 0; i < weights.length; i++) {
			System.out.println("Weights " + i);
			weights[i].print(1, 5);
		}
	}
	
	public NeuralNet add(NeuralNet nn) {
		int nL = nn.getNbLayers();
		Matrix[] w = new Matrix[nL];
		for (int j = 0; j < nL; j++) {
			w[j] = this.weights[j].plus(nn.getWeights()[j]);
		}
		return new NeuralNet(w);
	}
	
	public static NeuralNet randomNNShape(NeuralNet nn, double mutAmpl) {
		NeuralNet randNN = nn;
		randNN.setMinWeight(-mutAmpl);
		randNN.setMaxWeight(mutAmpl);
		randNN.randomInitParameters();
		return(randNN);
	}
}
