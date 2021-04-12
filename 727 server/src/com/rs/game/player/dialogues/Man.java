package com.rs.game.player.dialogues;

import com.rs.utils.Utils;

public class Man extends Dialogue {

	int npcId;

	private static final String[] SALUTATIONS = { "Hello there.", "Salutations!", "Nice to meet you.", "Howdy.",
			"Good day.", "Greetings." };

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendPlayerDialogue(9827, SALUTATIONS[Utils.random(SALUTATIONS.length)]);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = -2;
			switch (Utils.random(10)) {
			case 0:
				sendNPCDialogue(npcId, 9827, "I can't belive that Lachtopher boy. He tried to borrow",
						"money from me again.");
				break;
			case 1:
				sendNPCDialogue(npcId, 9827, "Hello, I'm glad to see an adventurar about. There's an",
						"increase in goblins hanging arround the area.");
				break;
			case 2:
				sendNPCDialogue(npcId, 9827, "Don't come near me, I have a cold!");
				break;
			case 3:
				sendNPCDialogue(npcId, 9827, "Another adventurer, off to save the world, eh?");
				break;
			case 4:
				sendNPCDialogue(npcId, 9827, "I have to go all the way through the swamp to go mining.",
						"My poor feet!");
				break;
			case 5:
				sendNPCDialogue(npcId, 9827, "Hey, do you like my clothes? They're new.");
				break;
			case 6:
				sendNPCDialogue(npcId, 9827, "Sorry, I don't speak to strangers. They're weird.");
				break;
			case 7:
				sendNPCDialogue(npcId, 9827, "Don't ask me for directions, I'm just a tourist here.");
				break;
			case 8:
				sendNPCDialogue(npcId, 9827, "I wish people would stop ringing the church bell. I can't tell",
						"what time it is.");
				break;
			case 9:
				sendNPCDialogue(npcId, 9827, "Hello to you too, adventurer.");
				break;
			}
			break;
		default:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
