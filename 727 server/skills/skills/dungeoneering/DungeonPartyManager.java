package skills.dungeoneering;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.game.player.Player;

import skills.Skills;

@SuppressWarnings("unused")
public final class DungeonPartyManager {

	private String leader; // username
	private int floor;
	private int complexity;
	private CopyOnWriteArrayList<DungeonPartyPlayer> team;
	private DungeonManager dungeon;

	public DungeonPartyManager(Player player) {
		leader = player.getUsername();
		team = new CopyOnWriteArrayList<DungeonPartyPlayer>();
		team.add(new DungeonPartyPlayer(player));
		setDefaults();
		start(DungeonConstants.SMALL_DUNGEON); // temporary
	}

	public void setDefaults() {
		floor = 1;
		complexity = 1;
	}

	public void start(int size) {
		if (dungeon != null)
			return;
		dungeon = new DungeonManager(this, size);
	}

	public int getComplexity() {
		return complexity;
	}

	public int getFloor() {
		return floor;
	}

	public int getFloorType() {
		return DungeonUtils.getFloorType(floor);
	}

	public int getDungeoneeringLevel() {
		int level = 120;
		for (DungeonPartyPlayer player : team) {
			int playerLevel = player.getPlayer().getSkills().getLevelForXp(Skills.DUNGEONEERING);
			if (playerLevel < level)
				level = playerLevel;
		}
		return level;
	}

	public DungeonPartyPlayer getDPlayer(Player player) {
		for (DungeonPartyPlayer p : team)
			if (p.getPlayer() == player)
				return p;
		return null;
	}

	public CopyOnWriteArrayList<DungeonPartyPlayer> getTeam() {
		return team;
	}

}
