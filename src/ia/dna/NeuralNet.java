package ia.dna;

import Jama.Matrix;
import mainPkg.Main;
import model.Game;
import model.Obstacle;
import model.Whale;

/**
 * DNA Implementation for the Neural Network. Also contains generic propagation (feedforward algorithm) and some constructors. DOES NOT cover biaises
 * @author louis
 */
public class NeuralNet implements DNA {
	/**
	 * Number of elements in the input vector
	 */
	private int nbInput;
	/**
	 * Number of elements in the output vector
	 */
	private int nbOutput; 
	/**
	 * Number of neurones in each hidden layer
	 */
	private int[] hidden; 
	/**
	 * Array of all the weight matrices. Matrix[i] are a layer's weights
	 */
	private Matrix[] weights;
	/**
	 * Maximum value for a weight
	 */
	private double maxWeight;
	/**
	 * Minimum value for a weight
	 */
	private double minWeight;
	
	/**
	 * Default initializer based on experience of best results
	 */
	public NeuralNet() {
		this.nbInput = 2;
		this.nbOutput = 1; 
		this.hidden = new int[]{3}; 
		this.weights = new Matrix[hidden.length+1]; 
		this.maxWeight = 0.75;
		this.minWeight = -maxWeight;
		randomInitWeights();
	}
	
	/**
	 * Initializer for every field in this class. The weihts initialization is done in method randomInitWeights
	 * @param nbInput to initialize the field of the same name
	 * @param nbOutput to initialize the field of the same name
	 * @param hidden to initialize the field of the same name 
	 * @param minWeight to initialize the field of the same name
	 * @param maxWeight to initialize the field of the same name
	 * @throws IllegalArgumentException in case minw >= maxw, or the number of input/output/hidden is 0
	 */
	public NeuralNet(int nbInput, int nbOutput, int[] hidden, double minWeight, double maxWeight) throws IllegalArgumentException {
		if ((maxWeight<=minWeight)||(nbInput<=0)||(nbOutput<=0)||(hidden.length<=0)) {
			System.out.println(nbInput + ", " + nbOutput + ", " + hidden + ", " + minWeight + ", " + maxWeight);
			throw new IllegalArgumentException("Error in NN constructor, values above");
		} else {
			this.nbInput = nbInput;
			this.nbOutput = nbOutput; 
			this.hidden = hidden; 
			this.weights = new Matrix[hidden.length+1]; 
			this.maxWeight = maxWeight;
			this.minWeight = minWeight;
			randomInitWeights();
		}
	}
	
	/**
	 * Initializes a neuralnet based on an array of Matrices. Hence the mimnw and maxw values are pointless
	 * @param w the array of matrices to use as weights
	 */
	public NeuralNet(Matrix[] w) {
		this.nbInput = w[0].getColumnDimension();
		this.nbOutput = w[w.length-1].getRowDimension();
		this.weights = w;
	}
	
