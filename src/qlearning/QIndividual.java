package qlearning;

import java.awt.Point;
import java.util.ArrayList;

import model.Obstacle;
import model.Whale;

public class QIndividual {
	
	ArrayList<Event> history;
	QDNA dna;
	
	public QIndividual(Class<? extends QDNA> dnaImpl) {
		history = new ArrayList<Event>();
		try {
			this.dna = dnaImpl.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.println("Error in instanciating the implementation of DNA");
			e.printStackTrace();
		}
	}
	
	public void addActionToHistory(Whale whale, Obstacle obstacle) {
		this.history.add(new Event(new Point(whale.getPosX(), whale.getPosY()), new Point(obstacle.getPosX(),obstacle.getPosY())));
	}
	
	public boolean decideJump(Obstacle obst, Whale whale) {
		return dna.decidejump(obst, whale);
	}
	
}
