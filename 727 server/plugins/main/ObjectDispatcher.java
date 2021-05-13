package main;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.route.strategy.RouteEvent;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

/**
 * @author Dennis
 */
public final class ObjectDispatcher {
	
	/**
	 * The object map which contains all the Objects on the world.
	 */
	private static final Object2ObjectArrayMap<ObjectSignature, ObjectType> OBJECTS = new Object2ObjectArrayMap<>();
	
	/**
	 * Executes the specified Objects if it's registered.
	 * @param player the player executing the Objects.
	 * @param parts the string which represents a Objects.
	 */
	public static void execute(Player player, WorldObject object, int optionId) {
		Optional<ObjectType> objects = getObject(object, object.getId());
		
		if(!objects.isPresent()) {
			player.getPackets().sendGameMessage("Object: " + object.getId() + " is not handled yet.");
			return;
		}
		try {
			objects.get().execute(player, object, optionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a Objects which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<ObjectType> getObject(WorldObject object, int objectId) {
		for(Entry<ObjectSignature, ObjectType> objects : OBJECTS.entrySet()) {
			if (isObjetId(objects.getValue(), objectId) || isObjectNamed(objects.getValue(), object)) {
				return Optional.of(objects.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Checks if the the Object Id matches the signature
	 * @param object
	 * @param objectId
	 * @return
	 */
	private static boolean isObjetId(ObjectType object, int objectId) {
		Annotation annotation = object.getClass().getAnnotation(ObjectSignature.class);
		ObjectSignature signature = (ObjectSignature) annotation;
		return Arrays.stream(signature.objectId()).anyMatch(right -> objectId == right);
	}
	
	/**
	 * Checks if the the Object Name matches the signature
	 * @param object
	 * @param objectId
	 * @return
	 */
	private static boolean isObjectNamed(ObjectType object, WorldObject worldObject) {
		Annotation annotation = object.getClass().getAnnotation(ObjectSignature.class);
		ObjectSignature signature = (ObjectSignature) annotation;
		return Arrays.stream(signature.name()).anyMatch(objectName -> worldObject.getDefinitions().getName().contains(objectName));
	}
	
	/**
	 * Loads all the Objects into the {@link #OBJECTS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<ObjectType> objectTypes = Utils.getClassesInDirectory("main.impl.objects").stream().map(clazz -> (ObjectType) clazz).collect(Collectors.toList());
		
		for(ObjectType object : objectTypes) {
			if(object.getClass().getAnnotation(ObjectSignature.class) == null) {
				throw new IncompleteAnnotationException(ObjectSignature.class, object.getClass().getName() + " has no annotation.");
			}
			OBJECTS.put(object.getClass().getAnnotation(ObjectSignature.class), object);
		}
	}
	
	/**
	 * Reloads all the Objects into the {@link #OBJECTS} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the commands in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		OBJECTS.clear();
		load();
	}
	
	public static void handleOption(final Player player, InputStream stream, int option) {
//		System.out.println("option " + option);
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;



		int id = getObjectIDFromStreamCopy(stream);

		int y = stream.readUnsignedShort();
		int x = stream.readUnsignedShort();
		boolean forceRun = stream.readUnsignedShort() == 1;
		if(id == -1)
			id = stream.readUnsignedShort();

		if (Settings.DEBUG)
			System.out.println(x+", "+ y +", "+id +", "+forceRun);
		
		int rotation = 0;
		if (player.isAtDynamicRegion()) {
			rotation = World.getRotation(player.getHeight(), x, y);
			if (rotation == 1) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
				y += defs.getSizeY() - 1;
			} else if (rotation == 2) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
				x += defs.getSizeY() - 1;
			}
		}
		final WorldTile tile = new WorldTile(x, y, player.getHeight());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);

		if (mapObject == null || mapObject.getId() != id)
			return;
		if (player.isAtDynamicRegion() && World.getRotation(player.getHeight(), x, y) != 0) { // temp fix
			ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
			if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
				for (int xs = 0; xs < defs.getSizeX() + 1 && (mapObject == null || mapObject.getId() != id); xs++) {
					for (int ys = 0; ys < defs.getSizeY() + 1 && (mapObject == null || mapObject.getId() != id); ys++) {
						tile.setLocation(x + xs, y + ys, tile.getHeight());
						mapObject = World.getRegion(regionId).getObject(id, tile);
					}
				}
			}
			if (mapObject == null || mapObject.getId() != id)
				return;
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(), (byte) (mapObject.getRotation() + rotation % 4), x, y,
						player.getHeight());
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		
		if (option == -1) {
			handleOptionExamine(player, object);
			return;
		}


		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				ObjectDispatcher.execute(player, object, option);
			}
		}, false));
	}

	private static int getObjectIDFromStreamCopy(InputStream stream) {
		try {
			InputStream stream2;
			stream2 = (InputStream) stream.clone();
			stream2.readInt();
			return stream2.readInt();
		} catch(Exception e) {
			return -1;
		}
	}

	private static void handleOptionExamine(final Player player, final WorldObject object) {
		if (Settings.DEBUG) {
			int offsetX = object.getX() - player.getX();
			int offsetY = object.getY() - player.getY();
			System.out.println("Offsets" + offsetX + " , " + offsetY);
		}
		player.getPackets().sendGameMessage("It's an " + object.getDefinitions().name + ".");
		if (Settings.DEBUG)
			if (Settings.DEBUG)

				Logger.log("ObjectHandler",
						"examined object id : " + object.getId() + ", " + object.getX() + ", " + object.getY() + ", "
								+ object.getHeight() + ", " + object.getType() + ", " + object.getRotation() + ", "
								+ object.getDefinitions().name);
	}
	
	@SuppressWarnings("unused")
	public static void handleItemOnObject(final Player player, final WorldObject object, final int interfaceId,
			final Item item) {
		final int itemId = item.getId();
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.faceObject(object);
				
					if (Settings.DEBUG)
						System.out.println("Item on object: " + object.getId());
				}
			
		}, false));
	}
}