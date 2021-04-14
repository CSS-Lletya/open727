package com.rs.game.player.dialogues.impl;

import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.player.dialogues.Dialogue;

public class ForfeitDialouge extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Forfeit Duel?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
		case OPTION_1:
			if (!player.getLastDuelRules().getRule(7)) {
				((DuelArena) player.getControlerManager().getControler()).endDuel(player,
						player.getLastDuelRules().getTarget());
			} else {
				sendDialogue("You can't forfeit during this duel.");
			}
			break;
		}
		end();
	}

	@Override
	public void finish() {

	}

}
