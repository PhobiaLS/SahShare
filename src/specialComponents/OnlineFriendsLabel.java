package specialComponents;

import javafx.scene.control.Label;
import server.messages.FriendInformation;

public class OnlineFriendsLabel extends Label {
	private FriendInformation friend;
	
	public OnlineFriendsLabel(FriendInformation friend) {
		this.friend = friend;
		setText(friend.getUsername());
	}
	
	public FriendInformation getFriend() {
		return friend;
	}
}
