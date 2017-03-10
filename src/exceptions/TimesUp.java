package exceptions;

import game.Teams;

public class TimesUp extends Exception {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5111439512766134499L;
	private Teams player;
	
	public TimesUp(Teams player2) {
		super("Times up!");
		this.player = player2;
	}

	public Teams getPlayer() {
		return player;
	}
	
	@Override
	public String toString() {
		return "Izgubio je igrac: " + player;
	}
}
