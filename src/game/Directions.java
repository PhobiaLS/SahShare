package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Daje inkrementere setnje za svaki pravac
 * @author lazar.stefanovic
 *
 */
public enum Directions {
	UP_LEFT(-1,-1),
	UP(-1,0),
	UP_RIGHT(-1,1),
	LEFT(0,-1),
	RIGHT(0,1),
	DOWN_LEFT(1,-1),
	DOWN(1,0),
	DOWN_RIGHT(1,1);
	
	private int x,y;
	
	
	private Directions(int x,int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public static List<Directions> getDirections(){
		List<Directions> list = new ArrayList<Directions>();
		list.add(UP_LEFT);
		list.add(UP);
		list.add(UP_RIGHT);
		list.add(LEFT);
		list.add(RIGHT);
		list.add(DOWN_LEFT);
		list.add(DOWN);
		list.add(DOWN_RIGHT);
		return list;
	}
	
	public static List<Directions> getDiagonal(){
		List<Directions> list = new ArrayList<Directions>();
		list.add(UP_LEFT);
		list.add(UP_RIGHT);
		list.add(DOWN_LEFT);
		list.add(DOWN_RIGHT);
		return list;
	}
	
	public static List<Directions> getStreight(){
		List<Directions> list = new ArrayList<Directions>();
		list.add(UP);
		list.add(LEFT);
		list.add(RIGHT);
		list.add(DOWN);
		return list;
	}
}
