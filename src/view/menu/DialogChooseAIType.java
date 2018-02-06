package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import controller.TypeOfAI;
import ia.dna.DNA;

public class DialogChooseAIType extends JDialog implements WindowListener {
	
	private ButtonGroup bg;
	private JRadioButton gameMesh;
	private JRadioButton nn;
	private JPanel container;
	private JPanel southPanel;
	
	private JPanel panelNN;
	private JPanel panelMesh;
	
	private JLabel labNN;
	private JLabel labMesh;
	
	private JButton launch;
	private SpinnerNumberModel  modelSizePop;
	private JSpinner spinnerSizePop;
	
	private Class<? extends DNA> dnaUsed;
	
	private Menu menu;
	
	public DialogChooseAIType(JDialog parent, String title, boolean modal, Menu menu){
		super(parent, title, modal);
		
		this.menu = menu;
		this.setResizable(false);
		this.addWindowListener(this);
		this.setLocationRelativeTo(parent);
		
		// RadioButton
		gameMesh = new JRadioButton("Game Mesh");
		gameMesh.setSelected(true);
		nn = new JRadioButton("Neural Network");
		
		bg = new ButtonGroup();
		bg.add(gameMesh);
		bg.add(nn);
	
		//JLabel 
		labNN = new JLabel(new ImageIcon(this.getClass().getResource("ressources/neuralnet.png")));
		labMesh = new JLabel(new ImageIcon(this.getClass().getResource("ressources/mesh_grid.png")));
		
		// NN panel
		panelNN = new JPanel();
		panelNN.setLayout(new BoxLayout(panelNN,BoxLayout.PAGE_AXIS));
		panelNN.add(nn);
		panelNN.add(labNN);
		
		// Mesh Panel
		panelMesh = new JPanel();
		panelMesh.setLayout(new BoxLayout(panelMesh,BoxLayout.PAGE_AXIS));
		panelMesh.add(gameMesh);
		panelMesh.add(labMesh);
		
		//Choose size pop
		modelSizePop = new SpinnerNumberModel(1000,1,100000,10);
		spinnerSizePop = new JSpinner(modelSizePop);

		//Launch Button
		launch = new JButton("Launch");
		launch.addActionListener(new TypeOfAI(this));
		
		//Main container
		container = new JPanel();
		container.setBackground(Color.BLACK);
		container.add(panelMesh);
		container.add(panelNN);
		
		//South Panel
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
		southPanel.add(spinnerSizePop);
		southPanel.add(launch);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(container, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		this.pack();
	}
	
	public JRadioButton getMeshRadio() {
		return this.gameMesh;
	}
	public JRadioButton getNNRadio() {
		return this.nn;
	}
	public int getSizePop() {
		return (int) this.spinnerSizePop.getValue();
	}

	public Class<? extends DNA> getDnaUsed() {
		return dnaUsed;
	}

	public void setDnaUsed(Class<? extends DNA> dnaUsed) {
		menu.setDnaUsed(dnaUsed);
	}

	public void setFramesPerAction(int framesPerAction) {
		menu.setFramesPerAction(framesPerAction);
	}

	public void setSizePop(int sizePop) {
		menu.setSizePop(sizePop);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);			
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
