package clientMessages;

public class GameAccept extends ClientMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7143735776695510992L;
	
	private int oponentId;
	
	public GameAccept(int clientID, int oponentId) {
		super(clientID);
		this.oponentId = oponentId;
	}

	public int getOponentId() {
		return oponentId;
	}
	
	@Override
	public String toString() {
		
		return getClientID() + "\n" +  getOponentId();
	}
}
