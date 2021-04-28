package com.rs.game;

import java.util.ArrayList;
import java.util.List;

import com.rs.cache.Cache;
import com.rs.game.player.Player;

import npc.NPC;

public final class RegionBuilder {

	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	/*
	 * build here the maps you wont edit again
	 */
	public static void init() {

		for (int mapX = 0; mapX < MAX_REGION_X; mapX++) {
			for (int mapY = 0; mapY < MAX_REGION_Y; mapY++) {
				if (Cache.STORE.getIndexes()[5].getArchiveId("m" + mapX + "_" + mapY) != -1)
					EXISTING_MAPS.add(getRegionHash(mapX, mapY));
			}
		}
		World.getRegion(7503, true);
		World.getRegion(7759, true);
		/*
		 * for(int i = 0; i < 2000; i++) {
		 * 
		 * int[] boundChuncks = RegionBuilder.findEmptyChunkBound( 8, 8); // reserves all map area RegionBuilder.cutMap(boundChuncks[0], boundChuncks[1], 8, 8, 0);
		 * 
		 * System.out.println(i+", "+Arrays.toString(boundChuncks)); }
		 */

	}

	public static int getRegion(int c) {
		return c >> 3;
	}

	/*
	 * do not use this out builder
	 */
	public static void noclipCircle(int x, int y, int plane, int ratio) throws InterruptedException {
		for (int xn = x - ratio; xn < x + ratio; xn++) {
			for (int yn = y - ratio; yn < y + ratio; yn++) {
				if (Math.pow(2, x - xn) + Math.pow(2, y - yn) <= Math.pow(2, ratio)) {
					int regionId = new WorldTile(xn, yn, 0).getRegionId();
					Region region = World.getRegion(regionId);
					int baseLocalX = xn - ((regionId >> 8) * 64);
					int baseLocalY = yn - ((regionId & 0xff) * 64);
					while (region.getLoadMapStage() != 2) { // blocks waiting
															// for load of
															// region to be come
						// System.out.println("nocliping: "+xn+", "+yn);
						Thread.sleep(1);
					}
//					System.out.println("nocliping: " + xn + ", " + yn + ", " + baseLocalX + ", " + baseLocalY);
//					System.out.println(region.forceGetRegionMap().getMasks()[plane][baseLocalX][baseLocalY]);
					region.forceGetRegionMap().setMask(plane, baseLocalX, baseLocalY, 0);
//					System.out.println(region.forceGetRegionMap().getMasks()[plane][baseLocalX][baseLocalY]);

					region.forceGetRegionMapClipedOnly().setMask(plane, baseLocalX, baseLocalY, 0);
				}
			}
		}
	}

	private static final Object ALGORITHM_LOCK = new Object();

	private static final List<Integer> EXISTING_MAPS = new ArrayList<Integer>();

	private static final int MAX_REGION_X = 127;
	private static final int MAX_REGION_Y = 255;

	public static int[] findEmptyRegionBound(int widthChunks, int heightChunks) {
		int regionHash = findEmptyRegionHash(widthChunks, heightChunks);
		return new int[] { (regionHash >> 8), regionHash & 0xff };
	}

	public static int[] findEmptyChunkBound(int widthChunks, int heightChunks) {
		int[] map = findEmptyRegionBound(widthChunks, heightChunks);
		map[0] *= 8;
		map[1] *= 8;
		return map;
	}

	public static int getRegionHash(int chunkX, int chunkY) {
		return (chunkX << 8) + chunkY;
	}

	public static int findEmptyRegionHash(int widthChunks, int heightChunks) {
		int regionsDistanceX = 1;
		while (widthChunks > 8) {
			regionsDistanceX += 1;
			widthChunks -= 8;
		}
		int regionsDistanceY = 1;
		while (heightChunks > 8) {
			regionsDistanceY += 1;
			heightChunks -= 8;
		}
		synchronized (ALGORITHM_LOCK) {
			for (int regionX = 1; regionX <= MAX_REGION_X - regionsDistanceX; regionX++) {
				skip: for (int regionY = 1; regionY <= MAX_REGION_Y - regionsDistanceY; regionY++) {
					int regionHash = getRegionHash(regionX, regionY); // map hash because skiping to next map up
					for (int checkRegionX = regionX - 1; checkRegionX <= regionX + regionsDistanceX; checkRegionX++) {
						for (int checkRegionY = regionY - 1; checkRegionY <= regionY
								+ regionsDistanceY; checkRegionY++) {
							int hash = getRegionHash(checkRegionX, checkRegionY);
							if (regionExists(hash))
								continue skip;

						}
					}
					reserveArea(regionX, regionY, regionsDistanceX, regionsDistanceY, false);
					return regionHash;
				}
			}
		}
		return -1;

	}

