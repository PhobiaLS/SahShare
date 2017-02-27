package appirience;

import javafx.scene.paint.Color;

public class ClassicTheme extends Theme {
	
	public ClassicTheme() {
		super("Classic theme");
		
		blackFieldColor = Color.web("rgb(222, 185, 119)");
		whiteFieldColor = Color.web("rgb(247, 206, 132)");
		boardBorderColor = Color.web("rgb(204, 102, 0)");
		possibleMovesColor = Color.web("rgb(153, 255, 51)");
	}
}
