package com.rs.game.player.dialogues;

public class RuneSpanLeaving extends Dialogue {

	@Override
	public void start() {
		sendDialogue("All your runes will be converted into points when you leave.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			sendOptionsDialogue("Teleport to the Wizards' Tower?", "Yes", "No");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 11) {
				player.getControlerManager().forceStop();
			}
			end();
		}
	}

	@Override
	public void finish() {

	}
}
