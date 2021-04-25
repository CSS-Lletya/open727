package com.rs.game.player;

import com.rs.game.WorldObject;

public class PossessedObject extends WorldObject {

	private Player owner;

	public PossessedObject(Player owner, int id, byte type, byte rotation, int x, int y, int plane) {
		super(id, type, rotation, x, y, plane);
		setOwner(owner);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
