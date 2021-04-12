package com.rs.tools;

import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;

public class PotionCreator {

	public static void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int amt = 1;
		String lastName = null;
		@SuppressWarnings("unused")
		String modified = null;
		for (int i = 23100; i < Utils.getItemDefinitionsSize(); i++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
			String name = def.getName();
			lastName = name;
			modified = lastName.replace(" ", "_").replace("(6)", "").replace("(5)", "").replace("(4)", "")
					.replace("(3)", "").replace("(2)", "").replace("(1)", "").toUpperCase();
			if (name.contains("flask") && !name.matches(lastName) && !def.isNoted()) {
				System.out.print(amt == 6 ? i + "\n\n" : i + ", ");
				if (amt == 6) {
					amt = 0;
				}
				amt++;
			}

		}
	}
}
