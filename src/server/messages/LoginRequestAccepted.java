package server.messages;

import server.Account;

public class LoginRequestAccepted extends ServerMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8215506097002226072L;
	
	private Account account;
	
	public LoginRequestAccepted(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}
	
}
