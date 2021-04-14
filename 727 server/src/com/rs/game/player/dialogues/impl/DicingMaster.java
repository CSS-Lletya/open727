package com.rs.game.player.dialogues.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;

public class DicingMaster extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
				"Hello, would you like to do a dice game?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Would you like to play a dice game?", "Yes", "No");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {

				sendPlayerDialogue(9827, "I would like to play a dice game.");
				stage = 2;
			} else {
				sendPlayerDialogue(9827, "No, thank you.");
				stage = 3;
			}
		} else if (stage == 2) {
			// TODO
		} else if (stage == 3) {
			if (componentId == 1) {
				end();
			}
		}
	}

	@Override
	public void finish() {

	}
}
