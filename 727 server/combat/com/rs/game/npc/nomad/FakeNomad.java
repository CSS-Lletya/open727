package com.rs.game.npc.nomad;

import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class FakeNomad extends NPC {

	private Nomad nomad;

	public FakeNomad(WorldTile tile, Nomad nomad) {
		super(8529, tile, -1, true, true);
		this.nomad = nomad;
		setForceMultiArea(true);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		nomad.destroyCopy(this);
	}

}
