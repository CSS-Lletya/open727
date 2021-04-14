package skills.dungeoneering;

import com.rs.game.World;
import com.rs.utils.Utils;

import skills.dungeoneering.rooms.BossRoom;

@SuppressWarnings("unused")
public final class Dungeon {

	private int type;
	private int complexity;
	private Room[][] map;
	private RoomReference[] creationReferences;
	private int creationCount;
	private int puzzlesCount;

	public static void main(String[] args) {
		long lastDung = Utils.currentTimeMillis();
		while (true) {
			System.out.print("TOOK: " + (Utils.currentTimeMillis() - lastDung) + ", ");
			lastDung = Utils.currentTimeMillis();
			new Dungeon(DungeonConstants.FROZEN_FLOORS, 1, DungeonConstants.LARGE_DUNGEON);
		}
	}

	public Dungeon(int type, int complexity, int size) {
		this.type = type;
		this.complexity = complexity;
		while (creationCount < DungeonConstants.DUNGEON_RATIO[size][0] * DungeonConstants.DUNGEON_RATIO[size][1] / 2)
			reset(size);
		if (creationCount > creationReferences.length)
			throw new RuntimeException("failed generating " + creationCount + " INVALID");
		// requestLoadMapsOnDemand();
//		System.out.println("Dungeon roomCount: " + creationCount);
	}

	public void requestLoadMapsOnDemand() {
		for (int i = 0; i < creationCount; i++) {
			RoomReference reference = creationReferences[i];
			Room room = map[reference.getX()][reference.getY()];
			int regionId = (((room.getChunkX() / 8) << 8) + (room.getChunkY() / 8));
			World.getRegion(regionId);
		}
	}

	public Room getRoom(RoomReference reference) {
		return map[reference.getX()][reference.getY()];
	}

	public void setBossRoom(int dungeonLevel) {
		BossRoom bossRoom = DungeonUtils.getBossRoom(type, complexity, dungeonLevel);
		if (bossRoom == null)
			throw new RuntimeException("failed finding Boss Room INVALID");
		for (int i = creationCount - 1; i >= 0; i--) {
			RoomReference reference = creationReferences[i];
			Room room = map[reference.getX()][reference.getY()];
			if (room.containsOnlySouthDoor()) {
				room.setRoom(bossRoom);
				return;
			}
		}
		throw new RuntimeException("No BOSS ROOM");
	}

	public void reset(int size) {
		creationCount = 0;
		puzzlesCount = 0;
		cacheDungeon(size);
		generateStartRoom();
	}

	public void cacheDungeon(int size) {
		map = new Room[DungeonConstants.DUNGEON_RATIO[size][0]][DungeonConstants.DUNGEON_RATIO[size][1]];
		creationReferences = new RoomReference[map.length * map[0].length];
	}

	public void generateStartRoom() {
		while (true) {
			RoomReference reference = DungeonUtils.getRandomRoom(map);
			Room room = DungeonUtils.getStartRoom(type, complexity, reference, map);
			if (room != null) {
				cacheRoom(reference, room);
				break;
			}
		}
	}

	public void cacheRoom(RoomReference reference, Room room) {
		map[reference.getX()][reference.getY()] = room;
		creationReferences[creationCount++] = reference;
		if (room.hasWestDoor() && map[reference.getX() - 1][reference.getY()] == null)
			generateNormalRoom(new RoomReference(reference.getX() - 1, reference.getY()));
		if (room.hasEastDoor() && map[reference.getX() + 1][reference.getY()] == null)
			generateNormalRoom(new RoomReference(reference.getX() + 1, reference.getY()));
		if (room.hasSouthDoor() && map[reference.getX()][reference.getY() - 1] == null)
			generateNormalRoom(new RoomReference(reference.getX(), reference.getY() - 1));
		if (room.hasNorthDoor() && map[reference.getX()][reference.getY() + 1] == null)
			generateNormalRoom(new RoomReference(reference.getX(), reference.getY() + 1));
	}

	public void generateNormalRoom(RoomReference reference) {
		Room room = DungeonUtils.getNormalRoom(type, complexity, reference, map);
		if (room == null)
			throw new RuntimeException("failed generating " + reference.toString() + " INVALID");
		cacheRoom(reference, room);
	}

	public int getMapWidth() {
		return map.length;
	}

	public int getMapHeight() {
		return map[0].length;
	}

	public RoomReference getStartRoomReference() {
		return creationReferences[0];
	}

}
