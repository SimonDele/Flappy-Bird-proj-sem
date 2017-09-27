import java.util.Random;

public class Main {
	public static Random rand = new Random();
	public static int score = 0; //J'ai été obligé de le sortir de PJeu sinon il fait n'imorte quoi
	public static void main(String[] args) {
		try {
			Fenetre frame = new Fenetre();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
