package com.rs.game.player;

import java.util.HashMap;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.utils.ItemExamines;

import skills.Skills;

public final class Equipment {

	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_WEAPON = 3, SLOT_CHEST = 4,
			SLOT_SHIELD = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13,
			SLOT_AURA = 14;

	private ItemsContainer<Item> items;

	private transient Player player;
	private transient int equipmentHpIncrease;

	static final byte[] DISABLED_SLOTS = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 };

	public Equipment() {
		items = new ItemsContainer<Item>(15, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		player.getPackets().sendItems(94, items);
		refresh(null);
	}

	public void refresh(byte... slots) {
		if (slots != null) {
			player.getPackets().sendUpdateItems(94, items, slots);
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		refreshConfigs(slots == null);
	}

	public void reset() {
		items.reset();
		init();
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public void sendExamine(int slotId) {
		Item item = items.get(slotId);
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshConfigs(boolean init) {
		double hpIncrease = 0;
		for (int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			if (item == null)
				continue;
			int id = item.getId();
			if (index == Equipment.SLOT_HAT) {
				if (id == 20135 || id == 20137 // torva
						|| id == 20147 || id == 20149 // pernix
						|| id == 20159 || id == 20161 // virtus
				)
					hpIncrease += 66;

			} else if (index == Equipment.SLOT_CHEST) {
				if (id == 20139 || id == 20141 // torva
						|| id == 20151 || id == 20153 // pernix
						|| id == 20163 || id == 20165 // virtus
				)
					hpIncrease += 200;
			} else if (index == Equipment.SLOT_LEGS) {
				if (id == 20143 || id == 20145 // torva
						|| id == 20155 || id == 20157 // pernix
						|| id == 20167 || id == 20169 // virtus
				)
					hpIncrease += 134;
			}

		}
//		if (player.getLastBonfire() > 0) {
//			int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
//			hpIncrease += (maxhp * Bonfire.getBonfireBoostMultiplier(player)) - maxhp;
//		}
		if (player.getHpBoostMultiplier() != 0) {
			int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
			hpIncrease += maxhp * player.getHpBoostMultiplier();
		}
		if (hpIncrease != equipmentHpIncrease) {
			equipmentHpIncrease = (int) hpIncrease;
			if (!init)
				player.refreshHitPoints();
		}
	}

	public static boolean hideArms(Item item) {
		String name = item.getName().toLowerCase();
		if
		// temp old graphics fix, but bugs alil new ones
		(name.contains("d'hide body") || name.contains("dragonhide body") || name.equals("stripy pirate shirt")
				|| (name.contains("chainbody") && (name.contains("iron") || name.contains("bronze")
						|| name.contains("steel") || name.contains("black") || name.contains("mithril")
						|| name.contains("adamant") || name.contains("rune") || name.contains("white")))
				|| name.equals("leather body") || name.equals("hardleather body") || name.contains("studded body"))
			return false;
		return item.getDefinitions().getEquipType() == 6;
	}

	public static boolean hideHair(Item item) {
		return item.getDefinitions().getEquipType() == 8;
	}

	public static boolean showBear(Item item) {
		String name = item.getName().toLowerCase();
		return !hideHair(item) || name.contains("horns") || name.contains("hat") || name.contains("afro")
				|| name.contains("cowl") || name.contains("tattoo") || name.contains("headdress")
				|| name.contains("hood") || (name.contains("mask") && !name.contains("h'ween"))
				|| (name.contains("helm") && !name.contains("full"));
	}

	public static byte getItemSlot(int itemId) {
		return (byte) ItemDefinitions.getItemDefinitions(itemId).getEquipSlot();
	}

	public static boolean isTwoHandedWeapon(Item item) {
		return item.getDefinitions().getEquipType() == 5;
	}

	public int getWeaponRenderEmote() {
		Item weapon = items.get(3);
		if (weapon == null)
			return 1426;
		return weapon.getDefinitions().getRenderAnimId();
	}

	public boolean hasShield() {
		return items.get(5) != null;
	}

	public int getWeaponId() {
		Item item = items.get(SLOT_WEAPON);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getChestId() {
		Item item = items.get(SLOT_CHEST);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getHatId() {
		Item item = items.get(SLOT_HAT);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getShieldId() {
		Item item = items.get(SLOT_SHIELD);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getLegsId() {
		Item item = items.get(SLOT_LEGS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void removeAmmo(int ammoId, int ammount) {
		if (ammount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, ammount));
			refresh(SLOT_ARROWS);
		}
	}

	public int getAuraId() {
		Item item = items.get(SLOT_AURA);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getCapeId() {
		Item item = items.get(SLOT_CAPE);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getRingId() {
		Item item = items.get(SLOT_RING);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getAmmoId() {
		Item item = items.get(SLOT_ARROWS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void deleteItem(int itemId, int amount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	/**
	 * Removes all equipment
	 */
	public void removeAllEquipment() {
		Item[] itemsBefore = items.getItemsCopy();
		for(Item item : itemsBefore)
			if(item != null)
				items.remove(item);
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		byte[] changedSlots = new byte[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = (byte) index;
		}
		byte[] finalChangedSlots = new byte[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public int getBootsId() {
		Item item = items.get(SLOT_FEET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getGlovesId() {
		Item item = items.get(SLOT_HANDS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}

	public int getEquipmentHpIncrease() {
		return equipmentHpIncrease;
	}

	public void setEquipmentHpIncrease(int hp) {
		this.equipmentHpIncrease = hp;
	}

	public boolean wearingArmour() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null || getItem(SLOT_AMULET) != null
				|| getItem(SLOT_WEAPON) != null || getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null || getItem(SLOT_FEET) != null;
	}

	public int getAmuletId() {
		Item item = items.get(SLOT_AMULET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public boolean hasTwoHandedWeapon() {
		Item weapon = items.get(SLOT_WEAPON);
		return weapon != null && isTwoHandedWeapon(weapon);
	}

	/**
	 * Removes equipment based on the slot
	 * @param slotId
	 */
	public void sendRemoveEquipment(final byte slotId) {
		player.stopAll(false, false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null)
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getCombatDefinitions().decreaseSpecialAttack(0);

		if (player.getHitpoints() > (player.getMaxHitpoints() * 1.15)) {
			player.setHitpoints(player.getMaxHitpoints());
			player.refreshHitPoints();
		}
		player.getAppearance().generateAppearenceData();;

	}

		/**
	 * Gets the skill weapon requirement of a weapon
	 *
	 * @param skill
	 * 		The skill
	 */
	public int getWeaponRequirement(int skill) {
		int weaponId = getWeaponId();
		if (weaponId == -1) {
			return 1;
		}
		ItemDefinitions definition = ItemDefinitions.forId(weaponId);
		HashMap<Integer, Integer> requirements = definition.getWearingSkillRequiriments();
		if (requirements == null) {
			return 1;
		}
		for (int skillId : requirements.keySet()) {
			if (skillId > 24 || skillId < 0) {
				continue;
			}
			int level = requirements.get(skillId);
			if (level < 0 || level > 120) {
				continue;
			}
			if (skill == skillId) {
				return level;
			}
		}
		return 1;
	}
	
	/**
	 * The bonuses of the player
	 */
	private int[] bonuses = new int[18];
	
	/**
	 * Gets the bonus at an index
	 *
	 * @param index
	 * 		The index
	 */
	public int getBonus(int index) {
		if (index < 0 || index >= bonuses.length) {
			System.out.println("Invalid bonus index expected: " + index);
			return 0;
		}
		return bonuses[index];
	}
}
