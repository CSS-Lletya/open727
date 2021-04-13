package skills.dungeoneering;

import java.util.Arrays;

import skills.dungeoneering.rooms.HandledRoom;

public class Room {

	private HandledRoom room;
	private int rotation;

	public Room(HandledRoom room, int rotation) {
		this.room = room;
		this.rotation = rotation;
	}

	public int getChunkX() {
		return room.getChunkX();
	}

	public int getChunkY() {
		return room.getChunkY();
	}

	public int getRotation() {
		return rotation;
	}

	@Override
	public String toString() {
		return "[Room][" + rotation + "]" + ", " + Arrays.toString(room.getDoorDirections()) + ", " + (getChunkX() << 3)
				+ ", " + (getChunkY() << 3);
	}

	public boolean hasSouthDoor() {
		return room.hasSouthDoor(rotation);
	}

	public boolean hasNorthDoor() {
		return room.hasNorthDoor(rotation);
	}

	public boolean hasWestDoor() {
		return room.hasWestDoor(rotation);
	}

	public boolean hasEastDoor() {
		return room.hasEastDoor(rotation);
	}

	public boolean containsOnlySouthDoor() {
		return room.getDoorDirections().length == 1 && room.getDoorDirections()[0] == DungeonConstants.SOUTH_DOOR;
	}

	public void setRoom(HandledRoom room) {
		this.room = room;
	}

	public HandledRoom getRoom() {
		return room;
	}

	public void openRoom(DungeonManager dungeon, RoomReference reference) {
		room.openRoom(dungeon, reference);
	}

}