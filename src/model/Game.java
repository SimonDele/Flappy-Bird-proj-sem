package model;

import java.util.ArrayList;

import mainPkg.Main;

import java.lang.Math;

/**
 * Handles whale-obstacle movement and their interactions.
 */
public class Game {
	// Dimensions given by Window
	public static int DIMY;
	public static int DIMX;
	public static int SCORE;
	public static int PASSED;
	// The guys we're interested in
	private ArrayList<Obstacle> obstacles;
	private Whale[] whales;
	private float tolerance; // percentage of whale we take out of hitbox
	
	// Constructor
	/**
	 * Builds the game based on the requested dimx, dimy and population size
	 * @param dimx the number of x pixels
	 * @param dimy the number of y pixels
	 * @param N the number of whales
	 */
	public Game(int dimx, int dimy, int N) {
		Game.DIMX = dimx;
		Game.DIMY = dimy;
		Game.SCORE = 0;
		Game.PASSED = 0;
		tolerance = 0.2f;
		
		whales = new Whale[N];
		
		// creating list
		obstacles = new ArrayList<Obstacle>();
		// instanciating. Be careful : if multiple, ASCENDING X order !	
		obstacles.add(new Obstacle(Game.DIMY/2,Game.DIMX)); // better not start too hard ?
		// creating the bird AFTER obstacles (need of INTERVAL & speed)
		for(int i=0;i<N;i++) {
			obstacles.get(0);
			whales[i] = new Whale(Game.DIMY/2, Obstacle.getSpeed());
		}
		
	}
	
	// Getters and Setters

	public ArrayList<Obstacle> getObstacles(){
		return obstacles;
	}
	public float getTolerance() {
		return tolerance;
	}
	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}
	
	public Whale[] getBirds() {
		return whales;
	}

	// Methods
	/// Updating the game 
	/**
	 * Updates a game frame - everyone advances an obstacle may be destroyed; the hitboxes are checked and their effects applied. 
	 * @param saut
	 */
	public void update(boolean[] saut) { // one game frame
		// Updating bird 
		for(int i=0; i<saut.length;i++) {
			whales[i].update(saut[i]);
			// Death test (won't define end() when multiple birds)
			if (!whales[i].isDead()) {
				hit(whales[i]);	
			}
		}
		
		
		// Updating obstacles and potentially deleting (returns deleting boolean)
		boolean destroy = false;
		for(int i=0; i<obstacles.size();i++) {
			destroy = obstacles.get(i).update() || destroy; // if once true, always true
		}
		if (destroy) {
			obstacles.remove(0);
			Game.PASSED++;
			for(int i=0;i<whales.length;i++) {
				if (!whales[i].isDead()) {
				//	whales[i].increaseFitness(DIMY);
				}
			}
		}
		
		// Obstacle generation : if you've passed minimal distance (1st line),
		//  have a GENPROBA proba of generating new one, per frame (2nd line)
		if (obstacles.size() > 0 && (obstacles.get(obstacles.size()-1).getPosX() < (Game.DIMX - Obstacle.MINDIST)) 
				&& (Main.rand.nextFloat() < Obstacle.GENPROBA)) {
			// in that case, obst generation.
			obstacles.add(new Obstacle((int) Main.rand.nextInt(Game.DIMY - 2*Obstacle.INTERVAL)
					+ Obstacle.INTERVAL,Game.DIMX));

		}else if(obstacles.size()==0){
			obstacles.add(new Obstacle((int) Main.rand.nextInt(Game.DIMY - 2*Obstacle.INTERVAL)
					+ Obstacle.INTERVAL,Game.DIMX));
		}
		
		Game.SCORE++;
	}
	
	/// Ending the game - all birds died (are hit())
	/**
	 * Not implemented (only usefull for player which is not the goal) - ending of the game
	 * @return whether the game ends
	 */
	public boolean end() { 
		return false;
	}
	
	/// Hitboxes - I hope I've made this clear.
	/**
	 * Whether a whale has hit the obstacle. checks all positions for it to seem natural to the human eye
	 * @param whale the whale to check the position
	 * @return whether the whale hit the obstacle
	 */
	private boolean hit(Whale whale) {
		if(obstacles.size()<1) {//Means that there are no obstacles on the map
			return false;
		}
		boolean hit = false;
		
		// First, let's define the radius of the bird; 
		// with some error constant (bird is slightly less than size):
		int radius = (int) (Whale.SIZE/2 *(1-tolerance));
		
		// Let's prevent too many accesses to Classes 
		int currentY = whale.getPosY();
		int currentX = whale.getPosX(); // even though it's always the same..? (opti ?)
		int obstX = obstacles.get(0).getPosX();
		int obstYUp = obstacles.get(0).getPosObstHaut();
		int obstYDown = obstacles.get(0).getPosObstBas();
		
		// First, the ceiling - can't jump too high
		if (currentY < -whale.getJumpHeight()*0.5f) {
			hit = true;
			
			// Then, floor 
		} else 
		if (currentY+radius > Game.DIMY) {
			hit = true;
			
			// Now, there are 5 areas where we can hit the block:
			// 1: hitting faceward the upper part of obstacle
		} else if (((currentY < obstYUp) 
				&&	(currentX + radius > obstX))) {
			hit = true;
			
			// 2: same thing, lower part
		} else if (((currentY > obstYDown) 
				&&  (currentX + radius > obstX))) {
			hit = true;	
			
			// 3: inbetween both parts of the obstacle 
		} else if (((currentX - obstX > 0) && (currentX-obstX-Obstacle.WIDTH < 0))
				&& ((currentY - radius < obstYUp)||(currentY + radius > obstYDown))) {
			hit = true;
			
			// 4: hitting the front edge from under or above 		
		} else if ((Game.distance(currentX, currentY, obstX, obstYUp)<radius)
				|| (Game.distance(currentX, currentY, obstX, obstYDown)<radius)) {
			hit = true;
			
			// 5: hitting the rear end
		} else if ((Game.distance(currentX, currentY, obstX + Obstacle.WIDTH, obstYUp)  <radius)
				|| (Game.distance(currentX, currentY, obstX + Obstacle.WIDTH, obstYDown)<radius)) {
			hit = true;
		}
		
		// Done ! Update bird status and inform Game.
		if (hit) {whale.hit(Game.PASSED,Game.SCORE - (int)(Math.abs(obstacles.get(0).getPosY() - currentY)/(float)Obstacle.getSpeed()));} // must
		return hit;
	}

	
	/// Defining distance for Hitboxes
	/**
	 * Simple norm2 distance definition for hitoxes
	 * @param posX1
	 * @param posY1
	 * @param posX2
	 * @param posY2
	 * @return the distance in pixels
	 */
	public static float distance(int posX1, int posY1, int posX2, int posY2) {
		return ((float)Math.sqrt((posX1-posX2)*(posX1-posX2)+(posY1-posY2)*(posY1-posY2)));
	}

}
