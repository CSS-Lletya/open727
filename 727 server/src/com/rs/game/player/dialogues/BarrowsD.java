package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;

public class BarrowsD extends Dialogue {

	@Override
	public void start() {
		sendDialogue("You've found a hidden tunnel, do you want to enter?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, I'm fearless.", "No way, that looks scary!");
		} else if (stage == 0) {
			if (componentId == OPTION_1)
				player.setNextWorldTile(new WorldTile(3534, 9677, 0));
			end();
		}
	}

	@Override
	public void finish() {

	}

}
