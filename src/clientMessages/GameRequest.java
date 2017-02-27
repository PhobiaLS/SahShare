package clientMessages;

public class GameRequest extends ClientMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5627085984956692826L;
	
	private String clientUsername;
	private int oponentId;
	private int gameTime;
	private int rating;

	public GameRequest(int clientID, String clientUsername, int oponentId, int gameTime, int rating) {
		super(clientID);
		this.clientUsername = clientUsername;
		this.oponentId = oponentId;
		this.gameTime = gameTime;
		this.rating = rating;
	}

	
	public String getClientUsername() {
		return clientUsername;
	}

	public int getOponentId() {
		return oponentId;
	}


	public int getGameTime() {
		return gameTime;
	}
	
	public int getRating() {
		return rating;
	}
	
}
