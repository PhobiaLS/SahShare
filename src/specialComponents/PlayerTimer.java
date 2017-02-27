package specialComponents;

import exceptions.TimesUp;

public class PlayerTimer {
	private int time;
	private int player;
	private int timerBeginingTime;
	
	public PlayerTimer(int time, int player) {
		this.time = time * 60;
		this.player = player;
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
	
	public int getPlayer() {
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