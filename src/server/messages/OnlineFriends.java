package server.messages;

import java.util.ArrayList;

public class OnlineFriends extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8734846158382335878L;
	
	private ArrayList<FriendInformation> friends;
	
	public OnlineFriends(ArrayList<FriendInformation> friends) {
		this.friends = friends;
	}

	public ArrayList<FriendInformation> getFriends() {
		return friends;
	}

}
