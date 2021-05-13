package com.rs.json.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.google.gson.reflect.TypeToken;
import com.rs.cache.Cache;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.Shop;
import com.rs.json.GsonHandler;
import com.rs.json.GsonLoader;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Nov 17, 2013
 */
public class ShopsLoader extends GsonLoader<Shop> {
	
	public static void main(String... args) {
		try {
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GsonHandler.initialize();
		
		ShopsLoader loader = GsonHandler.getJsonLoader(ShopsLoader.class);
		List<Shop> shops = loader.generateList();
		
		for(Shop shop : shops) {
			if (shop == null || shop.getName() == null)
				continue;
			if (!shop.getName().toLowerCase().contains("gold p"))
				continue;
			StringBuilder bldr = new StringBuilder();
			bldr.append(shop.getName() + ":\t");
			for (Item item : shop.getMainStock()) {
				if (item == null) {
					continue;
				}
				bldr.append("new Item(" + item.getId() + ", " + item.getAmount() + "), ");
			}
			System.out.println(bldr);
 		}
		
	}
 
	
	/**
	 * Restores the items in shops
	 */
	public void restoreShops() {
		Iterator<Entry<String, Shop>> it = shops.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Shop> entry = it.next();
			entry.getValue().restoreItems();
		}
	}

	/**
	 * Opens the shop for the player
	 *
	 * @param player
	 *            The player to open the shop for
	 * @param name
	 *            The name of the shop
	 * @return {@code true} if it was opened successfully
	 */
	public static boolean openShop(Player player, String name) {
		Shop shop = getShop(name);
		if (shop == null) {
			System.out.println("Attempted to open a shop by name: " + name);
			return false;
		}
		shop.addPlayer(player);
		return true;
	}

	/**
	 * Gets the shop that has a name matching the key
	 *
	 * @param key
	 *            The key to search for
	 * @return
	 */
	public static Shop getShop(String key) {
		Iterator<Entry<String, Shop>> it = getShops().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Shop> entry = it.next();
			if (entry.getValue().getName().equalsIgnoreCase(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Adds a shop to the list of shops
	 *
	 * @param shop
	 *            The shop to add
	 */
	public void addShopToDatabase(Shop shop) {
		List<Shop> list = load();
		list.add(shop);
		save(list);
	}

	/**
	 * The map of the shops
	 */
	private final static Map<String, Shop> shops = new HashMap<String, Shop>();

	public void sortShops() {
		sortedShops.clear();
		sortedShops.addAll(shops.values());
	}

	private final TreeSet<Shop> sortedShops = new TreeSet<>(new Comparator<Shop>() {

		@Override
		public int compare(Shop o1, Shop o2) {
			return o1.getName().compareTo(o2.getName());
		}
	});

	public TreeSet<Shop> getSortedShops() {
		return sortedShops;
	}

	public static Map<String, Shop> getShops() {
		return shops;
	}

	@Override
	public void initialize() {
		getShops().clear();
		List<Shop> shopList = load();
		if (shopList == null) {
			shopList = new ArrayList<>();
		}
		for (Shop shop : shopList) {
			if (shop == null) {
				continue;
			}
			shop.initialize();
			getShops().put(shop.getName(), shop);
		}
		sortShops();
	}

	@Override
	public String getFileLocation() {
		return "data/json/shops.json";
	}

	@Override
	public List<Shop> load() {
		List<Shop> autospawns = null;
		String json = null;
		try {
			File file = new File(getFileLocation());
			if (!file.exists()) {
				return null;
			}
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			json = new String(chars);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		autospawns = gson.fromJson(json, new TypeToken<List<Shop>>() {
		}.getType());
		return autospawns;
	}

}
