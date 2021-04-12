package com.rs.game.minigames;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.fightpits.FightPitsNPC;
import com.rs.game.npc.fightpits.TzKekPits;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class FightPits {

	private static final int THHAAR_MEJ_KAH = 2618;

	private static final List<Player> lobby = new ArrayList<Player>();
	public static final List<Player> arena = new ArrayList<Player>();
	public static final Object lock = new Object();
	private static GameTask gameTask;

	private static boolean startedGame;
	public static String currentChampion;

	private static WorldTile[] GAME_TELEPORTS = { new WorldTile(4577, 5086, 0), new WorldTile(4571, 5083, 0),
			new WorldTile(4564, 5086, 0), new WorldTile(4564, 5097, 0), new WorldTile(4571, 5101, 0),
			new WorldTile(4578, 5097, 0) };

	private static class GameTask extends TimerTask {

		private int minutes;
		private List<NPC> spawns;

		@Override
		public void run() {
			try {
				synchronized (lock) {
					if (!startedGame) {
						startedGame = true;
						passPlayersToArena();
					} else {
						if (minutes == 0) {
							for (Player player : arena)
								player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_KAH, "FIGHT!");
						} else if (minutes == 5) { // spawn tz-kih
							// spawns
							spawns = new ArrayList<NPC>();
							for (int i = 0; i < 10; i++)
								spawns.add(new FightPitsNPC(2734,
										new WorldTile(GAME_TELEPORTS[Utils.random(GAME_TELEPORTS.length)], 3)));
						} else if (minutes == 6) { // spawn tz-kek
							for (int i = 0; i < 10; i++)
								spawns.add(new TzKekPits(2736,
										new WorldTile(GAME_TELEPORTS[Utils.random(GAME_TELEPORTS.length)], 3)));
						} else if (minutes == 7) { // spawn tok-xil
							for (int i = 0; i < 10; i++)
								spawns.add(new FightPitsNPC(2739,
										new WorldTile(GAME_TELEPORTS[Utils.random(GAME_TELEPORTS.length)], 3)));
						} else if (minutes == 10) { // spawn tz-kek
							// alot hits appears on players
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									if (!startedGame) {
										stop();
										return;
									}
									for (Player player : arena)
										player.applyHit(new Hit(player, 150, HitLook.REGULAR_DAMAGE));
								}

							}, 0, 0);
						}
						minutes++;
					}
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}

		public void removeNPCs() {
			if (spawns == null)
				return;
			for (NPC n : spawns)
				n.finish();
		}

	}

	/*
	 * because of the lvl 22s
	 */
	public static void addNPC(NPC n) {
		synchronized (lock) {
			if (gameTask == null || gameTask.spawns == null)
				return;
			gameTask.spawns.add(n);
		}
	}

	public static boolean canFight() {
		synchronized (lock) {
			if (gameTask == null)
				return false;
			return gameTask.minutes > 0;
		}
	}

	public static void passPlayersToArena() {
		for (Iterator<Player> it = lobby.iterator(); it.hasNext();) {
			Player player = it.next();
			player.stopAll();
			player.getControlerManager().removeControlerWithoutCheck();
			enterArena(player);
			it.remove();
		}
		refreshFoes();
	}

	public static void refreshFoes() {
		int foes = arena.size() - 1;
		for (Player player : arena)
			player.getPackets().sendConfig(560, foes);

	}

	public static void enterArena(Player player) {
		player.lock(5);
		player.getControlerManager().startControler("FightPitsArena");
		player.setNextWorldTile(new WorldTile(GAME_TELEPORTS[Utils.random(GAME_TELEPORTS.length)], 3));
		player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_KAH,
				"Please wait for the signal before fight.");
		player.setCanPvp(true);
		player.setCantTrade(true);
		arena.add(player);
	}

	/*
	 * 0 - logout, 1 - walk, 2 - dead, 3 - teled
	 */
	public static void leaveArena(Player player, int type) {
		synchronized (lock) {
			arena.remove(player);
			player.reset();
			player.getControlerManager().removeControlerWithoutCheck();
			if (type != 3)
				player.getControlerManager().startControler("FightPitsLobby");
			if (type == 0) {
				player.setLocation(4592, 5073, 0);
			} else {
				if (type != 3)
					lobby.add(player);
				player.setCanPvp(false);
				player.setCantTrade(false);
				player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 34 : 0);
				if (player.hasSkull() && player.getSkullId() == 1) {// if has champion skull
					player.removeSkull();
					player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_KAH,
							"Well done in the pit, here take TokKul as reward.");
					int tokkul = (lobby.size() + arena.size()) * 100;
					tokkul *= Settings.DROP_RATE; // 10x more
					if (!player.getInventory().addItem(6529, tokkul) && type == 1)
						World.addGroundItem(new Item(6529, tokkul), new WorldTile(4585, 5076, 0), player, true, 180,
								true);
				}
				if (type == 1) {
					player.lock(5);
					player.addWalkSteps(4585, 5076, 5, false);
				} else if (type == 2)
					player.setNextWorldTile(new WorldTile(new WorldTile(4592, 5073, 0), 2));
			}
			refreshFoes();
			checkPlayersAmmount();
			if (startedGame && arena.size() <= 1)
				endGame();
		}
	}

	public static void enterLobby(Player player, boolean login) {
		synchronized (lock) {
			if (!login) {
				player.lock(5);
				player.addWalkSteps(4595, 5066, 5, false);
				player.getControlerManager().startControler("FightPitsLobby");
			}
			lobby.add(player);
			checkPlayersAmmount();
		}
	}

	/*
	 * 0 - logout, 1 normal, 2 death/tele
	 */
	public static void leaveLobby(Player player, int type) {
		synchronized (lock) {
			if (type != 0) {
				if (type == 1) {
					player.lock(5);
					player.addWalkSteps(4597, 5064, 5, false);
				}
				player.getControlerManager().removeControlerWithoutCheck();
			}
			lobby.remove(player);
			checkPlayersAmmount();
		}
	}

	public static void checkPlayersAmmount() {
		if (gameTask == null) {
			if (lobby.size() + arena.size() >= 2) // 2players in
				startGame(false);
		} else {
			if (lobby.size() + arena.size() < 2)
				cancelGame();
		}
	}

	public static void startGame(boolean end) {
		if (end) {
			gameTask.cancel();
			gameTask.removeNPCs();
			setChampion();
			startedGame = false;
		}
		gameTask = new GameTask();
		CoresManager.fastExecutor.scheduleAtFixedRate(gameTask, end ? 60000 : 10000, 60000);

	}

	public static void cancelGame() {
		gameTask.cancel();
		gameTask.removeNPCs();
		gameTask = null;
		if (startedGame)
			setChampion();
		startedGame = false;
	}

	public static void setChampion() {
		if (arena.isEmpty())
			return;
		Player champion = arena.get(0);
		currentChampion = champion.getDisplayName();
		champion.getPackets().sendIComponentText(373, 10, "Current Champion: JaLYt-Ket-" + currentChampion);
		champion.setFightPitsSkull();
		champion.setWonFightPits();
		champion.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_KAH,
				"Well done, you were the last person in the pit and won that fight! The next round will start soon, wait for my signal before fighting.");
	}

	public static void endGame() {
		startGame(true);
	}

	private FightPits() {

	}
}
