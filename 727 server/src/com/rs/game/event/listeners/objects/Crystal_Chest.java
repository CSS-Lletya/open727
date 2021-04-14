package com.rs.game.event.listeners.objects;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.event.EventListener;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.SimpleMessage;
import com.rs.utils.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 25, 2014
 */
public class Crystal_Chest extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 11231 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		return false;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		if (!player.getInventory().containsOneItem(989)) {
			player.getDialogueManager().startDialogue(SimpleMessage.class, "You need a crystal key to unlock this chest.");
			return true;
		}
		openChest(player);
		return true;
	}

	/**
	 * Opens the chest for a player and gives rewards from a random slot in the
	 * {@link #REWARDS} array
	 * 
	 * @param player
	 *            The player opening the chest
	 */
	public static void openChest(final Player player) {
		player.getInventory().deleteItem(989, 1);
		player.setNextAnimation(new Animation(536));
		List<Item> rewards = new ArrayList<Item>();
		for (int i = 0; i < Utils.random(1, 3); i++) {
			double chance = Utils.random(1, 50);
			int index = Utils.random(REWARDS.length);
			if (chance <= REWARDS[index][1]) {
				rewards.add(new Item((int) REWARDS[index][0], 1));
			}
		}
		int index = Utils.random(JUNK_REWARDS.length);
		int amt = JUNK_REWARDS[index][1];
		rewards.add(new Item(JUNK_REWARDS[index][0], amt == 1 ? amt : Utils.random(amt)));
		for (Item item : rewards) {
			player.getInventory().addItem(item);
		}
//		player.getDialogueManager().startDialogue(SimpleItemMessage.class, 989, "You find some treasure in the chest!");
//		player.getAchievementManager().notifyUpdate(CrystalChestAchievement.class);
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

	private static final double[][] REWARDS = new double[][] { { 6571, 5 }, // uncut-
																			// onyx
			{ 2572, 10 }, // ring of wealth
			{ 7394, 12.5 }, // wiz g
			{ 7386, 12.5 }, // wiz g
			{ 7390, 12.5 }, // wiz g
			{ 7388, 20.5 }, // wiz t
			{ 7392, 20.5 }, // wiz t
			{ 7396, 20.5 }, // wiz t
			{ 2595, 10 }, // black g
			{ 2591, 10 }, // black g
			{ 2593, 10 }, // black g
			{ 2597, 10 }, // black g
			{ 2587, 15 }, // black t
			{ 2583, 15 }, // black t
			{ 2585, 15 }, // black t
			{ 2589, 15 }, // black t
	};

	private static final int[][] JUNK_REWARDS = new int[][] { { 1631, 1 }, // dragon-stones
			{ 1635, 1 }, // gold ring
			{ 1637, 1 }, // sapphire ring
			{ 1639, 1 }, // emerald ring
			{ 1641, 1 }, // ruby ring
			{ 1641, 1 }, // diamond ring
			{ 5318, 100 }, // potato seed
			{ 5291, 100 }, // guam seed
			{ 5096, 100 }, // marigold seed
			{ 5101, 100 }, // redberry seed
			{ 2352, 25 }, // iron bar
			{ 2364, 25 }, // rune bar
			{ 995, 500000 }, // coins
	};

}
