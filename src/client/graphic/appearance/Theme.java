package client.graphic.appearance;

import javafx.scene.paint.Color;

public abstract class Theme {
	protected String themeName;
	protected Color blackFieldColor;
	protected Color whiteFieldColor;
	protected Color boardBorderColor;
	protected Color possibleMovesColor;
	
	public Theme(String themeName) {
		this.themeName = themeName;
	}

	public String getThemeName() {
		return themeName;
	}

	public Color getBlackFieldColor() {
		return blackFieldColor;
	}

	public Color getWhiteFieldColor() {
		return whiteFieldColor;
	}

	public Color getBoardBorderColor() {
		return boardBorderColor;
	}

	public Color getPossibleMovesColor() {
		return possibleMovesColor;
	}
	
}
