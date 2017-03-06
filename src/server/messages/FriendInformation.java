package server.messages;

public class FriendInformation extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1980497541098638189L;
	
	private int id;
	private String username;
	private int rating;
	
	public FriendInformation(int id, String username, int rating) {
		this.id = id;
		this.username = username;
		this.rating = rating;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getRating() {
		return rating;
	}
}
