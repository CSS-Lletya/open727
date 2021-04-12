package com.rs.game.player.dialogues;

public class MasterOfFear extends Dialogue {

	@Override
	public void start() {
		stage = Byte.parseByte(parameters[0].toString());
		if (stage == 3) {
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Donator cape", "Basket of eggs", "Sled");
		} else {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { "Master of fear",
							"Hello, " + player.getDisplayName() + ", welcome to the Refuge of Fear grotto.", },
					IS_NPC, 15149, 9843);
		}
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "How do I play?", "Can I see your reward stock?");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 11) {
				sendPlayerDialogue(9827, "How do I play Refuge of Fear?");
			} else if (componentId == 13) {
				sendPlayerDialogue(9827, "Can I see your reward stock?");
				stage = 3;
			} else {
				end();
			}
		} else if (stage == 3) {
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Donator cape", "Basket of eggs", "Sled");
			stage = 4;
		} else if (stage == 4) {
			if (componentId == 11) {
				if (player.getInventory().getFreeSlots() > 0) {
					player.getInventory().addItem(4401, 1);
					end();
				} else
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You don't have enoughe space in your inventory to store that item.");
			} else if (componentId == 13) {
				if (player.getInventory().getFreeSlots() > 0) {
					player.getInventory().addItem(4565, 1);
					end();
				} else
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You don't have enoughe space in your inventory to store that item.");
			} else if (componentId == 14) {
				if (player.getInventory().getFreeSlots() > 0) {
					player.getInventory().addItem(4084, 1);
					end();
				} else
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You don't have enoughe space in your inventory to store that item.");
			} else {
				end();
			}
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
