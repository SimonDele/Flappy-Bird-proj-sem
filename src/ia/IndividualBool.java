package ia;

import Main.Main;
import Modele.Bird;
import Modele.Obstacle;

public class IndividualBool extends Individual {
	private Boolean[][] genes;
	private int nrow;
	private int ncol;
	public static int MESHSIZE;
	
	// 
	public IndividualBool(int nrow, int ncol) {
		IndividualBool.MESHSIZE = Obstacle.getSpeed()*4;
		this.nrow = nrow/IndividualBool.MESHSIZE;
		this.ncol = ncol/IndividualBool.MESHSIZE +1;
		this.genes = new Boolean[this.nrow][this.ncol];
		
		float probaJump = 0.03f;
		for(int i=0;i<this.nrow;i++) {
			for(int j=0; j<this.ncol; j++) {
				genes[i][j]= (Main.rand.nextFloat() < probaJump);
			}
		}
	}
	
	public IndividualBool(Boolean[][] genes, int nrow, int ncol) {
		this.genes = genes;
		this.nrow = nrow;
		this.ncol = ncol;
	}
	
	public IndividualBool(Boolean[][] genes) throws IllegalArgumentException {
		this.genes = genes;
		// check that dimension is not < 2
		if ((genes.length != 0) && (genes[0].length != 0)) {
			this.nrow = genes.length;
			this.ncol = genes[0].length;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Boolean[][] getGenes(){return this.genes;}
	public int getNRow(){return this.nrow;}	
	public int getNCol(){return this.ncol;}


	public boolean decideJump(Obstacle obstacle, Bird bird) {
		int diffX = (obstacle.getPosX() - bird.getPosX())/IndividualBool.MESHSIZE;
		if((diffX > 0) && (diffX < ncol - 1)) { // if bird is at the left of the obstacle and their distance is less than ncol
			return genes[(bird.getPosY()-obstacle.getPosY())/IndividualBool.MESHSIZE+nrow/2][diffX];
			
		}else { // handling the bound (i.e. bird is at the right of the obstacle or their distance is more than ncol
			return genes[bird.getPosY()/IndividualBool.MESHSIZE + nrow/4][ncol-1];
			
		}
	}

	@Override
	public void mutate(double mutAmpl, double mutProba) {
		// This is only called for very specific selection methods
		// TODO Implement this : mutate the array
	}
	
	@Override
	public String toString() {
		String res = "IndividualBool :\n";
		res += "Ncol = "+ ncol + "Nrow = "+ nrow + "\n";
		for (int i = 0; i < nrow; i++) {
			for (int j = 0; j < ncol; j++) {
				res += this.genes[i][j] ? "1" : "0";
			}
			res += "\n";
		}
		return res;
	}
}
