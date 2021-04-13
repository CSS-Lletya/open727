package skills.dungeoneering.rooms;

import skills.dungeoneering.DungeonConstants;
import skills.dungeoneering.DungeonManager;
import skills.dungeoneering.RoomReference;

public class HandledRoom {

	private int chunkX;
	private int chunkY;
	private int[] doorsDirections;
	private RoomEvent event;

	public HandledRoom(int chunkX, int chunkY, RoomEvent event, int... doorsDirections) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.event = event;
		this.doorsDirections = doorsDirections;
	}

	public HandledRoom(int chunkX, int chunkY, int... doorsDirections) {
		this(chunkX, chunkY, null, doorsDirections);
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public int[] getDoorDirections() {
		return doorsDirections;
	}

	public boolean hasSouthDoor(int rotation) {
		return hasDoor(rotation == 0 ? DungeonConstants.SOUTH_DOOR
				: rotation == 1 ? DungeonConstants.EAST_DOOR
						: rotation == 2 ? DungeonConstants.NORTH_DOOR : DungeonConstants.WEST_DOOR);
	}

	public boolean hasNorthDoor(int rotation) {
		return hasDoor(rotation == 0 ? DungeonConstants.NORTH_DOOR
				: rotation == 1 ? DungeonConstants.WEST_DOOR
						: rotation == 2 ? DungeonConstants.SOUTH_DOOR : DungeonConstants.EAST_DOOR);
	}

	public boolean hasWestDoor(int rotation) {
		return hasDoor(rotation == 0 ? DungeonConstants.WEST_DOOR
				: rotation == 1 ? DungeonConstants.SOUTH_DOOR
						: rotation == 2 ? DungeonConstants.EAST_DOOR : DungeonConstants.NORTH_DOOR);
	}

	public boolean hasEastDoor(int rotation) {
		return hasDoor(rotation == 0 ? DungeonConstants.EAST_DOOR
				: rotation == 1 ? DungeonConstants.NORTH_DOOR
						: rotation == 2 ? DungeonConstants.WEST_DOOR : DungeonConstants.SOUTH_DOOR);
	}

	public boolean hasDoor(int direction) {
		for (int dir : doorsDirections)
			if (dir == direction)
				return true;
		return false;
	}

	public boolean isComplexity(int complexity) {
		return true;
	}

	public void openRoom(DungeonManager dungeon, RoomReference reference) {
		if (event == null)
			return;
		event.openRoom(dungeon, reference);
	}

}
