package com.rs.game.npc.dungeonnering;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.content.dungeoneering.DungeonManager;
import com.rs.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public class Guardian extends NPC {

	private DungeonManager manager;
	private RoomReference reference;

	public Guardian(int id, WorldTile tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, -1, true, true);
		this.manager = manager;
		this.reference = reference;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		manager.updateGuardian(reference);
	}

}
