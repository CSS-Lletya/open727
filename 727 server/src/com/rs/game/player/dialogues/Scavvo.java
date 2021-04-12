package com.rs.game.player.dialogues;

import com.rs.utils.ShopsHandler;

public class Scavvo extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "'Ello, matey! D'ya wanna buy some exciting new toys?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "No - toys are for kids.", "Let's have a look, then.",
					"Ohh, goody-goody - toys!", "Why do you sell most rune armour but not platebodies?");
			break;
		case 0:
			switch (componentId) {
			case 1:
				stage = -2;
				sendPlayerDialogue(9827, "No - toys are for kids.");
				break;
			case 2:
				ShopsHandler.openShop(player, 12);
				end();
				break;
			case 3:
				stage = 1;
				sendPlayerDialogue(9827, "Ohh, goody-goody - toys!");
				break;
			case 4:
				stage = 2;
				sendPlayerDialogue(9827, "Why do you sell most rune armour but not platebodies?");
				break;
			default:
				end();
				break;
			}
			break;
		case 1:
			ShopsHandler.openShop(player, 12);
			end();
			break;
		case 2:
			stage = -2;
			sendNPCDialogue(npcId, 9827, "Oh, you have to complete a special quest in order to wear",
					"rune platebodies. You should talk to the guild master", "downstairs about that.");
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
