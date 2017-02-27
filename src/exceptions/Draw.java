package exceptions;

public class Draw extends Exception {

	private static final long serialVersionUID = 956047937355853913L;

	public Draw() {
		super("Draw");
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
