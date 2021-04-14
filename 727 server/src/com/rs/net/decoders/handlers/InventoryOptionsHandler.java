package com.rs.net.decoders.handlers;

import java.util.List;

import com.rs.Settings;
import com.rs.cores.WorldThread;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.event.EventListener.ClickOption;
import com.rs.game.event.EventManager;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.route.CoordsEvent;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class InventoryOptionsHandler {

	public static void handleItemOption2(final Player player, final int slotId, final int itemId, Item item) {
		if (player.isEquipDisabled())
			return;
		long passedTime = Utils.currentTimeMillis() - WorldThread.LAST_CYCLE_CTM;
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				List<Integer> slots = player.getSwitchItemCache();
				int[] slot = new int[slots.size()];
				for (int i = 0; i < slot.length; i++)
					slot[i] = slots.get(i);
				player.getSwitchItemCache().clear();
				ButtonHandler.sendWear(player, slot);
				player.stopAll(false, true, false);
			}
		}, passedTime >= 600 ? 0 : passedTime > 330 ? 1 : 0);
		if (player.getSwitchItemCache().contains(slotId))
			return;
		player.getSwitchItemCache().add(slotId);
		if (EventManager.get().handleItemClick(player, item, ClickOption.SECOND)) {
			return;
		}
	}

	public static void handleItemOption1(Player player, final int slotId, final int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		
		if (EventManager.get().handleItemClick(player, item, ClickOption.FIRST)) {
			return;
		}
		
		if (Settings.DEBUG)
			Logger.log("ItemHandler", "Item Select:" + itemId + ", Slot Id:" + slotId);
	}

	/*
	 * returns the other
	 */
	public static Item contains(int id1, Item item1, Item item2) {
		if (item1.getId() == id1)
			return item2;
		if (item2.getId() == id1)
			return item1;
		return null;
	}

	public static boolean contains(int id1, int id2, Item... items) {
		boolean containsId1 = false;
		boolean containsId2 = false;
		for (Item item : items) {
			if (item.getId() == id1)
				containsId1 = true;
			else if (item.getId() == id2)
				containsId2 = true;
		}
		return containsId1 && containsId2;
	}

	public static void handleItemOnItem(final Player player, InputStream stream) {

//		int itemUsedWithId = stream.readShort();
//		int toSlot = stream.readShortLE128();
//		int interfaceId = stream.readInt() >> 16;
//		int interfaceId2 = stream.readInt() >> 16;
//		int fromSlot = stream.readShort();
//		int itemUsedId = stream.readShortLE128();
		
		int toSlot = stream.readShortLE128();
		int fromSlot = stream.readShortLE();
		int itemUsedWithId = stream.readShortLE128();
		int interfaceId2 = stream.readIntLE() >> 16;
		int interfaceId = stream.readIntV2() >> 16;
		int itemUsedId = stream.readShortLE();
		
		if (Settings.DEBUG)
			System.out.println(String.format("fromInter: %s, toInter: %s, fromSlot: %s, toSlot %s, item1: %s, item2: %s", interfaceId, interfaceId2, fromSlot, toSlot, itemUsedId, itemUsedWithId));
		
		//fromInter: 44498944, toInter: 44498944, fromSlot: 11694, toSlot 0, item1: 14484, item2: 8
		
		if ((interfaceId2 == 747 || interfaceId2 == 662) && interfaceId == Inventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(toSlot);
				}
			}
			return;
		}
		if (interfaceId == Inventory.INVENTORY_INTERFACE && interfaceId == interfaceId2
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28)
				return;
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != itemUsedId
					|| usedWith.getId() != itemUsedWithId)
				return;
			player.stopAll();
			
			if (Settings.DEBUG)
				Logger.log("ItemHandler", "Used:" + itemUsed.getId() + ", With:" + usedWith.getId());
		}
	}

	public static void handleItemOption3(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		
		if (EventManager.get().handleItemClick(player, item, ClickOption.THIRD)) {
			return;
		}
		
	}

	public static void handleItemOption4(Player player, int slotId, int itemId, Item item) {
		System.out.println("Option 4");
	}

	public static void handleItemOption5(Player player, int slotId, int itemId, Item item) {
		System.out.println("Option 5");
	}

	public static void handleItemOption6(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		
	}

	public static void handleItemOption7(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		if (!player.getControlerManager().canDropItem(item))
			return;
		player.stopAll(false);
		if (item.getDefinitions().isOverSized()) {
			player.getPackets().sendGameMessage("The item appears to be oversized.");
			player.getInventory().deleteItem(item);
			return;
		}
		if (item.getDefinitions().isDestroyItem()) {
			player.getDialogueManager().startDialogue("DestroyItemOption", slotId, item);
			return;
		}
		if (player.getPetManager().spawnPet(itemId, true)) {
			return;
		}
		player.getInventory().deleteItem(slotId, item);
		if (player.getCharges().degradeCompletly(item))
			return;
		World.addGroundItem(item, new WorldTile(player), player, false, 180, true);
		player.getPackets().sendSound(2739, 0, 1);
	}

	public static void handleItemOption8(Player player, int slotId, int itemId, Item item) {
		player.getInventory().sendExamine(slotId);
	}

	public static void handleItemOnNPC(final Player player, final NPC npc, final Item item) {
		if (item == null) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
					return;
				}
				if (npc instanceof Pet) {
					player.faceEntity(npc);
					player.getPetManager().eat(item.getId(), (Pet) npc);
					return;
				}
			}
		}, npc.getSize()));
	}
}
