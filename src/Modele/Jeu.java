package Modele;

import java.util.ArrayList;
import java.lang.Math;

import Main.Main;

public class Jeu {
	// Dimensions given by Window
	public static int DIMY;
	public static int DIMX;
	// The guys we're interested in
	private ArrayList<Obstacle> obstacles;
	private Bird bird;
	private float tolerance; // percentage of bird we take out of hitbox
	
	// Constructor
	public Jeu(int dimx, int dimy) {
		Jeu.DIMX = dimx;
		Jeu.DIMY = dimy;
		tolerance = 0.2f;
		
		// creating list
		obstacles = new ArrayList<Obstacle>();
		// instanciating. Be careful : if multiple, ASCENDING X order !	
		obstacles.add(new Obstacle(Jeu.DIMY/2,Jeu.DIMX)); // better not start too hard ?
		// creating the bird AFTER obstacles (need of INTERVAL & speed)
		bird = new Bird(Jeu.DIMY/2, obstacles.get(0).getSpeed());
	}
	
	// Getters and Setters
	public Bird getBird() {
		return bird;
	}
	public void setBird(Bird bird) {
		this.bird = bird;
	}
	public ArrayList<Obstacle> getObstacles(){
		return obstacles;
	}
	public float getTolerance() {
		return tolerance;
	}
	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}
	
	// Methods
	/// Updating the game 
	public void update(boolean saut) {
		// Updating bird 
		bird.update(saut);
		
		// Updating obstacles and potentially deleting (returns deleting boolean)
		boolean destroy = false;
		for(int i=0; i<obstacles.size();i++) {
			destroy = obstacles.get(i).update() || destroy; // if once true, always true
		}
		if (destroy) {obstacles.remove(0);}
		
		// Obstacle generation : if you've passed minimal distance (1st line),
		//  have a GENPROBA proba of generating new one, per frame (2nd line)
		if ((obstacles.get(obstacles.size()-1).getPosX() < (Jeu.DIMX - Obstacle.MINDIST)) 
				&& (Main.rand.nextFloat() < Obstacle.GENPROBA)) {
			// in that case, obst generation.
			obstacles.add(new Obstacle((int) Main.rand.nextInt(Jeu.DIMY - 2*Obstacle.INTERVAL)
					+ Obstacle.INTERVAL,Jeu.DIMX));
		}
		
		// Death test (won't define end() when multiple birds)
		hit();
	}
	
	/// Ending the game - all birds died (are hit())
	public boolean end() { 
		return false;
	}
	
	/// Hitboxes - I hope I've made this clear.
	public boolean hit() {
		boolean hit = false;
		
		// First, let's define the radius of the bird; 
		// with some error constant (bird is slightly less than size):
		int radius = (int) (bird.getSize()/2 *(1-tolerance));
		
		// Let's prevent too many accesses to Classes 
		int currentY = bird.getPosY();
		int currentX = bird.getPosX(); // even though it's always the same..? (opti ?)
		int obstX = obstacles.get(0).getPosX();
		int obstYUp = obstacles.get(0).getPosObstHaut();
		int obstYDown = obstacles.get(0).getPosObstBas();
		
		// First, the floor - there is NO upper limit !
		if (currentY+radius > Jeu.DIMY) {
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
		} else if (((currentX - obstX > 0) && (currentX-obstX-Obstacle.LARGEUR < 0))
				&& ((currentY - radius < obstYUp)||(currentY + radius > obstYDown))) {
			hit = true;
			
			// 4: hitting the front edge from under or above 		
		} else if ((Jeu.distance(currentX, currentY, obstX, obstYUp)<radius)
				|| (Jeu.distance(currentX, currentY, obstX, obstYDown)<radius)) {
			hit = true;
			
			// 5: hitting the rear end
		} else if ((Jeu.distance(currentX, currentY, obstX + Obstacle.LARGEUR, obstYUp)  <radius)
				|| (Jeu.distance(currentX, currentY, obstX + Obstacle.LARGEUR, obstYDown)<radius)) {
			hit = true;
		}
		
		// Done ! Update bird status and inform Game.
		if (hit) {bird.setDead(true);} // mustn't go back to true anyhow
		return hit;
	}

	
	/// Defining distance for Hitboxes
	public static float distance(int posX1, int posY1, int posX2, int posY2) {
		return ((float)Math.sqrt((posX1-posX2)*(posX1-posX2)+(posY1-posY2)*(posY1-posY2)));
	}

}
