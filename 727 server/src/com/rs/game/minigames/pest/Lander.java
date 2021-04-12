package com.rs.game.minigames.pest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.utils.Logger;

public class Lander {

	public static Lander[] landers = new Lander[3];

	private List<Player> lobby = new ArrayList<Player>();
	private final Object lock = new Object();
	private LobbyTimer timer;
	private LanderRequirement landerRequirement;

	public Lander(LanderRequirement landerRequirement) {
		this.landerRequirement = landerRequirement;
	}

	public class LobbyTimer extends TimerTask {

		private int minutes;

		@Override
		public void run() {
			try {
				synchronized (lock) {
					if (lobby.size() >= 1 && minutes == 1 || lobby.size() >= 1)// just for testing
						passPlayersToGame();
					else if (lobby.size() == 0) {
						timer.cancel();
						return;
					}
					minutes++;
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}

	private void passPlayersToGame() {
		List<Player> playing = new ArrayList<Player>();
		synchronized (lock) {
			for (int index = 0; index < lobby.size(); index++) {
				if (index > 25)
					return;
				Player player = lobby.get(index);
				if (player == null)
					return;
				playing.add(player);
				player.getControlerManager().forceStop();
				player.getControlerManager().startControler("PestControlGame", new PestControl(playing));
				remove(player);
			}
		}
		for (Player player : lobby) {
			player.getPackets().sendGameMessage("You have received priority over other players");
		}
	}

	public void enterLander(Player player) {
		if (player.getSkills().getCombatLevel() < landerRequirement.getRequirement()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a combat level of " + landerRequirement.getRequirement() + " or more to enter in boat.");
			return;
		}
		synchronized (lock) {
			if (lobby.size() == 0) // :O ?
				CoresManager.fastExecutor.scheduleAtFixedRate(timer = new LobbyTimer(), 0, 60000);
			add(player);
			player.setNextWorldTile(landerRequirement.getWorldTile());
			player.getPackets().sendGameMessage("You board the lander.", true);
		}
	}

	public void exitLander(Player player) {
		player.setNextWorldTile(landerRequirement.getExitTile());
	}

	public void add(Player player) {
		lobby.add(player);
		for (Player teamPlayer : lobby)
			teamPlayer.getControlerManager().getControler().sendInterfaces();
	}

	public void remove(Player player) {
		lobby.remove(player);
		for (Player teamPlayer : lobby)
			teamPlayer.getControlerManager().getControler().sendInterfaces();
	}

	public List<Player> getPlayers() {
		return lobby;
	}

	/**
	 * The requirements of the current lander.
	 * @author Khaled
	 *
	 */
	public static enum LanderRequirement {
		EASY(1, 40, new WorldTile(2661, 2639, 0), new WorldTile(0, 0, 0),
				new int[] { 3772, 3762, 3742, 3732, 3747, 3727, 3752, 3773, 3764, 3743, 3734, 3748, 3728, 3754, 3774,
						3766, 3744, 3736, 3749, 3729, 3756, 3775, 3768, 3745, 3738, 3750, 3730, 3758, 3776, 3770, 3746,
						3740, 3751, 3731, 3760 },
				2),

		INTERMEDIATE(2, 70, new WorldTile(0, 0, 0), new WorldTile(0, 0, 0), new int[] {}, 4),

		HARD(3, 100, new WorldTile(0, 0, 0), new WorldTile(0, 0, 0), new int[] {}, 6);

		private static Map<Integer, LanderRequirement> landers = new HashMap<Integer, LanderRequirement>();

		public static LanderRequirement forId(int id) {
			return landers.get(id);
		}

		static {
			for (LanderRequirement lander : LanderRequirement.values())
				landers.put(lander.getId(), lander);
		}

		int id, requirement, reward;
		int[] pests;
		WorldTile tile, exit;

		private LanderRequirement(int id, int requirement, WorldTile tile, WorldTile exit, int[] pests, int reward) {
			this.id = id;
			this.requirement = requirement;
			this.tile = tile;
			this.exit = exit;
			this.pests = pests;
			this.reward = reward;
		}

		public int getId() {
			return id;
		}

		public int getRequirement() {
			return requirement;
		}

		public int getReward() {
			return reward;
		}

		public int[] getPests() {
			return pests;
		}

		public WorldTile getWorldTile() {
			return tile;
		}

		public WorldTile getExitTile() {
			return exit;
		}
	}

	public static Lander[] getLanders() {
		return landers;
	}

	static {
		for (int i = 0; i < landers.length; i++)
			landers[i] = new Lander(LanderRequirement.forId(i));
	}
}
