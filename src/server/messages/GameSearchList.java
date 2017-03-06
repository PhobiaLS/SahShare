package server.messages;

import java.util.ArrayList;

public class GameSearchList extends ServerMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1116150076387492862L;
	
	private ArrayList<PlayerSearchInformation> listOfSearchGames;
	
	public GameSearchList(ArrayList<PlayerSearchInformation> list) {
		this.listOfSearchGames = list;
	}
	
	public ArrayList<PlayerSearchInformation> getListOfSearchGames() {
		return listOfSearchGames;
	}

}
