package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.controlers.TutorialIsland;

public class RuneScapeGuide extends Dialogue {

	int npcId;
	TutorialIsland controler;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		controler = (TutorialIsland) parameters[1];
		int s = controler.getStage();
		if (s == 0) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Greetings! I see you are a new arrival in this land. My",
							"job is welcome all new visitors. So Welcome!" },
					IS_NPC, npcId, 9827);
		} else if (s == 2) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "I'm glad you're making progress!" },
					IS_NPC, npcId, 9827);
			stage = 5;
		} else
			end();
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"You have already learned the first thing needed to",
							"succeed in this world talking to other people!" },
					IS_NPC, npcId, 9827);
		} else if (stage == 0) {
			stage = 1;
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"You will find many inhabitants of this world have useful",
							"things to say to you. By clicking on them with your", "mouse you can talk to them." },
					IS_NPC, npcId, 9827);
		} else if (stage == 1) {
			stage = 2;
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"I would also suggest reading through some of the",
							"supporting information on the website. There you can",
							"find the Knowledge Base, which contains all the.",
							"additional information you're ever likely to need. It also" },
					IS_NPC, npcId, 9827);
		} else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"contains maps and helpfull tips to help you on your", "journey." }, IS_NPC, npcId, 9827);
		} else if (stage == 3) {
			stage = 4;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"You will notice a flashing icon of a spanner; please click", "on this to continue the tutorial." },
					IS_NPC, npcId, 9827);
			controler.updateProgress();
		} else if (stage == 5) {
			stage = 6;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"To continue the tutorial go through that door over", "there and speak to your first instructor!" },
					IS_NPC, npcId, 9827);
		} else if (stage == 6) {
			controler.updateProgress();
			end();
		} else
			end();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
