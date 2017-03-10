package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;

import game.Directions;
import game.Teams;

public class Queen extends ChessPiece {

	private static final long serialVersionUID = 3276167627441063962L;

	public Queen(Teams team) {
		super(team);
		if (team == Teams.WHITE_PLAYER)
			image = "images/figure-icons/queen-icon-white.png";
		else
			image = "images/figure-icons/queen-icon-black.png";
	}

	@Override
	public ArrayList<Point> possibleMoves(int x, int y) {
		ArrayList<Point> lista = new ArrayList<Point>();
		
		for (Directions direction : Directions.getDiagonal()) {
			lista.addAll(directionalWalk(x, y, direction));
		}
		
		for (Directions direction : Directions.getStreight()) {
			lista.addAll(directionalWalk(x, y, direction));
		}
		return lista;
	}
}
