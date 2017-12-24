package ia;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import Main.Main;
import Modele.Bird;
import Modele.Obstacle;

public class IndividualQLearning {
	private int[][] mesh;
	private int nrow;
	private int ncol;
	public static int MESHSIZE;
	public ArrayList<Point> path; //path from one obstacle to the other
	
	public IndividualQLearning(int nrow, int ncol) {
		this.nrow = nrow;
		this.ncol = ncol;
		MESHSIZE = Obstacle.getSpeed()*4;
		mesh = new int[nrow][ncol];
		this.path = new ArrayList<Point>();
		
		int probaJump = 97; // in percent
		for(int i=0;i<nrow;i++) {
			for(int j=0; j<ncol; j++) {
				mesh[i][j]= (Main.rand.nextInt(100)- probaJump);
			}
		}
		printArray(mesh,nrow, ncol);
		//this.printArray(genes, nrow, ncol);
	}
//	public IndividualQLearning(Boolean[][] genes, int nrow, int ncol) {
//		this.nrow = nrow;
//		this.ncol = ncol;
//		fitness = 0;
//		this.genes = genes;
//	}
	
	public int[][] getMesh(){
		return mesh;
	}
	public int getNRow(){
		return nrow;
	}	
	public int getNCol(){
		return ncol;
	}

	public boolean decideJump(Obstacle obstacle, Bird bird) {
		int diffX = (obstacle.getPosX() - bird.getPosX())/IndividualQLearning.MESHSIZE;
		if((diffX > 0) && (diffX < ncol - 1)) { // if bird is at the left of the obstacle and their distance is less than ncol
			System.out.println("jump " + (bird.getPosY()-obstacle.getPosY())/IndividualQLearning.MESHSIZE+nrow/2 +"  " + diffX);
			path.add(new Point((bird.getPosY()-obstacle.getPosY())/IndividualQLearning.MESHSIZE+nrow/2,diffX));
			return mesh[(bird.getPosY()-obstacle.getPosY())/IndividualQLearning.MESHSIZE+nrow/2][diffX] > 0;
		}else { // handling the bound (i.e. bird is at the right of the obstacle or their distance is more than ncol
			System.out.println("jump " + (bird.getPosY()/IndividualQLearning.MESHSIZE + nrow/4) + " "+ (ncol-1));
			path.add(new Point(bird.getPosY()/IndividualQLearning.MESHSIZE + nrow/4,ncol-1));
			return mesh[bird.getPosY()/IndividualQLearning.MESHSIZE + nrow/4][ncol-1] > 0;
		
		}	
		
		
	}
	
	public void updateMesh(int value, Obstacle obstacle, Bird bird) {
		int i=1;
		for (Iterator iterator = path.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			//i--;
			System.out.println("update mesh ");
			if(mesh[point.x][point.y] > 0) {
				System.out.print(mesh[point.x][point.y] + " ");
				mesh[point.x][point.y] += value/i;	
				System.out.println();
				System.out.println(mesh[point.x][point.y] + " ");
			}else {
				System.out.print(mesh[point.x][point.y] + " ");
				mesh[point.x][point.y] -= value/i;
				System.out.println(mesh[point.x][point.y] + " ");
			}

		}
		
		path = new ArrayList<Point>();
		
	}
	
	public static void printArray(int[][] array, int dimX, int dimY) {
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("\n");
	}
}
