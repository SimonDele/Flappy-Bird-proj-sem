package ia;

import java.awt.Point;
import java.util.ArrayList;

import model.Whale;

public class InfoGenetic {
	private int numberGen;
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
	public void update(int numberGen, Whale[] whales) {
		this.maxFit = this.calcMaxFit(whales);
		this.avgFit = this.calcAverageFit(whales);
		this.numberGen = numberGen;
		serieAvg.add(new Point(numberGen, avgFit));

	}
	private int calcMaxFit(Whale[] whales) {
		int max = 0;
		for (int i = 0; i < whales.length; i++) {
			if(max < whales[i].getScore()) {
				max = whales[i].getScore();
			}
		}
		return max;
	}
	private int calcAverageFit(Whale[] whales) {
		float meanScore = 0;
		for (int i = 0; i < whales.length; i++) {
			meanScore+=whales[i].getScore();
		}
		return (int)(meanScore/(float)whales.length);
	}
}
