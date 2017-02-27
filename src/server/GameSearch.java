package server;

public class GameSearch {
	private int playerId;
	private String playerUsername;
	private int playerRating;
	private int gameTime;
	
	public GameSearch(int playerId, String playerUsername, int playerRating, int gameTime) {
		this.playerId = playerId;
		this.playerUsername = playerUsername;
		this.playerRating = playerRating;
		this.gameTime = gameTime;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public String getPlayerUsername() {
		return playerUsername;
	}
	
	public int getPlayerRating() {
		return playerRating;
	}

	public int getGameTime() {
		return gameTime;
	}
	
}
