package ia;

import Main.Main;
import Modele.Bird;
import Modele.Obstacle;

public class Individual {
	private int fitness;
	private Boolean[][] genes;
	private int nrow;
	private int ncol;
	public static int MESHSIZE;

	public Individual(int nrow, int ncol) {
		this.nrow = nrow;
		this.ncol = ncol;
		MESHSIZE = Obstacle.getSpeed()*4;
		fitness = 0;
		genes = new Boolean[nrow][ncol];
		
		float probaJump = 0.03f;
		for(int i=0;i<nrow;i++) {
			for(int j=0; j<ncol; j++) {
				genes[i][j]= (Main.rand.nextFloat() < probaJump);
			}
		}
		//this.printArray(genes, nrow, ncol);
	}
	public Individual(Boolean[][] genes, int nrow, int ncol) {
		this.nrow = nrow;
		this.ncol = ncol;
		fitness = 0;
		this.genes = genes;
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

	public boolean decideJump(Obstacle obstacle, Bird bird) {
		int diffX = (obstacle.getPosX() - bird.getPosX())/Individual.MESHSIZE;
		if((diffX > 0) && (diffX < ncol - 1)) { // if bird is at the left of the obstacle and their distance is less than ncol
			return genes[(bird.getPosY()-obstacle.getPosY())/Individual.MESHSIZE+nrow/2][diffX];
			
		}else { // handling the bound (i.e. bird is at the right of the obstacle or their distance is more than ncol
			return genes[bird.getPosY()/Individual.MESHSIZE + nrow/4][ncol-1];
			
		}	
	}
	
	public static void printArray(Boolean[][] array, int dimX, int dimY) {
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				System.out.print(array[i][j]? 1 : 0);
			}
			System.out.println("");
		}
		System.out.println("\n");
	}
}
