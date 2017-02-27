package specialComponents;

import serverMessages.PlayerSearchInformation;
import javafx.scene.control.Label;

public class PlayerSearchingLabel extends Label {
	private PlayerSearchInformation player;
	
	public PlayerSearchingLabel(PlayerSearchInformation player) {
		this.player = player;
		setText(player.getUsername() + "\t" + player.getRating() + " \t" + player.getGameTime());
	}
	
	public PlayerSearchInformation getPlayer() {
		return player;
	}
	
}
