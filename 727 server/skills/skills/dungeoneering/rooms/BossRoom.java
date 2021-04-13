package skills.dungeoneering.rooms;

import skills.dungeoneering.DungeonConstants;

public class BossRoom extends HandledRoom {

	private int requiredLevel;
	private int musicId;

	public BossRoom(RoomEvent event, int musicId, int requiredLevel, int chunkX, int chunkY) {
		super(chunkX, chunkY, event, DungeonConstants.SOUTH_DOOR);
		this.requiredLevel = requiredLevel;
		this.musicId = musicId;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public int getMusicId() {
		return musicId;
	}

}
