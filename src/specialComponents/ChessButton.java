package specialComponents;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.graphic.Graphics;
import game.*;

public class ChessButton extends Button {
    Image image;
	public Engine game;
	private int i, j;
	
	public ChessButton(int i, int j, Engine game) {
		this.game = game;
		this.i = i;
		this.j = j;
	}
	
	public void paintComponent(Graphics g) {
		image = game.getFigureImage(i, j);
		setGraphic(new ImageView(image));
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
}
