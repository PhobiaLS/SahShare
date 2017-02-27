package server;

import clientMessages.AnswerRematch;

public class RematchConformation {
	private int userId1;
	private int userId2;
	
	private AnswerRematch answer1;
	private AnswerRematch answer2;
	
	public RematchConformation(int userId1, int userId2) {
		this.userId1 = userId1;
		this.userId2 = userId2;
	}
	
	public int getUserId1() {
		return userId1;
	}
	
	public int getUserId2() {
		return userId2;
	}
	
	public AnswerRematch getAnswer1() {
		return answer1;
	}
	
	public AnswerRematch getAnswer2() {
		return answer2;
	}
	
	public void setAnswer1(AnswerRematch answer1) {
		this.answer1 = answer1;
	}
	
	public void setAnswer2(AnswerRematch answer2) {
		this.answer2 = answer2;
	}
}
