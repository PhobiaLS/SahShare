package clientMessages;

import game.Move;

public class PlayerMove extends ClientMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4604221870825503502L;
	
	private Move playerMove;
	
	public PlayerMove(int clientID, Move playerMove) {
		super(clientID);
		this.playerMove = playerMove;
	}

	public Move getPlayerMove() {
		return playerMove;
	}

}
