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
import Main.Main;

public class PJeu extends JPanel implements KeyListener {
	// Game's attributes
	private Bird bird; 
	private ArrayList<Obstacle> obstacles;
	private float tolerance; // ~ also amounts to shift necessary for ignoring the tail
	
	// Things to display
	private static JLabel labScore;
	private BufferedImage imBirdDown;
	private BufferedImage imBirdUp;
	private Image background;
	
	// Constructor
	public PJeu(int dimx, int dimy, Jeu jeu) {
		this.setSize(new Dimension(dimx,dimy));	
	
		// Game attributes init (once pointing towards, keeps pointing)
		bird = jeu.getBird();
		obstacles = jeu.getObstacles();
		tolerance = jeu.getTolerance();
		
		// Sprites:
		/// bird Up and Down
		Image imBirdTempDown = null;
		Image imBirdTempUp = null;
		try {
			imBirdTempDown = ImageIO.read(this.getClass().getResource("ressources/whaleDown.png"));
			imBirdTempDown = imBirdTempDown.getScaledInstance(bird.getSize(), bird.getSize(), Image.SCALE_DEFAULT);
			imBirdTempUp = ImageIO.read(this.getClass().getResource("ressources/whaleUp.png"));
			imBirdTempUp = imBirdTempUp.getScaledInstance(bird.getSize(), bird.getSize(), Image.SCALE_DEFAULT);
		}catch (IOException e) {
			System.out.println("Erreur lecture fichier bird");
			e.printStackTrace();
		}
		
		int mask = 0xFFFFF000;
		imBirdDown = new BufferedImage(imBirdTempDown.getWidth(null),imBirdTempDown.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imBirdDown.getGraphics().drawImage(imBirdTempDown, 0, 0 , null);
		imBirdDown = createColorImage(imBirdDown,mask);
		
		imBirdUp = new BufferedImage(imBirdTempUp.getWidth(null),imBirdTempUp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imBirdUp.getGraphics().drawImage(imBirdTempUp, 0, 0 , null);
		imBirdUp = createColorImage(imBirdUp,mask);
		
		/// Background
		try {
			background = ImageIO.read(this.getClass().getResource("ressources/background.png"));
			background = background.getScaledInstance(Fenetre.DIMX, Fenetre.DIMY, Image.SCALE_AREA_AVERAGING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/// Score
		labScore = new JLabel();
		labScore.setText(String.valueOf(bird.getScore()));
		labScore.setForeground(Color.red);
		Font f = new Font("Serif", Font.PLAIN, 36);
		labScore.setFont(f);
		this.add(labScore);

		// KeyListener (must strive to suppress this)
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	// Methods :
	/// Displaying
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Erasing current display by pressing bg onto it
		g2d.drawImage(background, 0, 0, this);
		
		// Displaying the game :
		/// Bird (up or down depending on speed)
		if (bird.getSpeed() < 0) {
			g2d.drawImage(imBirdDown, bird.getPosX()-(int)((1+tolerance)*bird.getSize()/2), bird.getPosY()-bird.getSize()/2,this);		
		} else {
			g2d.drawImage(imBirdUp, bird.getPosX()-(int)((1+tolerance)*bird.getSize()/2), bird.getPosY()-bird.getSize()/2,this);
		}

		/// Obstacles
		g2d.setColor(Color.green);
		for(int i=0; i<obstacles.size();i++) {
			// Lower part
			g2d.fillRect(obstacles.get(i).getPosX(),obstacles.get(i).getPosObstBas(), Obstacle.LARGEUR,this.getHeight()-obstacles.get(i).getPosObstBas());		
			// Upper part
			g2d.fillRect(obstacles.get(i).getPosX(),0, Obstacle.LARGEUR,obstacles.get(i).getPosObstHaut());		
		}

		/// Score
		labScore.setText(String.valueOf(bird.getScore()));		
	}
	
	/// Key eventing (must strive to get rid of)
	public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 32) { // Space pressing (for your futuristic clothes)
        	bird.up();
        }
    }
	@Override
	public void keyReleased(KeyEvent arg0) {	
	}
	@Override
	public void keyTyped(KeyEvent arg0) {		
	}
	
	/// Applying filter on sprite
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
