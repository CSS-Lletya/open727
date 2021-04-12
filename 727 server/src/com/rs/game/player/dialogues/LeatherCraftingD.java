package com.rs.game.player.dialogues;

import com.rs.game.player.actions.LeatherCrafting;
import com.rs.game.player.actions.LeatherCrafting.LeatherData;
import com.rs.game.player.content.SkillsDialogue;

public class LeatherCraftingD extends Dialogue {

	@Override
	public void start() {
		int[] items = new int[parameters.length];
		for (int i = 0; i < items.length; i++)
			items[i] = ((LeatherData) parameters[i]).getFinalProduct();

		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE,
				"Choose how many you wish to make,<br>then click on the item to begin.", 28, items, null);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int option = SkillsDialogue.getItemSlot(componentId);
		if (option > parameters.length) {
			end();
			return;
		}
		LeatherData data = (LeatherData) parameters[option];
		int quantity = SkillsDialogue.getQuantity(player);
		int invQuantity = player.getInventory().getItems().getNumberOf(data.getLeatherId());
		if (quantity > invQuantity)
			quantity = invQuantity;
		player.getActionManager().setAction(new LeatherCrafting(data, quantity));
		end();
	}

	@Override
	public void finish() {

	}

}
