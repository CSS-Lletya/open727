package skills.dungeoneering;

import skills.dungeoneering.rooms.BossRoom;
import skills.dungeoneering.rooms.HandledRoom;
import skills.dungeoneering.rooms.NormalRoom;
import skills.dungeoneering.rooms.RoomEvent;
import skills.dungeoneering.rooms.StartRoom;

public class DungeonConstants {

	public static int ROTATIONS_COUNT = 4;

	/*
	 * objects handling
	 */
	public static final int[] DUNGEON_DOORS = new int[] { 50342 }, DUNGEON_EXITS = new int[] { 51156 };

	/*
	 * floor types
	 */
	public static final int FROZEN_FLOORS = 0, ABANDONED_FLOORS = 1, FURNISHED_FLOORS = 2, ABANDONED2_FLOORS = 3,
			OCCULT_FLOORS = 4, WARPED_FLOORS = 5;

	/*
	 * dungeon sizes
	 */
	public static final int SMALL_DUNGEON = 0, MEDIUM_DUNGEON = 1, LARGE_DUNGEON = 2;

	public static final int[][] DUNGEON_RATIO = new int[][] { new int[] { 4, 4 }, new int[] { 4, 8 },
			new int[] { 8, 8 } };

	/*
	 * door directions
	 */
	public static final int EAST_DOOR = 0, WEST_DOOR = 1, NORTH_DOOR = 2, SOUTH_DOOR = 3;

	public static final int[] START_ROOM_MUSICS = new int[] { 822 // FROZEN_FLOORS
	};

	public static final int[][] DANGEROUS_MUSICS = new int[][] {
			// FROZEN_FLOORS
			new int[] { 832, 834, 827, 811, 824, 808, 831, 810, 833, 837, 814, 821 // equal
																					// to
																					// boss
																					// unlockable
			} };

	public static final int[][] SAFE_MUSICS = new int[][] {
			// FROZEN_FLOORS
			new int[] { 804, 805, 806, 807, 812, 813, 826, 823, 825, 826, 828, 829, 830, 835, 836

			} };

	/*
	 * Creatures
	 */
	public static final int[][] GUARDIAN_CREATURES = new int[][] {
			// FROZEN_FLOORS
			new int[] { 10762, // ice giants
					10770, // frost dragons
					10776, // iron dragon
					10782, // ice troll
					10791, // Thrower troll
					10797, // brute
					10815, // red dragon
					10821, // ghost
					10831, // Mysterious shade
					10906, // bat
					10910, // giant bat
			} };

	/*
	 * Creatures
	 */
	public static final int[][] PASSIVE_CREATURES = new int[][] {
			// FROZEN_FLOORS
			new int[] { 11086 // Protomastyx
			} };

	public static final StartRoom[][] START_ROOMS = new StartRoom[][] {
			// FROZEN_FLOORS
			new StartRoom[] {
					new StartRoom(new int[] { 1, 2, 3, 4, 5, 6 }, 14, 632, EAST_DOOR, WEST_DOOR, NORTH_DOOR,
							SOUTH_DOOR),
					new StartRoom(new int[] { 1, 2, 3, 4, 5, 6 }, 14, 624, SOUTH_DOOR),
					new StartRoom(new int[] { 1, 2, 3, 4, 5, 6 }, 14, 626, WEST_DOOR, SOUTH_DOOR),
					new StartRoom(new int[] { 1, 2, 3, 4, 5, 6 }, 14, 630, WEST_DOOR, NORTH_DOOR, SOUTH_DOOR) } };

	public static final NormalRoom[][] NORMAL_ROOMS = new NormalRoom[][] {
			// FROZEN_FLOORS
			new NormalRoom[] { new NormalRoom(1, 8, 240, SOUTH_DOOR), new NormalRoom(1, 10, 240, SOUTH_DOOR),
					new NormalRoom(1, 12, 240, SOUTH_DOOR)

					, new NormalRoom(1, 8, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 10, 242, SOUTH_DOOR, WEST_DOOR), new NormalRoom(1, 12, 242, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 8, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 10, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 12, 244, NORTH_DOOR, SOUTH_DOOR)

					, new NormalRoom(1, 8, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 10, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 12, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 8, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
					new NormalRoom(1, 10, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
					new NormalRoom(1, 12, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR)

					, new NormalRoom(1, 14, 240, SOUTH_DOOR), new NormalRoom(1, 16, 240, SOUTH_DOOR),
					new NormalRoom(1, 18, 240, SOUTH_DOOR), new NormalRoom(1, 20, 240, SOUTH_DOOR),
					new NormalRoom(1, 22, 240, SOUTH_DOOR)

					, new NormalRoom(1, 24, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 26, 242, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 28, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 30, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 32, 244, NORTH_DOOR, SOUTH_DOOR)

					, new NormalRoom(1, 34, 242, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 40, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 42, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 44, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 46, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 48, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 50, 244, NORTH_DOOR, SOUTH_DOOR)

					, new NormalRoom(1, 38, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 36, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR)

					, new NormalRoom(1, 52, 240, SOUTH_DOOR), new NormalRoom(1, 56, 240, SOUTH_DOOR),
					new NormalRoom(1, 56, 240, SOUTH_DOOR), new NormalRoom(1, 58, 240, SOUTH_DOOR),
					new NormalRoom(1, 60, 240, SOUTH_DOOR)

					, new NormalRoom(1, 62, 240, SOUTH_DOOR), new NormalRoom(1, 64, 240, SOUTH_DOOR),
					new NormalRoom(1, 66, 240, SOUTH_DOOR), new NormalRoom(1, 68, 240, SOUTH_DOOR),
					new NormalRoom(1, 70, 240, SOUTH_DOOR), new NormalRoom(1, 72, 240, SOUTH_DOOR),
					new NormalRoom(1, 74, 240, SOUTH_DOOR), new NormalRoom(1, 76, 240, SOUTH_DOOR),
					new NormalRoom(1, 78, 240, SOUTH_DOOR)

					, new NormalRoom(1, 62, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 64, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 66, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 68, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 70, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 72, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 74, 242, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 76, 242, SOUTH_DOOR, WEST_DOOR), new NormalRoom(1, 78, 242, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 62, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 64, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 66, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 68, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 70, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 72, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 74, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 76, 244, NORTH_DOOR, SOUTH_DOOR),
					new NormalRoom(1, 78, 244, NORTH_DOOR, SOUTH_DOOR)

					, new NormalRoom(1, 62, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 64, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 66, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 68, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 70, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 72, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 74, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 76, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
					new NormalRoom(1, 78, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

					, new NormalRoom(1, 62, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
					new NormalRoom(1, 64, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
					new NormalRoom(1, 66, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR)

					, new NormalRoom(1, 72, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
					new NormalRoom(1, 74, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
					new NormalRoom(1, 76, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
					new NormalRoom(1, 78, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR) } };

	public static final HandledRoom[][] PUZZLE_ROOMS = new HandledRoom[][] {};

	public static final BossRoom[][] BOSS_ROOMS = new BossRoom[][] {
			// FROZEN_FLOORS
			new BossRoom[] {
					// Gluttonous behemoth
					new BossRoom(new RoomEvent() {
						@Override
						public void openRoom(DungeonManager dungeon, RoomReference reference) {
							// TODO Auto-generated method stub

						}
					}, 817, 1, 28, 624),
					// Astea Frostweb
					new BossRoom(new RoomEvent() {
						@Override
						public void openRoom(DungeonManager dungeon, RoomReference reference) {
							// TODO Auto-generated method stub

						}
					}, 816, 1, 30, 624) } };

}
