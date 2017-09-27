import java.awt.Dimension;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	
	PJeu pjeu =  new PJeu();;
	
	public Fenetre(){
		this.setMinimumSize(new Dimension(400,600));
		this.setTitle("Smart Rockets");
	    this.setLocationRelativeTo(null);               
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    
		this.setContentPane(pjeu);
		this.setVisible(true);
		go();
		
		
	}
	
	private void go() {
		while(true) {
			pjeu.repaint();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
