package clientMessages;

public class SetUpChatConnection extends ClientMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1284653781215932898L;
	
	private int connectId;
	
	public SetUpChatConnection(int clientID, int connectId) {
		super(clientID);
		this.connectId = connectId;
	}

	public int getConnectId() {
		return connectId;
	}

}
