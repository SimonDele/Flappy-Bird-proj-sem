package ia;

import Main.Main;
import Modele.Obstacle;

public class BoolArray {
	private Boolean[][] genes;
	private int nrow;
	private int ncol;
	public static int MESHSIZE;
	
	public BoolArray(int nrow, int ncol) {
		MESHSIZE = Obstacle.getSpeed()*4;
		this.nrow = nrow/IndividualBool.MESHSIZE;
		this.ncol = ncol/IndividualBool.MESHSIZE +1;
		genes = new Boolean[this.nrow][this.ncol];
		
		float probaJump = 0.03f;
		for(int i=0;i<this.nrow;i++) {
			for(int j=0; j<this.ncol; j++) {
				genes[i][j]= (Main.rand.nextFloat() < probaJump);
			}
		}
		//this.printArray(genes, nrow, ncol);
	}
	public BoolArray(Boolean[][] genes, int nrow, int ncol) {
		this.nrow = nrow;
		this.ncol = ncol;
		this.genes = genes;
		MESHSIZE = Obstacle.getSpeed()*4;
	}
	
	public Boolean[][] getGenes(){
		return genes;
	}
	public int getNRow(){
		return nrow;
	}	
	public int getNCol(){
		return ncol;
	}
	public String toString() {
		String res = "";
		for (int i = 0; i < nrow; i++) {
			for (int j = 0; j < ncol; j++) {
				res+= String.valueOf(genes[i][j]? 1 : 0);
			}
			res += "\n";
		}
		res += "\n\n";
		return res;
	}
}
