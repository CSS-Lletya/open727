package com.rs.utils;

import com.rs.game.WorldTile;
import com.rs.json.impl.NPCAutoSpawn.Direction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 20, 2014
 */
public class NPCSpawning implements Comparable<NPCSpawning> {

	private int x, y, z, id;

	private Direction direction;

	public NPCSpawning(int x, int y, int z, int id, Direction direction) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setId(id);
		this.setDirection(direction);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getId() {
		return id;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public WorldTile getTile() {
		return new WorldTile(x, y, z);
	}

	@Override
	public int compareTo(NPCSpawning o) {
		return Double.compare(o.getId(), getId());
	}
}
