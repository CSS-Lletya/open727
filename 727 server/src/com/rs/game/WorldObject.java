package com.rs.game;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.Logger;

/**
 * A container class for a World Object.
 * @author Dennis
 *
 */
public class WorldObject extends WorldTile {

	/**
	 * The World Object Id.
	 */
	private final int id;
	
	/**
	 * The World Object Type.
	 */
	private final short type;
	
	/**
	 * The World Object Rotation.
	 */
	private short rotation;
	
	/**
	 * The World Object Life (like mining a Shooting star, it's life depletes when
	 * you get stardust, such.).
	 */
	private short life;

	/**
	 * Create a new World Object.
	 * @param id
	 * @param type
	 * @param rotation
	 * @param tile
	 */
	public WorldObject(int id, short type, short rotation, WorldTile tile) {
		super(tile.getX(), tile.getY(), tile.getHeight());
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	/**
	 * Create a new World Object.
	 * @param id
	 * @param type
	 * @param rotation
	 * @param x
	 * @param y
	 * @param plane
	 */
	public WorldObject(int id, short type, short rotation, int x, int y, int plane) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	/**
	 * Create a new World Object.
	 * @param id
	 * @param type
	 * @param rotation
	 * @param x
	 * @param y
	 * @param plane
	 * @param life
	 */
	public WorldObject(int id, short type, short rotation, int x, int y, int plane, short life) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = life;
	}

	/**
	 * Create a new World Object.
	 * @param object
	 */
	public WorldObject(WorldObject object) {
		super(object.getX(), object.getY(), object.getHeight());
		this.id = object.id;
		this.type = object.type;
		this.rotation = object.rotation;
		this.life = object.life;
	}

	/**
	 * Gets the World Object Id.
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the World Object Type.
	 * @return type
	 */
	public short getType() {
		return type;
	}

	/**
	 * Gets the World Object Rotation.
	 * @return rotation
	 */
	public short getRotation() {
		return rotation;
	}

	/**
	 * Sets the World Object Rotation
	 * @param rotation
	 */
	public void setRotation(byte rotation) {
		this.rotation = rotation;
	}

	/**
	 * Gets the World Objets Life.
	 * @return life
	 */
	public short getLife() {
		return life;
	}

	/**
	 * Sets the World Object Life.
	 * @param life
	 */
	public void setLife(short life) {
		this.life = life;
	}

	/**
	 * Removes Life points from a 
	 * World Object.
	 */
	public void decrementObjectLife() {
		this.life--;
	}

