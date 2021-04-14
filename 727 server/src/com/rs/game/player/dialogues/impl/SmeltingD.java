package com.rs.game.player.dialogues.impl;

import com.rs.game.WorldObject;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.dialogues.SkillsDialogue;
import com.rs.game.player.dialogues.SkillsDialogue.ItemNameFilter;

import skills.Skills;
import skills.smithing.Smelting;
import skills.smithing.Smelting.SmeltingBar;

public class SmeltingD extends Dialogue {

	private WorldObject object;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		int[] ids = new int[SmeltingBar.values().length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = SmeltingBar.values()[i].getProducedBar().getId();
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE,
				"How many bars you would like to smelt?<br>Choose a number, then click the bar to begin.", 28, ids,
				new ItemNameFilter() {
					int count = 0;

					@Override
					public String rename(String name) {
						SmeltingBar bar = SmeltingBar.values()[count++];
						if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired())
							name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + bar.getLevelRequired();
						return name;

					}
				});
	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(
				new Smelting(SkillsDialogue.getItemSlot(componentId), object, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {
	}
}
