package serverMessages;

public class PlayerSearchInformation extends ServerMessage {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2505783101985993068L;
	
	private int id;
	private String username;
	private int rating;
	private int gameTime;
	
	public PlayerSearchInformation(int id, String username, int rating, int gameTime) {
		this.id = id;
		this.username = username;
		this.rating = rating;
		this.gameTime = gameTime;
	}
	
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public int getRating() {
		return rating;
	}

	public int getGameTime() {
		return gameTime;
	}
	
}
