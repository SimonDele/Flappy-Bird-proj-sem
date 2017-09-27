import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PJeu extends JPanel implements KeyListener{
	
	private Bird bird;
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private static int score = 0;
	private static JLabel labScore = new JLabel();;
	public PJeu() {
		
		this.setSize(new Dimension(300,500));
		
		//Creation des composants
		bird = new Bird(100);
			
		obstacles.add(new Obstacle(Main.rand.nextInt(this.getHeight()),this.getWidth()));
		obstacles.add(new Obstacle(Main.rand.nextInt(this.getHeight()),(int) (this.getWidth()*0.75)));
		obstacles.add(new Obstacle(Main.rand.nextInt(this.getHeight()),(int) (this.getWidth()*0.5)));
		this.addKeyListener(this);
		this.setFocusable(true);
		
		//Score
		
		labScore.setText(String.valueOf(score));
		labScore.setForeground(Color.red);
		Font f = new Font("Serif", Font.PLAIN, 36);
		labScore.setFont(f);
		this.add(labScore);
	}
	

	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//Recouvrement de la fenetre avec la couleur de fond afin d'effacer ce qui est present
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());;
		
		//Replacement des composants
		// 1/ Bird
		bird.update();
		
		if(!bird.aToucheObstacle()) {
			g2d.setColor(Color.yellow);
			g2d.fillOval(bird.getPosX(), bird.getPosY(), 50, 50);
		
			// 2/Obstacles
			g2d.setColor(Color.green);
			for(int i=0; i<obstacles.size();i++) {
				obstacles.get(i).update(this.getHeight(), this.getWidth(), labScore);

				g2d.fillRect(obstacles.get(i).getPosX(),0, obstacles.get(i).getLargeur(),obstacles.get(i).getPosObstHaut());
				g2d.fillRect(obstacles.get(i).getPosX(),obstacles.get(i).getPosObstBas(), obstacles.get(i).getLargeur(),this.getHeight()-obstacles.get(i).getPosObstBas());		
			}
		}else {
			//T'as perdu 
		}
		
	}
	public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 32) { //Appuie sur la barre d'espace
        	bird.up();
        }
    }

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}
}
