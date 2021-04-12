package com.rs.game.player.dialogues;

import com.rs.game.player.Skills;
import com.rs.game.player.content.AncientEffigies;
import com.rs.utils.Utils;

/**
 * Ancient effifies dialogue handling.
 * 
 * @author Raghav/Own4g3 <Raghav_ftw@hotmail.com>
 * 
 */
public class AncientEffigiesD extends Dialogue {

	int itemId;
	int skill1; // this might needs to be saved
	int skill2;

	@Override
	public void start() {
		itemId = (Integer) parameters[0];
		sendDialogue(new String[] { "As you inspect the ancient effigy, you begin to feel a",
				"strange sensation of the relic searching your mind,", "drawing on your knowledge." });
		int random = Utils.getRandom(7);
		skill1 = AncientEffigies.SKILL_1[random];
		skill2 = AncientEffigies.SKILL_2[random];
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(new String[] { "Images from your experiences of " + AncientEffigies.getMessage(skill1),
					"fill your mind." });
			stage = 0;
		} else if (stage == 0) {
			player.getTemporaryAttributtes().put("skill1", skill1);
			player.getTemporaryAttributtes().put("skill2", skill2);
			sendOptionsDialogue("Which images do you wish to focus on?", Skills.SKILL_NAME[skill1],
					Skills.SKILL_NAME[skill2]);
			stage = 1;
		} else if (stage == 1 && componentId == 2) {
			if (player.getSkills().getLevel((Integer) player.getTemporaryAttributtes().get("skill1")) < AncientEffigies
					.getRequiredLevel(itemId)) {
				sendDialogue(

						new String[] { "The images in your mind fade; the ancient effigy seems",
								"to desire knowledge of experiences you have not yet", "had." });
				player.getPackets()
						.sendGameMessage("You require at lest level" + AncientEffigies.getRequiredLevel(itemId)
								+ Skills.SKILL_NAME[(Integer) player.getTemporaryAttributtes().get("skill1")]
								+ " to investigate the ancient effigy further.");
			} else {
				player.getTemporaryAttributtes().put("skill", skill1);
				sendDialogue(new String[] { "As you focus on your memories, you can almost hear a",
						"voice in the back of your mind whispering to you..." });
				stage = 2;
			}
		} else if (stage == 1 && componentId == 3) {
			if (player.getSkills().getLevel((Integer) player.getTemporaryAttributtes().get("skill2")) < AncientEffigies
					.getRequiredLevel(itemId)) {
				sendDialogue(

						new String[] { "The images in your mind fade; the ancient effigy seems",
								"to desire knowledge of experiences you have not yet", "had." });
				player.getPackets()
						.sendGameMessage("You require at lest level" + AncientEffigies.getRequiredLevel(itemId) + " "
								+ Skills.SKILL_NAME[(Integer) player.getTemporaryAttributtes().get("skill1")]
								+ " to investigate the ancient effigy further.");
			} else {
				player.getTemporaryAttributtes().put("skill", skill2);
				sendDialogue(new String[] { "As you focus on your memories, you can almost hear a",
						"voice in the back of your mind whispering to you..." });
				stage = 2;
			}
		} else if (stage == 2) {
			// player.getSkills().addXp((Integer)
			// player.getTemporaryAttributtes().get("skill"),
			// AncientEffigies.getExp(itemId));
			player.getPackets().sendGameMessage("You have gained " + AncientEffigies.getExp(itemId) + " "
					+ Skills.SKILL_NAME[(Integer) player.getTemporaryAttributtes().get("skill")] + " experience!");
			AncientEffigies.effigyInvestigation(player, itemId);
			sendDialogue(new String[] { "The ancient effigy glows briefly; it seems changed",
					"somehow and no longer responds to the same memories", "as before." });
			stage = 3;
		} else if (stage == 3) {
			sendDialogue(new String[] { "A sudden bolt of inspiration flashes through your mind,",
					"revealing new insight into your experiences!" });
			stage = -2;
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
