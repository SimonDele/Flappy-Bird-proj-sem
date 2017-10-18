package Vue;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DisplayInfoGenetic extends JPanel{
	

	public DisplayInfoGenetic() {
		this.setSize(new Dimension(1000, 500));
		this.setBackground(Color.black);
		this.add(new JLabel("Infos sur la génération précédente"));
	}
}
