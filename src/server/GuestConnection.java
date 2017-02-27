package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GuestConnection {
	private Socket socket;
	private ObjectOutputStream toGuest;
	private ObjectInputStream fromGuest;
	
	public GuestConnection(Socket socket, ObjectOutputStream toGuest, ObjectInputStream fromGuest) {
		this.socket = socket;
		this.toGuest = toGuest;
		this.fromGuest = fromGuest;
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectOutputStream getToGuest() {
		return toGuest;
	}

	public ObjectInputStream getFromGuest() {
		return fromGuest;
	}
	
}
