package clientMessages;

import game.GameConstants;

public class GameEnd extends ClientMessage implements GameConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6119324886731357493L;
	private int causeOfEnd;
	public GameEnd(int clientID, int causeOfEnd) {
		super(clientID);
		this.causeOfEnd = causeOfEnd;
	}
	
	public int getCauseOfEnd() {
		return causeOfEnd;
	}

}