	public static void reserveArea(int fromRegionX, int fromRegionY, int width, int height, boolean remove) {
		for (int regionX = fromRegionX; regionX < fromRegionX + width; regionX++) {
			for (int regionY = fromRegionY; regionY < fromRegionY + height; regionY++) {
				if (remove)
					EXISTING_MAPS.remove((Integer) getRegionHash(regionX, regionY));
				else
					EXISTING_MAPS.add(getRegionHash(regionX, regionY));
			}
		}
	}

	public static boolean regionExists(int mapHash) {
		return EXISTING_MAPS.contains(mapHash);

	}

	public static void cutRegion(int chunkX, int chunkY, int plane) {
		DynamicRegion toRegion = createDynamicRegion((((chunkX / 8) << 8) + (chunkY / 8)));
		int offsetX = (chunkX - ((chunkX / 8) * 8));
		int offsetY = (chunkY - ((chunkY / 8) * 8));
		toRegion.getRegionCoords()[plane][offsetX][offsetY][0] = 0;
		toRegion.getRegionCoords()[plane][offsetX][offsetY][1] = 0;
		toRegion.getRegionCoords()[plane][offsetX][offsetY][2] = 0;
		toRegion.getRegionCoords()[plane][offsetX][offsetY][3] = 0;
	}

	public static final void destroyMap(int chunkX, int chunkY, int widthRegions, int heightRegions) {
		synchronized (ALGORITHM_LOCK) {
			int fromRegionX = chunkX / 8;
			int fromRegionY = chunkY / 8;
			int regionsDistanceX = 1;
			while (widthRegions > 8) {
				regionsDistanceX += 1;
				widthRegions -= 8;
			}
			int regionsDistanceY = 1;
			while (heightRegions > 8) {
				regionsDistanceY += 1;
				heightRegions -= 8;
			}
			for (int regionX = fromRegionX; regionX < fromRegionX + regionsDistanceX; regionX++) {
				for (int regionY = fromRegionY; regionY < fromRegionY + regionsDistanceY; regionY++) {
					destroyRegion(getRegionHash(regionX, regionY));
				}
			}
			reserveArea(fromRegionX, fromRegionY, regionsDistanceX, regionsDistanceY, true);
		}
	}

	public static final void repeatMap(int toChunkX, int toChunkY, int widthChunks, int heightChunks, int rx, int ry,
			int plane, int rotation, int... toPlanes) {
		for (int xOffset = 0; xOffset < widthChunks; xOffset++) {
			for (int yOffset = 0; yOffset < heightChunks; yOffset++) {
				int nextChunkX = toChunkX + xOffset;
				int nextChunkY = toChunkY + yOffset;
				DynamicRegion toRegion = createDynamicRegion((((nextChunkX / 8) << 8) + (nextChunkY / 8)));
				int regionOffsetX = (nextChunkX - ((nextChunkX / 8) * 8));
				int regionOffsetY = (nextChunkY - ((nextChunkY / 8) * 8));
				for (int pIndex = 0; pIndex < toPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = rx;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = ry;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = plane;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
					World.getRegion((((rx / 8) << 8) + (ry / 8)), true);
				}
			}
		}
	}

	public static final void cutMap(int toChunkX, int toChunkY, int widthChunks, int heightChunks, int... toPlanes) {
		for (int xOffset = 0; xOffset < widthChunks; xOffset++) {
			for (int yOffset = 0; yOffset < heightChunks; yOffset++) {
				int nextChunkX = toChunkX + xOffset;
				int nextChunkY = toChunkY + yOffset;
				DynamicRegion toRegion = createDynamicRegion((((nextChunkX / 8) << 8) + (nextChunkY / 8)));
				int regionOffsetX = (nextChunkX - ((nextChunkX / 8) * 8));
				int regionOffsetY = (nextChunkY - ((nextChunkY / 8) * 8));
				for (int pIndex = 0; pIndex < toPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = 0;
				}
			}
		}
	}

