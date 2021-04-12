package com.rs.game.player.dialogues;

public class EnchantedGemDialouge extends Dialogue {

	@Override
	public void start() {
		/*
		 * Master master = player.getSlayerMaster(); if (master == null) { player.setSlayerMaster(Master.SPRIA); } sendEntityDialogue( SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master.getMaster()).name, "Good day, How may I help you?" }, IS_NPC, master.getMaster(), 9827);
		 */
	}

	@Override
	public void run(int interfaceId, int componentId) {
		/*
		 * Master master = player.getSlayerMaster(); if (stage == -1) { stage = 0; sendEntityDialogue(SEND_4_OPTIONS, new String[] { SEND_DEFAULT_OPTIONS_TITLE, "How many monsters do I have left?", "Where are you located in the land of " + Settings.SERVER_NAME + "?", "Give me a tip.", "Nothing, Nevermind." }, IS_PLAYER, player.getIndex(), 9827); } else if (stage == 0) { if (componentId == 1) { SlayerTask task = player.getSlayerTask(); if (task != null) {
		 * 
		 * sendEntityDialogue( SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master .getMaster()).name, "You're current assigned to kill " + task.getName().toLowerCase() + " only " + task.getAmount() + " more to go." }, IS_NPC, master.getMaster(), 9827); } else { sendEntityDialogue( SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master .getMaster()).name, "You currently don't have a task, see me to get one." }, IS_NPC, master.getMaster(), 9827); } stage = -1; } else if (componentId == 2) { sendEntityDialogue( SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master .getMaster()).name, "" + master.getDialouge() + "." }, IS_NPC, master.getMaster(), 9827); stage = -1; } else if (componentId == 3) { sendEntityDialogue( SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master .getMaster()).name, "I currently dont have any tips for you now." }, IS_NPC, master.getMaster(), 9827); stage = -1; } else { end(); } }
		 */
	}

	@Override
	public void finish() {

	}

}
