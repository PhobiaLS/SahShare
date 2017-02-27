package game;

import appirience.ClassicTheme;
import appirience.Theme;
import linguistics.EnglishLanguage;
import linguistics.Language;

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