	/*
	 * copys a single 8x8 map tile and allows you to rotate it
	 */
	public static void copyChunk(int fromChunkX, int fromChunkY, int fromPlane, int toChunkX, int toChunkY, int toPlane,
			int rotation) {
		DynamicRegion toRegion = createDynamicRegion((((toChunkX / 8) << 8) + (toChunkY / 8)));
		int regionOffsetX = (toChunkX - ((toChunkX / 8) * 8));
		int regionOffsetY = (toChunkY - ((toChunkY / 8) * 8));
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromChunkX;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromChunkY;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlane;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
		World.getRegion((((fromChunkY / 8) << 8) + (fromChunkX / 8)), true);
	}

	/*
	 * copy a exactly square of map from a place to another
	 */
	public static final void copyAllPlanesMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY,
			int ratio) {
		int[] planes = new int[4];
		for (int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, planes, planes);
	}

	/*
	 * copy a exactly square of map from a place to another
	 */
	public static final void copyAllPlanesMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY,
			int widthRegions, int heightRegions) {
		int[] planes = new int[4];
		for (int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, widthRegions, heightRegions, planes, planes);
	}

	/*
	 * copy a square of map from a place to another
	 */
	public static final void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio,
			int[] fromPlanes, int[] toPlanes) {
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, fromPlanes, toPlanes);
	}

	/*
	 * copy a rectangle of map from a place to another
	 */
	public static final void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int widthRegions,
			int heightRegions, int[] fromPlanes, int[] toPlanes) {
		if (fromPlanes.length != toPlanes.length)
			throw new RuntimeException("PLANES LENGTH ISNT SAME OF THE NEW PLANES ORDER!");
		for (int xOffset = 0; xOffset < widthRegions; xOffset++) {
			for (int yOffset = 0; yOffset < heightRegions; yOffset++) {
				int fromThisRegionX = fromRegionX + xOffset;
				int fromThisRegionY = fromRegionY + yOffset;
				int toThisRegionX = toRegionX + xOffset;
				int toThisRegionY = toRegionY + yOffset;
				int regionId = ((toThisRegionX / 8) << 8) + (toThisRegionY / 8);
				DynamicRegion toRegion = createDynamicRegion(regionId);
				int regionOffsetX = (toThisRegionX - ((toThisRegionX / 8) * 8));
				int regionOffsetY = (toThisRegionY - ((toThisRegionY / 8) * 8));
				for (int pIndex = 0; pIndex < fromPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromThisRegionX;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromThisRegionY;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlanes[pIndex];
					World.getRegion((regionId), true);
				}
			}
		}
	}

	/*
	 * temporary and used for dungeonnering only
	 * 
	 * //rotation 0 // a b // c d //rotation 1 // c a // d b //rotation2 // d c // b a //rotation3 // b d // a c
	 */
	public static final void copy2RatioSquare(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY,
			int rotation) {
		if (rotation == 0) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX + 1, toRegionY, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
		} else if (rotation == 1) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX, toRegionY, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX + 1, toRegionY, 0, rotation);
		} else if (rotation == 2) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX + 1, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX, toRegionY, 0, rotation);
		} else if (rotation == 3) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX + 1, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX, toRegionY + 1, 0, rotation);
		}
	}

	/*
	 * not recommended to use unless you want to make a more complex map
	 */
	public static DynamicRegion createDynamicRegion(int regionId) {
		synchronized (ALGORITHM_LOCK) {
			Region region = World.getRegions().get(regionId);
			if (region != null) {
				if (region instanceof DynamicRegion) // if its already dynamic lets
														// keep building it
					return (DynamicRegion) region;
				else
					destroyRegion(regionId);
			}
			DynamicRegion newRegion = new DynamicRegion(regionId);
			World.getRegions().put(regionId, newRegion);
			return newRegion;
		}
	}

	/*
	 * Safely destroys a dynamic region
	 */
	public static void destroyRegion(int regionId) {
		Region region = World.getRegions().get(regionId);
		if (region != null) {
			List<Integer> playerIndexes = region.getPlayerIndexes();
			List<Integer> npcIndexes = region.getNPCsIndexes();
			if (region.getFloorItems() != null)
				region.getFloorItems().clear();
			if (region.getSpawnedObjects() != null)
				region.getSpawnedObjects().clear();
			if (region.getRemovedObjects() != null)
				region.getRemovedObjects().clear();
			if (npcIndexes != null) {
				for (int npcIndex : npcIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null)
						continue;
					npc.finish();
				}
			}
			World.getRegions().remove(regionId);

			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || !player.hasStarted() || player.hasFinished())
						continue;
					player.setForceNextMapLoadRefresh(true);
					player.loadMapRegions();
				}
			}
		}
	}

	private RegionBuilder() {

	}
}
