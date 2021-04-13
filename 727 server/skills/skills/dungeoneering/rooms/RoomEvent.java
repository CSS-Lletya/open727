package skills.dungeoneering.rooms;

import skills.dungeoneering.DungeonManager;
import skills.dungeoneering.RoomReference;

public interface RoomEvent {

	public void openRoom(DungeonManager dungeon, RoomReference reference);
}