	/**
	 * This constructor copies the shape of the NN given as input (shape is the set {layers, minweight, maxweight}). RandomInit is called to initialize the weights matrices
	 * @param nn the NeuralNet whose shape we want to copy
	 */
	public NeuralNet(NeuralNet nn) {
		this.nbInput = nn.getNbInput();
		this.nbOutput = nn.getNbOutput();
		this.hidden = nn.getHidden();
		this.minWeight = nn.getMinWeight();
		this.maxWeight = nn.getMaxWeight();
		this.weights = new Matrix[hidden.length+1];
		randomInitWeights();
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

	/**
	 * Initializes the array of weights matrices based on the min and max weights, using randomMatrix method
	 */
	private void randomInitWeights() {
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
	
	/**
	 * Initializes a single Matrix using the Jama.Matrix.random method
	 * @param nrow te number of rows of the matrix
	 * @param ncol the number of columns of the matrix
	 * @return the randomly generated matrix
	 */
	public Matrix randomMatrix(int nrow, int ncol) {
		Matrix randMat = null;
		randMat = Matrix.random(nrow, ncol);
		randMat.timesEquals(maxWeight-minWeight);
		randMat.plusEquals(new Matrix(nrow,ncol,minWeight));		
		return randMat;
	}
	
	/**
	 * Propagation (feedforward) algorithm. Applies the equations (z=wa+b) and (a= activation(z)) for every layer (here b=0 : not taken into account)
	 * @param input the input vector to apply the algorithm on
	 * @return the output vector of this network based on the input
	 * @throws IndexOutOfBoundsException if the input is incorrect
	 */
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
	
	/**
	 * Applies the activation function elementwise on the matrix, using the matrix' setters (hence void). This will probabbly only be used on vectors, ie. (n,1) matrices.
	 * @param m the matrix to apply the actiation function on. 
	 */
	public static void activationFunction(Matrix m) {
		for (int i = 0; i < m.getRowDimension(); i++) {
			for (int j = 0; j < m.getColumnDimension(); j++) {
				m.set(i, j, sig(m.get(i, j)));
			}
		}
	}
	
	/**
	 * One possible actiavtion function : the generic sigmoid
	 * @param x the input to apply the function on
	 * @return the output, hence sigma(x)
	 */
	public static double sig(double x) {
		return 1/(1+Math.exp(-x));
	}
	
	/**
	 * One possible actiavtion function : the Rectified Linear Unit
	 * @param x the input to apply the function on
	 * @return the output, hence max(0,x)
	 */
	public static double reLU(double x) {
		return Math.max(0, x);
	}
	
	/**
	 * Debugging function to print all the neuralnet's weights
	 */
	public void print() {
		for (int i = 0; i < weights.length; i++) {
			System.out.println("Weights " + i);
			weights[i].print(1, 5);
		}
	}
	
	/**
	 * Addition of a neuralnet's weights with another (for mutations). Never used in practice
	 * @param nn the neural net to add the weights to
	 * @return the new neural net
	 * @deprecated
	 */
	public NeuralNet add(NeuralNet nn) {
		int nL = nn.getNbLayers();
		Matrix[] w = new Matrix[nL];
		for (int j = 0; j < nL; j++) {
			w[j] = this.weights[j].plus(nn.getWeights()[j]);
		}
		return new NeuralNet(w);
	}
	
	/**
	 * Never used in practice. Method to generate a neuralnet based on another's architecture
	 * @param nn the neuralnet to copy architecture
	 * @param mutAmpl the amplitude of mutations, to generate weights
	 * @return the new neuralnet
	 * @deprecated
	 */
	public static NeuralNet randomNNShape(NeuralNet nn, double mutAmpl) {
		NeuralNet randNN = nn;
		randNN.setMinWeight(-mutAmpl);
		randNN.setMaxWeight(mutAmpl);
		randNN.randomInitWeights();
		return(randNN);
	}

	@Override
	public boolean decidejump(Obstacle obstacle, Whale whale) {
		// only coded for nbInput = 2 !
		boolean decision = false;
		if (this.nbInput != 2) {
			throw new IllegalArgumentException();
		} else {
			double[][] input = new double[this.nbInput][1]; // will be a vector type Matrix
			input[0][0] = (whale.getPosY()-obstacle.getPosY())/(double)Game.DIMY + 0.5;
			input[1][0] = obstacle.getPosX()/(double)Game.DIMX;
			decision = this.propagation(new Matrix(input)).get(0, 0) > 0.5;
		}
		return decision;
	}

	@Override
	public void mutate(double mutAmpl, double mutProba) {
		// TODO Implement the NN's mutation : tweak some parameters under mutProba and of avg size mutAmpl
	}

	@Override
	public DNA crossover(DNA otherNN, double mutAmpl, double mutProba) {
		NeuralNet newNN = null;
		if (otherNN instanceof NeuralNet) {
			newNN = new NeuralNet(this); // this constructor copies the shape
			// Layers loop :
			for (int layer = 0; layer < this.weights.length; layer++) {
				// Weight matrices loops :
				for (int i = 0; i < this.weights[layer].getRowDimension(); i++) {
					for (int j = 0; j < this.weights[layer].getColumnDimension(); j++) {
						// classical crossover : parent copying
						if (Main.rand.nextFloat() < 0.5f) {
							newNN.getWeights()[layer].set(i,j,this.weights[layer].get(i,j));
						} else {
							newNN.getWeights()[layer].set(i,j, ((NeuralNet)otherNN).getWeights()[layer].get(i,j));
						} 
						// mutation
						if (Main.rand.nextFloat() < mutProba) {
							newNN.getWeights()[layer].set(i,j,newNN.getWeights()[layer].get(i,j) 
									+ (Main.rand.nextDouble() - 0.5)*2*mutAmpl);
						}
					}
				}
			}
		} else {
			System.out.println("Parents aren't IndividualNN !");
			throw new IllegalArgumentException();
		}
		return newNN;
	}
}
