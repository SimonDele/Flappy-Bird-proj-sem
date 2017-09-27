import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PJeu extends JPanel{
	
	private Bird bird;
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	
	public PJeu() {
		bird = new Bird(100);
		obstacles.add(new Obstacle(this.getHeight(),(int) Math.random()*this.getHeight()));
		
	}
	public void paintComponent(Graphics g) {
		

		Graphics2D g2d = (Graphics2D) g;
		//Recouvrement de la fenetre avec la couleur de fond afin d'effacer ce qui est present
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());;
		
		//Replacement des composants
		// 1/ Bird
		bird.update();
		g2d.setColor(Color.yellow);
		g2d.fillOval(bird.getPosX(), bird.getPosY(), 50, 50);
	
		// 2/Obstacles
		g2d.setColor(Color.green);
		for(int i=0; i<obstacles.size();i++) {
			obstacles.get(i).update(this.getHeight());
			g2d.fillRect(obstacles.get(i).getPosX(),0, obstacles.get(i).getLargeur(),obstacles.get(i).getPosObstHaut());
		}
	}
}
