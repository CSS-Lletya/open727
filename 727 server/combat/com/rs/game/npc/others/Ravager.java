package com.rs.game.npc.others;

import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class Ravager extends NPC {

	boolean destroyingObject = false;

	public Ravager(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, -1, false, false);
	}

	@Override
	public void processNPC() {
	}

}
