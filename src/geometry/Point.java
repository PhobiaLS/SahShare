package geometry;

import java.io.Serializable;
import java.util.List;

public class Point implements Serializable {
	
	private static final long serialVersionUID = 7000736698499772936L;
	private  int x;
	private  int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj){
		if (((Point)obj).getX() == x && ((Point)obj).getY() == y)
			return true;
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public static boolean exists(Point p, List<Point> list) {
		for (int i = 0; i < list.size(); i++)
			if (p.equals(list.get(i)))
				return true;
		return false;
	}
	
	public String toString(){
		return (x + " " + y);
	}
}
