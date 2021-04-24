package com.rs.game;

import com.rs.cache.loaders.ObjectDefinitions;

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
}