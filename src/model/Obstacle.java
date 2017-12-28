package model;

public class Obstacle {
	// Attributes for hitbox & display
	public static int INTERVAL;
	public static int LARGEUR; //Fixe
	
	// Positionning
	private int posY; // Center of obst
	private int posX;
	private int posObstHaut;
	private int posObstBas;
	
	// Game unfolding
	private static int speed; //Fixe
	public static float GENPROBA;
	public static int MINDIST; // minimal pixel distance between two obst
	
	// Constructor
	public Obstacle(int y, int x) {
		// Constants 
		GENPROBA = 0.1f;
		MINDIST = 450;
		INTERVAL = 200;
		LARGEUR = 50;
		speed = 7;
		
		// Position
		posY = y;
		posX = x;
		posObstHaut = y - INTERVAL/2;
		posObstBas = y + INTERVAL/2;
	}

	// Getters & Setters
	public int getPosObstHaut() {
		return posObstHaut;
	}
	public int getPosObstBas() {
		return posObstBas;
	}
	public int getPosX() {
		return posX;
	}
	public static int getSpeed() {
		return speed;
	}

	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}

	// Methods
	/// Updating
	public boolean update() {
		posX -= speed;
		return ((posX + Obstacle.LARGEUR) < 0); // returns whether to destroy it
	}
}
