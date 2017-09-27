
public class Obstacle {
	
	public static int taille = 50;
	private int largeur = 25; //Fixe
	
	private int posY; //Centre de l'obstacle
	private int posX;
	private int posObstHaut;
	private int posObstBas;
	
	private int speed = 10; //Fixe
	
	public Obstacle(int y, int x) {
		posY = y;
		posX = 500; //A changer, probleme à l'init vaut 0...
		posObstHaut = y - Obstacle.taille/2;
		posObstBas = y + Obstacle.taille/2;
		System.out.println(posObstHaut);
	}

	public int getPosObstHaut() {
		return posObstHaut;
	}

	public int getPosObstBas() {
		return posObstBas;
	}
	
	public int getLargeur() {
		return largeur;
	}
	public int getPosX() {
		return posX;
	}
	
	public void update(int hauteurEcran) {
		posX -= speed;
		posObstHaut = hauteurEcran - Obstacle.taille/2;
		posObstBas = hauteurEcran + Obstacle.taille/2;
		System.out.println(posObstHaut);
	}
}
