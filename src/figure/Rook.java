package figure;

import geometry.Point;
import java.util.ArrayList;

public class Rook extends Figure {
	
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
		Figure[][] board = game.getBoard();
		//desno//
		for (int k = j+1; k < board.length; k++) {
			if(board[i][k]==null){
				lista.add(new Point(i, k));
			} else {
				if(board[i][k].getTeam()!=team)
					lista.add(new Point(i, k));
				break;
			}
		}
		//levo//
		for (int k = j-1; k > -1; k--) {
			if(board[i][k]==null){
				lista.add(new Point(i, k));
			} else {
				if(board[i][k]!=null && board[i][k].getTeam()!=team)
					lista.add(new Point(i, k));
				break;
			}
		}
		//gore//
		for (int k = i-1; k > -1; k--) {
			if(board[k][j]==null){
				lista.add(new Point(k, j));
			} else {
				if(board[k][j]!=null && board[k][j].getTeam()!=team)
					lista.add(new Point(k, j));
				break;
			} 
		}
		//dole//
		for (int k = i+1; k < board.length; k++) {
			if(board[k][j]==null){
				lista.add(new Point(k, j));
			} else {
				if(board[k][j]!=null && board[k][j].getTeam()!=team)
					lista.add(new Point(k, j));
				break;
			}
		}
		
		return lista;
	}

}