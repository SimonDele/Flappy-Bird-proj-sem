package Modele;
import javax.swing.JLabel;

import Main.Main;

public class Obstacle {
	
	public static int INTERVAL;
	public static int LARGEUR; //Fixe
	
	private int posY; //Centre de l'obstacle
	private int posX;
	private int posObstHaut;
	private int posObstBas;
	
	private int speed; //Fixe
	public static float GENPROBA;
	public static int MINDIST; // minimal pixel distance between two obst
	
	public Obstacle(int y, int x) {
		// constantes 
		GENPROBA = 0.1f;
		MINDIST = 250;
		INTERVAL = 200;
		LARGEUR = 50;
		speed = 4  ;
		
		// 
		posY = y;
		posX = x;
		posObstHaut = y - INTERVAL/2;
		posObstBas = y + INTERVAL/2;
		//System.out.println("Haut"+ posObstHaut + "Bas" + posObstBas);
	}

	public int getPosObstHaut() {
		return posObstHaut;
	}

	public int getPosObstBas() {
		return posObstBas;
	}
	public int getPosX() {
		return posX;
	}
	
	//A virer il me semble
	/*
	
		posX -= speed;
		if(posX < 0) {
			labScore.setText(String.valueOf(++Main.score));
			posX = largeurEcran;
			posObstHaut = posY - INTERVAL/2;
			posObstBas = posY + INTERVAL/2;
			posY = Main.rand.nextInt(hauteurEcran);
		}
	}
	*/
	public boolean update() {
		posX -= speed;
		return posX + Obstacle.LARGEUR< 0; // returns whether to destroy it
	
	}
}
