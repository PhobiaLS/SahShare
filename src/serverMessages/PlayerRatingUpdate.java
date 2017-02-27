package serverMessages;

public class PlayerRatingUpdate extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8813821791028850086L;
	
	private int playerId;
	private int newRating;
	
	public PlayerRatingUpdate(int playerId, int newRating) {
		this.playerId = playerId;
		this.newRating = newRating;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public int getNewRating() {
		return newRating;
	}

}
