package exceptions;

public class Checkmate extends Exception {

	private static final long serialVersionUID = -6506821198183794676L;

	public Checkmate() {
		super("Checkmate");
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	
	public String toString(){
		return getMessage();
	}
}
