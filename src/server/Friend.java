package server;

import java.io.Serializable;

public class Friend implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1063978973158341097L;
	private int id;
	private String username;
	
	public Friend(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	
}
