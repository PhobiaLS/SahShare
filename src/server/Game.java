package server;

public class Game {
	private UserConnection user1;
	private UserConnection user2;
	
	public Game(UserConnection user1, UserConnection user2) {
		this.user1 = user1;
		this.user2 = user2;
	}
	
	public UserConnection getUser1() {
		return user1;
	}
	
	public UserConnection getUser2() {
		return user2;
	}
	
	public void setUser1(UserConnection user1) {
		this.user1 = user1;
	}
	
	public void setUser2(UserConnection user2) {
		this.user2 = user2;
	}
	
	@Override
	public String toString() {
		return "Game: " + user1.toString() + "\n" + user2.toString();
	}
}
