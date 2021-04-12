package com.rs.game.player.dialogues;

import com.rs.game.player.controlers.TutorialIsland;

public class MasterChef extends Dialogue {

	int npcId;
	TutorialIsland controler;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		controler = (TutorialIsland) parameters[1];
		int s = controler.getStage();
		if (s == 20)
			sendNPCDialogue(npcId, 9827,
					"Ah! Welcome, newcomer. I am the Master Chef, Lew. It is here I will teach you how to cook food truly fit for a king.");
		else
			end();
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendPlayerDialogue(9827, "I already know how to cook. Brynna taught me just now.");
		} else if (stage == 0) {
			stage = 1;
			sendNPCDialogue(npcId, 9827,
					"Hahahahahaha! You call THAT cooking? Some shrimp on an open log fire? Oh, no, no, no. I am going to teach you the fine art of cocking bread.");
		} else if (stage == 1) {
			stage = 2;
			sendNPCDialogue(npcId, 9827,
					"And no fine meal is complete without good music, so will cover that while you're here too.");
		} else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { "", "The Cooking Guide gives you a bucket of water", "and a pot of flour!" },
					IS_ITEM, 1929, 1);
			player.getInventory().addItem(1929, 1);
			player.getInventory().addItem(1933, 1);
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
