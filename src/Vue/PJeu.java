package Vue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
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
	private BufferedImage imBird;
	private Image background;
	
	public PJeu(int dimx, int dimy, Jeu jeu) {
		
		this.jeu = jeu;
		
		this.setSize(new Dimension(dimx,dimy));
		
		
	
		//Creation des composants
		bird = new Bird(Fenetre.DIMY/2);
		obstacles = jeu.getObstacles();
		/*
		obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Obstacle((int) Main.rand.nextInt(Fenetre.DIMY - 2*Obstacle.INTERVAL) 
				+ Obstacle.INTERVAL,Fenetre.DIMX));
		*/
		
		//Images
		Image imBirdTemp = null;
		try {
			imBirdTemp = ImageIO.read(this.getClass().getResource("ressources/whaledown.png"));
			imBirdTemp = imBirdTemp.getScaledInstance(bird.getSize(), bird.getSize(), Image.SCALE_DEFAULT);
		}catch (IOException e) {
			System.out.println("Erreur lecture fichier");
			e.printStackTrace();
		}
		imBird = new BufferedImage(imBirdTemp.getWidth(null),imBirdTemp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imBird.getGraphics().drawImage(imBirdTemp, 0, 0 , null);
		imBird = createColorImage(imBird,0xFF00FF00);
		try {
			background = ImageIO.read(this.getClass().getResource("ressources/background.png"));
			background = background.getScaledInstance(Fenetre.DIMX, Fenetre.DIMY, Image.SCALE_AREA_AVERAGING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
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
		g2d.drawImage(background, 0, 0, this);
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
	
    private BufferedImage createColorImage(BufferedImage originalImage, int mask) {
        BufferedImage colorImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(), originalImage.getType());

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                int pixel = originalImage.getRGB(x, y) & mask;
                colorImage.setRGB(x, y, pixel);
            }
        }

        return colorImage;
    }
}
