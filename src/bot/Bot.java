package bot;

import java.util.List;
import exceptions.Checkmate;
import exceptions.Draw;
import exceptions.Promotion;
import game.Engine;
import game.GameConstants;
import geometry.Point;

public class Bot {
	
	Engine engine;
	
	public Bot(Engine engine){
		this.engine = engine;
	}
	
	//Daje random broj
	private int randomize(int max){
		return ((int) Math.floor(Math.random()*max));
	}
	
	//Odigra potez//
	public void play() throws Draw, Checkmate {
		Point move = null;
		List<Point> moveableFigures;
		Point figura = null;
		try {
			moveableFigures = engine.getMovableFigures();//Uzima neku figuru koja moze da se pomera
			figura = moveableFigures.get(randomize(moveableFigures.size()));
			List<Point> possibleMoves = engine.getPossibleMoves(figura.getX(), figura.getY());
			//Uzima neku od mogucih pozicija
			move = possibleMoves.get(randomize(possibleMoves.size()));
			try {
				engine.playMove(move);
			} catch (Promotion e) {
				int rand = randomize(4);
				switch (rand) {
				case 0:
					rand = GameConstants.FIGURE_BISHOP;
					break;
				case 1:
					rand = GameConstants.FIGURE_KING;
					break;
				case 2:
					rand = GameConstants.FIGURE_QUEEN;
					break;
				case 3:
					rand = GameConstants.FIGURE_ROOK;
					break;
				}
				engine.zamena(move.getX(), move.getY(), rand);
			}
		} catch (Draw e1) {
			throw new Draw();
		} catch (Checkmate e1) {
			throw new Checkmate();
		} 		
		
	}
	
}
