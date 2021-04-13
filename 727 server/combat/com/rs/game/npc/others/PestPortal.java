package com.rs.game.npc.others;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class PestPortal extends NPC {

	public PestPortal(int id, WorldTile tile) {
		super(id, tile, -1, true, true);
		setCantFollowUnderCombat(true);
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		cancelFaceEntityNoCheck();
	}

	@Override
	public void sendDeath(Entity killer) {
		resetWalkSteps();
		getCombat().removeTarget();
		super.sendDeath(killer);
	}
}