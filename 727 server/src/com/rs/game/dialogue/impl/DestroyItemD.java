package com.rs.game.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * Item destroying.
 * 
 * @author Dennis
 *
 */
public class DestroyItemD {

	/**
	 * The Item being Destroyed.
	 */
	private Item item;

	/**
	 * Sends the chatbox interface.
	 * 
	 * @param player
	 * @param item
	 */
	public void sendChatInterface(Player player, Item item) {
		this.item = item;
		player.getInterfaceManager().sendChatBoxInterface(1183);
		player.getPackets().sendIComponentText(1183, 22,
				"Are you sure you want to destroy your " + item.getName() + "?");
		player.getPackets().sendIComponentText(1183, 7, item.getName());
		player.getPackets().sendItemOnIComponent(1183, 13, item.getId(), 1);
	}

	/**
	 * Executes the Item destruction.
	 * 
	 * @param player
	 * @param interfaceId
	 * @param componentId
	 */
	public void executeDestroy(Player player, int interfaceId, int componentId) {
		if (interfaceId == 1183) {
			if (componentId == 9) {
				player.getInventory().deleteItem(item);
				player.getInterfaceManager().closeChatBoxInterface();
				player.getPackets().sendSound(4500, 0, 1);
			} else {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		}
	}

	/**
	 * Gets the Item that's being destroyed.
	 * 
	 * @return
	 */
	public Item getItem() {
		return item;
	}

	public DestroyItemD(Item item) {
		this.item = item;
	}

	public DestroyItemD() {
	}

	public static DestroyItemD INSTANCE = new DestroyItemD();
}