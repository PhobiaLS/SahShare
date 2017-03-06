package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;

import game.Directions;

public class Queen extends ChessPiece {

	private static final long serialVersionUID = 3276167627441063962L;

	public Queen(int team) {
		super(team);
		if (team == 1)
			image = "images/figure-icons/queen-icon-white.png";
		else
			image = "images/figure-icons/queen-icon-black.png";
	}

	@Override
	public ArrayList<Point> possibleMoves(int i, int j) {
		ArrayList<Point> lista = new ArrayList<Point>();
		
		lista.addAll(directionalWalk(i, j, Directions.UP_LEFT));
		lista.addAll(directionalWalk(i, j, Directions.UP));
		lista.addAll(directionalWalk(i, j, Directions.UP_RIGHT));
		lista.addAll(directionalWalk(i, j, Directions.LEFT));
		lista.addAll(directionalWalk(i, j, Directions.RIGHT));
		lista.addAll(directionalWalk(i, j, Directions.DOWN_LEFT));
		lista.addAll(directionalWalk(i, j, Directions.DOWN));
		lista.addAll(directionalWalk(i, j, Directions.DOWN_RIGHT));
		
		return lista;
	}
}
