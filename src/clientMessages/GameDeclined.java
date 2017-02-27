package clientMessages;

public class GameDeclined extends ClientMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4213343198090559132L;
	
	private int oponentId;

	public GameDeclined(int clientID, int oponentId) {
		super(clientID);
		this.oponentId = oponentId;
	}
	
	public int getOponentId() {
		return oponentId;
	}

}
