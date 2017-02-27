package geometry;

import java.io.Serializable;
import java.util.ArrayList;

public class Point implements Serializable {
	
	private static final long serialVersionUID = 7000736698499772936L;
	private  int i;
	private  int j;
	
	public Point(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	@Override
	public boolean equals(Object obj){
		if (((Point)obj).getI() == i && ((Point)obj).getJ() == j)
			return true;
		return false;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
	
	public static boolean exists(Point p, ArrayList<Point> list) {
		for (int ii = 0; ii < list.size(); ii++)
			if (p.equals(list.get(ii)))
				return true;
		return false;
	}
	
	public String toString(){
		return (i + " " + j);
	}
}
