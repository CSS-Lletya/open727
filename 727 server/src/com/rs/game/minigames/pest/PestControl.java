package com.rs.game.minigames.pest;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.RegionBuilder;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.PestPortal;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

/**
 * The class that handles all the functions of the 'Pest Control' minigame.
 * @author Khaled
 *
 */
public class PestControl {

	/**
	 * A list of currently active players in pest control.
	 */
	private List<Player> team;

	/**
	 * An array of values, used for the empty Dynamic region.
	 */
	private int[] boundChunks;

	/**
	 * A lock, for synchronization purposes.
	 */
	private final Object lock = new Object();

	/**
	 * Outside area of the first lander.
	 */
	public static final WorldTile OUTSIDE_AREA = new WorldTile(2657, 2639, 0);

	/**
	 * An array of Non-Playable Portal Characters.
	 */
	public PestPortal[] portals = new PestPortal[4];

	/**
	 * The Non-Playable Knight Character.
	 */
	private NPC knight;

	/**
	 * A timer that extends {@code TimerTask}. Used to create an accurate timer for the 'Pest Control' minigame.
	 * @author Khaled
	 *
	 */
	class PestGameTimer extends TimerTask {

		int minutes = 0;

		@Override
		public void run() {
			if (minutes == 20 || knight.isDead())
				endGame();
			minutes++;
		}
	}

	/**
	 * Constructs a new {@code ClanWars} {@code Object}.
	 * @param team The members participating in the minigame.
	 */
	public PestControl(final List<Player> team) {// OMFG IMPORTANT COORDS <3 2614, 2568
		this.team = team;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				boundChunks = RegionBuilder.findEmptyChunkBound(8, 8);
				RegionBuilder.copyAllPlanesMap(328, 320, boundChunks[0], boundChunks[1], 8);
				CoresManager.slowExecutor.schedule(new Runnable() {

					@Override
					public void run() {
						sendBeginningWave();
						for (Player player : team)
							player.setNextWorldTile(getWorldTile(35 - Utils.random(4), 54 - (Utils.random(3))));
					}
				}, 1200, TimeUnit.MILLISECONDS);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}

	/**
	 * Creates required Non-Playable-Characters.
	 */
	private void sendBeginningWave() {
		knight = new PestPortal(3782, getWorldTile(32, 32));
		int portalIndex = 6142;
		for (int index = 0; index < portals.length; index++)
			portals[index] = new PestPortal(portalIndex++, getPortalLocations()[index]);
	}

	/**
	 * Creates an array of new {@code WorldTile}.
	 * @return the array of new {@code WorldTile}.
	 */
	public WorldTile[] getPortalLocations() {
		return new WorldTile[] { getWorldTile(4, 31), // purple
				getWorldTile(56, 28), // blue
				getWorldTile(45, 10), // yellow
				getWorldTile(21, 9) };// light pink
	}

	/**
	 * Retrieves a new {@code WorldTile} using the boundChunks of the dynamic region.
	 * @param mapX The 'x' coordinate value.
	 * @param mapY The 'y' coordinate value.
	 * @return a new {@code WorldTile}
	 */
	public WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
	}

	/**
	 * Completes the game and creates a new task to destroy the map region.
	 */
	public void endGame() {
		for (Player player : team) {
			synchronized (lock) {
				player.setNextWorldTile(OUTSIDE_AREA);
				player.reset();
				player.getControlerManager().forceStop();
			}
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
		team.clear();
	}

	/**
	 * A gutter of the current team.
	 * @return The playing team.
	 */
	public List<Player> getPlayers() {
		return team;
	}

	/**
	 * A gutter of the Non-Playable Knight Character.
	 * @return the Non-Playable Knight Character.
	 */
	public NPC getKnight() {
		return knight;
	}
}
