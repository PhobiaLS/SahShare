package serverMessages;

import java.util.ArrayList;

import server.WaitingPlayer;

public class WaitingPlayersMessage extends ServerMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6421673605859799966L;
	
	private ArrayList<WaitingPlayer> waitingPlayers;
	
	public WaitingPlayersMessage(ArrayList<WaitingPlayer> waitingPlayers) {
		this.waitingPlayers = waitingPlayers;
	}

	public ArrayList<WaitingPlayer> getWaitingPlayers() {
		return waitingPlayers;
	}
	
	
}
