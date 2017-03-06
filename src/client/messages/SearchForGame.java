package client.messages;

public class SearchForGame extends ClientMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 356034339255391088L;
	
	private int gameTime;
	private String username;
	private int rating;
	
	public SearchForGame(int clientID, int gameTime, String username, int rating) {
		super(clientID);
		this.gameTime = gameTime;
		this.username = username;
		this.rating = rating;
	}
	
	public int getGameTime() {
		return gameTime;
	}

	public String getUsername() {
		return username;
	}

	public int getRating() {
		return rating;
	}

}
