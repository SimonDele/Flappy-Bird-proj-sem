package qlearning;

import java.util.ArrayList;

import model.Obstacle;
import model.Whale;

public interface QDNA {
	public void applyBonus(ArrayList<Event>[] historyRollout);
	public void applyMalus(ArrayList<Event>[] historyRollout);
	
	/**
	 * Asks the DNA model whether to jump, based on the obstacle and the bird it controls
	 * @param obstacle the obstacle to dodge
	 * @param whale the bird associated with the DNA implementation
	 * @return a boolean = (if bird should jump)
	 */
	public boolean decidejump(Obstacle obstacle, Whale whale);
}
