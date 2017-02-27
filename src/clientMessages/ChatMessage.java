package clientMessages;

public class ChatMessage extends ClientMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8258236598382931821L;
	
	private String username;
	private int connectId;
	private int type;
	private String message;
	
	public ChatMessage(int clientID, String username, int connectId, int type, String message) {
		super(clientID);
		this.username = username;
		this.connectId = connectId;
		this.type = type;
		this.message = message;
	}

	public int getConnectId() {
		return connectId;
	}
	
	public String getUsername() {
		return username;
	}

	public int getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
