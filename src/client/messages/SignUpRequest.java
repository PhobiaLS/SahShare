package client.messages;

public class SignUpRequest extends GuestMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2188582140889405977L;
	
	private String username;
	private String password;
	private String email;

	public SignUpRequest(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

}
