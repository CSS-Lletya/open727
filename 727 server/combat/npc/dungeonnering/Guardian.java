package npc.dungeonnering;

import com.rs.game.Entity;
import com.rs.game.WorldTile;

import npc.NPC;
import skills.dungeoneering.DungeonManager;
import skills.dungeoneering.RoomReference;

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
