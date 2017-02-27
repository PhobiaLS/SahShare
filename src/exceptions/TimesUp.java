package exceptions;

public class TimesUp extends Exception {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5111439512766134499L;
	private int player;
	
	public TimesUp(int player) {
		super("Times up!");
		this.player = player;
	}

	public int getPlayer() {
		return player;
	}
	
	@Override
	public String toString() {
		return "Izgubio je igrac: " + player;
	}
}
