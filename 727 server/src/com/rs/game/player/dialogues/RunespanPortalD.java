package com.rs.game.player.dialogues;

import com.rs.game.player.controlers.RunespanControler;

public class RunespanPortalD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Where would you like to travel to?", "The Runecrafting Guild",
				"Low level entrance into the Runespan", "High level entrance into the Runespan");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				player.getPackets().sendGameMessage("That option isn't yet working.", true);
				end();
			} else {
				RunespanControler.enterRunespan(player, componentId == OPTION_3);
				end();
			}
		}

	}

	@Override
	public void finish() {

	}

}
