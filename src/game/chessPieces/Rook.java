package game.chessPieces;

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
	public ArrayList<Point> possibleMoves(int x, int y) {
		ArrayList<Point> lista = new ArrayList<Point>();
		
		for (Directions direction : Directions.getStreight()) {
			lista.addAll(directionalWalk(x, y, direction));
		}
		
		return lista;
	}

}