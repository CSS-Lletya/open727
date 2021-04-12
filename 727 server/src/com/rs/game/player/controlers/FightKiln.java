package com.rs.game.player.controlers;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.fightkiln.FightKilnNPC;
import com.rs.game.npc.fightkiln.HarAken;
import com.rs.game.npc.fightkiln.TokHaarKetDill;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.cutscenes.Cutscene;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class FightKiln extends Controler {

	public static final WorldTile OUTSIDE = new WorldTile(4744, 5172, 0);

	private static final int TOKHAAR_HOK = 15195, TOKHAAR_HOK_SCENE = 15200;

	private static final int[] MUSICS = { 1088, 1082, 1086 };

	public void playMusic() {
		player.getMusicsManager().playMusic(selectedMusic);
	}

	/*
	 * 0 - south east 1 - south west 2 - north west 3 - north east TokHaar-Hur - 15201 TokHaar-Xil - 15202 TokHaar-Mej - 15203 TokHaar-Ket - 15204 TokHaar-Tok-Xil - 15205 TokHaar-Yt-Mejkot - 15206 TokHaar-Ket-Zek - 15207 TokHaar-Jad - 15208 TokHaar-Ket-Dill - 15213
	 * 
	 * 
	 */
	private final int[][] WAVES = { { 15202, 15202, 15205, 15201, 15201 }, // 1
			{ 15202, 15202, 15205, 15205, 15201 }, // 2
			{ 15202, 15205, 15205, 15205, 15201 }, // 3
			{ 15205, 15205, 15205, 15203, 15203 }, // 4
			{ 15202, 15205, 15205, 15205, 15213 }, // 5
			{ 15202, 15205, 15203, 15203, 15205, 15205 }, // 6
			{ 15203, 15205, 15205, 15205, 15202, 15205 }, // 7
			{ 15207, 15205, 15205 }, // 8
			{ 15205, 15205, 15205, 15205, 15205, 15205 }, // 9
			{ 15205, 15208 }, // 10
			{ 15203, 15203, 15203, 15203 }, // 11
			{ 15203, 15205, 15205, 15203 }, // 12
			{ 15203, 15207, 15203 }, // 13
			{ 15207, 15207, 15203, 15203 }, // 14
			{ 15207, 15207, 15205 }, // 15
			{ 15207, 15207, 15205, 15203, 15203 }, // 16
			{ 15207, 15207, 15205, 15203, 15206 }, // 17
			{ 15207, 15207, 15205, 15206, 15205, 15205 }, // 18
			{ 15203, 15203, 15203, 15203, 15203, 15203, 15203, 15203, 15203 }, // 19
			{ 15207, 15208 }, // 20
			{ 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201 }, // 21
			{ 15206, 15201, 15204, 15204, 15201 }, // 22
			{ 15206, 15206, 15204, 15206, 15201 }, // 23
			{ 15206, 15206, 15206, 15205, 15206 }, // 24
			{ 15206, 15206, 15205, 15205, 15207 }, // 25
			{ 15206, 15206, 15205, 15207, 15207 }, // 26
			{ 15204, 15206, 15205, 15204, 15206 }, // 27
			{ 15213, 15213, 15207, 15213, 15213, 15213, 15213 }, // 28
			{ 15206, 15206, 15206, 15206, 15206, 15206 }, // 29
			{ 15206, 15208, 15206, 15206 }, // 30
			{ 15205, 15205, 15205, 15205 }, // 31
			{ 15206, 15206, 15206, 15206 }, // 32
			{ 15207, 15207, 15207, 15207 }, // 33
			{ 15205, 15208, 15206 }, // 34
			{ 15207, 15206, 15206, 15208 }, // 35
			{ 15208, 15208 } // 36
	};

	private int[] boundChuncks;
	private Stages stage;
	private boolean logoutAtEnd;
	private boolean login;
	public int selectedMusic;
	private NPC tokHaarHok;
	private HarAken harAken;
	private int aliveNPCSCount;

	public static void enterFightKiln(Player player, boolean quickEnter) {
		if (!player.isCompletedFightCaves()
				|| player.getInventory().containsOneItem(23653, 23654, 23655, 23656, 23657, 23658))
			return;
		Familiar familiar = player.getFamiliar();
		if (familiar != null && ((familiar.getBob() != null
				&& familiar.getBob().containsOneItem(23653, 23654, 23655, 23656, 23657, 23658))
				|| familiar.isFinished()))
			return;
		if (!quickEnter)
			player.getDialogueManager().startDialogue("FightKilnDialogue");
		else
			player.getControlerManager().startControler("FightKilnControler", 1); // start at wave 1
	}

	private static enum Stages {
		LOADING, RUNNING, DESTROYING
	}

	@Override
	public void start() {
		loadCave(false);
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (stage != Stages.RUNNING)
			return false;
		if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			if (!logoutAtEnd) {
				logoutAtEnd = true;
				player.getPackets()
						.sendGameMessage("<col=ff0000>You will be logged out automatically at the end of this wave.");
				player.getPackets()
						.sendGameMessage("<col=ff0000>If you log out sooner, you will have to repeat this wave.");
			} else
				player.forceLogout();
			return false;
		}
		return true;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 68111) {
			if (stage != Stages.RUNNING)
				return false;
			exitCave(1);
			return false;
		}
		return true;
	}

	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean login() {
		loadCave(true);
		return false;
	}

	public int[] getMap() {
		int wave = getCurrentWave();
		if (wave < 11)
			return new int[] { 504, 632 };
		if (wave < 21)
			return new int[] { 512, 632 };
		if (wave < 31)
			return new int[] { 520, 632 };
		if (wave < 34)
			return new int[] { 528, 632 };
		return new int[] { 536, 632 };
	}

	public void buildMap() {
		int[] map = getMap();
		RegionBuilder.copyAllPlanesMap(map[0], map[1], boundChuncks[0], boundChuncks[1], 8);
	}

	public void loadCave(final boolean login) {
		final FightKiln kiln = this;
		stage = Stages.LOADING;
		player.lock(); // locks player
		Runnable event = new Runnable() {
			@Override
			public void run() {
				CoresManager.slowExecutor.execute(new Runnable() {
					@Override
					public void run() {
						int currentWave = getCurrentWave();
						// finds empty map bounds
						if (boundChuncks == null) {
							boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
							buildMap();
						} else if (!login
								&& (currentWave == 11 || currentWave == 21 || currentWave == 31 || currentWave == 34)) {
							buildMap();
							player.setForceNextMapLoadRefresh(true);
							player.loadMapRegions();
						}
						// selects a music
						selectedMusic = MUSICS[Utils.random(MUSICS.length)];
						playMusic();
						player.setForceMultiArea(true);
						player.stopAll();
						if (currentWave == 0) { // SCENE 0
							player.getInventory().removeItems(new Item(23653, Integer.MAX_VALUE),
									new Item(23654, Integer.MAX_VALUE), new Item(23655, Integer.MAX_VALUE),
									new Item(23656, Integer.MAX_VALUE), new Item(23657, Integer.MAX_VALUE),
									new Item(23658, Integer.MAX_VALUE));
							player.setNextWorldTile(getWorldTile(31, 51));
							tokHaarHok = new NPC(TOKHAAR_HOK, getWorldTile(30, 36), -1, true, true);
							tokHaarHok.setDirection(Utils.getFaceDirection(0, 1));
							// 1delay because player cant walk while teleing :p, + possible issues avoid
							WorldTasksManager.schedule(new WorldTask() {
								int count = 0;
								boolean run;

								@Override
								public void run() {
									if (count == 0) {
										WorldTile lookTo = getWorldTile(29, 39);
										player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
												Cutscene.getY(player, lookTo.getY()), 3500);
										WorldTile posTile = getWorldTile(27, 30);
										player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
												Cutscene.getY(player, posTile.getY()), 3500);
										run = player.getRun();
										player.setRun(false);
										WorldTile walkTo = getWorldTile(31, 39);
										player.addWalkSteps(walkTo.getX(), walkTo.getY(), -1, false);
									} else if (count == 1)
										player.getPackets().sendResetCamera();
									else if (count == 2) {
										player.getDialogueManager().startDialogue("TokHaarHok", 0, TOKHAAR_HOK, kiln);
										player.setRun(run);
										stage = Stages.RUNNING;
										player.unlock(); // unlocks player
										stop();
									}
									count++;
								}

							}, 1, 6);
						} else if (currentWave == 38) { // SCENE 7, WIN
							player.setNextWorldTile(getWorldTile(38, 25));
							player.setNextFaceWorldTile(getWorldTile(38, 26));
							tokHaarHok = new NPC(TOKHAAR_HOK_SCENE, getWorldTile(37, 30), -1, true, true);
							tokHaarHok.setDirection(Utils.getFaceDirection(0, -1));
							player.getPackets().sendBlackOut(2);
							player.getPackets().sendConfig(1241, 1);
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									WorldTile lookTo = getWorldTile(40, 28);
									player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
											Cutscene.getY(player, lookTo.getY()), 2200);
									WorldTile posTile = getWorldTile(29, 28);
									player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
											Cutscene.getY(player, posTile.getY()), 2500);
									HarAken harAken = new HarAken(15211, getWorldTile(45, 26), kiln);
									harAken.spawn();
									harAken.sendDeath(player);
									CoresManager.fastExecutor.schedule(new TimerTask() {
										@Override
										public void run() {
											try {
												player.getDialogueManager().startDialogue("TokHaarHok", 6,
														TOKHAAR_HOK_SCENE, kiln);
											} catch (Throwable e) {
												Logger.handle(e);
											}
										}
									}, 5000);
								}
							}, 1);
						} else if (currentWave == 37) { // SCENE 6
							player.setNextWorldTile(getWorldTile(38, 25));
							player.setNextFaceWorldTile(getWorldTile(38, 26));
							tokHaarHok = new NPC(TOKHAAR_HOK_SCENE, getWorldTile(37, 30), -1, true, true);
							tokHaarHok.setDirection(Utils.getFaceDirection(0, -1));
							player.getPackets().sendBlackOut(2);
							player.getPackets().sendConfig(1241, 1);
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									WorldTile lookTo = getWorldTile(40, 28);
									player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
											Cutscene.getY(player, lookTo.getY()), 2200);
									WorldTile posTile = getWorldTile(29, 28);
									player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
											Cutscene.getY(player, posTile.getY()), 2500);
									player.getDialogueManager().startDialogue("TokHaarHok", 5, TOKHAAR_HOK_SCENE, kiln);
								}

							}, 1);
						} else if (currentWave == 34) { // SCENE 5
							teleportPlayerToMiddle();
							player.getPackets().sendBlackOut(2);
							player.getPackets().sendConfig(1241, 1);
							WorldTasksManager.schedule(new WorldTask() {

								int count = 0;

								@Override
								public void run() {
									if (count == 0) {
										WorldTile lookTo = getWorldTile(32, 41);
										player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
												Cutscene.getY(player, lookTo.getY()), 1000);
										WorldTile posTile = getWorldTile(32, 38);
										player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
												Cutscene.getY(player, posTile.getY()), 1200);
									} else if (count == 6) {
										WorldTile lookTo = getWorldTile(64, 30);
										player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
												Cutscene.getY(player, lookTo.getY()), 3000);
										WorldTile posTile = getWorldTile(42, 36);
										player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
												Cutscene.getY(player, posTile.getY()), 3000);
										player.setNextWorldTile(getWorldTile(33, 39));
										player.setNextFaceWorldTile(getWorldTile(32, 39));
									} else if (count == 12) {
										stop();
										tokHaarHok = new NPC(TOKHAAR_HOK_SCENE, getWorldTile(28, 38), -1, true, true);
										tokHaarHok.setDirection(Utils.getFaceDirection(1, 0));

										WorldTile lookTo = getWorldTile(30, 38);
										player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
												Cutscene.getY(player, lookTo.getY()), 2500);
										WorldTile posTile = getWorldTile(30, 30);
										player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
												Cutscene.getY(player, posTile.getY()), 3000);
										player.getDialogueManager().startDialogue("TokHaarHok", 7, TOKHAAR_HOK_SCENE,
												kiln);
									}
									count++;

								}

							}, 1, 0);
						} else if (currentWave == 31) { // SCENE 4
							player.setNextWorldTile(getWorldTile(21, 21));
							player.setNextFaceWorldTile(getWorldTile(20, 20));
							player.getPackets().sendBlackOut(2);
							player.getPackets().sendConfig(1241, 1);
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									WorldTile lookTo = getWorldTile(20, 17);
									player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
											Cutscene.getY(player, lookTo.getY()), 2500);
									WorldTile posTile = getWorldTile(25, 26);
									player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
											Cutscene.getY(player, posTile.getY()), 3000);
									player.getDialogueManager().startDialogue("TokHaarHok", 4, TOKHAAR_HOK_SCENE, kiln);
								}

							}, 1);
						} else if (currentWave == 21) { // SCENE 3
							tokHaarHok = new NPC(TOKHAAR_HOK_SCENE, getWorldTile(30, 43), -1, true, true);
							tokHaarHok.setDirection(Utils.getFaceDirection(0, -1));
							teleportPlayerToMiddle();
							player.getPackets().sendBlackOut(2);
							player.getPackets().sendConfig(1241, 1);
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									WorldTile lookTo = getWorldTile(31, 43);
									player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
											Cutscene.getY(player, lookTo.getY()), 2500);
									WorldTile posTile = getWorldTile(31, 34);
									player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
											Cutscene.getY(player, posTile.getY()), 4000);
									player.getDialogueManager().startDialogue("TokHaarHok", 3, TOKHAAR_HOK_SCENE, kiln);
								}

							}, 1);
						} else if (currentWave == 11) { // SCENE 2
							tokHaarHok = new NPC(TOKHAAR_HOK_SCENE, getWorldTile(45, 45), -1, true, true);
							tokHaarHok.setDirection(Utils.getFaceDirection(-1, -1));
							teleportPlayerToMiddle();
							player.getPackets().sendBlackOut(2);
							player.getPackets().sendConfig(1241, 1);
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									WorldTile lookTo = getWorldTile(45, 45);
									player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
											Cutscene.getY(player, lookTo.getY()), 1000);
									WorldTile posTile = getWorldTile(38, 37);
									player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
											Cutscene.getY(player, posTile.getY()), 3000);
									player.getDialogueManager().startDialogue("TokHaarHok", 2, TOKHAAR_HOK_SCENE, kiln);
								}

							}, 1);
						} else if (currentWave == 1) { // SCENE 1
							player.getInventory().removeItems(new Item(23653, Integer.MAX_VALUE),
									new Item(23654, Integer.MAX_VALUE), new Item(23655, Integer.MAX_VALUE),
									new Item(23656, Integer.MAX_VALUE), new Item(23657, Integer.MAX_VALUE),
									new Item(23658, Integer.MAX_VALUE));
							tokHaarHok = new NPC(TOKHAAR_HOK_SCENE, getWorldTile(30, 36), -1, true, true);
							tokHaarHok.setDirection(Utils.getFaceDirection(0, 1));
							player.setNextWorldTile(getWorldTile(31, 39));
							player.getPackets().sendBlackOut(2);
							player.getPackets().sendConfig(1241, 1);
							player.setNextFaceWorldTile(tokHaarHok);
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									WorldTile lookTo = getWorldTile(31, 40);
									player.getPackets().sendCameraLook(Cutscene.getX(player, lookTo.getX()),
											Cutscene.getY(player, lookTo.getY()), 1000);
									WorldTile posTile = getWorldTile(31, 50);
									player.getPackets().sendCameraPos(Cutscene.getX(player, posTile.getX()),
											Cutscene.getY(player, posTile.getY()), 3000);
									player.getDialogueManager().startDialogue("TokHaarHok", 1, TOKHAAR_HOK_SCENE, kiln);
									stage = Stages.RUNNING;
									player.unlock();
								}

							}, 1);
						} else if (login) { // LOGIN during
							kiln.login = login;
							teleportPlayerToMiddle();
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									stage = Stages.RUNNING;
									player.unlock();
								}
							}, 1);
						}
					}
				});

			}
		};
		if (!login)
			FadingScreen.fade(player, event);
		else
			event.run();
	}

	public WorldTile getMaxTile() {
		if (getCurrentWave() < 11)
			return getWorldTile(49, 49);
		if (getCurrentWave() < 21)
			return getWorldTile(47, 47);
		if (getCurrentWave() < 31)
			return getWorldTile(45, 45);
		if (getCurrentWave() < 34)
			return getWorldTile(43, 43);
		return getWorldTile(41, 41);
	}

	public WorldTile getMinTile() {
		if (getCurrentWave() < 11)
			return getWorldTile(14, 14);
		if (getCurrentWave() < 21)
			return getWorldTile(16, 16);
		if (getCurrentWave() < 31)
			return getWorldTile(18, 18);
		if (getCurrentWave() < 34)
			return getWorldTile(20, 20);
		return getWorldTile(22, 22);
	}

	/*
	 * 20, 20 min X, min Y 42 42 maxX, maxY
	 */
	/*
	 * 0 - north 1 - south 2 - east 3 - west
	 */
	public WorldTile getTentacleTile() {
		int corner = Utils.random(4);
		int position = Utils.random(5);
		while (corner != 0 && position == 2)
			position = Utils.random(5);
		switch (corner) {
		case 0: // north
			return getWorldTile(21 + (position * 5), 42);
		case 1: // south
			return getWorldTile(21 + (position * 5), 20);
		case 2: // east
			return getWorldTile(42, 21 + (position * 5));
		case 3: // west
		default:
			return getWorldTile(20, 21 + (position * 5));
		}
	}

	public WorldTile getSpawnTile(int count, int size) {
		int position = count % 4;
		switch (position) {
		case 0: // east south
			WorldTile maxTile = getMaxTile();
			WorldTile minTile = getMinTile();
			return new WorldTile(maxTile.getX() - 1 - size, minTile.getY() + 2, 1);
		case 1: // west south
			return getMinTile().transform(2, 2, 0);
		case 2: // west north
			maxTile = getMaxTile();
			minTile = getMinTile();
			return new WorldTile(minTile.getX() + 2, maxTile.getY() - 1 - size, 1);
		case 3: // east north
		default:
			return getMaxTile().transform(-1 - size, -1 - size, 0);
		}
	}

	@Override
	public void moved() {
		if (stage != Stages.RUNNING || !login)
			return;
		login = false;
		setWaveEvent();
	}

	public void startWave() {
		if (stage != Stages.RUNNING)
			return;
		int currentWave = getCurrentWave();
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0, 316);
		player.getPackets().sendConfig(639, currentWave);
		if (currentWave > WAVES.length) {
			if (currentWave == 37)
				aliveNPCSCount = 1;
			return;
		} else if (currentWave == 0) {
			exitCave(1);
			return;
		}
		aliveNPCSCount = WAVES[currentWave - 1].length;
		for (int i = 0; i < WAVES[currentWave - 1].length; i += 4) {
			final int next = i;
			CoresManager.fastExecutor.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						if (stage != Stages.RUNNING)
							return;
						spawn(next);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, (next / 4) * 4000);
		}
	}

	public void spawn(int index) {
		int currentWave = getCurrentWave();
		for (int i = index; i < (index + 4 > WAVES[currentWave - 1].length ? WAVES[currentWave - 1].length
				: index + 4); i++) {
			int npcId = WAVES[currentWave - 1][i];
			if (npcId == 15213)
				new TokHaarKetDill(WAVES[currentWave - 1][i],
						getSpawnTile(i, NPCDefinitions.getNPCDefinitions(npcId).size), this);
			else
				new FightKilnNPC(WAVES[currentWave - 1][i],
						getSpawnTile(i, NPCDefinitions.getNPCDefinitions(npcId).size), this);

		}
	}

	private int[] getLavaCrystal() {
		switch (getCurrentWave()) {
		case 1:
		case 13:
		case 25:
			return new int[] { 23653 };
		case 3:
		case 15:
		case 27:
			return new int[] { 23654 };
		case 5:
		case 18:
		case 29:
			return new int[] { 23655 };
		case 7:
		case 19:
		case 31:
			return new int[] { 23656 };
		case 9:
		case 21:
			return new int[] { 23657 };
		case 11:
		case 23:
			return new int[] { 23658 };
		case 35:
			return new int[] { 23657, 23658 };
		default:
			return null;
		}
	}

	public void checkCrystal() {
		if (stage != Stages.RUNNING)
			return;
		if (aliveNPCSCount == 1) {
			int[] crystals = getLavaCrystal();
			if (crystals != null) {
				for (int crystal : crystals) {
					World.addGroundItem(new Item(crystal), getWorldTile(32, 32));
				}
			}
		}
	}

	public void removeNPC() {
		if (stage != Stages.RUNNING)
			return;
		aliveNPCSCount--;
		if (aliveNPCSCount == 0)
			nextWave();
	}

	public void win() {
		if (stage != Stages.RUNNING)
			return;
		exitCave(4);
	}

	public void unlockPlayer() {
		stage = Stages.RUNNING;
		player.unlock(); // unlocks player
	}

	public void removeScene() {
		FadingScreen.fade(player, new Runnable() {
			@Override
			public void run() {
				if (stage != Stages.RUNNING)
					unlockPlayer();
				removeTokHaarTok();
				player.getPackets().sendResetCamera();
				player.getPackets().sendBlackOut(0);
				player.getPackets().sendConfig(1241, 0);
				if (getCurrentWave() == 38) {
					Integer reward = (Integer) player.getTemporaryAttributtes().get("FightKilnReward");
					if (reward != null)
						win();
				} else {
					teleportPlayerToMiddle();
					setWaveEvent();
				}
			}

		});
	}

	public void teleportPlayerToMiddle() {
		player.setNextWorldTile(getWorldTile(31, 32));
	}

	public void removeTokHaarTok() {
		if (tokHaarHok != null)
			tokHaarHok.finish();
	}

	public void nextWave() {
		if (stage != Stages.RUNNING)
			return;
		playMusic();
		int nextWave = getCurrentWave() + 1;
		setCurrentWave(nextWave);
		if (logoutAtEnd) {
			player.forceLogout();
			return;
		} else if (nextWave == 1 || nextWave == 11 || nextWave == 21 || nextWave == 31 || nextWave == 34
				|| nextWave == 37 || nextWave == 38) {
			harAken = null;
			player.stopAll();
			loadCave(false);
			return;
		}
		setWaveEvent();
	}

	public void setWaveEvent() {
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if (stage != Stages.RUNNING)
						return;
					startWave();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 6000);
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("You have been defeated!");
				} else if (loop == 3) {
					player.reset();
					exitCave(1);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		exitCave(2);
	}

	/*
	 * logout or not. if didnt logout means lost, 0 logout, 1, normal, 2 tele
	 */
	public void exitCave(int type) {
		stage = Stages.DESTROYING;
		WorldTile outside = new WorldTile(OUTSIDE, 2); // radomizes alil
		if (type == 0) {
			player.setLocation(outside);
			if (getCurrentWave() == 0) // leaves if didnt start
				removeControler();
		} else if (type == 2) {
			player.setLocation(outside);
			removeControler();
		} else {
			player.setForceMultiArea(false);
			player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
			if (type == 1 || type == 4) {
				player.setNextWorldTile(outside);
				if (type == 4) {
					player.setCompletedFightKiln();
					player.getPackets().sendGameMessage("You were victorious!!");
					Integer reward = (Integer) player.getTemporaryAttributtes().get("FightKilnReward");
					int itemId = reward != null && reward == 1 ? 6571 : 23659;
					if (!player.getInventory().addItem(itemId, 1))
						World.addGroundItem(new Item(itemId, 1), new WorldTile(player), player, true, 180, true);
					player.reset();
				}
			}
			player.getInventory().removeItems(new Item(23653, Integer.MAX_VALUE), new Item(23654, Integer.MAX_VALUE),
					new Item(23655, Integer.MAX_VALUE), new Item(23656, Integer.MAX_VALUE),
					new Item(23657, Integer.MAX_VALUE), new Item(23658, Integer.MAX_VALUE));
			removeCrystalEffects();
			removeControler();
		}
		/*
		 * 1200 delay because of leaving
		 */
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}

	private void removeCrystalEffects() {
		player.setInvulnerable(false);
		player.getSkills().restoreSkills();
		player.setHpBoostMultiplier(0);
		player.getEquipment().refreshConfigs(false);
		player.getTemporaryAttributtes().remove("FightKilnCrystal");
	}

	/*
	 * gets worldtile inside the map
	 */
	public WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile(boundChuncks[0] * 8 + mapX, boundChuncks[1] * 8 + mapY, 1);
	}

	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean logout() {
		/*
		 * only can happen if dungeon is loading and system update happens
		 */
		if (stage != Stages.RUNNING)
			return false;
		exitCave(0);
		return false;

	}

	public int getCurrentWave() {
		if (getArguments() == null || getArguments().length == 0)
			return 0;
		return (Integer) getArguments()[0];
	}

	public void setCurrentWave(int wave) {
		if (getArguments() == null || getArguments().length == 0)
			this.setArguments(new Object[1]);
		getArguments()[0] = wave;
	}

	@Override
	public void forceClose() {
		/*
		 * shouldnt happen
		 */
		if (stage != Stages.RUNNING)
			return;
		exitCave(2);
	}

	public void showHarAken() {
		if (harAken == null) {
			harAken = new HarAken(15211, getWorldTile(45, 26), this);
			harAken.setDirection(Utils.getFaceDirection(-1, -1));
		} else {
			if (stage != Stages.RUNNING)
				return;
			switch (Utils.random(3)) {
			case 0:
				harAken.setLocation(getWorldTile(29, 17));
				harAken.setDirection(Utils.getFaceDirection(0, 1));
				break;
			case 1:
				harAken.setLocation(getWorldTile(17, 30));
				harAken.setDirection(Utils.getFaceDirection(1, 0));
				break;
			case 2:
				harAken.setLocation(getWorldTile(42, 30));
				harAken.setDirection(Utils.getFaceDirection(-1, 0));
				break;
			}
			harAken.spawn();
			// TODO set worldtile
		}
		harAken.setCantInteract(false);
		harAken.setNextAnimation(new Animation(16232));
	}

	public static void useCrystal(final Player player, int id) {
		if (!(player.getControlerManager().getControler() instanceof FightKiln)
				|| player.getTemporaryAttributtes().get("FightKilnCrystal") != null)
			return;
		player.getInventory().deleteItem(new Item(id, 1));
		switch (id) {
		case 23653: // invulnerability
			player.getPackets().sendGameMessage("<col=7E2217>>The power of this crystal makes you invulnerable.");
			player.getTemporaryAttributtes().put("FightKilnCrystal", Boolean.TRUE);
			player.setInvulnerable(true);
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					player.getTemporaryAttributtes().remove("FightKilnCrystal");
					player.getPackets().sendGameMessage(
							"<col=7E2217>The power of the crystal dwindles and you're vulnerable once more.");
					player.setInvulnerable(false);
				}
			}, 15, TimeUnit.SECONDS);
			break;
		case 23654: // RESTORATION
			player.heal(player.getMaxHitpoints());
			player.getPrayer().restorePrayer(player.getSkills().getLevelForXp(Skills.PRAYER) * 10);
			player.getPackets().sendGameMessage("<col=7E2217>The power of this crystal heals you fully.");
			break;
		case 23655: // MAGIC
			boostCrystal(player, Skills.MAGIC);
			break;
		case 23656: // RANGED
			boostCrystal(player, Skills.RANGE);
			break;
		case 23657: // STRENGTH
			boostCrystal(player, Skills.STRENGTH);
			break;
		case 23658: // CONSTITUTION
			player.getTemporaryAttributtes().put("FightKilnCrystal", Boolean.TRUE);
			player.setHpBoostMultiplier(0.5);
			player.getEquipment().refreshConfigs(false);
			player.heal(player.getSkills().getLevelForXp(Skills.HITPOINTS) * 5);
			player.getPackets().sendGameMessage("<col=7E2217>The power of this crystal improves your Constitution.");
			CoresManager.slowExecutor.schedule(new Runnable() {

				@Override
				public void run() {
					try {
						player.getTemporaryAttributtes().remove("FightKilnCrystal");
						player.getPackets().sendGameMessage(
								"<col=7E2217>The power of the crystal dwindles and your constitution prowess returns to normal.");
						player.setHpBoostMultiplier(0);
						player.getEquipment().refreshConfigs(false);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}

			}, 210, TimeUnit.SECONDS);
			break;
		}
	}

	private static void boostCrystal(final Player player, final int skill) {
		player.getTemporaryAttributtes().put("FightKilnCrystal", Boolean.TRUE);
		if (skill == Skills.RANGE)
			player.getPackets().sendGameMessage(
					"<col=7E2217>The power of the crystal improves your Ranged prowess, at the expense of your Defence, Strength and Magical ability.");
		else if (skill == Skills.MAGIC)
			player.getPackets().sendGameMessage(
					"<col=7E2217>The power of the crystal improves your Magic prowess, at the expense of your Defence, Strength and Ranged ability.");
		else if (skill == Skills.STRENGTH)
			player.getPackets().sendGameMessage(
					"<col=7E2217>The power of the crystal improves your Strength prowess, at the expense of your Defence, Ranged and Magical ability.");
		CoresManager.fastExecutor.schedule(new TimerTask() {

			private int count;

			@Override
			public void run() {
				try {
					if (count++ == 7 || !(player.getControlerManager().getControler() instanceof FightKiln)) {
						player.getTemporaryAttributtes().remove("FightKilnCrystal");
						player.getPackets().sendGameMessage("<col=7E2217>The power of the crystal dwindles and your "
								+ Skills.SKILL_NAME[skill] + " prowess returns to normal.");
						player.getSkills().set(Skills.DEFENCE, player.getSkills().getLevelForXp(Skills.DEFENCE));
						player.getSkills().set(Skills.STRENGTH, player.getSkills().getLevelForXp(Skills.STRENGTH));
						player.getSkills().set(Skills.RANGE, player.getSkills().getLevelForXp(Skills.RANGE));
						player.getSkills().set(Skills.MAGIC, player.getSkills().getLevelForXp(Skills.MAGIC));
						cancel();
					} else {
						for (int i = 1; i < 7; i++) {
							if (i == skill || i == 3 || i == 5)
								continue;
							player.getSkills().set(i, player.getSkills().getLevelForXp(i) / 2);
						}
						player.getSkills().set(skill, (int) (player.getSkills().getLevelForXp(skill) * 1.5));
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30000);
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == 15195 && getCurrentWave() == 0) {
			player.getDialogueManager().startDialogue("TokHaarHok", 0, TOKHAAR_HOK, this);
			return false;
		}
		return true;

	}

	@Override
	public void process() {
		if (harAken != null)
			harAken.process();
	}

	public void hideHarAken() {
		if (stage != Stages.RUNNING)
			return;
		harAken.resetTimer();
		harAken.setCantInteract(true);
		harAken.setNextAnimation(new Animation(16234));
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if (stage != Stages.RUNNING)
						return;
					harAken.finish();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 3000);
	}
}
