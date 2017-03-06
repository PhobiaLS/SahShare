package chessPieces;

import geometry.Point;

import java.util.ArrayList;

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
	public ArrayList<Point> possibleMoves(int i, int j) {
		ArrayList<Point> lista = new ArrayList<Point>();
		ChessPiece[][] board = game.getBoard();
		if(i+2<8){
			if(j+1<8 && (board[i+2][j+1]==null || (board[i+2][j+1]!=null && board[i+2][j+1].getTeam()!=team)))
				lista.add(new Point(i+2, j+1));
			
			if(j-1>-1 && (board[i+2][j-1]==null || (board[i+2][j-1]!=null && board[i+2][j-1].getTeam()!=team)))
				lista.add(new Point(i+2, j-1));
		}
		if(i+1<8){
			if(j+2<8 && (board[i+1][j+2]==null || (board[i+1][j+2]!=null && board[i+1][j+2].getTeam()!=team)))
				lista.add(new Point(i+1, j+2));
			
			if(j-2>-1 && (board[i+1][j-2]==null || (board[i+1][j-2]!=null && board[i+1][j-2].getTeam()!=team)))
				lista.add(new Point(i+1, j-2));
		}
		if(i-2>-1){
			if(j+1<8 && (board[i-2][j+1]==null || (board[i-2][j+1]!=null && board[i-2][j+1].getTeam()!=team)))
				lista.add(new Point(i-2, j+1));
			
			if(j-1>-1 && (board[i-2][j-1]==null || (board[i-2][j-1]!=null && board[i-2][j-1].getTeam()!=team)))
				lista.add(new Point(i-2, j-1));
		}
		if(i-1>-1){
			if(j+2<8 && (board[i-1][j+2]==null || (board[i-1][j+2]!=null && board[i-1][j+2].getTeam()!=team)))
				lista.add(new Point(i-1, j+2));
			
			if(j-2>-1 && (board[i-1][j-2]==null || (board[i-1][j-2]!=null && board[i-1][j-2].getTeam()!=team)))
				lista.add(new Point(i-1, j-2));
		}
		return lista;
	}

}
