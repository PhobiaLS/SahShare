package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;

import game.GameConstants;
import game.Teams;

public class Pawn extends ChessPiece {

	private static final long serialVersionUID = -730083232381245843L;

	public Pawn(Teams team) {
		super(team);
		if (team == Teams.WHITE_PLAYER) {
			image = "images/figure-icons/pawn-icon-white.png";
		} else {
			image = "images/figure-icons/pawn-icon-black.png";
		}
	}

	@Override
	public ArrayList<Point> possibleMoves(int x, int y) {
		ArrayList<Point> lista = new ArrayList<Point>();
		ChessPiece[][] board = game.getBoard();
		//Beli  ->  dole\\
		if(team == Teams.WHITE_PLAYER) {
			if(x==6 && board[5][y]==null && board[4][y]==null)
				lista.add(new Point(4, y));
			//Provera za Gore Desno
			if(x-1>-1) {
				if(board[x-1][y]==null) {
					lista.add(new Point(x-1, y));
				}
				if(y+1<GameConstants.BOARD_SIZE && board[x-1][y+1]!=null && board[x-1][y+1].getTeam()!=team) {
					lista.add(new Point(x-1, y+1));
				}
				if(y-1>-1 && board[x-1][y-1]!=null && board[x-1][y-1].getTeam()!=team) {
					lista.add(new Point(x-1, y-1));
				}
			}
		} else {
			if(x==1 && board[2][y]==null && board[3][y]==null) {
				lista.add(new Point(3, y));
			}
			if(x+1<GameConstants.BOARD_SIZE){
				if(board[x+1][y]==null) {
					lista.add(new Point(x+1, y));
				}
				if(y+1<GameConstants.BOARD_SIZE && board[x+1][y+1]!=null && board[x+1][y+1].getTeam()!=team) {
					lista.add(new Point(x+1, y+1));
				}
				if(y-1>-1 && board[x+1][y-1]!=null && board[x+1][y-1].getTeam()!=team) {
					lista.add(new Point(x+1, y-1));
				}
			}
		}
		
		return lista;
	}

}
