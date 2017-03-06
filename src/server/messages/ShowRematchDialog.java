package server.messages;

public class ShowRematchDialog extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1785693226440297532L;
	private int causeOfEnd;
	
	public ShowRematchDialog(int causeOfEnd) {
		this.causeOfEnd = causeOfEnd;
	}
	
	public int getCauseOfEnd() {
		return causeOfEnd;
	}
}
