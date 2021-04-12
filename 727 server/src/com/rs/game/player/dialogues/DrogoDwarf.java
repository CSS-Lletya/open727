package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utils.ShopsHandler;

public class DrogoDwarf extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
				"'Ello. Welcome to my Mining shop, friend." }, IS_NPC, npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Do you want to trade?", "Hello, shorty.",
					"Why don't you ever restock ores and bars?");
		} else if (stage == 0) {
			if (componentId == 2) {
				stage = 2;
				sendPlayerDialogue(9827, "Do you want to trade?");
			} else if (componentId == 3) {
				stage = 3;
				sendPlayerDialogue(9827, "Hello, shorty.");
			} else {
				stage = 4;
				sendPlayerDialogue(9827, "Why don't you ever restock ores and bars?");
			}
		} else if (stage == 2) {
			ShopsHandler.openShop(player, 30);
			end();
		} else if (stage == 3) {
			stage = -2;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"I may be short but at least I've got manners." }, IS_NPC, npcId, 9827);
		} else if (stage == 4) {
			stage = -2;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"The only ores and bars I sell are those sold to me." }, IS_NPC, npcId, 9827);

		} else
			end();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
