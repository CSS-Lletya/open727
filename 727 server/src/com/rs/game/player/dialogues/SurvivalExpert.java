package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.controlers.TutorialIsland;

public class SurvivalExpert extends Dialogue {

	int npcId;
	TutorialIsland controler;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		controler = (TutorialIsland) parameters[1];
		int s = controler.getStage();
		if (s == 4) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Hello there, newcomer. My name is Brynna. My job is",
							"to teach you a few survival tips and tricks. First off",
							"we're going to start with the most basic survival skill of", "alls making a fire." },
					IS_NPC, npcId, 9827);
		} else if (s == 11) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Hello again. I'm here to teach you a few survival tips",
							"and tricks. First off we're going to start with the most",
							"basic survival skill of all: making a fire." },
					IS_NPC, npcId, 9827);
			stage = 1;
		} else
			end();
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { "", "The Survival Guide gives you a tinderbox and a bronze", "axe!" }, IS_ITEM, 590,
					1);
		} else if (stage == 0) {
			player.getInventory().addItem(1351, 1);
			player.getInventory().addItem(590, 1);
			controler.updateProgress();
			end();
		} else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Well done! Next we need to get some food in our",
							"bellies. We'll need something to cook. There are shrimp",
							"in the pond there so lets catch and cook some." },
					IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { "", "The Survival Guide gives you a net!" }, IS_ITEM,
					303, 1);
			player.getInventory().addItem(303, 1);
			controler.updateProgress();
		} else {
			end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
