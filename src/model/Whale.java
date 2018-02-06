package model;

import java.lang.Math;

/**
 * The modelisation of a game's whale. Its main purpose is to fall down and be brought back up.
 */
public class Whale {
	// Position (! now it's the CENTER !)
	/**
	 * X position of the center of the whale
	 */
	private int posX; 
	/**
	 * Y position of the center of the whale
	 */
	private int posY; 
	
	// Falling attributes
	/**
	 * Acceleration of the whale in pixels per frame² - how fast it falls
	 */
	private float gravity;
	/**
	 * Whale's sense of time in its trajectory, or the abscissa of the line that tells what the value of its speed should be 
	 */
	private int time;
	/**
	 * The current Y speed of the whale
	 */
	private float speed;
	/**
	 * The Y speed of the whale at t=0; calculated with a bit of calculus
	 */
	private float v0;
	/**
	 * Height of the jump in pixels, which will determine the v0
	 */
	private float jumpHeight;
	
	// Attributes on hitboxes & death
	/**
	 * The diameter of the whale's sprite
	 */
	public static int SIZE;
	/**
	 * Boolean for whether the whale died; stops score incrementation
	 */
	private boolean dead; 
	/**
	 * X speed when dead - obstacle speed
	 */
	private int  deadSpeed;
	/**
	 * How far we've gone in number of obstacles
	 */
	private int score; 
	/**
	 * A function of the distance crossed by the whale slightly different than score and used by the Genetic algo
	 */
	private int fitness;
	// Constructor
	/**
	 * Initializes everything based on our experience (for the gravity and heights, etc..). Sets y value and deadSpeed.
	 * @param y the original y value
	 * @param deadSpeed the x speed when dead
	 */
	public Whale(int y, int deadSpeed) { // give obstSpeed as input
		time = 0;
		score = 0;
		fitness = 0;
		SIZE = 80;
		posY = y;
		posX = 10 + SIZE/2;
		gravity = 0.5f;
		jumpHeight = Obstacle.INTERVAL *0.5f; // we can play on this 0.5
		v0 = (float) Math.sqrt(2*gravity*jumpHeight);
		dead = false; // don't try any other way
		this.deadSpeed = deadSpeed;
	}
	
	// Getters and Setters
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getSIZE() {
		return SIZE;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getFitness() {
		return fitness;
	}
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	public float getJumpHeight() {
		return jumpHeight;
	}

	/**
	 * Increases the fitness value
	 * @param addToFitness the value to be added
	 */
	public void increaseFitness(int addToFitness) {
		this.fitness += addToFitness;
	}
	/// Updating bird position 
	/**
	 * Updates whale position based on whether it wants to jump, the current time (hence speed), and whether it's dead
	 * @param jump whether to jump
	 */
	public void update(boolean jump) {
		// you're either alive and kicking, or dead and kicked left
		if (!dead) {
			if (jump) {time=0;} 
			speed = -time++*gravity + v0;
			this.posY -= speed;
		} else this.posX -= deadSpeed;
	}
	
	/**
	 * When the whale hit something bad. Dies on the spot and sets its score & fitness
	 * @param score the score to be set
	 * @param fitness the final fitness to add to the current
	 */
	public void hit(int score, int fitness) {
		dead = true;
		this.score = score;
		this.fitness += fitness;
		//System.out.println("score " + score + " fitness " + fitness);
	}
}
