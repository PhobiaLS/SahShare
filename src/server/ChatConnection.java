package server;

public class ChatConnection {
	private int playerId1;
	private int playerId2;
	
	public ChatConnection(int playerId1, int playerId2) {
		this.playerId1 = playerId1;
		this.playerId2 = playerId2;
	}

	public int getPlayerId1() {
		return playerId1;
	}

	public int getPlayerId2() {
		return playerId2;
	}
}
