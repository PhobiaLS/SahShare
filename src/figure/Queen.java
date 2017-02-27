package figure;

import geometry.Point;
import java.util.ArrayList;

public class Queen extends Figure {

	private static final long serialVersionUID = 3276167627441063962L;

	public Queen(int team) {
		super(team);
		if (team == 1)
			image = "images/figure-icons/queen-icon-white.png";
		else
			image = "images/figure-icons/queen-icon-black.png";
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
				if(board[i][k].getTeam()!=team)
					lista.add(new Point(i, k));
				break;
			}
		}
		//gore//
		for (int k = i-1; k > -1; k--) {
			if(board[k][j]==null){
				lista.add(new Point(k, j));
			} else {
				if(board[k][j].getTeam()!=team)
					lista.add(new Point(k, j));
				break;
			}
		}
		//dole//
		for (int k = i+1; k < board.length; k++) {
			if(board[k][j]==null){
				lista.add(new Point(k, j));
			} else{
				if (board[k][j].getTeam()!=team)
					lista.add(new Point(k, j));
				break;
			}
		}
		int[] ind = {0,0,0,0};
		for(int br=1;br<8; br++){
			if(i+br<8){
				if(j+br<8 && ind[0]==0){
					if(board[i+br][j+br]!=null){
						ind[0]=1;
						if(board[i+br][j+br].getTeam()!=team)
							lista.add(new Point(i+br, j+br));
					} else
						lista.add(new Point(i+br, j+br));
				}
				if(j-br>-1 && ind[1]==0){
					if(board[i+br][j-br]!=null){
						ind[1]=1;
						if(board[i+br][j-br].getTeam()!=team)
							lista.add(new Point(i+br, j-br));
					} else 
						lista.add(new Point(i+br, j-br));
				}
			}
			
			if(i-br>-1){
				if(j+br<8 && ind[2]==0){
					if(board[i-br][j+br]!=null){
						ind[2]=1;
						if(board[i-br][j+br].getTeam()!=team)
							lista.add(new Point(i-br, j+br));
					} else 
						lista.add(new Point(i-br, j+br));
				}
				if(j-br>-1 && ind[3]==0){
					if(board[i-br][j-br]!=null){
						ind[3]=1;
						if(board[i-br][j-br].getTeam()!=team)
							lista.add(new Point(i-br, j-br));
					} else
						lista.add(new Point(i-br, j-br));
				}
			}
		}
		
		return lista;
	}
}
