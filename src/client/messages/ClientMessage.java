package client.messages;

import game.GameConstants;

import java.io.Serializable;

public class ClientMessage implements Serializable, GameConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4404802185294150865L;
	
	private int clientID;
	
	public ClientMessage(int clientID) {
		this.clientID = clientID;
	}

	public int getClientID() {
		return clientID;
	}
	
	

}
