package com.rs.tools;

import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.utils.Utils;

public class ObjectCheck {

	public static void main(String[] args) throws IOException {
		Cache.init();
		for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
			ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(i);
			if (def.containsOption("Steal-from")) {
				System.out.println(def.id + " - " + def.name);
			}
		}
	}

}
