
public class Bird {
	private int posX = 10; //Fixe
	private int posY; 
	private int gravity = 2; //Fixe
	
	public Bird() {
		posY = 0;
	}
	public Bird(int y) {
		posY = y;
		System.out.println(posY);
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
}
