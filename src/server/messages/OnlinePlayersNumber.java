package server.messages;

public class OnlinePlayersNumber extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8090147231724399896L;
	
	private int number;
	
	public OnlinePlayersNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}

}
