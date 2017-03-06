package game;

import java.io.Serializable;

import chessPieces.ChessPiece;
import geometry.*;

public class Move implements Serializable{

	private static final long serialVersionUID = -8042188616031818781L;
	
	private Point from;
	private Point to;
	private ChessPiece fromFig;
	private ChessPiece toFig;
	
	public Move(Point from, Point to, ChessPiece fromFig, ChessPiece toFig) {
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

	public ChessPiece getFromFig() {
		return fromFig;
	}

	public ChessPiece getToFig() {
		return toFig;
	}
	
}
