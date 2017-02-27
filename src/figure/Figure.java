package figure;

import java.io.Serializable;
import java.util.ArrayList;
import game.*;
import geometry.*;

public abstract class Figure implements Serializable{

	private static final long serialVersionUID = -4520361727949498133L;
	
	protected static Engine game;
	protected int team;
	protected String image = null;
	
	public Figure(int team) {
		this.team = team;
	}
	
	public abstract ArrayList<Point> possibleMoves(int i, int j);
	
	public String getImage() {
		return image;
	}
	
	public void setEngine(Engine game) {
		Figure.game = game;
	}
	
	public int getTeam() {
		return team;
	}
	
}
