package Modele;
import javax.swing.JLabel;

import Main.Main;

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
	private int speed; //Fixe
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
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
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