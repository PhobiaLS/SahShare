package server.messages;

import java.io.Serializable;

public class PlayerSearchEnded extends ServerMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8230486237280082830L;
	
	int playerId;
	
	public PlayerSearchEnded(int playerId) {
		this.playerId = playerId;
	}
	
	public int getPlayerId() {
		return playerId;
	}
}
