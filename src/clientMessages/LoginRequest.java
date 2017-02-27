package clientMessages;

public class LoginRequest extends GuestMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5169802647953397087L;

	private String username;
	private String password;
	
	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
