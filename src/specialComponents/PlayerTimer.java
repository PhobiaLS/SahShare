package specialComponents;

import exceptions.TimesUp;
import game.Teams;

public class PlayerTimer {
	private int time;
	private Teams player;
	private int timerBeginingTime;
	
	public PlayerTimer(int time, Teams whitePlayer) {
		this.time = time * 60;
		this.player = whitePlayer;
		this.timerBeginingTime = time;
	}
	
	public void reduce() throws TimesUp {
		time--;
		if (time <= 0)
			throw new TimesUp(player);
	}
	
	public void setTime(int time) {
		this.time = time * 60;
	}
	
	public Teams getPlayer() {
		return player;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getTimerBeginingTime() {
		return timerBeginingTime;
	}

	@Override
	public String toString() {
		int min = time / 60;
		int sec = time % 60;
		if (sec < 10)
			return "" + min + ":0" + sec;
		return "" + min + ":" + sec;
	}
}