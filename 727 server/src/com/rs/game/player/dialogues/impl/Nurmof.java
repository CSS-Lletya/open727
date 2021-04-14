package com.rs.game.player.dialogues.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class Nurmof extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
				"Greetings and welcome to my pickaxe shop. Do you want", "to buy my premium quality pickaxes?" },
				IS_NPC, npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, please.", "No, thank you.",
					"Are your pickaxes better than other pickaxes, then?");
		} else if (stage == 0) {
			if (componentId == 2) {
				ShopsHandler.openShop(player, 32);
				end();
			} else if (componentId == 3) {
				stage = -2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "No, thank you." },
						IS_PLAYER, player.getIndex(), 9827);
			} else {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Are your pickaxes better than other pickaxes, then?" },
						IS_PLAYER, player.getIndex(), 9827);
			}
		} else if (stage == 1) {
			stage = -2;
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Of course they are! My pickaxes are made of higher grade",
							"metal than your ordinary bronze pickaxes, allowing you to",
							"mine ore just that little bit faster." },
					IS_NPC, npcId, 9827);
		} else
			end();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
