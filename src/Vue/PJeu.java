package Vue;

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

import Modele.Bird;
import Modele.Jeu;
import Modele.Obstacle;

public class PJeu extends JPanel  {
	private int height;
	private int width;
	// Game's attributes
	private Bird[] birds;
	private ArrayList<Obstacle> obstacles;
	private float tolerance; // ~ also amounts to shift necessary for ignoring the tail
	
	// Things to display
	private static JLabel labScore;
	private BufferedImage imBirdDown;
	private BufferedImage imBirdUp;
	private BufferedImage imRock;
	private BufferedImage imIceberg;
	private float rockRatio; // (total width of Rock)/(Rock width witin hitbox)
	private int rockHeight; 
	private float icebergRatio;
	private int deltaRock; // translation for visual to match theoric
	private int icebergHeight;
	private int deltaIceberg;
	private Image background;
	
	// Constructor
	public PJeu(int dimx, int dimy, Jeu jeu) {
		this.setSize(new Dimension(dimx,dimy));	
		width = getWidth();
		height = getHeight();
		// Game attributes init (once pointing towards, keeps pointing)
		birds = jeu.getBirds();
		obstacles = jeu.getObstacles();
		tolerance = jeu.getTolerance();

		// Sprites:
		/// bird Up and Down
		Image imBirdTempDown = null;
		Image imBirdTempUp = null;
		try {
			imBirdTempDown = ImageIO.read(this.getClass().getResource("ressources/whaleDown.png"));
			imBirdTempDown = imBirdTempDown.getScaledInstance(Bird.SIZE, Bird.SIZE, Image.SCALE_DEFAULT);
			imBirdTempUp = ImageIO.read(this.getClass().getResource("ressources/whaleUp.png"));
			imBirdTempUp = imBirdTempUp.getScaledInstance(Bird.SIZE, Bird.SIZE, Image.SCALE_DEFAULT);
			
		}catch (IOException e) {
			System.out.println("Erreur lecture fichier bird");
			e.printStackTrace();
		}
				
		int mask = 0xFFFFFF00;
		imBirdDown = new BufferedImage(imBirdTempDown.getWidth(null),imBirdTempDown.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imBirdDown.getGraphics().drawImage(imBirdTempDown, 0, 0 , null);
		imBirdDown = createColorImage(imBirdDown,mask);
		
		imBirdUp = new BufferedImage(imBirdTempUp.getWidth(null),imBirdTempUp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imBirdUp.getGraphics().drawImage(imBirdTempUp, 0, 0 , null);
		imBirdUp = createColorImage(imBirdUp,mask);
		

		/// rocks & icebergs
		Image imRockTemp = null;
		Image imIcebergTemp = null;
		rockRatio = 2.6f;
		System.out.println(rockRatio*Obstacle.LARGEUR);
		rockHeight = (int)(0.75f*Fenetre.DIMY);
		icebergRatio = 1.7f;
		icebergHeight = (int)(0.75f*Fenetre.DIMY);
		try {
			imRockTemp = ImageIO.read(this.getClass().getResource("ressources/rock.png"));
			imRockTemp = imRockTemp.getScaledInstance((int)(Obstacle.LARGEUR*rockRatio), rockHeight, Image.SCALE_DEFAULT);
			imIcebergTemp = ImageIO.read(this.getClass().getResource("ressources/iceberg.png"));
			imIcebergTemp = imIcebergTemp.getScaledInstance((int)(Obstacle.LARGEUR*icebergRatio), icebergHeight, Image.SCALE_DEFAULT);
		}catch (IOException e) {
			System.out.println("Erreur lecture fichier rock ou iceberg");
			e.printStackTrace();
		}
		
		imRock = new BufferedImage(imRockTemp.getWidth(null),imRockTemp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imRock.getGraphics().drawImage(imRockTemp, 0, 0 , null);
		imIceberg = new BufferedImage(imIcebergTemp.getWidth(null),imIcebergTemp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imIceberg.getGraphics().drawImage(imIcebergTemp, 0, 0 , null);
		deltaRock = (int)(1/20.5f*imRockTemp.getWidth(null));
		deltaIceberg = (int)(1/10f*imIcebergTemp.getWidth(null));
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
		Bird bird;
		for (int i=0; i<birds.length;i++) {
			bird = birds[i];
			if (bird.getSpeed() < 0) {
				g2d.drawImage(imBirdDown, bird.getPosX()-(int)((1+tolerance)*Bird.SIZE/2), bird.getPosY()-Bird.SIZE/2,this);		
			} else {
				g2d.drawImage(imBirdUp, bird.getPosX()-(int)((1+tolerance)*Bird.SIZE/2), bird.getPosY()-Bird.SIZE/2,this);
			}
		}

		/// Obstacles
		g2d.setColor(Color.green);
		for(int i=0; i<obstacles.size();i++) {
			// Lower part
			//g2d.fillRect(obstacles.get(i).getPosX(),obstacles.get(i).getPosObstBas(), Obstacle.LARGEUR,this.getHeight()-obstacles.get(i).getPosObstBas());		
			g2d.drawImage(imRock, obstacles.get(i).getPosX() - 2*deltaRock, obstacles.get(i).getPosObstBas() - deltaRock,this);		
			// Upper part
			//g2d.fillRect(obstacles.get(i).getPosX(),0, Obstacle.LARGEUR,obstacles.get(i).getPosObstHaut());	
			g2d.drawImage(imIceberg, obstacles.get(i).getPosX() - deltaIceberg, obstacles.get(i).getPosObstHaut() - icebergHeight,this);		
		}

		/// Score
		
		labScore.setText(String.valueOf(Jeu.SCORE));		
	}
	
	
	/// Applying filter on sprite
    private BufferedImage createColorImage(BufferedImage originalImage, int mask) {
        BufferedImage colorImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(), originalImage.getType());
        
        int pixel;
        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
            	pixel = originalImage.getRGB(x, y) & mask;
                colorImage.setRGB(x, y, pixel);
            }
        }

        return colorImage;
    }
}

// on a bossé dur pour ça
//if ((originalImage.getRGB(x, y) >> 56) != 0) {
//	pixel = 0xFFFFFFFF;
//} else pixel = originalImage.getRGB(x, y);
