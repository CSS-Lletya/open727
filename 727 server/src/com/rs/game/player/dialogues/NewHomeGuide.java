package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.controlers.NewHomeControler;

public class NewHomeGuide extends Dialogue {

	private NewHomeControler controler;
	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		controler = (NewHomeControler) parameters[1];
		int s = controler.getStage();
		if (s == 0) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Greetings! I see you are a new arrival in this land. My",
							"job is welcome all new visitors. So Welcome!" },
					IS_NPC, npcId, 9827);
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Anyways, you might be wondering, who made this server.",
							"The devlopers are currently, Dragonkk, Cjay0091, Sonicforce41, and Cjay0091" },
					IS_NPC, npcId, 9827);
		}
	}

	@Override
	public void finish() {

	}

}
