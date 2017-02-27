package exceptions;

public class Promotion extends Exception {
	
	private static final long serialVersionUID = -4531944923351693105L;

	public Promotion() {
		super("Promotion");
	}
	
	public String getMessage() {
		return super.toString();
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
