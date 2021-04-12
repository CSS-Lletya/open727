package com.rs.game.minigames.clanwars;

import java.util.TimerTask;

import com.rs.game.player.Player;
import com.rs.utils.Logger;

/**
 * The timer task subclass used for clan wars updating.
 * @author Emperor
 *
 */
public final class ClanWarsTimer extends TimerTask {// this is the class for updating the timer

	/**
	 * The clan wars object.
	 */
	private final ClanWars clanWars;

	/**
	 * The start ticks (before the wall goes down and the war commences).
	 */
	private int startTicks = 200;

	/**
	 * If the clan war has started.
	 */
	private boolean started;

	/**
	 * The amount of time left.
	 */
	private int timeLeft;

	/**
	 * The last amount of minutes.
	 */
	private int lastMinutes = -1;

	/**
	 * The time-out counter, we use this to check if the war has expired due to inactivity.
	 */
	private int timeOut = 0;

	/**
	 * Constructs a new {@code ClanWarsTimer} {@code Object}.
	 * @param clanWars The clan wars object.
	 */
	public ClanWarsTimer(ClanWars clanWars) {
		this.clanWars = clanWars;
		this.timeLeft = clanWars.getTimeLeft();
	}

	@Override
	public void run() {
		try {
			if (!started) {
				if (startTicks-- == 8) {
					WallHandler.dropWall(clanWars);
				} else if (startTicks == 0) {
					started = true;
					for (Player player : clanWars.getFirstPlayers()) {
						player.getPackets().sendGlobalConfig(270, 0);
						player.getPackets().sendGlobalConfig(260, 1);
					}
					for (Player player : clanWars.getSecondPlayers()) {
						player.getPackets().sendGlobalConfig(270, 0);
						player.getPackets().sendGlobalConfig(260, 1);
					}
					WallHandler.removeWall(clanWars);
					clanWars.updateWar();
				}
				return;
			}
			if (clanWars.getFirstPlayers().isEmpty() || clanWars.getSecondPlayers().isEmpty()) {
				if (++timeOut == 1_000) { // 10 minutes until the war is time-out.
					clanWars.endWar();
					cancel();
					return;
				}
			} else {
				timeOut = 0; // Reset time-out.
			}
			if ((timeLeft * 0.6) / 60 != lastMinutes) {
				lastMinutes = (int) Math.ceil((timeLeft * 0.6) / 60);
				for (Player player : clanWars.getFirstPlayers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
				for (Player player : clanWars.getSecondPlayers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
				for (Player player : clanWars.getFirstViewers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
				for (Player player : clanWars.getSecondViewers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
			}
			if (timeLeft-- == 0) {
				clanWars.endWar();
				cancel();
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	/**
	 * Joins the war.
	 * @param p The player.
	 * @param firstTeam If the player is part of the first team/viewers.
	 */
	public void refresh(Player p, boolean firstTeam) {
		p.getPackets().sendGlobalConfig(261,
				(firstTeam ? clanWars.getFirstPlayers() : clanWars.getSecondPlayers()).size());
		p.getPackets().sendGlobalConfig(262,
				(firstTeam ? clanWars.getSecondPlayers() : clanWars.getFirstPlayers()).size());
		p.getPackets().sendGlobalConfig(263, clanWars.getKills() >> (firstTeam ? 0 : 24) & 0xFFFF);
		p.getPackets().sendGlobalConfig(264, clanWars.getKills() >> (firstTeam ? 24 : 0) & 0xFFFF);
		p.getPackets().sendGlobalConfig(260, started ? 1 : 0);
		p.getPackets().sendGlobalConfig(270, started ? lastMinutes : startTicks);
	}

	/**
	 * If the war has started.
	 * @return {@code True} if so.
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Gets the time left.
	 * @return The time left.
	 */
	public int getTimeLeft() {
		return timeLeft;
	}

	/**
	 * Checks if the clan ended due to a timeout.
	 * @return {@code True} if so.
	 */
	public boolean isTimeOut() {
		return timeOut > 499;
	}

}