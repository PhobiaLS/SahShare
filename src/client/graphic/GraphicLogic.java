package client.graphic;

import client.graphic.appearance.ClassicTheme;
import client.graphic.appearance.Theme;
import client.graphic.linguistics.EnglishLanguage;
import client.graphic.linguistics.Language;

public class GraphicLogic {
	private Language language;
	private Theme theme;
	
	public GraphicLogic() {
		language = new EnglishLanguage();
		theme = new ClassicTheme();
	}

	public Language getLanguage() {
		return language;
	}
	
	public Theme getTheme() {
		return theme;
	}
	
	public void setLanguage(Language language) {
		this.language = language;
	}
}
