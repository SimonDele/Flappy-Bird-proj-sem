package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ia.InfoGenetic;

public class DisplayInfoGenetic extends JPanel{
	
	private JLabel numGen;
	private JLabel maxFit;
	private JLabel medianFit;
	
	public DisplayInfoGenetic(InfoGenetic infoGenetic) {
		
		this.setSize(new Dimension(1000, 500));
		this.setBackground(Color.black);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		numGen = new JLabel("Generation n°"+infoGenetic.getNumberGen());
		numGen.setForeground(Color.white);
		numGen.setFont(new Font("Serif", Font.PLAIN, 32));
		
		maxFit = new JLabel("max fitness : "+infoGenetic.getMaxFit());
		maxFit.setForeground(Color.white);
		
		medianFit = new JLabel("max fitness : "+infoGenetic.getMedianFit());
		medianFit.setForeground(Color.white);
		
		this.add(numGen);
		this.add(maxFit);
		this.add(medianFit);
		
	}
}
