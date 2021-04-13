package com.rs.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.utils.Utils;

public class ItemBonusesPacker {

	public static final void main(String[] args) throws IOException {
		Cache.init();
		DataOutputStream out = new DataOutputStream(new FileOutputStream("data/items/bonuses.ib"));
		for (int itemId = 0; itemId < Utils.getItemDefinitionsSize(); itemId++) {
			File file = new File("bonuses/" + itemId + ".txt");
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				out.writeShort(itemId);
				reader.readLine();
				// att bonuses
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.readLine();
				// def bonuses
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.readLine();
				// Damage absorption
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.readLine();
				// Other bonuses
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.close();
				if (reader.readLine() != null)
					throw new RuntimeException("Should be null line" + itemId);
			}
		}
		out.flush();
		out.close();
	}

}
