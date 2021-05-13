package com.rs.game;

import com.rs.Settings;
import com.rs.utils.Utils;

/**
 * A container class that represents a World Tile.
 * @author Dennis
 *
 */
public class WorldTile {

	/**
	 * The X coordinate.
	 */
	private short x;
	/**
	 * The Y coordinate.
	 */
	private short y;
	/**
	 * The Z coordinate.
	 * (Height)
	 */
	private byte z;

	/**
	 * Creates a new World Tile coordinate.
	 * @param x
	 * @param y
	 * @param plane
	 */
	public WorldTile(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) plane;
	}

	/**
	 * Creates a new World Tile coordinate.
	 * @param tile
	 */
	public WorldTile(WorldTile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.z = tile.z;
	}

	/**
	 * Creates a new World Tile coordinate with
	 * a randomized tile distance of of parameter.
	 * @param tile
	 * @param randomize
	 */
	public WorldTile(WorldTile tile, int randomize) {
		this.x = (short) (tile.x + Utils.getRandom(randomize * 2) - randomize);
		this.y = (short) (tile.y + Utils.getRandom(randomize * 2) - randomize);
		this.z = tile.z;
	}

	/**
	 *  Creates a new World Tile coordinate via.
	 *  Hash (Region ID)
	 * @param hash
	 */
	public WorldTile(int hash) {
		this.x = (short) (hash >> 14 & 0x3fff);
		this.y = (short) (hash & 0x3fff);
		this.z = (byte) (hash >> 28);
	}

	/**
	 * Sets the current World Tile location
	 * to a new World Tile location via parameter.
	 * @param xOffset
	 * @param yOffset
	 * @param planeOffset
	 */
	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		x += xOffset;
		y += yOffset;
		z += planeOffset;
	}

	/**
	 * Sets the current World Tile location
	 * to a new World Tile location via parameter.
	 * @param tile
	 */
	public final void setLocation(WorldTile tile) {
		setLocation(tile.x, tile.y, tile.z);
	}

	/**
	 * Sets the current World Tile location
	 * to a new World Tile location via parameter.
	 * @param x
	 * @param y
	 * @param plane
	 */
	public final void setLocation(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) plane;
	}

	/**
	 * Gets the X coordinate.
	 * @return X
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the X in a Region.
	 * @return X
	 */
	public int getXInRegion() {
		return x & 0x3F;
	}

	/**
	 * Gets the Y in a Region.
	 * @return Y
	 */
	public int getYInRegion() {
		return y & 0x3F;
	}

	/**
	 * Gets the Y coordinate.
	 * @return Y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the Height coordinate.
	 * @return Z
	 */
	public int getHeight() {
		if (z > 3)
			return 3;
		return z;
	}

	/**
	 * Gets the X Chunk.
	 * @return X
	 */
	public int getChunkX() {
		return (x >> 3);
	}

	/**
	 * Gets the Y Chunk.
	 * @return Y
	 */
	public int getChunkY() {
		return (y >> 3);
	}

	/**
	 * Gets the Regions X coordinate.
	 * @return X
	 */
	public int getRegionX() {
		return (x >> 6);
	}

	/**
	 * Gets the Regions X coordinate.
	 * @return Y
	 */
	public int getRegionY() {
		return (y >> 6);
	}

	/**
	 * Gets the Region ID.
	 * @return Region Id
	 */
	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	/**
	 * Gets the Local X of a World Tile.
	 * @param tile
	 * @param mapSize
	 * @return X
	 */
	public int getLocalX(WorldTile tile, int mapSize) {
		return x - 8 * (tile.getChunkX() - (Settings.MAP_SIZES[mapSize] >> 4));
	}

	/**
	 * Gets the Local Y of a World Tile.
	 * @param tile
	 * @param mapSize
	 * @return Y
	 */
	public int getLocalY(WorldTile tile, int mapSize) {
		return y - 8 * (tile.getChunkY() - (Settings.MAP_SIZES[mapSize] >> 4));
	}

	/**
	 * Gets the Local X of a World Tile.
	 * @param tile
	 * @return local X
	 */
	public int getLocalX(WorldTile tile) {
		return getLocalX(tile, 0);
	}

	/**
	 * Gets the Local Y of a World Tile.
	 * @param tile
	 * @return local Y
	 */
	public int getLocalY(WorldTile tile) {
		return getLocalY(tile, 0);
	}

	/**
	 * Gets the Local X of the current World Tile.
	 * @param tile
	 * @return local X
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the Local Y of the current World Tile.
	 * @param tile
	 * @return local Y
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the Regions Hash
	 * @return Region Hash
	 */
	public int getRegionHash() {
		return getRegionY() + (getRegionX() << 8) + (z << 16);
	}

	/**
	 * Gets the Tile Hash.
	 * @return Tile Hash
	 */
	public int getTileHash() {
		return y + (x << 14) + (z << 28);
	}

	/**
	 * Checks if the Entity is within the Tile via.
	 * distance parameter.
	 * @param tile
	 * @param distance
	 * @return
	 */
	public boolean withinDistance(WorldTile tile, int distance) {
		if (tile.z != z)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance;
	}

	/**
	 * Checks if the Entity is within the Tile location.
	 * @param tile
	 * @param distance
	 * @return
	 */
	public boolean withinDistance(WorldTile tile) {
		if (tile.z != z)
			return false;
		return Math.abs(tile.x - x) <= 14 && Math.abs(tile.y - y) <= 14;
	}

	/**
	 * Gets the Coordinate X Face
	 * @param sizeX
	 * @return X
	 */
	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	/**
	 * Gets the Coordinate X Face via
	 * parameters.
	 * @param sizeX
	 * @return X
	 */
	public static final int getCoordFaceX(int x, int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	/**
	 * Gets the Coordinate Y Face via
	 * parameters.
	 * @param sizeY
	 * @return Y
	 */
	public static final int getCoordFaceY(int y, int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	/**
	 * Gets the Coordinate X Face via
	 * parameters.
	 * @param sizeX
	 * @return X
	 */
	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	/**
	 * Gets the Coordinate Y Face via
	 * parameters.
	 * @param sizeY
	 * @return Y
	 */
	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	/**
	 * Gets the Coordinate Y Face via
	 * parameters.
	 * @param sizeY
	 * @return Y
	 */
	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	/**
	 * Transforms a World Tile via parameter.
	 * @param x
	 * @param y
	 * @param plane
	 * @return
	 */
	public WorldTile transform(int x, int y, int plane) {
		return new WorldTile(this.x + x, this.y + y, this.z + plane);
	}

	/**
	 * Checks if this world tile's coordinates match the other world tile.
	 * @param other The world tile to compare with.
	 * @return {@code True} if so.
	 */
	public boolean matches(WorldTile other) {
		return x == other.x && y == other.y && z == other.z;
	}

	/**
	 * Gets the Fixed Unique Id of a World Tile.
	 * @return id
	 */
	public long getFixedUniqueId() {
		return x * 2311 + x * 277 + z * 2371;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof WorldTile))
			return false;
		WorldTile other = (WorldTile) obj;
		return x == other.x && y == other.y && z == other.z;
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}
	
	/**
	 * Gets the distance between this location and the given location.
	 *
	 * @param location
	 * 		The location argued.
	 * @return The distance.
	 */
	public int getDistance(WorldTile location) {
		return Utils.getDistance(getX(), getY(), location.getX(), location.getY());
	}
}
