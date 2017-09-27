import java.util.Random;

public class Main {
	public static Random rand = new Random();
	
	public static void main(String[] args) {
		try {
			Fenetre frame = new Fenetre();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
