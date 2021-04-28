package com.rs.json.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rs.json.JsonLoader;

import npc.drops.Drop;
import npc.drops.DropManager;
import npc.drops.DropTable;

/**
 * The {@link JsonLoader} implementation that loads all npc drops.
 * @author lare96 <http://github.com/lare96>
 */
public final class MobDropTableLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link MobDropTableLoader}.
	 */
	public MobDropTableLoader() {
		super("./data/json/mob_drops.json");
	}
	
	/**
	 * A constant defined to write a new set of npc ids for the client.
	 */
	private final boolean OUTPUT = false;
	
	/**
	 * A set of written ids.
	 */
	private Set<Integer> written = new HashSet<>();
	
	/**
	 * The writer to write our ids.
	 */
	private DataOutputStream out;
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int[] array = builder.fromJson(reader.get("ids"), int[].class);
		Drop[] common = Objects.requireNonNull(builder.fromJson(reader.get("common"), Drop[].class));
		Drop[] rare = Objects.requireNonNull(builder.fromJson(reader.get("rare"), Drop[].class));
		int first = array[0];
		for(int i = 0; i < array.length; i++) {
			int id = array[i];
			if(id != first)
				DropManager.REDIRECTS.put(array[i], first);
			DropManager.getTables().put(array[i], new DropTable(common, rare));
		}
		
	}
	
	@Override
	public void start() {
		if(OUTPUT) {
			try {
				File out = new File("./mob_drops.dat");
				this.out = new DataOutputStream(new FileOutputStream(out));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void end() {
		if(out != null) {
			try {
				out.flush();
				out.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		written.clear();
	}
}