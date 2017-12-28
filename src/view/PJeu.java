package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Game;
import model.Obstacle;
import model.Whale;

public class PJeu extends JPanel  {
	private int height;
	private int width;
	// Game's attributes
	private Whale[] whales;
	private ArrayList<Obstacle> obstacles;
	private float tolerance; // ~ also amounts to shift necessary for ignoring the tail
	
	// Things to display
	private static JLabel labScore;
	private BufferedImage imBirdDown;
	private BufferedImage imBirdUp;
	private Image background;
	
	// Constructor
	public PJeu(int dimx, int dimy, Game game) {
		this.setSize(new Dimension(dimx,dimy));	
		width = getWidth();
		height = getHeight();
		// Game attributes init (once pointing towards, keeps pointing)
		whales = game.getBirds();
		obstacles = game.getObstacles();
		tolerance = game.getTolerance();

		// Sprites:
		/// bird Up and Down
		Image imBirdTempDown = null;
		Image imBirdTempUp = null;
		try {
			imBirdTempDown = ImageIO.read(this.getClass().getResource("ressources/whaleDown.png"));
			imBirdTempDown = imBirdTempDown.getScaledInstance(Whale.SIZE, Whale.SIZE, Image.SCALE_DEFAULT);
			imBirdTempUp = ImageIO.read(this.getClass().getResource("ressources/whaleUp.png"));
			imBirdTempUp = imBirdTempUp.getScaledInstance(Whale.SIZE, Whale.SIZE, Image.SCALE_DEFAULT);
		}catch (IOException e) {
			System.out.println("Erreur lecture fichier bird");
			e.printStackTrace();
		}
		
		int mask = 0x3FFFF000;

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
		labScore.setText(String.valueOf(0));
		labScore.setForeground(Color.red);
		Font f = new Font("Serif", Font.PLAIN, 36);
		labScore.setFont(f);
		this.add(labScore);
		this.setFocusable(true);
	}
	
	// Methods :
	/// Update instance jeu
	public void updateJeu(Game game) {
		whales = game.getBirds();
		obstacles = game.getObstacles();
		tolerance = game.getTolerance();
	}
	/// Displaying
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Erasing current display by pressing bg onto it
		
		if(this.getWidth() != this.width || this.getHeight() != this.height) {
			background = background.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_AREA_AVERAGING);
			this.height = this.getHeight();
			this.width = this.getWidth();
		}
		
		g2d.drawImage(background, 0, 0, this);
		
		// Displaying the game :
		/// Bird (up or down depending on speed)
		Whale whale;
		for (int i=0; i<whales.length;i++) {
			whale = whales[i];
			if (whale.getSpeed() < 0) {
				g2d.drawImage(imBirdDown, whale.getPosX()-(int)((1+tolerance)*Whale.SIZE/2), whale.getPosY()-Whale.SIZE/2,this);		
			} else {
				g2d.drawImage(imBirdUp, whale.getPosX()-(int)((1+tolerance)*Whale.SIZE/2), whale.getPosY()-Whale.SIZE/2,this);
			}
		}

		/// Obstacles
		g2d.setColor(Color.green);
			for(int i=0; i<obstacles.size();i++) {

				try {
					// Lower part
					g2d.fillRect(obstacles.get(i).getPosX(),obstacles.get(i).getPosObstBas(), Obstacle.LARGEUR,this.getHeight()-obstacles.get(i).getPosObstBas());		
					// Upper part
					g2d.fillRect(obstacles.get(i).getPosX(),0, Obstacle.LARGEUR,obstacles.get(i).getPosObstHaut());							
				}catch(IndexOutOfBoundsException e) {
					System.out.println("Erreur incomprise");
				}

			}
			
		

		/// Score
		
		labScore.setText(String.valueOf(Game.PASSED));		
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
