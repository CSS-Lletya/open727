package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class UnbanAll {

	public static void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File dir = new File("./data/characters/");
		File[] accs = dir.listFiles();
		for (File acc : accs) {
			String name = Utils.formatPlayerNameForProtocol(acc.getName().replace(".p", ""));
			System.out.println(acc);
			if (Utils.containsInvalidCharacter(name)) {
				acc.delete();
				return;
			}
			try {
				Player player = (Player) SerializableFilesManager.loadSerializedFile(acc);
				for (String string : Settings.REMOVING_ITEMS) {
					for (Item item : player.getBank().getContainerCopy()) {
						if (item.getName().toLowerCase().contains(string)) {
							player.getBank().removeItem(item.getId());
						}
					}
					for (Item item : player.getBank().getContainerCopy()) {
						if (item.getName().toLowerCase().contains(string)) {
							player.getInventory().removeItems(item);
						}
					}
					for (Item item : player.getBank().getContainerCopy()) {
						if (item.getName().toLowerCase().contains(string)) {
							player.getInventory().removeItems(item);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
