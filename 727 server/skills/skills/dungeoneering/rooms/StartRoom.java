package skills.dungeoneering.rooms;

import com.rs.utils.Utils;

import skills.dungeoneering.DungeonManager;
import skills.dungeoneering.RoomReference;

public final class StartRoom extends HandledRoom {

	private int[] complexitys;

	public StartRoom(int[] complexitys, int chunkX, int chunkY, int musicId, int... doorsDirections) {
		super(chunkX, chunkY, new RoomEvent() {
			@Override
			public void openRoom(DungeonManager dungeon, RoomReference reference) {
				dungeon.telePartyToRoom(reference);
				dungeon.spawnNPC(reference, 11226, 6 + Utils.random(3), 6 + Utils.random(3)); // smoother
				dungeon.linkPartyToDungeon();
			}
		}, doorsDirections);
		this.complexitys = complexitys;

	}

	@Override
	public boolean isComplexity(int complexity) {
		for (int c : complexitys)
			if (c == complexity)
				return true;
		return false;
	}

}