	/**
	 * Gets the World Object Definition
	 * @return definition
	 */
	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getObjectDefinitions(getId());
	}
	
	/*
	 * by default doesnt changeClipData
	 */
	public static final void createTemporaryObject(final WorldObject object, long time) {
		createTemporaryObject(object, time, false);
	}

	public static final void createTemporaryObject(final WorldObject object, long time, final boolean clip) {
		final int regionId = object.getRegionId();
		WorldObject realMapObject = World.getRegion(regionId).getRealObject(object);
		// remakes object, has to be done because on static region coords arent
		// same of real
		final WorldObject realObject = realMapObject == null ? null : new WorldObject(realMapObject.getId(), realMapObject.getType(), realMapObject.getRotation(), object.getX(), object.getY(), object.getHeight());
		createObject(object, clip);
		final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		if (realObject != null && clip)
			World.getRegion(regionId).removeMapObject(realObject, baseLocalX, baseLocalY);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					World.getRegion(regionId).removeObject(object);
					if (clip) {
						World.getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
						if (realObject != null) {
							int baseLocalX = object.getX() - ((regionId >> 8) * 64);
							int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
							World.getRegion(regionId).addMapObject(realObject, baseLocalX, baseLocalY);
						}
					}
					World.players().filter(p -> !p.getMapRegionsIds().contains(regionId)).forEach(p -> {
						if (realObject != null)
							p.getPackets().sendSpawnedObject(realObject);
						else
							p.getPackets().sendDestroyObject(object);
					});
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static final boolean isSpawnedObject(WorldObject object) {
		final int regionId = object.getRegionId();
		WorldObject spawnedObject = World.getRegion(regionId).getSpawnedObject(object);
		if (spawnedObject != null && object.getId() == spawnedObject.getId())
			return true;
		return false;
	}

	public static final boolean removeTemporaryObject(final WorldObject object, long time, final boolean clip) {
		final int regionId = object.getRegionId();
		// remakes object, has to be done because on static region coords arent
		// same of real
		final WorldObject realObject = object == null ? null : new WorldObject(object.getId(), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getHeight());
		removeObject(object, clip);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					World.getRegion(regionId).removeRemovedObject(object);
					if (clip) {
						int baseLocalX = object.getX() - ((regionId >> 8) * 64);
						int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
						World.getRegion(regionId).addMapObject(realObject, baseLocalX, baseLocalY);
					}
					World.players().filter(p -> !p.getMapRegionsIds().contains(regionId)).forEach(p -> p.getPackets().sendSpawnedObject(realObject));
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);

		return true;
	}

	public static final void removeObject(WorldObject object, boolean clip) {
		int regionId = object.getRegionId();
		World.getRegion(regionId).addRemovedObject(object);
		if (clip) {
			int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			World.getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
		}
		synchronized (World.getPlayers()) {
			World.players().forEach(p -> p.getPackets().sendDestroyObject(object));
		}
	}

	public static final WorldObject getObject(WorldTile tile) {
		int regionId = tile.getRegionId();
		int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return World.getRegion(regionId).getObject(tile.getHeight(), baseLocalX, baseLocalY);
	}

	public static final WorldObject getObject(WorldTile tile, int type) {
		int regionId = tile.getRegionId();
		int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return World.getRegion(regionId).getObject(tile.getHeight(), baseLocalX, baseLocalY, type);
	}

	public static final void createObject(WorldObject object, boolean clip) {
		int regionId = object.getRegionId();
		World.getRegion(regionId).addObject(object);
		if (clip) {
			int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			World.getRegion(regionId).addMapObject(object, baseLocalX, baseLocalY);
		}
		synchronized (World.getPlayers()) {
			World.players().filter(p -> !p.getMapRegionsIds().contains(regionId)).forEach(p -> p.getPackets().sendSpawnedObject(object));
		}
	}

	public static final void sendObjectAnimation(WorldObject object, Animation animation) {
		sendObjectAnimation(null, object, animation);
	}

	public static final void sendObjectAnimation(Entity creator, WorldObject object, Animation animation) {
		if (creator == null) {
			World.players().filter(p -> p.withinDistance(object)).forEach(p-> p.getPackets().sendObjectAnimation(object, animation));
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
				if (playersIndexes == null)
					continue;
				for (Integer playerIndex : playersIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || !player.hasStarted() || player.hasFinished() || !player.withinDistance(object))
						continue;
					player.getPackets().sendObjectAnimation(object, animation);
				}
			}
		}
	}
	
	public static void destroySpawnedObject(WorldObject object, boolean clip) {
		int regionId = object.getRegionId();
		int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		WorldObject realMapObject = World.getRegion(regionId).getRealObject(object);

		World.getRegion(regionId).removeObject(object);
		if (clip)
			World.getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
		World.players().filter(p -> !p.getMapRegionsIds().contains(regionId)).forEach(p -> {
			if (realMapObject != null)
				p.getPackets().sendSpawnedObject(realMapObject);
			else
				p.getPackets().sendDestroyObject(object);
		});
	}

	public static void destroySpawnedObject(WorldObject object) {
		int regionId = object.getRegionId();
		int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		World.getRegion(regionId).removeObject(object);
		World.getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
		World.players().filter(p -> !p.getMapRegionsIds().contains(regionId)).forEach(p -> p.getPackets().sendDestroyObject(object));
	}

	public static final void spawnTempGroundObject(final WorldObject object, final int replaceId, long time) {
		final int regionId = object.getRegionId();
		WorldObject realMapObject = World.getRegion(regionId).getRealObject(object);
		final WorldObject realObject = realMapObject == null ? null : new WorldObject(realMapObject.getId(), realMapObject.getType(), realMapObject.getRotation(), object.getX(), object.getY(), object.getHeight());
		WorldObject.createObject(object, false);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					World.getRegion(regionId).removeObject(object);
					FloorItem.createGroundItem(new Item(replaceId), object, null, false, 180, false);
					World.players().filter(p -> p.getHeight() != object.getHeight() || !p.getMapRegionsIds().contains(regionId)).forEach(p -> {
						if (realObject != null)
							p.getPackets().sendSpawnedObject(realObject);
						else
							p.getPackets().sendDestroyObject(object);
					});
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, time, TimeUnit.MILLISECONDS);
	}
	
	public static final void spawnObject(WorldObject object) {
		World.getRegion(object.getRegionId()).addObject(object, object.getHeight(), object.getXInRegion(),
				object.getYInRegion());
	}
}