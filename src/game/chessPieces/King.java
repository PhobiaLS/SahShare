package game.chessPieces;

import geometry.Point;
import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {

	private static final long serialVersionUID = -4371509185383365709L;

	public King(int team) {
		super(team);
		if (team == 1)
			image = "images/figure-icons/king-icon-white.png";
		else
			image = "images/figure-icons/king-icon-black.png";
	}

	@Override
	public List<Point> possibleMoves(int i, int j) {
		List<Point> pos = new ArrayList<>();
		ChessPiece[][] board = game.getBoard();
		
		// proverava sever
		if (j + 1 < 8 && board[i][j + 1] == null && !canDiagonalEat(i, j, i, (j + 1)) && !canKnightEat(i, j + 1) && !canStraightEat(i, j, i, j + 1))
			pos.add(new Point(i, j + 1));
		else if (j + 1 < 8 && board[i][j + 1] != null && board[i][j + 1].getTeam() != team && !canDiagonalEat(i, j, i, j + 1) && !canKnightEat(i, j + 1) && !canStraightEat(i, j, i, j + 1)) {
			pos.add(new Point(i, j + 1));
		}
		
		// proverava severo-istok
		if (i + 1 < 8 && j + 1 < 8 && board[i + 1][j + 1] == null && !canDiagonalEat(i, j, i + 1, j + 1) && !canKnightEat(i + 1, j + 1) && !canStraightEat(i, j, i + 1, j + 1))
			pos.add(new Point(i + 1, j + 1));
		else if (i + 1 < 8 && j + 1 < 8 && board[i + 1][j + 1] != null && board[i + 1][j + 1].getTeam() != team && !canDiagonalEat(i, j, i + 1, j + 1) && !canKnightEat(i + 1, j + 1) && !canStraightEat(i, j, i, j + 1)) {
			pos.add(new Point(i + 1, j + 1));
		}
		
		// proverava istok
		if (i + 1 < 8 && board[i + 1][j] == null && !canDiagonalEat(i, j, i + 1, j) && !canKnightEat(i + 1, j) && !canStraightEat(i, j, i + 1, j))
			pos.add(new Point(i + 1, j));
		else if (i + 1 < 8 && board[i + 1][j] != null && board[i + 1][j].getTeam() != team && !canDiagonalEat(i, j, i + 1, j) && !canKnightEat(i + 1, j) && !canStraightEat(i, j, i + 1, j)) {
			pos.add(new Point(i + 1, j));
		}
		
		// proverava jugo-istok
		if (i + 1 < 8 && j - 1 >= 0 && board[i + 1][j - 1] == null && !canDiagonalEat(i, j, i + 1, j - 1) && !canKnightEat(i + 1, j - 1) && !canStraightEat(i, j, i + 1, j - 1))
			pos.add(new Point(i + 1, j - 1));
		else if (i + 1 < 8 && j - 1 >= 0 && board[i + 1][j - 1] != null && board[i + 1][j - 1].getTeam() != team && !canDiagonalEat(i, j, i + 1, j - 1) && !canKnightEat(i + 1, j - 1) && !canStraightEat(i, j, i + 1, j - 1)) {
			pos.add(new Point(i + 1, j - 1));
		}
		
		// proverava jug
		if (j - 1 >= 0 && board[i][j - 1] == null && !canDiagonalEat(i, j, i, j - 1) && !canKnightEat(i, j - 1) && !canStraightEat(i, j, i, j - 1))
			pos.add(new Point(i, j - 1));
		else if (j - 1 >= 0 && board[i][j - 1] != null && board[i][j - 1].getTeam() != team &&!canDiagonalEat(i, j, i, j - 1) && !canKnightEat(i, j - 1) && !canStraightEat(i, j, i, j - 1)) {
			pos.add(new Point(i, j - 1));
		}
		
		// proverava jugo-zapad
		if (i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1] == null && !canDiagonalEat(i, j, i - 1, j - 1) && !canKnightEat(i - 1, j - 1) && !canStraightEat(i, j, i - 1, j - 1))
			pos.add(new Point(i - 1, j - 1));
		else if (i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1] != null && board[i - 1][j - 1].getTeam() != team && !canDiagonalEat(i, j, i - 1, j - 1) && !canKnightEat(i - 1, j - 1) && !canStraightEat(i, j, i - 1, j - 1)) {
			pos.add(new Point(i - 1, j - 1));
		}
		
		// proverava zapad
		if (i - 1 >= 0 && board[i - 1][j] == null && !canDiagonalEat(i, j, i - 1, j) && !canKnightEat(i - 1, j) && !canStraightEat(i, j, i - 1, j))
			pos.add(new Point(i - 1, j));
		else if (i - 1 >= 0 && board[i - 1][j] != null && board[i - 1][j].getTeam() != team && !canDiagonalEat(i, j, i - 1, j) && !canKnightEat(i - 1, j) && !canStraightEat(i, j, i - 1, j)) {
			pos.add(new Point(i - 1, j));
		}
		
		// proverava severo-zapad
		if (i - 1 >= 0 && j + 1 < 8 && board[i - 1][j + 1] == null && !canDiagonalEat(i, j, i - 1, j + 1) && !canKnightEat(i - 1, j + 1) && !canStraightEat(i, j, i - 1, j + 1))
			pos.add(new Point(i - 1, j + 1));
		else if (i - 1 >= 0 && j + 1 < 8 && board[i - 1][j + 1] != null && board[i - 1][j + 1].getTeam() != team && !canDiagonalEat(i, j, i - 1, j + 1) && !canKnightEat(i - 1, j + 1) && !canStraightEat(i, j, i - 1, j + 1)) {
			pos.add(new Point(i - 1, j + 1));
		}
		
		return pos;
	}
	// trebalo bi da je okej 100 %
	private boolean canStraightEat(int starti, int startj, int i, int j) {
		ChessPiece[][] board = game.getBoard();
		
		boolean ind1 = true;
		boolean ind2 = true;
		for (int ii = 1; ii < 8; ii++) {
			if (ind1 && j + ii < 8 && board[i][j +ii] != null && board[i][j + ii].getTeam() != team && (board[i][j + ii] instanceof Rook || board[i][j + ii] instanceof Queen))
				return true;
			if (ind2 && i + ii < 8 && board[i + ii][j] != null && board[i + ii][j].getTeam() != team && (board[i + ii][j] instanceof Rook || board[i + ii][j] instanceof Queen))
				return true;
			if (ind1 && j + ii < 8 && board[i][j + ii] != null && board[i][j + ii] != board[starti][startj])
				ind1 = false;
			if (ind2 && i + ii < 8 && board[i + ii][j] != null && board[i + ii][j] != board[starti][startj])
				ind2 = false;
		}
		
		ind1 = true;
		ind2 = true;
		
		for (int ii = 1; ii < 8; ii++) {
			if (ind1 && j - ii >= 0 && board[i][j - ii] != null && board[i][j - ii].getTeam() != team && (board[i][j - ii] instanceof Rook || board[i][j - ii] instanceof Queen))
				return true;
			if (ind2 && i - ii >= 0 && board[i - ii][j] != null && board[i - ii][j].getTeam() != team && (board[i - ii][j] instanceof Rook || board[i - ii][j] instanceof Queen))
				return true;
			if (ind2 && i - ii >= 0 && board[i - ii][j] != null && board[i - ii][j] != board[starti][startj])
				ind2 = false;
			if (ind1 && j - ii >= 0 && board[i][j - ii] != null && board[i][j - ii] != board[starti][startj])
				ind1 = false;
		}
		
		// odvojeno samo za kralja
		if (j + 1 < 8 && board[i][j + 1] != null && board[i][j + 1].getTeam() != team && board[i][j + 1] instanceof King)
			return true;
		if (i + 1 < 8 && board[i + 1][j] != null && board[i + 1][j].getTeam() != team && board[i + 1][j] instanceof King)
			return true;
		if (j - 1 >= 0 && board[i][j - 1] != null && board[i][j - 1].getTeam() != team && board[i][j - 1] instanceof King)
			return true;
		if (i - 1 >= 0 && board[i - 1][j] != null && board[i - 1][j].getTeam() != team && board[i - 1][j] instanceof King)
			return true;
		return false;
		
	}
	
	//
	// ne radi dobro
	private boolean canDiagonalEat(int starti, int startj, int i, int j) {
		ChessPiece[][] board = game.getBoard();
		boolean ind1 = true;
		boolean ind2 = true;
		boolean ind3 = true;
		boolean ind4 = true;
		int br = 1;
		
		
		for (int ii = 1; ii < 8; ii++) {
			if (ind1 && i + ii < 8 && j + ii < 8 && board[i + ii][j + ii] != null && board[i + ii][j + ii].getTeam() != team) {
				if (board[i + ii][j + ii] instanceof Bishop || board[i + ii][j + ii] instanceof Queen)
					return true;
				if (br <= 1 &&  board[i + ii][j + ii] instanceof King)
					return true;
			}
			
			if (ind1 && i + ii < 8 && j +ii < 8 && board[i + ii][j + ii] != null && board[i + ii][j + ii] != board[starti][startj])
				ind1 = false;
			
			
			if (ind2 && i + ii < 8 && j - ii >= 0 && board[i + ii][j - ii] != null && board[i + ii][j - ii].getTeam() != team) {
				if (board[i + ii][j - ii] instanceof Bishop || board[i + ii][j - ii] instanceof Queen)
					return true;
				if (br <= 1 && board[i + ii][j - ii] instanceof King)
					return true;
			}
			
			if (ind2 && i + ii < 8 && j - ii >= 0 && board[i + ii][j - ii] != null && board[i + ii][j - ii] != board[starti][startj])
				ind2 = false;
			
			if (ind3 && i - ii >= 0 && j - ii >= 0 && board[i - ii][j - ii] != null && board[i - ii][j - ii].getTeam() != team) {
				if (board[i - ii][j - ii] instanceof Bishop || board[i - ii][j - ii] instanceof Queen)
					return true;
				if (br <= 1 && board[i - ii][j - ii] instanceof King)
					return true;
			}
			
			if (ind3 && i - ii >= 0 && j - ii >= 0 && board[i - ii][j - ii] != null && board[i - ii][j - ii] != board[starti][startj])
				ind3 = false;
			
			if (ind4 && i - ii >= 0 && j + ii < 8 && board[i - ii][j + ii] != null && board[i - ii][j + ii].getTeam() != team) {
				if (board[i - ii][j + ii] instanceof Bishop || board[i - ii][j + ii] instanceof Queen)
					return true;
				if (br <= 1 && board[i - ii][j + ii] instanceof King)
					return true;
			}
			
			if (ind4 && i - ii >= 0 && j + ii < 8 && board[i - ii][j + ii] != null && board[i - ii][j + ii] != board[starti][startj])
				ind4 = false;
			
			br++;
		}
		
		if (team == 1) {
			if (i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j - 1] != null && board[i - 1][j - 1].getTeam()!= team && board[i - 1][j - 1] instanceof Pawn)
				return true;
			if (i - 1 < 8 && j + 1 < 8 && board[i - 1][j + 1] != null && board[i - 1][j + 1].getTeam()!= team && board[i - 1][j + 1] instanceof Pawn)
				return true;
		}
		else {
			if (i + 1 < 8 && j - 1 >= 0 && board[i + 1][j - 1] != null && board[i + 1][j - 1].getTeam()!= team && board[i + 1][j - 1] instanceof Pawn)
				return true;
			if (i + 1 < 8 && j + 1 < 8 && board[i + 1][j + 1] != null && board[i + 1][j + 1].getTeam()!= team && board[i + 1][j + 1] instanceof Pawn)
				return true;
		}
		
		
		return false;
	}
	
	// trebalo bi da je tacno 100%
	private boolean canKnightEat(int i, int j) {
		ChessPiece[][] board = game.getBoard();
		if (i + 1 < 8 && j + 2 < 8 && board[i + 1][j + 2] != null && board[i + 1][j + 2].getTeam() != team && board[i + 1][j + 2] instanceof Knight)
			return true;
		
		if (i + 2 < 8 && j + 1 < 8 && board[i + 2][j + 1] != null && board[i + 2][j + 1].getTeam() != team && board[i + 2][j + 1] instanceof Knight)
			return true;
		
		if (i + 2 < 8 && j - 1 >= 0 && board[i + 2][j - 1] != null && board[i + 2][j - 1].getTeam() != team && board[i + 2][j - 1] instanceof Knight)
			return true;
		
		if (i + 1 < 8 && j - 2 >= 0 && board[i + 1][j - 2] != null && board[i + 1][j - 2].getTeam() != team && board[i + 1][j - 2] instanceof Knight)
			return true;
		
		if (i - 1 >= 0 && j - 2 >= 0 && board[i - 1][j - 2] != null && board[i - 1][j - 2].getTeam() != team && board[i - 1][j - 2] instanceof Knight)
			return true;
		
		if (i - 2 >= 0 && j - 1 >= 0 && board[i - 2][j - 1] != null && board[i - 2][j - 1].getTeam() != team && board[i - 2][j - 1] instanceof Knight)
			return true;
		
		if (i - 2 >= 0 && j + 1 < 8 && board[i - 2][j + 1] != null && board[i - 2][j + 1].getTeam() != team && board[i - 2][j + 1] instanceof Knight)
			return true;
		
		if (i - 1 >= 0 && j + 2 < 8 && board[i - 1][j + 2] != null && board[i - 1][j + 2].getTeam() != team && board[i - 1][j + 2] instanceof Knight)
			return true;
		
		return false;
	}
	
}
