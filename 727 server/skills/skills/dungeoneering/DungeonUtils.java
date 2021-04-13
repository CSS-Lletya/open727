package skills.dungeoneering;

import java.util.ArrayList;

import com.rs.utils.Utils;

import skills.dungeoneering.rooms.BossRoom;
import skills.dungeoneering.rooms.HandledRoom;

public final class DungeonUtils {

	public static int getFloorType(int floorId) {
		if (floorId <= 11)
			return DungeonConstants.FROZEN_FLOORS;
		if (floorId <= 17)
			return DungeonConstants.ABANDONED_FLOORS;
		if (floorId <= 29)
			return DungeonConstants.FURNISHED_FLOORS;
		if (floorId <= 35)
			return DungeonConstants.ABANDONED2_FLOORS;
		if (floorId <= 47)
			return DungeonConstants.OCCULT_FLOORS;
		return DungeonConstants.WARPED_FLOORS;
	}

	public static int getSafeMusic(int type) {
		return DungeonConstants.SAFE_MUSICS[type][Utils.random(DungeonConstants.DANGEROUS_MUSICS[type].length)];
	}

	public static int getDangerousMusic(int type) {
		return DungeonConstants.DANGEROUS_MUSICS[type][Utils.random(DungeonConstants.DANGEROUS_MUSICS[type].length)];
	}

	public static RoomReference getRandomRoom(Room[][] map) {
		return new RoomReference(Utils.random(map.length), Utils.random(map[0].length));
	}

	public static int getGuardianCreature(int type) {
		return DungeonConstants.GUARDIAN_CREATURES[type][Utils
				.random(DungeonConstants.GUARDIAN_CREATURES[type].length)];
	}

	public static int getPassiveCreature(int type) {
		return DungeonConstants.PASSIVE_CREATURES[type][Utils.random(DungeonConstants.PASSIVE_CREATURES[type].length)];
	}

	public static Room getStartRoom(int type, int complexity, RoomReference reference, Room[][] map) {
		Room[] possibleRooms = selectPossibleRooms(reference, map, DungeonConstants.START_ROOMS[type], true, false,
				complexity);
		if (possibleRooms.length == 0)
			return null;
		return possibleRooms[Utils.random(possibleRooms.length)];
	}

	public static Room getPuzzleRoom(int type, int complexity, RoomReference reference, Room[][] map) {
		Room[] possibleRooms = selectPossibleRooms(reference, map, DungeonConstants.PUZZLE_ROOMS[type], true, true,
				complexity);
		if (possibleRooms.length == 0)
			return null;
		return possibleRooms[Utils.random(possibleRooms.length)];
	}

	public static Room getNormalRoom(int type, int complexity, RoomReference reference, Room[][] map) {
		Room[] possibleRooms = selectPossibleRooms(reference, map, DungeonConstants.NORMAL_ROOMS[type], true, true,
				complexity);
		if (possibleRooms.length == 0)
			return null;
		return possibleRooms[Utils.random(possibleRooms.length)];
	}

	public static BossRoom getBossRoom(int type, int complexity, int dungeonLevel) {
		BossRoom[] possibleRooms = selectPossibleBossRooms(type, complexity, dungeonLevel);
		if (possibleRooms.length == 0)
			return null;
		return possibleRooms[Utils.random(possibleRooms.length)];
	}

	public static BossRoom[] selectPossibleBossRooms(int type, int complexity, int dungeonLevel) {
		ArrayList<BossRoom> possiblities = new ArrayList<BossRoom>();
		for (BossRoom handledRoom : DungeonConstants.BOSS_ROOMS[type]) {
			if (!handledRoom.isComplexity(complexity))
				continue;
			if (handledRoom.getRequiredLevel() > dungeonLevel)
				continue;
			possiblities.add(handledRoom);
		}
		return possiblities.toArray(new BossRoom[possiblities.size()]);
	}

	public static Room[] selectPossibleRooms(RoomReference reference, Room[][] map, HandledRoom[] handledRooms,
			boolean checkBounds, boolean checkMap, int complexity) {
		ArrayList<Room> possiblities = new ArrayList<Room>();
		for (HandledRoom handledRoom : handledRooms) {
			if (!handledRoom.isComplexity(complexity))
				continue;
			for (int rotation = 0; rotation < DungeonConstants.ROTATIONS_COUNT; rotation++) {
				Room room = new Room(handledRoom, rotation);
				if (checkBounds && !checkDungeonBounds(reference, map, room))
					continue;
				if (checkMap) {
					Room checkRoom = reference.getX() == 0 ? null : map[reference.getX() - 1][reference.getY()];
					if (checkRoom != null) {
						if (room.hasWestDoor()) {
							if (!checkRoom.hasEastDoor())
								continue;
						} else {
							if (checkRoom.hasEastDoor())
								continue;
						}
					}
					checkRoom = reference.getX() == map.length - 1 ? null : map[reference.getX() + 1][reference.getY()];
					if (checkRoom != null) {
						if (room.hasEastDoor()) {
							if (!checkRoom.hasWestDoor())
								continue;
						} else {
							if (checkRoom.hasWestDoor())
								continue;
						}
					}
					checkRoom = reference.getY() == 0 ? null : map[reference.getX()][reference.getY() - 1];
					if (checkRoom != null) {
						if (room.hasSouthDoor()) {
							if (!checkRoom.hasNorthDoor())
								continue;
						} else {
							if (checkRoom.hasNorthDoor())
								continue;
						}
					}
					checkRoom = reference.getY() == map[0].length - 1 ? null
							: map[reference.getX()][reference.getY() + 1];
					if (checkRoom != null) {
						if (room.hasNorthDoor()) {
							if (!checkRoom.hasSouthDoor())
								continue;
						} else {
							if (checkRoom.hasSouthDoor())
								continue;
						}
					}
				}
				possiblities.add(room);
			}
		}
		return possiblities.toArray(new Room[possiblities.size()]);
	}

	public static boolean checkDungeonBounds(RoomReference reference, Room[][] map, Room room) {
		if (reference.getX() == 0 && room.hasWestDoor())
			return false;
		if (reference.getX() == map.length - 1 && room.hasEastDoor())
			return false;
		if (reference.getY() == 0 && room.hasSouthDoor())
			return false;
		if (reference.getY() == map[0].length - 1 && room.hasNorthDoor())
			return false;
		return true;
	}

	public static boolean isExit(int objectId) {
		for (int id : DungeonConstants.DUNGEON_EXITS)
			if (id == objectId)
				return true;
		return false;
	}

	public static boolean isDoor(int objectId) {
		for (int id : DungeonConstants.DUNGEON_DOORS)
			if (id == objectId)
				return true;
		return false;
	}

}
