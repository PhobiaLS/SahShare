package figure;

import geometry.Point;
import java.util.ArrayList;

public class Bishop extends Figure {
	
	private static final long serialVersionUID = 4537285960280863488L;

	public Bishop(int team) {
		super(team);
		if (team == 1)
			image = "images/figure-icons/bishop-icon-white.png";
		else
			image = "images/figure-icons/bishop-icon-black.png";
	}

	@Override
	public ArrayList<Point> possibleMoves(int i, int j) {
		ArrayList<Point> pos = new ArrayList<>();
		Figure[][] board = game.getBoard();
		
		boolean ind1 = true;
		boolean ind2 = true;
		boolean ind3 = true;
		boolean ind4 = true;
		
		for (int ii = 1; ii < 8; ii++) {
			if (i + ii < 8 && j + ii < 8 && board[i + ii][j + ii] != null && board[i + ii][j + ii].getTeam() == team)
				ind1 = false;
			
			if (ind1) {
				if (i + ii < 8 && j + ii < 8 && board[i + ii][j + ii] == null)
					pos.add(new Point(i + ii, j + ii));
				else if (i + ii < 8 && j + ii < 8 && board[i + ii][j + ii].getTeam() != team) {
					ind1 = false;
					pos.add(new Point(i + ii, j + ii));
				}
			}
			
			if (i + ii < 8 && j - ii >= 0 && board[i + ii][j - ii] != null && board[i + ii][j - ii].getTeam() == team)
				ind2 = false;
			
			if (ind2) {
				if (i + ii < 8 && j - ii >= 0 && board[i + ii][j - ii] == null)
					pos.add(new Point(i + ii, j - ii));
				else if (i + ii < 8 && j - ii >= 0 && board[i + ii][j - ii].getTeam() != team) {
					ind2 = false;
					pos.add(new Point(i + ii, j - ii));
				}
			}
			
			if (i - ii >= 0 && j - ii >= 0 && board[i - ii][j - ii] != null && board[i - ii][j - ii].getTeam() == team)
				ind3 = false;
			
			if (ind3) {
				if (i - ii >= 0 && j - ii >= 0 && board[i - ii][j - ii] == null)
					pos.add(new Point(i - ii, j - ii));
				else if (i - ii >= 0 && j - ii >= 0 && board[i - ii][j - ii].getTeam() != team) {
					ind3 = false;
					pos.add(new Point(i - ii, j - ii));
				}
			}
			
			if (i - ii >= 0 && j + ii < 8 && board[i - ii][j + ii] != null && board[i - ii][j + ii].getTeam() == team)
				ind4 = false;
			
			if (ind4) {
				if (i - ii >= 0 && j + ii < 8 && board[i - ii][j + ii] == null)
					pos.add(new Point(i - ii, j + ii));
				else if (i - ii >= 0 && j + ii < 8 && board[i - ii][j + ii].getTeam() != team) {
					ind4 = false;
					pos.add(new Point(i - ii, j + ii));
				}
			}
		}
		
		
		return pos;
	}

}
