package server;

public class WaitingPlayer {
	private int id;
	private int gameTime;
	
	public WaitingPlayer(int id, int gameTime) {
		this.id = id;
		this.gameTime = gameTime;
	}

	public int getId() {
		return id;
	}

	public int getGameTime() {
		return gameTime;
	}
	
	
}
