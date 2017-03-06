package server.messages;

public class SignUpRequestDenied extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4861722896621272342L;
	
	private String message;
	
	public SignUpRequestDenied(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
