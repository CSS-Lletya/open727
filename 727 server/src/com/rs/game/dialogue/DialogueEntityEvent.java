package com.rs.game.dialogue;

public class DialogueEntityEvent extends DialogueEvent {
	
	
	public DialogueEntityEvent(boolean player, int face, String text){
		super((byte) 1, text);
		this.player = player;
		this.face = face;
	}
	
	private boolean player;
	
	private int face;

	public boolean entityPlayer() {
		return player;
	}

	public int getFace() {
		return face;
	}

}
