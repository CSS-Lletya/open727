package com.rs.game.player.dialogues;

import com.rs.game.player.QuestManager.Quests;
import com.rs.game.player.controlers.NomadsRequiem;

public class NomadThrone extends Dialogue {

	private int npcId;
	private NomadsRequiem requiem;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		requiem = (NomadsRequiem) parameters[1];
		sendNPCDialogue(npcId, 9785,
				player.getQuestManager().getQuestStage(Quests.NOMADS_REQUIEM) != 2
						? "The assassin aproaches, but I wonder what compels you to seek death."
						: "The assassin returns? Whomever perserved your life must delight in your suffering!");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = (byte) (player.getQuestManager().getQuestStage(Quests.NOMADS_REQUIEM) != 2 ? 2 : 0);
			sendNPCDialogue(npcId, 9785,
					"Is it out of foolish loyalty to your master, or is it merely stupidity that brings you here to face me?");
			break;
		case 0:
			stage = 1;
			sendPlayerDialogue(9827, "You will not defeat me this time!");
			break;
		case 1:
			stage = -2;
			sendNPCDialogue(npcId, 9785, "We shall see.");
			break;
		case 2:
			stage = 3;
			sendPlayerDialogue(9827, "Actually yes. I really am quite stupid.");
			break;
		case 3:
			stage = 4;
			sendNPCDialogue(npcId, 9785, "Ha! Finally some words of truth.");
			break;
		case 4:
			stage = 5;
			sendNPCDialogue(npcId, 9785, "Come then, tell me why you are really here.");
			break;
		case 5:
			stage = -2;
			sendPlayerDialogue(9827, "Because I want a SEXY CAPE!");
			player.getQuestManager().setQuestStage(Quests.NOMADS_REQUIEM, 2);
			break;
		default:
			end();
			break;
		}
	}

	@Override
	public void finish() {
		requiem.continueThroneScene();
	}

}
