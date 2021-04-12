package com.rs.game.player.dialogues;

import com.rs.game.item.Item;
import com.rs.game.player.Equipment;

/**
 * Flaming skull color changing.
 * @author Dragonkk
 */
public class FlamingSkull extends Dialogue {

	private static final String COLORS[] = { "Green", "Purple", "Blue", "Red" };

	private Item item;
	private int slot;

	@Override
	public void start() {
		item = (Item) parameters[0];
		slot = (Integer) parameters[1];
		int index = (item.getId() == 24437 ? 24442 : item.getId()) - 24439;
		sendOptionsDialogue("What colour do you want your skull to be?", COLORS[(index + 1) % 4],
				COLORS[(index + 2) % 4], COLORS[(index + 3) % 4]);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int index = (item.getId() == 24437 ? 24442 : item.getId()) - 24439;
		int option;
		if (componentId == OPTION_1)
			option = 1;
		else if (componentId == OPTION_2)
			option = 2;
		else
			option = 3;
		int itemId = 24439 + ((index + option) % 4);
		item.setId(itemId == 24442 ? 24437 : itemId);
		if (slot == -1) {
			player.getEquipment().refresh(Equipment.SLOT_HAT);
			player.getAppearance().generateAppearanceData();
		} else
			player.getInventory().refresh(slot);
		end();
	}

	@Override
	public void finish() {

	}
}
