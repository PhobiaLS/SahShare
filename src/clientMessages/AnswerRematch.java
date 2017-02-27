package clientMessages;

import clientMessages.ClientMessage;

public class AnswerRematch extends ClientMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8989657710299289708L;

	private boolean answer;
	
	public AnswerRematch(int clientID, boolean answer) {
		super(clientID);
		this.answer = answer;
	}

	public boolean isAnswer() {
		return answer;
	}
}
