package serverMessages;

import server.Friend;

public class FriendLogin extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6422792307520609626L;
	
	private Friend friend;
	
	public FriendLogin(Friend friend) {
		this.friend = friend;
	}

	public Friend getFriend() {
		return friend;
	}

}
