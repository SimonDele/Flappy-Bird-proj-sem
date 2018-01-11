package view.game;

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

import mainPkg.Main;
import model.Game;
import model.Obstacle;
import model.Whale;

/**
 * @author simon
 */
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
		int mask2 = 0x80FF00FF;
		imBirdDown = new BufferedImage(imBirdTempDown.getWidth(null),imBirdTempDown.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imBirdDown.getGraphics().drawImage(imBirdTempDown, 0, 0 , null);
		if(Main.sizePop>1) {
			imBirdDown = createColorImage(imBirdDown,mask);
		}else {
			imBirdDown = createColorImage(imBirdDown,mask2);
		}
		imBirdUp = new BufferedImage(imBirdTempUp.getWidth(null),imBirdTempUp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		imBirdUp.getGraphics().drawImage(imBirdTempUp, 0, 0 , null);
		if(Main.sizePop>1) {
			imBirdUp = createColorImage(imBirdUp,mask);
		}else {
			imBirdUp = createColorImage(imBirdUp,mask2);
		}
		
		
		/// rocks & icebergs
		Image imRockTemp = null;
		Image imIcebergTemp = null;
		rockRatio = 2.6f;
		rockHeight = (int)(0.75f*Frame.DIMY);
		icebergRatio = 1.7f;
		icebergHeight = (int)(0.75f*Frame.DIMY);
		try {
			imRockTemp = ImageIO.read(this.getClass().getResource("ressources/rock.png"));
			imRockTemp = imRockTemp.getScaledInstance((int)(Obstacle.WIDTH*rockRatio), rockHeight, Image.SCALE_DEFAULT);
			imIcebergTemp = ImageIO.read(this.getClass().getResource("ressources/iceberg.png"));
			imIcebergTemp = imIcebergTemp.getScaledInstance((int)(Obstacle.WIDTH*icebergRatio), icebergHeight, Image.SCALE_DEFAULT);
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
			background = background.getScaledInstance(Frame.DIMX, Frame.DIMY, Image.SCALE_AREA_AVERAGING);
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
				g2d.drawImage(imRock, obstacles.get(i).getPosX() - 2*deltaRock, obstacles.get(i).getPosObstBas() - deltaRock,this);							//g2d.fillRect(obstacles.get(i).getPosX(),obstacles.get(i).getPosObstBas(), Obstacle.LARGEUR,this.getHeight()-obstacles.get(i).getPosObstBas());		
				// Upper part
				g2d.drawImage(imIceberg, obstacles.get(i).getPosX() - deltaIceberg, obstacles.get(i).getPosObstHaut() - icebergHeight,this);				}catch(IndexOutOfBoundsException e) {
				//System.out.println("Erreur incomprise");
			}catch(Exception e){	
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
