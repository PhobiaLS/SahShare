package bot;

import java.util.List;
import exceptions.Checkmate;
import exceptions.Draw;
import exceptions.Promotion;
import game.ChessPiecesEnum;
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
				ChessPiecesEnum piece = ChessPiecesEnum.PAWN;
				switch (rand) {
				case 0:
					piece = ChessPiecesEnum.BISHOP;
					break;
				case 1:
					piece = ChessPiecesEnum.KING;
					break;
				case 2:
					piece = ChessPiecesEnum.QUEEN;
					break;
				case 3:
					piece = ChessPiecesEnum.ROOK;
					break;
				}
				engine.zamena(move.getX(), move.getY(), piece);
			}
		} catch (Draw e1) {
			throw new Draw();
		} catch (Checkmate e1) {
			throw new Checkmate();
		} 		
		
	}
	
}
