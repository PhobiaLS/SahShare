package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;
import java.util.List;

import game.Directions;

public class King extends ChessPiece {

	private static final long serialVersionUID = -4371509185383365709L;

	public King(int team) {
		super(team);
		if (team == 1) {
			image = "images/figure-icons/king-icon-white.png";
		} else {
			image = "images/figure-icons/king-icon-black.png";
		}
	}

	@Override
	public List<Point> possibleMoves(int x, int y) {
		List<Point> list = new ArrayList<>();
		for (Point point : generateCheckPoints(x,y)) {
			if(game.validPosition(point) && !game.isAttackedKing(point)) {
				list.add(point);
			}
		}	
		return list;
	}
	
	/**
	 * Za sad cu ovako, mozad budem promenio da se inkremeteri pamet u neki niz, ida se
	 * onda prolazi kroz niz i ispituje svaka pozicija posle inkrementacije
	 * @param x
	 * @param y
	 * @return
	 */
	public static List<Point> generateCheckPoints(int x, int y) {
		List<Point> list = new ArrayList<Point>();
		list.add(new Point(x+Directions.UP_LEFT.getX(), y+Directions.UP_LEFT.getY()));
		list.add(new Point(x+Directions.UP.getX(), y+Directions.UP.getY()));
		list.add(new Point(x+Directions.UP_RIGHT.getX(), y+Directions.UP_RIGHT.getY()));
		list.add(new Point(x+Directions.LEFT.getX(), y+Directions.LEFT.getY()));
		list.add(new Point(x+Directions.RIGHT.getX(), y+Directions.RIGHT.getY()));
		list.add(new Point(x+Directions.DOWN_LEFT.getX(), y+Directions.DOWN_LEFT.getY()));
		list.add(new Point(x+Directions.DOWN.getX(), y+Directions.DOWN.getY()));
		list.add(new Point(x+Directions.DOWN_RIGHT.getX(), y+Directions.DOWN_RIGHT.getY()));
		return list;
	}
	
}
