package server.messages;

import server.Friend;

public class FriendLogout extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2474753277022501654L;
	
	private Friend friend;
	
	public FriendLogout(Friend friend) {
		this.friend = friend;
	}

	public Friend getFriend() {
		return friend;
	}

}
