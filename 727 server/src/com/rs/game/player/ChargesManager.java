package com.rs.game.player;

import java.util.HashMap;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.utils.Utils;

public class ChargesManager {

	private transient Player player;

	private HashMap<Integer, Integer> charges;

	public ChargesManager() {
		charges = new HashMap<Integer, Integer>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		Item[] items = player.getEquipment().getItems().getItems();
		for (byte slot = 0; slot < items.length; slot++) {
			Item item = items[slot];
			if (item == null)
				continue;
			if (player.getAttackedByDelay() > Utils.currentTimeMillis()) {
				int newId = ItemConstants.getDegradeItemWhenCombating(item.getId());
				if (newId != -1) {
					item.setId(newId);
					player.getEquipment().refresh(slot);
					player.getAppearance().generateAppearenceData();
					player.getPackets().sendGameMessage("Your " + item.getDefinitions().getName() + " degraded.");
				}
			}
			int defaultCharges = ItemConstants.getItemDefaultCharges(item.getId());
			if (defaultCharges == -1)
				continue;
			if (ItemConstants.itemDegradesWhileWearing(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
			else if (player.getAttackedByDelay() > Utils.currentTimeMillis())
				degrade(item.getId(), defaultCharges, slot);
		}
	}

	public void die() {
		Item[] equipItems = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < equipItems.length; slot++) {
			if (equipItems[slot] != null && degradeCompletly(equipItems[slot]))
				player.getEquipment().getItems().set(slot, null);
		}
		Item[] invItems = player.getInventory().getItems().getItems();
		for (int slot = 0; slot < invItems.length; slot++) {
			if (invItems[slot] != null && degradeCompletly(invItems[slot]))
				player.getInventory().getItems().set(slot, null);
		}
	}

	/*
	 * return disapear;
	 */
	public boolean degradeCompletly(Item item) {
		int defaultCharges = ItemConstants.getItemDefaultCharges(item.getId());
		if (defaultCharges == -1)
			return false;
		while (true) {
			if (ItemConstants.itemDegradesWhileWearing(item.getId())
					|| ItemConstants.itemDegradesWhileCombating(item.getId())) {
				charges.remove(item.getId());
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId == -1)
					return ItemConstants.getItemDefaultCharges(item.getId()) == -1 ? false : true;
				item.setId(newId);
			} else {
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId != -1) {
					charges.remove(item.getId());
					item.setId(newId);
				}
				break;
			}
		}
		return false;
	}

	public void wear(byte slot) {
		Item item = player.getEquipment().getItems().get(slot);
		if (item == null)
			return;
		int newId = ItemConstants.getDegradeItemWhenWear(item.getId());
		if (newId == -1)
			return;
		player.getEquipment().getItems().set(slot, new Item(newId, 1));
		player.getEquipment().refresh(slot);
		player.getAppearance().generateAppearenceData();
		player.getPackets().sendGameMessage("Your " + item.getDefinitions().getName() + " degraded.");
	}

	private void degrade(int itemId, int defaultCharges, byte slot) {
		Integer c = charges.remove(itemId);
		if (c == null)
			c = defaultCharges;
		else {
			c--;
			if (c == 0) {
				int newId = ItemConstants.getItemDegrade(itemId);
				player.getEquipment().getItems().set(slot, newId != -1 ? new Item(newId, 1) : null);
				if (newId == -1)
					player.getPackets().sendGameMessage(
							"Your " + ItemDefinitions.getItemDefinitions(itemId).getName() + " became into dust.");
				else
					player.getPackets().sendGameMessage(
							"Your " + ItemDefinitions.getItemDefinitions(itemId).getName() + " degraded.");
				player.getEquipment().refresh(slot);
				player.getAppearance().generateAppearenceData();
				return;
			}
		}
		charges.put(itemId, c);
	}

}
