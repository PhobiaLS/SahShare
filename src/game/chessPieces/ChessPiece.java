package game.chessPieces;

import java.io.Serializable;
import java.util.ArrayList;
import game.*;
import geometry.*;

/**
 * @author Phobia
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
	
	public abstract ArrayList<Point> possibleMoves(int i, int j);
	
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
	 * Ovo je za fogire koje se krecu u pravima, Queen, Rook, Bishop
	 * @param x,y,direction
	 * @return ArrayList<Point>
	 */
	final protected ArrayList<Point> directionalWalk(int x,int y, Directions direction){
		int[] incremeters = retIncrementers(direction); // uzima inkrementere u zavisnosti od pravca kretanja
		int inc_x = incremeters[0], inc_y = incremeters[1];  
		ArrayList<Point> list = new ArrayList<>();
		ChessPiece[][] board = game.getBoard(); // uzima tablu 
		x -= inc_x; y -= inc_y; // uzima prvu poziciju setnje
		//
		while((x > -1 && x < 8) && (y > -1 && y < 8)){
			if(board[x][y]==null){
				list.add(new Point(x, y));
			} else {
				if(board[x][y].getTeam()!=team)
					list.add(new Point(x, y));
				break;
			}
			x -= inc_x; y -= inc_y; // pomera na sledecu poziciju setnje
		}
		
		return list;
	}	
	
	/**
	 * F-ja u zavisnosti od prosledjenog pravca, vraca inkrementere za x i y 
	 * @param direction
	 * @return {x,y} incrementers for the walk
	 */
	private int[] retIncrementers(Directions direction){
		switch (direction) {
		case UP_LEFT:
			return new int[] {-1,-1};
		case UP:
			return new int[] {-1,0};
		case UP_RIGHT:
			return new int[] {-1,1};
		case LEFT: 
			return new int[] {0,-1};
		case RIGHT:
			return new int[] {0,1};
		case DOWN_LEFT:
			return new int[] {1,-1};
		case DOWN:
			return new int[] {1,0};
		case DOWN_RIGHT:
			return new int[] {1,1};
		default:
			return new int[] {0,0};
		}
	}
	
	
}
