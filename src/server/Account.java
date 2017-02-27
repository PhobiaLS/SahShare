package server;

import java.io.Serializable;

public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4474478397914420920L;
	
	private int id;
	private String username;
	private int rating;
	private String avatar;
	
	public Account(int id, String username, int rating, String avatar) {
		this.id = id;
		this.username = username;
		this.rating = rating;
		this.avatar = avatar;
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

	public String getAvatar() {
		return avatar;
	}
	
	public void updateRating(int rating) {
		this.rating = rating;
	}
	
}
