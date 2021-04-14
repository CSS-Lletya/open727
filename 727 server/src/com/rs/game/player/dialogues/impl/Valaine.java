package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class Valaine extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello there. Want to have a look at what we're selling", "today?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, please.", "How should I use your shop?",
					"No, thank you.");
			break;
		case 0:
			switch (componentId) {
			case 2:
				ShopsHandler.openShop(player, 17);
				end();
				break;
			case 3:
				stage = 1;
				sendNPCDialogue(npcId, 9827, "I'm glad you ask! You can buy as many of the items",
						"stocked as you wish. The price of these items changes", "based on the amount in stock.");
				break;
			case 4:
			default:
				end();
				break;
			}
			break;
		case 1:
			stage = -2;
			sendNPCDialogue(npcId, 9827,
					"You can also sell most items to the shop and the price given will be based on the amount in stock.");
			break;
		default:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
