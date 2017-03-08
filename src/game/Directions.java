package game;

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
	
	
}
