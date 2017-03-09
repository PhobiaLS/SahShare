package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;
import java.util.List;
import game.GameConstants;

/**
 * @author lazar.stefanovic
 *
 */
/**
 * @author lazar.stefanovic
 *
 */
/**
 * @author lazar.stefanovic
 *
 */
public class Knight extends ChessPiece {

	private static final long serialVersionUID = 9166826962036919559L;
	
	public Knight(int team) {
		super(team);
		
		if (team == 1)
			image = "images/figure-icons/knight-icon-white.png";
		else
			image = "images/figure-icons/knight-icon-black.png";
	}

	@Override
	public List<Point> possibleMoves(int x, int y) {
		List<Point> list = new ArrayList<Point>();
		for (Point point : generateCheckPoints(x,y)) {
			if(checker(point.getX(), point.getY()))
				list.add(point);
		}	
		return list;
	}
	
	
	/**
	 * Proverava da li je pozicija validna
	 * @param x
	 * @param y
	 * @return boolean
	 */
	private boolean checker(int x, int y){
		ChessPiece[][] board = game.getBoard();
		if(board[x][y]==null || (board[x][y]!=null && board[x][y].getTeam()!=team))
			return true;
		return false;
	}
	
	/**
	 * Za sad cu ovako, mozad budem promenio da se inkremeteri pamet u neki niz, ida se
	 * onda prolazi kroz niz i ispituje svaka pozicija posle inkrementacije
	 * @param x
	 * @param y
	 * @return
	 */
	private List<Point> generateCheckPoints(int x, int y){
		List<Point> list = new ArrayList<Point>();
		if((x+2 > -1 && x+2 < GameConstants.BOARD_SIZE) && (y+1 > -1 && y+1 < GameConstants.BOARD_SIZE))
			list.add(new Point(x+2, y+1));
		if((x+2 > -1 && x+2 < GameConstants.BOARD_SIZE) && (y-1 > -1 && y-1 < GameConstants.BOARD_SIZE))
			list.add(new Point(x+2, y+1));
		if((x+1 > -1 && x+1 < GameConstants.BOARD_SIZE) && (y+2 > -1 && y+2 < GameConstants.BOARD_SIZE))
			list.add(new Point(x+1, y+2));
		if((x+1 > -1 && x+1 < GameConstants.BOARD_SIZE) && (y-2 > -1 && y-2 < GameConstants.BOARD_SIZE))
			list.add(new Point(x+1, y-2));
		if((x-2 > -1 && x-2 < GameConstants.BOARD_SIZE) && (y+1 > -1 && y+1 < GameConstants.BOARD_SIZE))
			list.add(new Point(x-2, y+1));
		if((x-2 > -1 && x-2 < GameConstants.BOARD_SIZE) && (y-1 > -1 && y-1 < GameConstants.BOARD_SIZE))
			list.add(new Point(x-2, y-1));
		if((x-1 > -1 && x-1 < GameConstants.BOARD_SIZE) && (y+2 > -1 && y+2 < GameConstants.BOARD_SIZE))
			list.add(new Point(x-1, y+2));
		if((x-1 > -1 && x-1 < GameConstants.BOARD_SIZE) && (y-2 > -1 && y-2 < GameConstants.BOARD_SIZE))
			list.add(new Point(x-1, y-2));
		return list;
	}
	
}
