package client.messages;

public class GameSessionEnded extends ClientMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8194554855501098558L;
	private int causeOfEnd;
	public GameSessionEnded(int clientID) {
		super(clientID);
	}
	
	public int getCauseOfEnd() {
		return causeOfEnd;
	}
}
