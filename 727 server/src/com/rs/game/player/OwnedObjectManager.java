package com.rs.game.player;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.task.Task;
import com.rs.utils.Utils;

public class OwnedObjectManager {

	public static final AtomicLong keyMaker = new AtomicLong();

	private static final Map<String, OwnedObjectManager> ownedObjects = new ConcurrentHashMap<String, OwnedObjectManager>();

	private Player player;
	private WorldObject[] objects;
	private short count;
	private long cycleTime;
	private long lifeTime;
	private String managerKey;

	public static void processAll() {
		for (OwnedObjectManager object : ownedObjects.values())
			object.process();
	}

	public static boolean isPlayerObject(Player player, WorldObject object) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext();) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX() && manager.getCurrentObject().getY() == object.getY()
					&& manager.getCurrentObject().getHeight() == object.getHeight()
					&& manager.getCurrentObject().getId() == object.getId())
				return true;
		}
		return false;
	}

	public static interface ConvertEvent {

		public boolean canConvert(Player player);

	}

	public static boolean convertIntoObject(WorldObject object, WorldObject toObject, ConvertEvent event) {
		for (OwnedObjectManager manager : ownedObjects.values()) {
			if (manager.getCurrentObject().getX() == toObject.getX()
					&& manager.getCurrentObject().getY() == toObject.getY()
					&& manager.getCurrentObject().getHeight() == toObject.getHeight()
					&& manager.getCurrentObject().getId() == object.getId()) {
				if (event != null && !event.canConvert(manager.player))
					return false;
				manager.convertIntoObject(toObject);
				return true;
			}
		}
		return false;
	}

	public static boolean removeObject(Player player, WorldObject object) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext();) {
			final OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX() && manager.getCurrentObject().getY() == object.getY()
					&& manager.getCurrentObject().getHeight() == object.getHeight()
					&& manager.getCurrentObject().getId() == object.getId()) {
				World.get().submit(new Task(0, true) {
					@Override
					protected void execute() {
						manager.delete();
						this.cancel();
					}
				});
				return true;
			}
		}
		return false;
	}

	public static void linkKeys(Player player) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext();) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			manager.player = player;
		}
	}

	public static void addOwnedObjectManager(Player player, WorldObject[] objects, long cycleTime) {
		new OwnedObjectManager(player, objects, cycleTime);
	}

	private OwnedObjectManager(Player player, WorldObject[] objects, long cycleTime) {
		managerKey = player.getUsername() + "_" + keyMaker.getAndIncrement();
		this.cycleTime = cycleTime;
		this.objects = objects;
		this.player = player;
		spawnObject();
		player.getOwnedObjectManagerKeys().add(managerKey);
		ownedObjects.put(managerKey, this);
	}

	public static int getObjectsforValue(Player player, int objectId) {
		int count = 0;
		for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext();) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getId() == objectId)
				count++;
		}
		return count;
	}

	public void reset() {
		for (OwnedObjectManager object : ownedObjects.values())
			object.delete();
	}

	public void resetLifeTime() {
		this.lifeTime = Utils.currentTimeMillis() + cycleTime;
	}

	public boolean forceMoveNextStage() {
		if (count != -1)
			destroyObject(objects[count]);
		count++;
		if (count == objects.length) {
			remove();
			return false;
		}
		spawnObject();
		return true;
	}

	private void spawnObject() {
		WorldObject.createObject(objects[count], true);
		resetLifeTime();
	}

	public void convertIntoObject(WorldObject object) {
		destroyObject(objects[count]);
		objects[count] = object;
		spawnObject();
	}

	private void remove() {
		ownedObjects.remove(managerKey);
		if (player != null)
			player.getOwnedObjectManagerKeys().remove(managerKey);
	}

	public void delete() {
		destroyObject(objects[count]);
		remove();
	}

	public void process() {
		if (Utils.currentTimeMillis() > lifeTime)
			forceMoveNextStage();
	}

	public WorldObject getCurrentObject() {
		return objects[count];
	}

	public void destroyObject(WorldObject object) {
		WorldObject.destroySpawnedObject(object);
	}

}
