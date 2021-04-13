package com.rs.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.json.impl.NPCAutoSpawn;
import com.rs.json.impl.ObjectSpawnLoader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 1, 2014
 */
public class GsonHandler {

	/**
	 * Initializes all json loaders
	 */
	public static void initialize() {
		try {
			addJsonLoaders();
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		try {
			for (GsonLoader<?> loader : CLASSES) {
				loader.initialize();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOADED = Boolean.TRUE;
	}

	/**
	 * Waits for the json loaders to be loaded
	 */
	public static void waitForLoad() {
		while (!LOADED) {
			System.out.flush();
		}
	}

	/**
	 * Adds all json loaders to the map
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void addJsonLoaders() throws InstantiationException, IllegalAccessException {
		CLASSES.add(NPCAutoSpawn.class.newInstance());
		CLASSES.add(ObjectSpawnLoader.class.newInstance());
	}

	/**
	 * Gets a {@link #JsonLoader} by the class
	 * 
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getJsonLoader(Class<?> clazz) {
		try {
			GsonLoader<?> loader = CACHED_LOADERS.get(clazz.getSimpleName());
			if (loader != null) {
				return (T) loader;
			} else {
				for (GsonLoader<?> listLoader : CLASSES) {
					if (listLoader.getClass().getSimpleName().equals(clazz.getSimpleName())) {
						CACHED_LOADERS.put(listLoader.getClass().getSimpleName(), listLoader);
						return (T) listLoader;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * If all loaders have loaded
	 */
	public static boolean LOADED = Boolean.FALSE;

	/** The cached loaders */
	private static Map<String, GsonLoader<?>> CACHED_LOADERS = new HashMap<String, GsonLoader<?>>();

	/**
	 * Adds all of the loaders to the map
	 */
	private static final List<GsonLoader<?>> CLASSES = new ArrayList<GsonLoader<?>>();
}
