package com.rs.net.decoders.handlers;

import java.util.List;

import com.rs.Settings;
import com.rs.cores.WorldThread;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.minigames.Barrows;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Equipment;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.content.AncientEffigies;
import com.rs.game.player.content.ArmourSets;
import com.rs.game.player.content.ArmourSets.Sets;
import com.rs.game.player.content.Dicing;
import com.rs.game.player.content.Foods;
import com.rs.game.player.content.Pots;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import skills.crafting.GemCutting;
import skills.crafting.GemCutting.Gem;
import skills.crafting.LeatherCrafting;
import skills.firemaking.Firemaking;
import skills.fletching.Fletching;
import skills.fletching.Fletching.Fletch;
import skills.herblore.HerbCleaning;
import skills.herblore.Herblore;
import skills.hunter.BoxAction;
import skills.hunter.BoxAction.HunterEquipment;
import skills.magic.Magic;
import skills.prayer.Burying.Bone;
import skills.runecrafting.Runecrafting;
import skills.summoning.Summoning;
import skills.summoning.Summoning.Pouches;

public class InventoryOptionsHandler {

	public static void handleItemOption2(final Player player, final int slotId, final int itemId, Item item) {
		if (Firemaking.isFiremaking(player, itemId))
			return;
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.emptyPouch(player, pouch);
			player.stopAll(false);
		} else if (itemId >= 15086 && itemId <= 15100) {
			Dicing.handleRoll(player, itemId, true);
			return;
		} else {
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
		}
	}

	public static void dig(final Player player) {
		player.resetWalkSteps();
		player.setNextAnimation(new Animation(830));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				if (Barrows.digIntoGrave(player))
					return;
				if (player.getX() == 3005 && player.getY() == 3376 || player.getX() == 2999 && player.getY() == 3375
						|| player.getX() == 2996 && player.getY() == 3377
						|| player.getX() == 2989 && player.getY() == 3378
						|| player.getX() == 2987 && player.getY() == 3387
						|| player.getX() == 2984 && player.getY() == 3387) {
					// mole
					player.setNextWorldTile(new WorldTile(1752, 5137, 0));
					player.getPackets()
							.sendGameMessage("You seem to have dropped down into a network of mole tunnels.");
					return;
				}
				player.getPackets().sendGameMessage("You find nothing.");
			}

		});
	}

	public static void handleItemOption1(Player player, final int slotId, final int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (Foods.eat(player, item, slotId))
			return;
		if (itemId >= 15086 && itemId <= 15100) {
			Dicing.handleRoll(player, itemId, false);
			return;
		}
		if (Pots.pot(player, item, slotId))
			return;
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.fillPouch(player, pouch);
			return;
		}
		if (itemId == 22370) {
			Summoning.openDreadnipInterface(player);
		}
		if (itemId == 952) {// spade
			dig(player);
			return;
		}
		if (HerbCleaning.clean(player, item, slotId))
			return;
		Bone bone = Bone.forId(itemId);
		if (bone != null) {
			Bone.bury(player, slotId);
			return;
		}
		if (Magic.useTabTeleport(player, itemId))
			return;
		if (itemId == AncientEffigies.SATED_ANCIENT_EFFIGY || itemId == AncientEffigies.GORGED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.NOURISHED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.STARVED_ANCIENT_EFFIGY)
			player.getDialogueManager().startDialogue("AncientEffigiesD", itemId);
		else if (itemId == 4155)
			player.getDialogueManager().startDialogue("EnchantedGemDialouge");
		else if (itemId == 1856) {// Information Book
			player.getInterfaceManager().sendInterface(275);
			player.getPackets().sendIComponentText(275, 2, Settings.SERVER_NAME);
			player.getPackets().sendIComponentText(275, 16, "Welcome to " + Settings.SERVER_NAME + ".");
			player.getPackets().sendIComponentText(275, 17, "If want some an item use command ::item id.");
			player.getPackets().sendIComponentText(275, 18, "If you don't have an item list you can find ids");
			player.getPackets().sendIComponentText(275, 19, "at http://itemdb.biz");
			player.getPackets().sendIComponentText(275, 20, "You can change your prayers and spells at home.");
			player.getPackets().sendIComponentText(275, 21, "If you need any help, do ::ticket. (Don't abuse it)");
			player.getPackets().sendIComponentText(275, 22, "at start of your message on public chat.");
			player.getPackets().sendIComponentText(275, 22, "By the way you can compare your ::score with your mates.");
			player.getPackets().sendIComponentText(275, 23, "Oh and ye, don't forget to ::vote and respect rules.");
			player.getPackets().sendIComponentText(275, 24, "");
			player.getPackets().sendIComponentText(275, 25, "Forums: " + Settings.WEBSITE_LINK);
			player.getPackets().sendIComponentText(275, 26, "");
			player.getPackets().sendIComponentText(275, 27, "Enjoy your time on " + Settings.SERVER_NAME + ".");
			player.getPackets().sendIComponentText(275, 28, "<img=1> Staff Team");
			player.getPackets().sendIComponentText(275, 29, "");
			player.getPackets().sendIComponentText(275, 30, "");
			player.getPackets().sendIComponentText(275, 14, "<u>Visit Website</u>");
			for (int i = 31; i < 300; i++)
				player.getPackets().sendIComponentText(275, i, "");
		} else if (itemId == HunterEquipment.BOX.getId()) // almost done
			player.getActionManager().setAction(new BoxAction(HunterEquipment.BOX));
		else if (itemId == HunterEquipment.BRID_SNARE.getId())
			player.getActionManager().setAction(new BoxAction(HunterEquipment.BRID_SNARE));
		else if (item.getDefinitions().getName().startsWith("Burnt"))
			player.getDialogueManager().startDialogue("SimplePlayerMessage", "Ugh, this is inedible.");

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
			if (!player.getControlerManager().canUseItemOnItem(itemUsed, usedWith))
				return;
			Fletch fletch = Fletching.isFletching(usedWith, itemUsed);
			if (fletch != null) {
				player.getDialogueManager().startDialogue("FletchingD", fletch);
				return;
			}
			int herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
			if (herblore > -1) {
				player.getDialogueManager().startDialogue("HerbloreD", herblore, itemUsed, usedWith);
				return;
			}
			if (itemUsed.getId() == LeatherCrafting.NEEDLE.getId()
					|| usedWith.getId() == LeatherCrafting.NEEDLE.getId()) {
				if (LeatherCrafting.handleItemOnItem(player, itemUsed, usedWith)) {
					return;
				}
			}
			Sets set = ArmourSets.getArmourSet(itemUsedId, itemUsedWithId);
			if (set != null) {
				ArmourSets.exchangeSets(player, set);
				return;
			}
			if (Firemaking.isFiremaking(player, itemUsed, usedWith))
				return;
			else if (contains(1755, Gem.OPAL.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.OPAL);
			else if (contains(1755, Gem.JADE.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.JADE);
			else if (contains(1755, Gem.RED_TOPAZ.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.RED_TOPAZ);
			else if (contains(1755, Gem.SAPPHIRE.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.SAPPHIRE);
			else if (contains(1755, Gem.EMERALD.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.EMERALD);
			else if (contains(1755, Gem.RUBY.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.RUBY);
			else if (contains(1755, Gem.DIAMOND.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.DIAMOND);
			else if (contains(1755, Gem.DRAGONSTONE.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.DRAGONSTONE);
			else if (contains(1755, Gem.ONYX.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.ONYX);
			else
				player.getPackets().sendGameMessage("Nothing interesting happens.");
			if (Settings.DEBUG)
				Logger.log("ItemHandler", "Used:" + itemUsed.getId() + ", With:" + usedWith.getId());
		}
	}

	public static void handleItemOption3(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (itemId == 20767 || itemId == 20769 || itemId == 20771)
			SkillCapeCustomizer.startCustomizing(player, itemId);
		else if (itemId >= 15084 && itemId <= 15100)
			player.getDialogueManager().startDialogue("DiceBag", itemId);
		else if (itemId == 24437 || itemId == 24439 || itemId == 24440 || itemId == 24441)
			player.getDialogueManager().startDialogue("FlamingSkull", item, slotId);
		else if (Equipment.getItemSlot(itemId) == Equipment.SLOT_AURA)
			player.getAuraManager().sendTimeRemaining(itemId);
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
		Pouches pouches = Pouches.forId(itemId);
		if (pouches != null)
			Summoning.spawnFamiliar(player, pouches);
		else if (itemId == 1438)
			Runecrafting.locate(player, 3127, 3405);
		else if (itemId == 1440)
			Runecrafting.locate(player, 3306, 3474);
		else if (itemId == 1442)
			Runecrafting.locate(player, 3313, 3255);
		else if (itemId == 1444)
			Runecrafting.locate(player, 3185, 3165);
		else if (itemId == 1446)
			Runecrafting.locate(player, 3053, 3445);
		else if (itemId == 1448)
			Runecrafting.locate(player, 2982, 3514);
		else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354 && itemId <= 10362)
			player.getDialogueManager().startDialogue("Transportation", "Edgeville", new WorldTile(3087, 3496, 0),
					"Karamja", new WorldTile(2918, 3176, 0), "Draynor Village", new WorldTile(3105, 3251, 0),
					"Al Kharid", new WorldTile(3293, 3163, 0), itemId);
		else if (itemId == 1704 || itemId == 10352)
			player.getPackets().sendGameMessage(
					"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		else if (itemId >= 3853 && itemId <= 3867)
			player.getDialogueManager().startDialogue("Transportation", "Burthrope Games Room",
					new WorldTile(2880, 3559, 0), "Barbarian Outpost", new WorldTile(2519, 3571, 0), "Gamers' Grotto",
					new WorldTile(2970, 9679, 0), "Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
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
