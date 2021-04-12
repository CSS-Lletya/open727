package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.SerializableFilesManager;

public class SkillsReseter {

	public static int[] KEEP_SKILLS = { Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.HITPOINTS, Skills.MAGIC,
			Skills.RANGE, Skills.PRAYER, Skills.SUMMONING };

	public static boolean resetSkill(int id) {
		for (int s : KEEP_SKILLS)
			if (s == id)
				return false;
		return true;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// Cache.init();
		File[] chars = new File("data/characters").listFiles();
		for (File acc : chars) {
			try {
				Player player = (Player) SerializableFilesManager.loadSerializedFile(acc);
				player.getEquipment().getItems().removeAll(new Item(20771, Integer.MAX_VALUE));
				player.getEquipment().getItems().removeAll(new Item(20769, Integer.MAX_VALUE));
				player.getInventory().getItems().removeAll(new Item(10566, Integer.MAX_VALUE));
				player.getEquipment().getItems().removeAll(new Item(10566, Integer.MAX_VALUE));
				player.getBank().removeItem(10566);
				player.getInventory().getItems().removeAll(new Item(10637, Integer.MAX_VALUE));
				player.getEquipment().getItems().removeAll(new Item(10637, Integer.MAX_VALUE));
				player.getBank().removeItem(10637);
				player.getInventory().getItems().removeAll(new Item(23650, Integer.MAX_VALUE));
				player.getEquipment().getItems().removeAll(new Item(23650, Integer.MAX_VALUE));
				player.getBank().removeItem(23650);
				SerializableFilesManager.storeSerializableClass(player, acc);
			} catch (Throwable e) {
				e.printStackTrace();
				System.out.println("failed: " + acc.getName());
			}
		}
		System.out.println("Done.");
	}
}
