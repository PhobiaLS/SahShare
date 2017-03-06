package chessPieces;

import geometry.Point;
import java.util.ArrayList;

import game.Directions;

public class Rook extends ChessPiece {
	
	private static final long serialVersionUID = 6358395367051671062L;

	public Rook(int team) {
		super(team);
		if (team == 1)
			image = "images/figure-icons/rook-icon-white.png";
		else
			image = "images/figure-icons/rook-icon-black.png";
	}

	@Override
	public ArrayList<Point> possibleMoves(int i, int j) {
		ArrayList<Point> lista = new ArrayList<Point>();
		
		lista.addAll(directionalWalk(i, j, Directions.UP));
		lista.addAll(directionalWalk(i, j, Directions.LEFT));
		lista.addAll(directionalWalk(i, j, Directions.RIGHT));
		lista.addAll(directionalWalk(i, j, Directions.DOWN));
		
		return lista;
	}

}