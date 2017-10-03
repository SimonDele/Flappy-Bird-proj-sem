package Modele;

import Vue.Fenetre;
import java.lang.Math;

public class Bird {
	private int posX; //Fixe
	private int posY; 
	private float gravity; //Fixe
	private int size;
	private int time;
	private float speed;
	private float v0;
	private float jumpHeight;

	//Useless non ? 
/*	public Bird() {
		time = 0;
		posY = 0;
		posX = 10;
		size = 50;
		gravity = 0.7f;
		jumpHeight = Obstacle.INTERVAL / 2;
	}
	*/
	public Bird(int y) {
		time = 0;
		posY = y;
		posX = 10;
		size = 50;
		gravity = 0.7f;
		jumpHeight = Obstacle.INTERVAL / 2;
		v0 = (float) Math.sqrt(2*gravity*jumpHeight);
		System.out.println(v0);
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	//A virer ??
	public void update() {
		speed = -time*gravity + v0;
		this.posY -= speed;
		time++;	
	}
	
	//Meme chose qu'au dessus...
	public void update(boolean saut) {
		speed = -time*gravity + v0;
		this.posY -= speed;
		time++;
	}
	
	//C'est la même chose (ou presque) que dans la méthode end de la classe jeu non....?
	// Elle est d'ailleur jamais appelé
	public boolean aToucheObstacle() {
		return ((this.getPosY() < 0) || (this.getPosY() > Fenetre.DIMY - 2*this.getSize()));
	}
	public void up() {
		time = 0;
	}
}
