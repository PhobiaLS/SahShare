package server.messages;

public class GameAccepted extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7306897955291289730L;
	private String opponentUsername;
	private int playerRating;
	
	public GameAccepted(String opponentUsername, int playerRating) {
		this.opponentUsername = opponentUsername;
		this.playerRating = playerRating;
	}
	
	public String getOpponentUsername() {
		return opponentUsername;
	}
	
	public int getPlayerRating() {
		return playerRating;
	}
}
