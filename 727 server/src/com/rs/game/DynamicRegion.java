package com.rs.game;

import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.utils.Logger;

public class DynamicRegion extends Region {

	// int dynamicregion squares amt
	// Region[] array with the region squares
	// int[][] squaresBounds;

	private int[][][][] regionCoords;

	private RegionMap removedMap;

	public DynamicRegion(int regionId) {
		super(regionId);
		checkLoadMap();
		// plane,x,y,(real x, real y,or real plane coord, or rotation)
		regionCoords = new int[4][8][8][4];
	}

	@Override
	public void removeMapObject(WorldObject object, int x, int y) {
		if (removedMap == null)
			removedMap = new RegionMap(getRegionId(), false);
		int plane = object.getHeight();
		int type = object.getType();
		int rotation = object.getRotation();
		if (x < 0 || y < 0 || x >= removedMap.getMasks()[plane].length || y >= removedMap.getMasks()[plane][x].length)
			return;
		ObjectDefinitions objectDefinition = ObjectDefinitions.getObjectDefinitions(object.getId()); // load here

		if (type == 22 ? objectDefinition.getClipType() != 0 : objectDefinition.getClipType() == 0)
			return;
		if (type >= 0 && type <= 3) {
			removedMap.addWall(plane, x, y, type, rotation, objectDefinition.isProjectileCliped(), true);
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			removedMap.addObject(plane, x, y, sizeX, sizeY, objectDefinition.isProjectileCliped(), true);
		} else if (type == 22) {
			// map.addFloor(plane, x, y);
		}
	}

	// override by static region to empty
	@Override
	public void checkLoadMap() {
		if (getLoadMapStage() == 0) {
			setLoadMapStage(1);
			// lets use slow executor, if we take 1-3sec to load objects who
			// cares? what maters are the players on the loaded regions lul
			CoresManager.slowExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						setLoadMapStage(2);
						if (!isLoadedObjectSpawns()) {
							loadObjectSpawns();
							setLoadedObjectSpawns(true);
						}
						if (!isLoadedNPCSpawns()) {
							loadNPCSpawns();
							setLoadedNPCSpawns(true);
						}
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			});
		}
	}

	@Override
	public void setMask(int plane, int localX, int localY, int mask) {

	}

	/*
	 * gets the real tile objects from real region
	 */
	@Override
	public WorldObject[] getObjects(int plane, int localX, int localY) {
		int currentChunkX = localX / 8;
		int currentChunkY = localY / 8;
		int rotation = regionCoords[plane][currentChunkX][currentChunkY][3];
		int realChunkX = regionCoords[plane][currentChunkX][currentChunkY][0];
		int realChunkY = regionCoords[plane][currentChunkX][currentChunkY][1];
		if (realChunkX == 0 || realChunkY == 0) {
			return null;
		}
		int realRegionId = RegionBuilder.getRegionHash(realChunkX / 8, realChunkY / 8);
		Region region = World.getRegion(realRegionId, true);
		if (region instanceof DynamicRegion) {
			if (Settings.DEBUG)
				Logger.log(this, "YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			return null; // no information so that data not loaded
		}
		int realRegionOffsetX = (realChunkX - ((realChunkX / 8) * 8));
		int realRegionOffsetY = (realChunkY - ((realChunkY / 8) * 8));
		int posInChunkX = (localX - (currentChunkX * 8));
		int posInChunkY = (localY - (currentChunkY * 8));
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				int fakeChunckX = posInChunkX;
				int fakeChunckY = posInChunkY;
				posInChunkX = fakeChunckY;
				posInChunkY = 7 - fakeChunckX;
			}
		}
		int realLocalX = (realRegionOffsetX * 8) + posInChunkX;
		int realLocalY = (realRegionOffsetY * 8) + posInChunkY;
		return region.getObjects(regionCoords[plane][currentChunkX][currentChunkY][2], realLocalX, realLocalY);
	}

	// overrided by static region to get mask from needed region
	@Override
	public int getMaskClipedOnly(int plane, int localX, int localY) {
		int currentChunkX = localX / 8;
		int currentChunkY = localY / 8;
		int rotation = regionCoords[plane][currentChunkX][currentChunkY][3];
		int realChunkX = regionCoords[plane][currentChunkX][currentChunkY][0];
		int realChunkY = regionCoords[plane][currentChunkX][currentChunkY][1];
		if (realChunkX == 0 || realChunkY == 0)
			return -1;
		int realRegionId = (((realChunkX / 8) << 8) + (realChunkY / 8));
		Region region = World.getRegion(realRegionId, true);
		if (region instanceof DynamicRegion) {
			if (Settings.DEBUG)
				Logger.log(this, "YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			return -1; // no information so that data not loaded
		}
		int realRegionOffsetX = (realChunkX - ((realChunkX / 8) * 8));
		int realRegionOffsetY = (realChunkY - ((realChunkY / 8) * 8));
		int posInChunkX = (localX - (currentChunkX * 8));
		int posInChunkY = (localY - (currentChunkY * 8));
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				int fakeChunckX = posInChunkX;
				int fakeChunckY = posInChunkY;
				posInChunkX = fakeChunckY;
				posInChunkY = 7 - fakeChunckX;
			}
		}
		int realLocalX = (realRegionOffsetX * 8) + posInChunkX;
		int realLocalY = (realRegionOffsetY * 8) + posInChunkY;
		return region.getMaskClipedOnly(regionCoords[plane][currentChunkX][currentChunkY][2], realLocalX, realLocalY);
	}

	@Override
	public int getRotation(int plane, int localX, int localY) {
		return regionCoords[plane][localX / 8][localY / 8][3];
	}

	/*
	 * gets clip data from the original region part
	 */

	@Override
	public int getMask(int plane, int localX, int localY) {
		int currentChunkX = localX / 8;
		int currentChunkY = localY / 8;
		int rotation = regionCoords[plane][currentChunkX][currentChunkY][3];
		int realChunkX = regionCoords[plane][currentChunkX][currentChunkY][0];
		int realChunkY = regionCoords[plane][currentChunkX][currentChunkY][1];
		if (realChunkX == 0 || realChunkY == 0)
			return -1;
		int realRegionId = (((realChunkX / 8) << 8) + (realChunkY / 8));
		Region region = World.getRegion(realRegionId, true);
		if (region instanceof DynamicRegion) {
			if (Settings.DEBUG)
				Logger.log(this, "YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			return -1; // no information so that data not loaded
		}
		int realRegionOffsetX = (realChunkX - ((realChunkX / 8) * 8));
		int realRegionOffsetY = (realChunkY - ((realChunkY / 8) * 8));
		int posInChunkX = (localX - (currentChunkX * 8));
		int posInChunkY = (localY - (currentChunkY * 8));
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				int fakeChunckX = posInChunkX;
				int fakeChunckY = posInChunkY;
				posInChunkX = fakeChunckY;
				posInChunkY = 7 - fakeChunckX;
			}
		}
		int realLocalX = (realRegionOffsetX * 8) + posInChunkX;
		int realLocalY = (realRegionOffsetY * 8) + posInChunkY;

		int mask = region.getMask(regionCoords[plane][currentChunkX][currentChunkY][2], realLocalX, realLocalY);

		if (removedMap != null)
			mask = mask & (~removedMap.getMasks()[plane][localX][localY]);
		return mask;
	}

	public int[][][][] getRegionCoords() {
		return regionCoords;
	}

	public void setRegionCoords(int[][][][] regionCoords) {
		this.regionCoords = regionCoords;
	}
}
