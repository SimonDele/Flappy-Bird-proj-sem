
public class Bird {
	private int posX = 10; //Fixe
	private int posY; 
	private int gravity = 7; //Fixe
	
	public Bird() {
		posY = 0;
	}
	public Bird(int y) {
		posY = y;
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
	public void update() {
		this.posY += gravity;
	}
	public boolean aToucheObstacle() {
		return false;
	}
	public void up() {
		this.posY -= 100;
	}
}
