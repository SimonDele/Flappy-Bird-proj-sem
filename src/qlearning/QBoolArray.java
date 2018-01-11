package qlearning;

import java.util.ArrayList;

import mainPkg.Main;
import model.Game;
import model.Obstacle;
import model.Whale;

public class QBoolArray implements QDNA{
	private int[][] array;
	private int nrow;
	private int ncol;
	public static int MESHSIZE;
	
	private int malus = -1;
	private int bonus = 1;
	
	public QBoolArray() {
		int nrow = 2*Game.DIMY;
		int ncol = Obstacle.MINDIST;
		QBoolArray.MESHSIZE = Obstacle.getSpeed()*4;
		this.nrow = nrow/QBoolArray.MESHSIZE;
		this.ncol = ncol/QBoolArray.MESHSIZE +1;
		this.array = new int[this.nrow][this.ncol];
		double probaJump = 0.03;
		initArray(probaJump);
	}
	
	public QBoolArray(int nrow, int ncol) {
		QBoolArray.MESHSIZE = Obstacle.getSpeed()*4;
		this.nrow = nrow/QBoolArray.MESHSIZE;
		this.ncol = ncol/QBoolArray.MESHSIZE +1;
		this.array = new int[this.nrow][this.ncol];
		
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
				array[i][j]= Main.rand.nextInt(10) - 8;
			}
		}
		//System.out.println(this);
		//int i = -45;
		//System.out.println(i);
	}
	
	public QBoolArray(int[][] genes, int nrow, int ncol) {
		this.array = genes;
		this.nrow = nrow;
		this.ncol = ncol;
	}
	
	public QBoolArray(int[][] genes) throws IllegalArgumentException {
		this.array = genes;
		// check that dimension is not < 2
		if ((genes.length != 0) && (genes[0].length != 0)) {
			this.nrow = genes.length;
			this.ncol = genes[0].length;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public int[][] getArray(){return this.array;}
	public int getNRow(){return this.nrow;}	
	public int getNCol(){return this.ncol;}

	@Override
	public boolean decidejump(Obstacle obstacle, Whale whale) {
		int diffX = (obstacle.getPosX() - whale.getPosX())/QBoolArray.MESHSIZE;
		if((diffX > 0) && (diffX < ncol - 1)) { // if bird is at the left of the obstacle and their distance is less than ncol
			return array[(whale.getPosY()-obstacle.getPosY())/QBoolArray.MESHSIZE+nrow/2][diffX] > 0;
			
		}else { // handling the bound (i.e. bird is at the right of the obstacle or their distance is more than ncol)
			return array[whale.getPosY()/QBoolArray.MESHSIZE + nrow/4][ncol-1] > 0;
		}
	}

	
	@Override
	public String toString() {
		String res = "BoolArray :\n";
		res += "Ncol = "+ ncol + "Nrow = "+ nrow + "\n";
		for (int i = 0; i < nrow; i++) {
			for (int j = 0; j < ncol; j++) {
				res += this.array[i][j]+"|";
			}
			res += "\n";
		}
		return res;
	}

	@Override
	public void applyBonus(ArrayList<Event>[] historyRollout) {
		
		System.out.println("bonus");
		
		
		//all tests are made on the "array" variable whereas all update are made the "copyMesh" variable
		//thus we avoid that one value is modified in one direction and then in the opposite direction, annihilating the modification
		int[][] copyMesh = new int[array.length][]; 
		
		for(int i=0;i<array.length;i++) {
			copyMesh[i] = array[i].clone();
		}
		
		ArrayList<Event> events;
		Event event;
		
		for(int k=0; k<historyRollout.length; k++) {
			events = historyRollout[k];
			
			for(int i=0; i< events.size(); i++) {
				
				event = events.get(i);
				
				int dx = event.pointObst.x - event.pointWhale.y;
				
				if((dx/QBoolArray.MESHSIZE > 0) && (dx/QBoolArray.MESHSIZE < ncol - 1)) { // if whale is at the left of the obstacle and their distance is less than ncol
					if(array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE] > 0) {
						System.out.println("before :" +  array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
						copyMesh[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE] +=bonus;
						System.out.println("after :" +  array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
					}else {
						System.out.println("before :" +  array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
						copyMesh[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE] -=bonus;
						System.out.println("after :" +  array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
					}
					
				}else { // handling the bound (i.e. whale is at the right of the obstacle or their distance is more than ncol)
					if(array[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1] >0) {
						copyMesh[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1] +=bonus;
					}else {
						copyMesh[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1] -=bonus;				
					}
				}			
			}
		}
		
	
		this.array = copyMesh;
		
		
	}
	
	@Override
	public void applyMalus(ArrayList<Event>[] historyRollout) {
		System.out.println("malus");
		//all tests are made on the "array" variable whereas all update are made the "copyMesh" variable
		//thus we avoid that one value is modified in one direction and then in the opposite direction, annihilating the modification
		int[][] copyMesh = new int[array.length][]; 
		
		for(int i=0;i<array.length;i++) {
			copyMesh[i] = array[i].clone();
		}
		
		ArrayList<Event> events;
		Event event;
		for(int k=0; k<historyRollout.length; k++) {
			events = historyRollout[k];
		
			for(int i=0; i< events.size(); i++) {
				
				event = events.get(i);
				int dx = event.pointObst.x - event.pointWhale.y;
				
				if((dx/QBoolArray.MESHSIZE < 0) && (dx/QBoolArray.MESHSIZE > ncol - 1)) { // if bird is at the left of the obstacle and their distance is less than ncol
					if(array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE] > 0) {
						System.out.println("before " + array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
						copyMesh[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE] +=malus;
						System.out.println("after " + copyMesh[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
					}else {
						System.out.println("before " + array[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
						copyMesh[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE] -=malus;
						System.out.println("after " + copyMesh[(event.pointWhale.y-event.pointObst.y)/QBoolArray.MESHSIZE+nrow/2][dx/QBoolArray.MESHSIZE]);
					}		
				}else { // handling the bound (i.e. bird is at the right of the obstacle or their distance is more than ncol)
					if(array[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1] >0) {
						System.out.println("beofre : " + array[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1]);
						copyMesh[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1] += malus;
						System.out.println("after1 " + copyMesh[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1]);
						
						
					}else {
						System.out.println("before " + array[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1]);
						copyMesh[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1] -= malus;	
						System.out.println("after11 " + copyMesh[event.pointWhale.y/QBoolArray.MESHSIZE + nrow/4][ncol-1]);
					}
				}
			}
		}
		
		//System.out.println("array equals : " + this.array.equals(copyMesh));
//		System.out.println(this);
//		System.out.println("-------------");
//		String res = "";
//		for (int i = 0; i < nrow; i++) {
//			for (int j = 0; j < ncol; j++) {
//				res += copyMesh[i][j]+"|";
//			}
//			res += "\n";
//		}
//		System.out.println(res);
		this.array = copyMesh;
	}
}
