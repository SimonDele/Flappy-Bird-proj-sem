package ia.dna;

import mainPkg.Main;
import model.Game;
import model.Obstacle;
import model.Whale;

public class BoolArray implements DNA {
	private Boolean[][] array;
	private int nrow;
	private int ncol;
	public static int MESHSIZE;
	
	public BoolArray() {
		int nrow = 2*Game.DIMY;
		int ncol = Obstacle.MINDIST;
		BoolArray.MESHSIZE = Obstacle.getSpeed()*4;
		this.nrow = nrow/BoolArray.MESHSIZE;
		this.ncol = ncol/BoolArray.MESHSIZE +1;
		this.array = new Boolean[this.nrow][this.ncol];
		double probaJump = 0.03;
		initArray(probaJump);
	}
	
	public BoolArray(int nrow, int ncol) {
		BoolArray.MESHSIZE = Obstacle.getSpeed()*4;
		this.nrow = nrow/BoolArray.MESHSIZE;
		this.ncol = ncol/BoolArray.MESHSIZE +1;
		this.array = new Boolean[this.nrow][this.ncol];
		
		// Initializes the boolean array, estimating 3% of 'true' is the best approximation
		double probaJump = 0.03;
		initArray(probaJump);
	}
	
	/**
	 * Private method called only by constructors to initialize the array at first generation
	 * @param probaJump
	 */
	private void initArray(double probaJump) {
		for(int i=0;i<this.nrow;i++) {
			for(int j=0; j<this.ncol; j++) {
				array[i][j]= (Main.rand.nextFloat() < probaJump);
			}
		}
	}
	
	public BoolArray(Boolean[][] genes, int nrow, int ncol) {
		this.array = genes;
		this.nrow = nrow;
		this.ncol = ncol;
	}
	
	public BoolArray(Boolean[][] genes) throws IllegalArgumentException {
		this.array = genes;
		// check that dimension is not < 2
		if ((genes.length != 0) && (genes[0].length != 0)) {
			this.nrow = genes.length;
			this.ncol = genes[0].length;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Boolean[][] getArray(){return this.array;}
	public int getNRow(){return this.nrow;}	
	public int getNCol(){return this.ncol;}

	@Override
	public boolean decidejump(Obstacle obstacle, Whale whale) {
		int diffX = (obstacle.getPosX() - whale.getPosX())/BoolArray.MESHSIZE;
		if((diffX > 0) && (diffX < ncol - 1)) { // if bird is at the left of the obstacle and their distance is less than ncol
			return array[(whale.getPosY()-obstacle.getPosY())/BoolArray.MESHSIZE+nrow/2][diffX];
			
		}else { // handling the bound (i.e. bird is at the right of the obstacle or their distance is more than ncol)
			return array[whale.getPosY()/BoolArray.MESHSIZE + nrow/4][ncol-1];
		}
	}

	public void mutate(double mutAmpl, double mutProba) {
		// TODO Implement this : change boolean values of array under proba mutProba
	}

	@Override
	public DNA crossover(DNA parentDNA, double mutAmpl, double mutProba) throws IllegalArgumentException {
		Boolean[][] newArray = null;
		if ((parentDNA instanceof BoolArray)) {
			newArray = new Boolean[this.nrow][this.ncol];
			for(int i = 0; i < nrow;  i++) {
				for(int j = 0; j < ncol ; j++) {
					if(Main.rand.nextFloat() < 0.5f)
						newArray[i][j] = this.array[i][j];
					else 
						newArray[i][j] = ((BoolArray)parentDNA).getArray()[i][j];
					
					// This is a non-continuous, exceptionnal case; we're using the counter-intuitive mutAmpl (not proba) since it is implemented to decrease
					if (Main.rand.nextFloat() < mutAmpl)
						newArray[i][j] = !newArray[i][j];
				}
			}
		} else {
			System.out.println("Parents are not BoolArray !");
			throw new IllegalArgumentException();
		}
		return new BoolArray(newArray);
	}
	
	public String toString() {
		String res = "BoolArray :\n";
		res += "Ncol = "+ ncol + "Nrow = "+ nrow + "\n";
		for (int i = 0; i < nrow; i++) {
			for (int j = 0; j < ncol; j++) {
				res += this.array[i][j] ? "1" : "0";
			}
			res += "\n";
		}
		return res;
	}
}
