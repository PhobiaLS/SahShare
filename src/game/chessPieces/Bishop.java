package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;

import game.Directions;
import game.Teams;

public class Bishop extends ChessPiece {
	
	private static final long serialVersionUID = 4537285960280863488L;

	public Bishop(Teams team) {
		super(team);
		if (team == Teams.WHITE_PLAYER)
			image = "images/figure-icons/bishop-icon-white.png";
		else
			image = "images/figure-icons/bishop-icon-black.png";
	}

	@Override
	public ArrayList<Point> possibleMoves(int x, int y) {
		ArrayList<Point> lista = new ArrayList<Point>();
		
		for (Directions direction : Directions.getDiagonal()) {
			lista.addAll(directionalWalk(x, y, direction));
		}
		
		return lista;
	}

}
