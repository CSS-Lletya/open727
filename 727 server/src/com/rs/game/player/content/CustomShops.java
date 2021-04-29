package com.rs.game.player.content;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.ChatColors;
import com.rs.utils.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 31, 2014
 */
public enum CustomShops {

	VOTE("Vote_Exchange") {
		@Override
		public void sendCost(Player player, Item item) {
			sendCustomCost(player, ShopPrices.VOTE_SHOP, item, "Vote Tokens");
		}

		@Override
		public boolean buyCustomCurrency(Player player, Item item, int buyQuantity) {
			return buyWith(player, "Vote Tokens", item, buyQuantity, player.getInventory().getNumerOf(10944), ShopPrices.VOTE_SHOP);
		}

		@Override
		public boolean onBuy(Player player, Item item, int buyQuantity, int customPrice) {
			if (player.getInventory().addItem(item.getId(), item.getId() == 18831 ? item.getAmount() : buyQuantity)) {
				player.getInventory().deleteItem(10944, customPrice);
				return true;
			}
			return false;
		}
	};

	CustomShops(String[] name) {
		this.name = name;
	}

	CustomShops(String name) {
		this.name = new String[] { name };
	}

	private final String[] name;

	/**
	 * Sends the custom cost of the item
	 * 
	 * @param player
	 *            The player
	 * @param item
	 *            The item
	 */
	public abstract void sendCost(Player player, Item item);

	/**
	 * Buys an item with the custom currency
	 * 
	 * @param player
	 *            The player
	 * @param item
	 *            The item
	 * @param buyQuantity
	 *            The amount to buy
	 * @return
	 */
	public abstract boolean buyCustomCurrency(Player player, Item item, int buyQuantity);

	/**
	 * What to do when the item is bought
	 * 
	 * @param player
	 *            The player
	 * @param item
	 *            The item bought
	 * @param buyQuantity
	 *            The amount bought
	 * @param customPrice
	 *            The custom price for the item
	 */
	public abstract boolean onBuy(Player player, Item item, int buyQuantity, int customPrice);

	/**
	 * Updates the currency of the item you have in the shop
	 * 
	 * @param player
	 *            The player
	 */
	public void updateShopCurrency(Player player) {
	}

	/**
	 * Sends the custom cost of the item
	 * 
	 * @param player
	 *            The player
	 * @param items
	 *            The items array
	 * @param item
	 *            The item
	 * @param name
	 *            The currency name
	 */
	protected void sendCustomCost(Player player, int[][] items, Item item, String name) {
		boolean found = false;
		for (int[] item2 : items) {
			if (item.getId() == item2[0]) {
				player.getPackets().sendGameMessage("<col=" + ChatColors.BLUE + ">" + (item.getAmount() == 5000 || item.getAmount() == 50_000 ? 1 : item.getAmount()) + "x " + item.getName() + "</col>: currently costs: <col=" + ChatColors.MAROON + ">" + Utils.format(item2[1]) + " " + name + ".");
				found = true;
			}
		}
		if (!found) {
			player.getPackets().sendGameMessage(item.getId() + " ~" + item.getName() + " wasnt registered.");
			System.err.println(item.getId() + " ~" + item.getName() + " wasnt registered.");
		}
	}

	/**
	 * Buys the item with the custom currency
	 * 
	 * @param player
	 *            The player
	 * @param name
	 *            The name of the currency
	 * @param item
	 *            The item bought
	 * @param buyQuantity
	 *            The amount of the item bought
	 * @param points
	 *            The points used
	 * @param prices
	 *            The array of prices
	 * @return
	 */
	protected boolean buyWith(Player player, String name, Item item, int buyQuantity, int points, int[][] prices) {
		for (int[] price : prices) {
			if (item.getId() == price[0]) {
				int iPrice = price[1] * buyQuantity;
				boolean shouldPurchaseQuantity = (name.equalsIgnoreCase("Wilderness Points") && item.getId() != 12852) || item.getId() == 18831;
				if (shouldPurchaseQuantity) {
					iPrice = price[1] * 1;
					buyQuantity = item.getAmount();
				}
				if (buyQuantity < 1 || iPrice < 1) {
					player.getPackets().sendGameMessage("You need to buy more than 0 of that!");
					return false;
				}
				if (points < iPrice) {
					player.getPackets().sendGameMessage("You need <col=" + ChatColors.MAROON + ">" + Utils.format(iPrice - points) + "</col> more " + name + " to purchase <col=" + ChatColors.MAROON + ">" + buyQuantity + "</col> " + item.getName().toLowerCase() + ".");
					return false;
				} else {
					player.getTemporaryAttributtes().put("custom_shop_price", iPrice);
					player.getPackets().sendGameMessage("You buy <col=" + ChatColors.MAROON + ">" + Utils.format(buyQuantity) + "</col> " + item.getName() + "" + (item.getName().endsWith("s") ? "" : "s") + " from the " + this.name[0]);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the custom shop by the name
	 * 
	 * @param name
	 *            The name to search by
	 * @return A {@link #CustomShops()} {@code Object}
	 */
	public static CustomShops getCustomShop(String name) {
		for (CustomShops shop : CustomShops.values()) {
			for (String shopName : shop.name) {
				if (shopName.equalsIgnoreCase(name.replaceAll(" ", "_"))) {
					return shop;
				}
			}
		}
		return null;
	}	
}