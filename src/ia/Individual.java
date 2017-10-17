package ia;

import Main.Main;
import Modele.Bird;
import Modele.Obstacle;

public class Individual {
	private int fitness;
	private Boolean[][] genes;
	private int nrow;
	private int ncol;

	public Individual(int nrow, int ncol) {
		this.nrow = nrow;
		this.ncol = ncol;
		fitness = 0;
		genes = new Boolean[nrow][ncol];
		
		float probaJump = 0.05f;
		for(int i=0;i<nrow;i++) {
			for(int j=0; j<ncol; j++) {
				genes[i][j]= (Main.rand.nextFloat() < probaJump);
			}
		}/*
		for(int i=0;i<nrow;i++) {
			for(int j=0; j<ncol; j++) {
				genes[i][j] = (i > nrow/2 - 5);
			}
		}*/
		this.printArray(genes, nrow, ncol);
	}
	private void printArray(Boolean[][] array, int dimX, int dimY) {
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				System.out.print(array[i][j]? 1 : 0);
			}
			System.out.println("");
		}
		//System.out.println("\n");
	}
	public boolean decideJump(Obstacle obstacle, Bird bird) {
		int diffX = (obstacle.getPosX() - bird.getPosX())/Obstacle.getSpeed();
		if((diffX > 0) && (diffX < ncol - 1)) { // if bird is at the left of the obstacle and their distance is less than ncol
			System.out.println("if");
			return genes[(bird.getPosY()-obstacle.getPosY())/Obstacle.getSpeed()+nrow/2][diffX];
			
		}else { // handling the bound (i.e. bird is at the right of the obstacle or their distance is more than ncol
			System.out.println("else");
			return genes[bird.getPosY()/Obstacle.getSpeed() + nrow/4][ncol-1];
			
		}
		
	}
}
