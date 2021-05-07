package com.rs.json.impl;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.json.GsonHandler;
import com.rs.json.GsonLoader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public class ObjectSpawnLoader extends GsonLoader<ObjectSpawnLoader.ObjectSpawn> {

	@Override
	public void initialize() {
		List<ObjectSpawn> spawns = load();
		for (ObjectSpawn spawn : spawns) {
			List<ObjectSpawn> regionSpawns = null;

			/* Populating the region spawns or generating a new one if it doesnt exist */
			if (map.get(spawn.getTile().getRegionId()) == null) {
				regionSpawns = new ArrayList<>();
			} else {
				regionSpawns = map.get(spawn.getTile().getRegionId());
			}

			regionSpawns.add(spawn);
			map.put(spawn.getTile().getRegionId(), regionSpawns);
		}
	}

	@Override
	public String getFileLocation() {
		return "data/json/objectspawns.json";
	}

	@Override
	public List<ObjectSpawn> load() {
		List<ObjectSpawn> autospawns = null;
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
		autospawns = gson.fromJson(json, new TypeToken<List<ObjectSpawn>>() {
		}.getType());
		return autospawns;
	}

	public List<ObjectSpawn> getSpawns(int regionId) {
		return map.get(regionId);
	}

	private Map<Integer, List<ObjectSpawn>> map = new HashMap<>();

	public static final void loadObjectSpawns(int regionId) {
		GsonHandler.waitForLoad();
		ObjectSpawnLoader loader = GsonHandler.getJsonLoader(ObjectSpawnLoader.class);
		List<ObjectSpawn> spawns = loader.getSpawns(regionId);
		if (spawns == null) {
			return;
		}
		for (ObjectSpawn spawn : spawns) {
			WorldObject.createObject(new WorldObject(spawn.getId(), (short) spawn.getType(), (short) spawn.getRotation(), spawn.getX(), spawn.getY(), spawn.getPlane()), true);
		}
	}

	public static class ObjectSpawn {

		public ObjectSpawn(int id, int type, int rotation, int x, int y, int plane, boolean clip) {
			this.id = id;
			this.type = type;
			this.rotation = rotation;
			this.x = x;
			this.y = y;
			this.plane = plane;
			this.clip = clip;
		}

		private final int id;
		private final int type;
		private final int rotation;
		private final int x;
		private final int y;
		private final int plane;

		private final boolean clip;

		public WorldTile getTile() {
			return new WorldTile(getX(), getY(), getPlane());
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * @return the rotation
		 */
		public int getRotation() {
			return rotation;
		}

		/**
		 * @return the x
		 */
		public int getX() {
			return x;
		}

		/**
		 * @return the y
		 */
		public int getY() {
			return y;
		}

		/**
		 * @return the plane
		 */
		public int getPlane() {
			return plane;
		}

		/**
		 * @return the clip
		 */
		public boolean isClip() {
			return clip;
		}
	}

}
