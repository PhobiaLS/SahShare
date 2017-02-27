package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserConnection {
	private int id;
	private String username;
	private Socket socket;
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	
	public UserConnection(int id, String username, Socket socket, ObjectOutputStream toClient, ObjectInputStream fromClient) {
		this.id = id;
		this.username = username;
		this.socket = socket;
		this.toClient = toClient;
		this.fromClient = fromClient;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public ObjectOutputStream getToClient() {
		return toClient;
	}

	public ObjectInputStream getFromClient() {
		return fromClient;
	}
	
	@Override
	public String toString() {
		return "UserConnection: " + getId();
	}
}
