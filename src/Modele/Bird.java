package Modele;

import java.lang.Math;

public class Bird {
	// Position (! now it's the CENTER !)
	private int posX; 
	private int posY; 
	
	// Falling attributes
	private float gravity; // acceleration : how fast
	private int time; // for equation
	private float speed; // self-explanatory
	private float v0; // original speed - you don't want to touch this
	private float jumpHeight; // how high we jump
	
	// Attributes on hitboxes & death
	public static int SIZE; // bird sprite diameter
	private boolean dead; // boolean on death; stops score incrementation
	private int  deadSpeed; // speed of obstacles
	private int score; // how far we've gone
	
	// Constructor
	public Bird(int y, int deadSpeed) { // give obstSpeed as input
		time = 0;
		score = 0;
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
	public float getJumpHeight() {
		return jumpHeight;
	}

	// Methods
	/// Updating bird position 
	public void update(boolean saut) {
		// you're either alive and kicking, or dead and kicked left
		if (!dead) {
			if (saut) {time=0;} 
			speed = -time++*gravity + v0;
			this.posY -= speed;
		} else this.posX -= deadSpeed;
	}
	
	public void hit(int score) {
		dead = true;
		this.score = score;
	}
}
