package com.rs.game.dialogue;

public class DialogueEvent {
	
	public DialogueEvent(byte type, String text){
		this.type = type;
		this.text = text;
	}

	private byte type;
	
	private String text;
	
	private boolean removeContinue;
	
	public byte getType(){
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
