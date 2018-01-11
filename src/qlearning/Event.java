package qlearning;

import java.awt.Point;

public class Event {
	public Point pointWhale;
	public Point pointObst;
	
	public Event(Point pointWhale, Point pointObst) {
		this.pointWhale = pointWhale;
		this.pointObst = pointObst;
	}
}
