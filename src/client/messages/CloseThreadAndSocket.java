package client.messages;

public class CloseThreadAndSocket extends ClientMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7130193206842514891L;

	public CloseThreadAndSocket(int clientID) {
		super(clientID);
	}

}
