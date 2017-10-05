package Vue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class PJeu extends JPanel implements KeyListener{
	
	private Bird bird;
	private ArrayList<Obstacle> obstacles;
	private static int score;
	private static JLabel labScore;
	private Jeu jeu;
	private Image imBird;
	
	public PJeu(int dimx, int dimy, Jeu jeu) {
		
		this.jeu = jeu;
		
		this.setSize(new Dimension(dimx,dimy));
		
		//Images
		imBird = null;
		/*try {
			//imBird = ImageIO.read();
		} catch (IOException e) {
			System.out.println("Erreur lecture fichier");
			e.printStackTrace();
		}*/
		System.out.println(this.getClass().getResource("ressources/whaledown.png"));
		
		//Creation des composants
		bird = new Bird(Fenetre.DIMY/2);
		obstacles = jeu.getObstacles();
		/*
		obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Obstacle((int) Main.rand.nextInt(Fenetre.DIMY - 2*Obstacle.INTERVAL) 
				+ Obstacle.INTERVAL,Fenetre.DIMX));
		*/
		this.addKeyListener(this);
		this.setFocusable(true);
		
		//Score
		score = 0;
		labScore = new JLabel();
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
		
		boolean destroy = false;
		if(!bird.aToucheObstacle()) {
			//g2d.setColor(Color.yellow);
			g2d.drawImage(imBird, bird.getPosX(), bird.getPosY(),this);
			
			//g2d.fillOval(bird.getPosX(), bird.getPosY(), bird.getSize(), bird.getSize());
		
			// 2/Obstacles
			g2d.setColor(Color.green);
			for(int i=0; i<obstacles.size();i++) {
				destroy = obstacles.get(i).update() || destroy;
				
				g2d.fillRect(obstacles.get(i).getPosX(),0, Obstacle.LARGEUR,obstacles.get(i).getPosObstHaut());
				g2d.fillRect(obstacles.get(i).getPosX(),obstacles.get(i).getPosObstBas(), Obstacle.LARGEUR,this.getHeight()-obstacles.get(i).getPosObstBas());		
			}
			/*
			if (destroy) {
				obstacles.remove(0);
			}
			if ((obstacles.get(obstacles.size()-1).getPosX() < Fenetre.DIMX - Obstacle.MINDIST) 
					&& (Main.rand.nextFloat() < Obstacle.GENPROBA)) {
				obstacles.add(new Obstacle((int) Main.rand.nextInt(Fenetre.DIMY - 2*Obstacle.INTERVAL)
						+ Obstacle.INTERVAL,Fenetre.DIMX));
			}*/
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
