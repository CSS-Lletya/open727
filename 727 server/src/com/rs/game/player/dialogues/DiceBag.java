package com.rs.game.player.dialogues;

public class DiceBag extends Dialogue {

	private int oldDice;

	@Override
	public void start() {
		oldDice = (Integer) parameters[0];
		sendOptionsDialogue("What would you like to roll?", "One 6-sided die", "Two 6-sided dice", "One 4-sided die",
				"One 8-sided die", "More...");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			if (componentId == OPTION_1) {
				chooseDice(15086);
			} else if (componentId == OPTION_2) {
				chooseDice(15088);
			} else if (componentId == OPTION_3) {
				chooseDice(15088);
			} else if (componentId == OPTION_4) {
				chooseDice(15090);
			} else {
				sendOptionsDialogue("What would you like to roll?", "One 10-sided die", "Two 12-sided dice",
						"One 20-sided die", "Two 10-sided dice for 1-100", "Back...");
				stage = 1;
			}
			break;
		case 1:
			if (componentId == OPTION_1) {
				chooseDice(15092);
			} else if (componentId == OPTION_2) {
				chooseDice(15094);
			} else if (componentId == OPTION_3) {
				chooseDice(15096);
			} else if (componentId == OPTION_4) {
				chooseDice(15098);
			} else {
				sendOptionsDialogue("What would you like to roll?", "One 6-sided die", "Two 6-sided dice",
						"One 4-sided die", "One 8-sided die", "More...");
				stage = -1;
			}
			break;
		}
	}

	private void chooseDice(int newDice) {
		if (oldDice != -1)
			player.getInventory().deleteItem(oldDice, 1);
		player.getInventory().addItem(newDice, 1);
		end();
	}

	@Override
	public void finish() {

	}
}
