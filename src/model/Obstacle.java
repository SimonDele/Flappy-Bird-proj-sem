package model;

/**
 * Modelisation of a Game's obstacle; its main purpose is to go left and be destroyed.
 */
public class Obstacle {
	// Attributes for hitbox & display
	/**
	 * number of pixels in the space in which to cross the obstacle
	 */
	public static int INTERVAL;
	/**
	 * number of pixels of an obstacle's width
	 */
	public static int WIDTH; //Fixe
	
	// Positionning
	/**
	 * Y position of the center of the obstacle
	 */
	private int posY;
	/**
	 * X position of the left of the obstacle
	 */
	private int posX;
	/**
	 * y position of the lower left edge of the upper obstacle
	 */
	private int posObstHaut;
	/**
	 * y position of the upper left edge of the lower obstacle
	 */
	private int posObstBas;
	
	// Game unfolding
	/**
	 * obstacle speed
	 */
	private static int speed;
	/**
	 * Probability to generate an obstacle at the next frame
	 */
	public static float GENPROBA;
	/**
	 * Minimal pixel distance allowed between two obstacles
	 */
	public static int MINDIST; 
	
	// Constructor
	/**
	 * Initializes values based on our experience of a fair game
	 * @param y obstacle y position
	 * @param x obstacle x position
	 */
	public Obstacle(int y, int x) {
		// Constants 
		GENPROBA = 0.1f;
		MINDIST = 450;
		INTERVAL = 200;
		WIDTH = 50;
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
	/**
	 * Moves the obstacle to the left and tells whether to destroy it (too far left)
	 * @return whether to destroy the obstacle : posX (+width) < 0
	 */
	public boolean update() {
		posX -= speed;
		return ((posX + Obstacle.WIDTH) < 0); // returns whether to destroy it
	}
}
