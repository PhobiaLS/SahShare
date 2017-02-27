package specialComponents;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FigureChoseButton extends Button {
	
	public FigureChoseButton(int team, String figure) {
		String imgString;
		if (team == 1) {
			if (figure.equals("Bishop"))
				imgString = "images/figure-icons/bishop-icon-white.png";
			else if (figure.equals("Knight"))
				imgString = "images/figure-icons/knight-icon-white.png";
			else if (figure.equals("Queen"))
				imgString = "images/figure-icons/queen-icon-white.png";
			else
				imgString = "images/figure-icons/rook-icon-white.png";
		}
		else {
			if (figure.equals("Bishop"))
				imgString = "images/figure-icons/bishop-icon-black.png";
			else if (figure.equals("Knight"))
				imgString = "images/figure-icons/knight-icon-black.png";
			else if (figure.equals("Queen"))
				imgString = "images/figure-icons/queen-icon-black.png";
			else
				imgString = "images/figure-icons/rook-icon-black.png";
		}
		
		Image image = new Image(imgString);
		setGraphic(new ImageView(image));
	}
}
