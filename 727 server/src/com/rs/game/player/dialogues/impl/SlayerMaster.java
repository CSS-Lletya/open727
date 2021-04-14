package com.rs.game.player.dialogues.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

import skills.slayer.Slayer;
import skills.slayer.Slayer.Master;
import skills.slayer.Slayer.SlayerTask;

public class SlayerMaster extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		Master master = player.getSlayerMaster();
		if (master == null)
			player.setSlayerMaster(Master.SPRIA);
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "Good day, How may I help you?" }, IS_NPC,
				npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		Master master = player.getSlayerMaster();
		if (npcId != master.getMaster())
			stage = 2;
		if (stage == -1) {
			if (player.getSlayerTask() != null) {
				sendEntityDialogue(SEND_4_OPTIONS,
						new String[] { SEND_DEFAULT_OPTIONS_TITLE, "How many monsters do I have left?",
								"What do you have in your shop?", "Give me a tip.", "Nothing, Nevermind." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 0;
			} else {
				sendEntityDialogue(SEND_4_OPTIONS,
						new String[] { SEND_DEFAULT_OPTIONS_TITLE, "Please give me a task.",
								"What do you have in your shop?", "Give me a tip.", "Nothing, Nevermind." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 1;
			}
		} else if (stage == 0 || stage == 1) {
			if (componentId == 1) {
				SlayerTask task = player.getSlayerTask();
				if (task != null && stage == 0) {
					sendEntityDialogue(SEND_1_TEXT_CHAT,
							new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
									"You're current assigned to kill " + task.getName().toLowerCase() + " only "
											+ task.getAmount() + " more to go." },
							IS_NPC, npcId, 9827);
				} else {
					Slayer.submitRandomTask(player);
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Your new task is to kill " + player.getSlayerTask().getName().toLowerCase() + "." },
							IS_NPC, npcId, 9827);
				}
				stage = -1;
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"I have multiple items for sale." }, IS_NPC, npcId, 9827);
				ShopsHandler.openShop(player, 29);
				stage = -1;
			} else if (componentId == 3) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"I currently dont have any tips for you now." }, IS_NPC, npcId, 9827);
				stage = -1;
			} else {
				end();
			}
		} else if (stage == 2) {
			sendEntityDialogue(SEND_3_OPTIONS,
					new String[] { SEND_DEFAULT_OPTIONS_TITLE, "Can you become my master?",
							"What do you have in your shop?", "Nothing, Nevermind." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
			if (stage == 3) {
				if (componentId == 1) {
					if (player.getSlayerTask() != null) {
						sendEntityDialogue(SEND_1_TEXT_CHAT,
								new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
										"I cannot teach you until your slayer task is complete. Come back later." },
								IS_NPC, npcId, 9827);
						return;
					}
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"You are now under my wings." }, IS_NPC, npcId, 9827);
					player.setSlayerMaster(Master.forId(npcId));
				} else if (componentId == 2) {
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"I have multiple items for sale." }, IS_NPC, npcId, 9827);
					ShopsHandler.openShop(player, 29);
				} else {
					end();
				}
			}
		}
	}

	@Override
	public void finish() {

	}

}
