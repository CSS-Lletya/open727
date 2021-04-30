package com.rs.game.player.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.Player;
import com.rs.utils.ItemExamines;
import com.rs.utils.Utils;

import player.CombatDefinitions;
import skills.Skills;

public class Shop {

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995;
	public int id = 0;

	private String name;
	private Item[] mainStock;
	private Item[] generalStock;
	private int money;
	private CopyOnWriteArrayList<Player> viewingPlayers;

	private int[] defaultQuantity;

	public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.setName(name);
		this.money = money;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++) {
			defaultQuantity[i] = mainStock[i].getAmount();
		}
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS) {
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
		}
	}

	public void initialize() {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		defaultQuantity = new int[mainStock.length];
		if (isGeneralStore()) {
			generalStock = new Item[MAX_SHOP_ITEMS];
		}
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null) {
				continue;
			}
			defaultQuantity[i] = mainStock[i].getAmount();
		}
		fixMainStock();
	}

	private void fixMainStock() {
		Item[] stock = new Item[mainStock.length];
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null || mainStock[i].getId() == -1 || mainStock[i].getAmount() == -1) {
				continue;
			}
			stock[i] = mainStock[i];
		}
		mainStock = stock;
	}

	public boolean isGeneralStore() {
		return name.equalsIgnoreCase("General Store");
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				viewingPlayers.remove(player);
				player.getTemporaryAttributtes().remove("Shop");
			}
		});
		player.getPackets().sendConfig(118, 4); // sets
																	// mainstock
																	// items set
		player.getPackets().sendConfig(1496, -1); // sets samples items set
		player.getPackets().sendConfig(532, money);
		sendStore(player);
		player.getPackets().sendGlobalConfig(199, -1);// unknown
		player.getInterfaceManager().sendInterface(1265); // opens shop
		for (int i = 0; i < MAX_SHOP_ITEMS; i++)
			player.getPackets().sendGlobalConfig(946 + i,
					i < defaultQuantity.length ? defaultQuantity[i] : generalStock != null ? 0 : -1);// prices
		player.getPackets().sendGlobalConfig(1241, 16750848);// unknown
		player.getPackets().sendGlobalConfig(1242, 15439903);// unknown
		player.getPackets().sendGlobalConfig(741, -1);// unknown
		player.getPackets().sendGlobalConfig(743, -1);// unknown
		player.getPackets().sendGlobalConfig(744, 0);// unknown
		if (generalStock != null)
			player.getPackets().sendHideIComponent(1265, 19, false); // unlocks
																		// general
																		// store
																		// icon
		player.getPackets().sendAccessMask(1265, 20, 0, getStoreSize() * 6, 1150, 0,1,2,3,4,5,9);
		sendInventory(player);
		player.getPackets().sendIComponentText(1265, 85, name);
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendAccessMask(1266, 0, 0, 27, 0, 1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7, "Value", "Sell 1", "Sell 5", "Sell 10",
				"Sell 50", "Examine");
	}


	public void addItem(Item item) {
		List<Item> list = new ArrayList<Item>(Arrays.asList(mainStock));
		ListIterator<Item> it = list.listIterator();
		while (it.hasNext()) {
			Item i = it.next();
			if (i == null) {
				it.remove();
			}
		}
		list.add(item);
		mainStock = list.toArray(mainStock);
	}

	public Item[] getMainStock() {
		return this.mainStock;
	}

	public void removeItem(Item item) {
		List<Item> list = new ArrayList<Item>(Arrays.asList(mainStock));
		ListIterator<Item> it = list.listIterator();
		while (it.hasNext()) {
			Item i = it.next();
			if (i == null) {
				it.remove();
			}
		}
		list.remove(item);
		mainStock = list.toArray(mainStock);
	}

	public int getSlotId(int clickSlotId) {
		return clickSlotId / 6;
	}

	public void buy(Player player, int slotId, int quantity) {
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage("There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		int amountCoins = player.getInventory().getItems().getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();
		CustomShops shops = CustomShops.getCustomShop(name);
		if (shops != null) {
			if (shops.buyCustomCurrency(player, item, buyQ)) {
				if (player.getTemporaryAttributtes().get("custom_shop_price") != null) {
					@SuppressWarnings("unused")
					int cost = (int) player.getTemporaryAttributtes().get("custom_shop_price");
					shops.onBuy(player, item, buyQ, (int) player.getTemporaryAttributtes().remove("custom_shop_price"));
//					FileLogger.getFileLogger().writeLog("shops/", player.getDisplayName() + " bought:[" + item.getAmount() + "x " + item.getName() + ", id=" + item.getId() + "] from " + getName() + " cost: " + cost, true);
					shops.updateShopCurrency(player);
				} else {
					System.out.println("Attempted to buy an item buy the shop price was null in attributes.");
				}
			}
			return;
		}
		boolean enoughCoins = maxQuantity >= buyQ;
		if (!enoughCoins) {
			player.getPackets().sendGameMessage("You don't have enough coins.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ) {
			player.getPackets().sendGameMessage("The shop has run out of stock.");
		}
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			player.getInventory().deleteItem(money, totalPrice);
			player.getInventory().addItem(item.getId(), buyQ);
			item.setAmount(item.getAmount() - buyQ);
			if (item.getAmount() <= 0 && slotId >= mainStock.length) {
				generalStock[slotId - mainStock.length] = null;
			}
			refreshShop();
			sendInventory(player);
		}
	}

	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null) {
				continue;
			}
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null) {
					continue;
				}
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0) {
					generalStock[i] = null;
				}
				needRefresh = true;
			}
		}
		if (needRefresh) {
			refreshShop();
		}
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null) {
					continue;
				}
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId) {
			return;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null) {
			return;
		}

		int originalId = item.getId();
		if (item.getDefinitions().isNoted()) {
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		}
		if (item.getDefinitions().isDestroyItem() || ItemConstants.getItemDefaultCharges(item.getId()) != -1 || !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		CustomShops shops = CustomShops.getCustomShop(name);
		if (shops != null) {
			player.getPackets().sendGameMessage("You cannot sell any items to this shop.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.getPackets().sendGameMessage("You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinitions.getItemDefinitions(item.getId()).getValue() / 4;
		int numberOff = player.getInventory().getItems().getNumberOf(originalId);
		if (quantity > numberOff) {
			quantity = numberOff;
		}
		if (!addItem(item.getId(), quantity)) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		player.getInventory().deleteItem(originalId, quantity);
		player.getInventory().addItem(money, price * quantity);
	}

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId) {
			return;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null) {
			return;
		}
		if (item.getDefinitions().isNoted()) {
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		}
		if (item.getDefinitions().isNoted() || !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.getPackets().sendGameMessage("You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinitions.getItemDefinitions(item.getId()).getValue() / 4;
		player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": shop will buy for: " + price + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ". Right-click the item to sell.");
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null) {
				continue;
			}
			if (mainStock[i].getId() == itemId) {
				return defaultQuantity[i];
			}
		}
		return 0;
	}

	public void sendInfo(Player player, int slotId) {
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		sendInfo(player, item);
//		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		CustomShops shops = CustomShops.getCustomShop(name);
		if (shops != null) {
			shops.sendCost(player, item);
		} else {
			player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": currently costs " + Utils.format(price) + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
		}
	}
	
	public static void sendInfo(Player player, Item item) {
		player.getInterfaceManager().sendInventoryInterface(449);
		player.getPackets().sendGlobalConfig(741, item.getId());
		player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.getPackets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = new int[18];
		ItemDefinitions defs = item.getDefinitions();
		bonuses[CombatDefinitions.STAB_ATTACK] += defs.getStabAttack();
		bonuses[CombatDefinitions.SLASH_ATTACK] += defs.getSlashAttack();
		bonuses[CombatDefinitions.CRUSH_ATTACK] += defs.getCrushAttack();
		bonuses[CombatDefinitions.MAGIC_ATTACK] += defs.getMagicAttack();
		bonuses[CombatDefinitions.RANGE_ATTACK] += defs.getRangeAttack();
		bonuses[CombatDefinitions.STAB_DEF] += defs.getStabDef();
		bonuses[CombatDefinitions.SLASH_DEF] += defs.getSlashDef();
		bonuses[CombatDefinitions.CRUSH_DEF] += defs.getCrushDef();
		bonuses[CombatDefinitions.MAGIC_DEF] += defs.getMagicDef();
		bonuses[CombatDefinitions.RANGE_DEF] += defs.getRangeDef();
		bonuses[CombatDefinitions.SUMMONING_DEF] += defs.getSummoningDef();
		bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS] += defs.getAbsorveMeleeBonus();
		bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS] += defs.getAbsorveMageBonus();
		bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS] += defs.getAbsorveRangeBonus();
		bonuses[CombatDefinitions.STRENGTH_BONUS] += defs.getStrengthBonus();
		bonuses[CombatDefinitions.RANGED_STR_BONUS] += defs.getRangedStrBonus();
		bonuses[CombatDefinitions.PRAYER_BONUS] += defs.getPrayerBonus();
		bonuses[CombatDefinitions.MAGIC_DAMAGE] += defs.getMagicDamage();
		boolean hasBonus = false;
		for (int bonus : bonuses)
			if (bonus != 0) {
				hasBonus = true;
				break;
			}
		if (hasBonus) {
			HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					if (skillId == Skills.ATTACK && level == 1) {
						continue;
					}
					if (skillId == Skills.FIREMAKING && level == 61) {
						continue;
					}
					boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
					reqsText += "<br>" + (hasReq ? "<col=00ff00>" : "<col=ff0000>") + "Level " + level + " " + Skills.SKILL_NAME[skillId];
				}
				player.getPackets().sendGlobalString(26, "<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.getPackets().sendGlobalString(26, "<br>Worn on yourself");
			player.getPackets().sendGlobalString(35, "<br>Attack<br><col=ffff00>+" + bonuses[CombatDefinitions.STAB_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SLASH_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.CRUSH_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.MAGIC_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.RANGE_ATTACK] + "<br><col=ffff00>---" + "<br>Strength" + "<br>Ranged Strength" + "<br>Magic Damage" + "<br>Absorve Melee" + "<br>Absorve Magic" + "<br>Absorve Ranged" + "<br>Prayer Bonus");
			player.getPackets().sendGlobalString(36, "<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.getPackets().sendGlobalString(52, "<<br>Defence<br><col=ffff00>+" + bonuses[CombatDefinitions.STAB_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SLASH_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.CRUSH_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.MAGIC_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.RANGE_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SUMMONING_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.STRENGTH_BONUS] + "<br><col=ffff00>" + bonuses[CombatDefinitions.RANGED_STR_BONUS] + "<br><col=ffff00>" + bonuses[CombatDefinitions.MAGIC_DAMAGE] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else {
			player.getPackets().sendGlobalString(26, "");
			player.getPackets().sendGlobalString(35, "");
			player.getPackets().sendGlobalString(36, "");
			player.getPackets().sendGlobalString(52, "");
		}

	}


	public int getBuyPrice(Item item, int dq) {
		int value = ItemDefinitions.getItemDefinitions(item.getId()).getValue();
		return value <= 0 ? 1 : value;
	}

	public int getSellPrice(Item item, int dq) {
		return (ItemDefinitions.getItemDefinitions(item.getId()).getValue() * (3 / 4));
	}

	public void sendExamine(Player player, int slotId) {
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendAccessMask(620, 25, 0, getStoreSize() * 6, 1150);
		}
	}

	public int getStoreSize() {
		return mainStock.length + (generalStock != null ? generalStock.length : 0);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length + (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null) {
			System.arraycopy(generalStock, 0, stock, mainStock.length, generalStock.length);
		}
		player.getPackets().sendItems(4, stock);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
