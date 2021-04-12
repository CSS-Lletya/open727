package com.rs.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.utils.Utils;

public class MySQLDumpLists {

	public static void main(String[] args) {
		try {
			System.out.println("Dumping...");
			Cache.init();
			dumpItems();
			dumpNPCs();
			dumpObjects();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void dumpItems() throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new FileWriter("C:/Users/Nick Hartskeerl/Desktop/RsPsCoding V2.0/includes/data/itemdb.sql", true));
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			String name = ItemDefinitions.getItemDefinitions(i).getName();
			if (name == null)
				continue;
			System.out.println("Item: " + i + ", name: " + name + "");
			writer.write("INSERT INTO `itemdb` (`id`, `name`) VALUES (" + i + ", '" + name.replaceAll("'", "") + "');");
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	public static void dumpNPCs() throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new FileWriter("C:/Users/Nick Hartskeerl/Desktop/RsPsCoding V2.0/includes/data/npcdb.sql", true));
		for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
			String name = NPCDefinitions.getNPCDefinitions(i).name;
			if (name == null)
				continue;
			System.out.println("NPC: " + i + ", name: " + name + "");
			writer.write("INSERT INTO `npcdb` (`id`, `name`) VALUES (" + i + ", '" + name.replaceAll("'", "") + "');");
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	public static void dumpObjects() throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new FileWriter("C:/Users/Nick Hartskeerl/Desktop/RsPsCoding V2.0/includes/data/objectdb.sql", true));
		for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
			String name = ObjectDefinitions.getObjectDefinitions(i).name;
			if (name == null)
				continue;
			System.out.println("Object: " + i + ", name: " + name + "");
			writer.write(
					"INSERT INTO `objectdb` (`id`, `name`) VALUES (" + i + ", '" + name.replaceAll("'", "") + "');");
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
}
