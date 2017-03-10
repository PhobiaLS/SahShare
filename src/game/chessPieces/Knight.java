package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;
import java.util.List;

import game.Teams;

/**
 * @author lazar.stefanovic
 *
 */
public class Knight extends ChessPiece {

	private static final long serialVersionUID = 9166826962036919559L;
	
	public Knight(Teams team) {
		super(team);
		
		if (team == Teams.WHITE_PLAYER)
			image = "images/figure-icons/knight-icon-white.png";
		else
			image = "images/figure-icons/knight-icon-black.png";
	}

	@Override
	public List<Point> possibleMoves(int x, int y) {
		List<Point> list = new ArrayList<Point>();
		for (Point point : generateCheckPoints(x,y)) {
			if(game.validPosition(point))
				list.add(point);
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
	 public static List<Point> generateCheckPoints(int x, int y){
		List<Point> list = new ArrayList<Point>();
		list.add(new Point(x+2, y-1));
		list.add(new Point(x+2, y+1));
		list.add(new Point(x-2, y+1));
		list.add(new Point(x-2, y-1));
		list.add(new Point(x+1, y+2));
		list.add(new Point(x+1, y-2));
		list.add(new Point(x-1, y+2));
		list.add(new Point(x-1, y-2));
		return list;
	}
	
}
