package com.rs.game.player.dialogues;

import com.rs.game.item.Item;
import com.rs.game.player.content.SkillsDialogue;

import skills.herblore.Herblore;

public class HerbloreD extends Dialogue {

	private int items;
	private Item first;
	private Item second;

	@Override
	public void start() {
		items = (Integer) parameters[0];
		first = (Item) parameters[1];
		second = (Item) parameters[2];
		int amount;
		if (first.getId() == Herblore.PESTLE_AND_MORTAR)
			amount = player.getInventory().getItems().getNumberOf(second.getId());
		else if (second.getId() == Herblore.PESTLE_AND_MORTAR)
			amount = player.getInventory().getItems().getNumberOf(first.getId());
		else {
			amount = player.getInventory().getItems().getNumberOf(first.getId());
			if (amount > player.getInventory().getItems().getNumberOf(second.getId()))
				amount = player.getInventory().getItems().getNumberOf(second.getId());
		}
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE,
				"Choose how many you wish to make,<br>then click on the item to begin.", amount, new int[] { items },
				null);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new Herblore(first, second, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {
	}
}
