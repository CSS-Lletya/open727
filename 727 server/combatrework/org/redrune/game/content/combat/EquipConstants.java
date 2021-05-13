package org.redrune.game.content.combat;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public interface EquipConstants {
	
	/**
	 * The names of items that are fully body items
	 */
	String[] FULL_BODY = { "Investigator's coat", "armour", "hauberk", "top", "shirt", "platebody", "Ahrims robetop", "Karils leathertop", "brassard", "Robe top", "robetop", "platebody (t)", "platebody (g)", "chestplate", "torso", "Morrigan's", "leather body", "robe top", "Pernix body", "Torva platebody" };
	
	/**
	 * The names of items that are full hat items
	 */
	String[] FULL_HAT = { "sallet", "med helm", "coif", "Dharok's helm", "hood", "Initiate helm", "Coif", "Helm of neitiznot" };
	
	/**
	 * The names of items that are full mask items
	 */
	String[] FULL_MASK = { "Christmas ghost hood", "Dragon full helm (or)", "sallet", "full helm", "mask", "Veracs helm", "Guthans helm", "Torags helm", "Karils coif", "full helm (t)", "full helm (g)", "mask" };
	
	/**
	 * The slot in the equipment container
	 */
	byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13, SLOT_AURA = 14;
	
	/**
	 * Checks if an item's definitions will result in being full body.
	 *
	 * @param def
	 * 		The definitions
	 */
	static boolean isFullBody(ItemDefinitions def) {
		String weapon = def.getName();
		for (String name : FULL_BODY) {
			if (weapon.contains(name)) {
				return true;
			}
		}
		return def.getId() == 6107 || def.getId() == 13624 || def.getId() == 13887;
	}
	
	/**
	 * Checks if an item's definitions will result in being full hat.
	 *
	 * @param def
	 * 		The definitions
	 */
	static boolean isFullHat(ItemDefinitions def) {
		String weapon = def.getName();
		for (String name : FULL_HAT) {
			if (weapon.endsWith(name)) {
				return true;
			}
		}
		return def.getId() == 14824;
	}
	
	/**
	 * Checks if an item's definitions will result in being full mask.
	 *
	 * @param def
	 * 		The definitions
	 */
	static boolean isFullMask(ItemDefinitions def) {
		String weapon = def.getName();
		for (String name : FULL_MASK) {
			if (weapon.endsWith(name)) {
				return true;
			}
		}
		return false;
	}
	
	static int getItemSlot(int itemId) {
		return ItemDefinitions.forId(itemId).getEquipSlot();
	}
	
	static boolean isTwoHanded(Item item) {
		return item.getDefinitions().getEquipType() == 5;
	}
}
