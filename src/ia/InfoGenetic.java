package ia;

import java.awt.Point;
import java.util.ArrayList;

import Modele.Bird;

public class InfoGenetic {
	private int numberGen;
	private int medianFit;
	private int avgFit;
	private int maxFit;
	private ArrayList<Point> serieFit;
	private ArrayList<Point> serieAvg;
	
	public InfoGenetic(int numberGen) {
		this.numberGen = numberGen;
		serieFit = new ArrayList<Point>();
		serieAvg = new ArrayList<Point>();
	}
	public int getNumberGen() {
		return numberGen;
	}
	public int getMedianFit() {
		return medianFit;
	}
	public int getAvgFit() {
		return avgFit;
	}
	public int getMaxFit() {
		return maxFit;
	}
	public ArrayList<Point> getSerieFit(){
		return serieFit;
	}
	public ArrayList<Point> getSerieAvg(){
		return serieAvg;
	}
	public void update(int numberGen, Bird[] birds) {
		this.maxFit = this.calcMaxFit(birds);
		this.medianFit = this.calcMedianFit(birds);
		this.avgFit = this.calcAverageFit(birds);
		this.numberGen = numberGen;
		serieFit.add(new Point(numberGen, medianFit));
		serieAvg.add(new Point(numberGen, avgFit));

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
	private int calcAverageFit(Bird[] birds) {
		float meanScore = 0;
		for (int i = 0; i < birds.length; i++) {
			meanScore+=birds[i].getScore();
		}
		return (int)(meanScore/(float)birds.length);
	}
}
