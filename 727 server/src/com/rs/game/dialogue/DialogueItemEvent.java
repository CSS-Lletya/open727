package com.rs.game.dialogue;

public class DialogueItemEvent extends DialogueEvent {
	
	
	public DialogueItemEvent(int itemId, int amount, String text){
		super((byte) 2, text);
		this.itemId = itemId;
		this.amount = amount;
	}
	
	private int itemId, amount;

	public int getItemId() {
		return itemId;
	}

	public int getAmount() {
		return amount;
	}

}
