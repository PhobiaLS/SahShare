package game;

import java.io.Serializable;
import figure.Figure;
import geometry.*;

public class Move implements Serializable{

	private static final long serialVersionUID = -8042188616031818781L;
	
	private Point from;
	private Point to;
	private Figure fromFig;
	private Figure toFig;
	
	public Move(Point from, Point to, Figure fromFig, Figure toFig) {
		this.from = from;
		this.to = to;
		this.fromFig = fromFig;
		this.toFig = toFig;
	}

	public Point getFrom() {
		return from;
	}

	public Point getTo() {
		return to;
	}

	public Figure getFromFig() {
		return fromFig;
	}

	public Figure getToFig() {
		return toFig;
	}
	
}
