package ia;

import java.awt.Point;
import java.util.ArrayList;

import Modele.Bird;

public class InfoGenetic {
	private int numberGen;
	private int medianFit;
	private int maxFit;
	private ArrayList<Point> serieFit;
	
	public InfoGenetic(int numberGen) {
		this.numberGen = numberGen;
		serieFit = new ArrayList<Point>();
	}
	public int getNumberGen() {
		return numberGen;
	}
	public int getMedianFit() {
		return medianFit;
	}
	public int getMaxFit() {
		return maxFit;
	}
	public ArrayList<Point> getSerieFit(){
		return serieFit;
	}
	public void update(int numberGen, Bird[] birds) {
		this.maxFit = this.calcMaxFit(birds);
		this.medianFit = this.calcMedianFit(birds);
		this.numberGen = numberGen;
		serieFit.add(new Point(numberGen, medianFit));
	}
	private int calcMaxFit(Bird[] birds) {
		int max = 0;
		for (int i = 0; i < birds.length; i++) {
			if(max < birds[i].getScore()) {
				max = birds[i].getScore();
			}
		}
		return max;
	}
	private int calcMedianFit(Bird[] birds) {
		return birds[(int)birds.length/2].getScore();
	}
}
