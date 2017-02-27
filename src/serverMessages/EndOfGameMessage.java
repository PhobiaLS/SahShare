package serverMessages;

public class EndOfGameMessage extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4237176028980654733L;
	
	private String message;
	
	public EndOfGameMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
