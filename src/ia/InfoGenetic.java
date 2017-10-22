package ia;

public class InfoGenetic {
	private int numberGen;
	private int medianFit;
	private int maxFit;
	
	public InfoGenetic(int numberGen) {
		this.numberGen = numberGen;
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
	public void update(int numberGen, int medianFit, int maxFit) {
		this.numberGen = numberGen;
		this.medianFit = medianFit;
		this.maxFit = maxFit;
	}
}
