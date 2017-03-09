package game.chessPieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.*;
import geometry.*;

/**
 * @author lazar.stefanovic
 *
 */
public abstract class ChessPiece implements Serializable{

	private static final long serialVersionUID = -4520361727949498133L;
	
	protected static Engine game;
	protected int team;
	protected String image = null;
	
	public ChessPiece(int team) {
		this.team = team;
	}
	
	public abstract List<Point> possibleMoves(int x, int y);
	
	public String getImage() {
		return image;
	}
	
	public void setEngine(Engine game) {
		ChessPiece.game = game;
	}
	
	public int getTeam() {
		return team;
	}

	/**
	 * U zavisnosti od prosledjenog pravca, vraca sve moguce pozicije na koje figura može da stane.
	 * Ovo je za figure koje se krecu u pravcima, Queen, Rook, Bishop
	 * @param x,y,direction
	 * @return ArrayList<Point>
	 */
	final protected ArrayList<Point> directionalWalk(int x,int y, Directions direction){
		ArrayList<Point> list = new ArrayList<>();
		ChessPiece[][] board = game.getBoard(); // uzima tablu 
		int inc_x = direction.getX(), inc_y = direction.getY();  
		x += inc_x; y += inc_y; // uzima prvu poziciju setnje

		while(game.inTable(new Point(x,y))){
			if(board[x][y]==null){
				list.add(new Point(x, y));
			} else {
				if(board[x][y].getTeam()!=team)
					list.add(new Point(x, y));
				break;
			}
			x += inc_x; y += inc_y; // pomera na sledecu poziciju setnje
		}
		
		return list;
	}	
}
