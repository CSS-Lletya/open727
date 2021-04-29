package com.rs.game;

public enum UseFlag {
	GROUND_ITEM(0x1), NPC(0x2), WORLD_OBJECT(0x4), PLAYER(0x8), SELF(0x10), ICOMPONENT(0x20), WORLD_TILE(0x40);

	private int flag;

	private UseFlag(int flag) {
		this.flag = flag;
	}

	public int getFlag() {
		return flag;
	}
}