package com.rs.game.dialogue;

public class DialogueEvent {
	
	public DialogueEvent(int type, String text){
		this.type = type;
		this.text = text;
	}

	private int type;
	
	private String text;
	
	private boolean removeContinue;
	
	public int getType(){
		return type;
	}
	
	public String getText(){
		return text;
	}
	
	public void removeContinueButton(){
		removeContinue = true;
	}
	
	public boolean isRemoveContinue(){
		return removeContinue;
	}
	
}
