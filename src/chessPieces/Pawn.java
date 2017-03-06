package chessPieces;

import geometry.Point;
import java.util.ArrayList;

public class Pawn extends ChessPiece {

	private static final long serialVersionUID = -730083232381245843L;

	public Pawn(int team) {
		super(team);
		if (team == 1)
			image = "images/figure-icons/pawn-icon-white.png";
		else
			image = "images/figure-icons/pawn-icon-black.png";
	}

	@Override
	public ArrayList<Point> possibleMoves(int i, int j) {
		ArrayList<Point> lista = new ArrayList<Point>();
		ChessPiece[][] board = game.getBoard();
		//Beli  ->  dole\\
		if(team==1){
			if(i==6 && board[5][j]==null && board[4][j]==null)
				lista.add(new Point(4, j));
			//Provera za Gore Desno
			if(i-1>-1){
				if(board[i-1][j]==null)
					lista.add(new Point(i-1, j));
				if(j+1<8 && board[i-1][j+1]!=null && board[i-1][j+1].getTeam()!=team)
					lista.add(new Point(i-1, j+1));
				if(j-1>-1 && board[i-1][j-1]!=null && board[i-1][j-1].getTeam()!=team)
					lista.add(new Point(i-1, j-1));
			}
		} else {
			if(i==1 && board[2][j]==null && board[3][j]==null)
				lista.add(new Point(3, j));
			if(i+1<8){
				if(board[i+1][j]==null)
					lista.add(new Point(i+1, j));
				if(j+1<8 && board[i+1][j+1]!=null && board[i+1][j+1].getTeam()!=team)
					lista.add(new Point(i+1, j+1));
				if(j-1>-1 && board[i+1][j-1]!=null && board[i+1][j-1].getTeam()!=team)
					lista.add(new Point(i+1, j-1));
			}
		}
		
		return lista;
	}

}
