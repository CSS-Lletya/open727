package com.rs.game.player.dialogues;

import skills.Skills;

/**
 * Setting a skill level.
 * 
 * @author Raghav
 * 
 */
public class SetSkills extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.getEquipment().wearingArmour()) {
			sendDialogue("Please remove your armour first.");
			stage = -2;
		} else
			sendOptionsDialogue("Choose a skill", "" + Skills.SKILL_NAME[0], "" + Skills.SKILL_NAME[1],
					"" + Skills.SKILL_NAME[2], "" + Skills.SKILL_NAME[3], "More options.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				player.getTemporaryAttributtes().put("skillId", Skills.ATTACK);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_2) {
				player.getTemporaryAttributtes().put("skillId", Skills.DEFENCE);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_3) {
				player.getTemporaryAttributtes().put("skillId", Skills.STRENGTH);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_4) {
				player.getTemporaryAttributtes().put("skillId", Skills.HITPOINTS);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_5) {
				stage = 0;
				sendOptionsDialogue("Choose a skill", "" + Skills.SKILL_NAME[4], "" + Skills.SKILL_NAME[5],
						"" + Skills.SKILL_NAME[6], "" + Skills.SKILL_NAME[23], "Never mind.");
			}
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				player.getTemporaryAttributtes().put("skillId", Skills.RANGE);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_2) {
				player.getTemporaryAttributtes().put("skillId", Skills.PRAYER);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_3) {
				player.getTemporaryAttributtes().put("skillId", Skills.MAGIC);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_4) {
				player.getTemporaryAttributtes().put("skillId", Skills.SUMMONING);
				player.getPackets().sendRunScript(108, new Object[] { "Enter skill level:" });
			} else if (componentId == OPTION_5)
				end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
