package com.rs.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.rs.cache.Cache;
import com.rs.game.item.Item;
import com.rs.tools.RsWikiEquipSlotsDumper.EquipSlot.SlotType;

public class RsWikiEquipSlotsDumper {

	private static ArrayList<EquipSlot> slots = new ArrayList<EquipSlot>();

	private static boolean dumpEquipmentSlot(Item item) {
		if (item.getDefinitions() != null && item.getDefinitions().isWearItem() != true)
			return false;
		ArrayList<String> lines = getPage(item);
		if (lines == null) {
			return false;
		}
		Iterator<String> iterator = lines.iterator();
		try {
			while (iterator.hasNext()) {
				String line = iterator.next();
				if (line.startsWith("</th><th rowspan=\"3\" colspan=\"2\" width=\"30\" align=\"center\">")) {
					line = iterator.next();
					// System.out.println(line.indexOf("\"><img alt=\""));
					line = line.substring(line.indexOf("title=") + "title=".length() + 1,
							line.indexOf("\"><img alt=\""));
					EquipSlot ep = getEquipSlot(item.getId(), line);
					if (ep != null) {
						slots.add(ep);
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static EquipSlot getEquipSlot(int id, String line) {
		return new EquipSlot(id, SlotType.valueOf(line.toUpperCase()));
	}

	public static ArrayList<String> getPage(Item item) {
		try {
			WebPage page = new WebPage("http://runescape.wikia.com/wiki/" + item.getName());
			try {
				page.load();
			} catch (Exception e) {
				System.out.println("Invalid page: " + item.getId() + ", " + item.getName());
				return null;
			}
			return page.getLines();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 75; i++) {
			Item item = new Item(i, 1);
			if (dumpEquipmentSlot(item)) {
				System.out.println("Dumped Item " + item.getName());
			}
		}
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File("./slots.s")));
			for (EquipSlot slot : slots) {
				out.writeShort(slot.getId());
				out.write(slot.getType().toString().getBytes());
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class EquipSlot {

		private int id;
		private SlotType type;

		public EquipSlot(int id, SlotType type) {
			this.id = id;
			this.type = type;
		}

		public int getId() {
			return id;
		}

		public SlotType getType() {
			return type;
		}

		enum SlotType {
			WEAPON_SLOT, HEAD_SLOT, RING_SLOT, BODY_SLOT, LEGWEAR_SLOT, HANDS_SLOT, AURA_SLOT, FEET_SLOT, CAPE_SLOT, AMMUNITION_SLOT, NECK_SLOT, SLOT_SHIELD
		}
	}
}
