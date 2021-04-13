package com.rs.game.player;

import java.io.Serializable;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.cutscenes.Cutscene;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public final class DominionTower implements Serializable {

	public static final int CLIMBER = 0, ENDURANCE = 1, MAX_FACTOR = 10000000;
	private static final long serialVersionUID = -5230255619877910203L;

	private transient Player player;
	private transient int[] mapBaseCoords;

	private int nextBossIndex;
	private int progress;
	private int dominionFactor;
	private long totalScore;
	private boolean talkedWithFace;
	private int killedBossesCount;
	private int maxFloorEndurance;
	private int maxFloorClimber;

	private static final int[] NORMAL_ARENA = { 456, 768 }, NOMAD_ARENA = { 456, 776 };

	public void setPlayer(Player player) {
		this.player = player;
	}

	public DominionTower() {
		nextBossIndex = -1;
	}

	public boolean hasRequiriments() {
		return player.getSkills().getCombatLevelWithSummoning() >= 110;
	}

	public void openSpectate() {
		player.getInterfaceManager().sendInterface(1157);
	}

	public void growFace() {
		player.getPackets().sendSound(7913, 0, 2);
		player.getDialogueManager().startDialogue("SimpleMessage",
				"The face on the wall groans and cowls at you. Perhaps you should", "talk to it first.");
	}

	public void openModes() {
		if (!hasRequiriments()) {
			player.getDialogueManager().startDialogue("DTSpectateReq");
			return;
		}
		if (!talkedWithFace) {
			growFace();
			return;
		}
		if (progress == 256) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You have some dominion factor which you must exchange before", "starting another match.");
			player.getPackets()
					.sendGameMessage("You can't go back into the arena, you must go to the next floor on entrance.");
			return;
		}
		player.getInterfaceManager().sendInterface(1164);
		player.getPackets().sendIComponentText(1164, 27,
				progress == 0 ? "Ready for a new match" : "Floor progress: " + progress);
	}

	public void handleButtons(int interfaceId, int componentId) {
		if (interfaceId == 1164) {
			if (componentId == 26)
				openClimberMode();
			else if (componentId == 28)
				openEnduranceMode();
			else if (componentId == 29)
				openSpecialMode();
			else if (componentId == 30)
				openFreeStyleMode();
			else if (componentId == 31)
				openSpectate();
		} else if (interfaceId == 1163) {
			if (componentId == 89)
				player.closeInterfaces();
		} else if (interfaceId == 1168) {
			if (componentId == 254)
				player.closeInterfaces();
		} else if (interfaceId == 1170) {
			if (componentId == 85)
				player.closeInterfaces();
		} else if (interfaceId == 1173) {
			if (componentId == 58)
				player.closeInterfaces();
			else if (componentId == 59)
				startEnduranceMode();
		}
	}

	private static final int[] MUSICS = { 1015, 1022, 1018, 1016, 1021 };

	public static final class Boss {

		private String name;
		private String text;
		private int[] ids;
		private boolean forceMulti;
		private Item item;
		private int voice;
		private int[] arena;

		public Boss(String name, String text, int... ids) {
			this(name, text, -1, false, null, NORMAL_ARENA, ids);
		}

		public Boss(String name, String text, int voice, boolean forceMulti, Item item, int[] arena, int... ids) {
			this.name = name;
			this.text = text;
			this.forceMulti = forceMulti;
			this.ids = ids;
			this.item = item;
			this.voice = voice;
			this.arena = arena;
		}

		public boolean isForceMulti() {
			return forceMulti;
		}

		public String getName() {
			return name;
		}
	}

	private static final Boss[] BOSSES = { new Boss("Elvarg", "Grrrr", 14548),
			new Boss("Delrith", "Grrrr", -1, false, new Item(2402, 1), NORMAL_ARENA, 14578),
			new Boss("Evil Chicken", "Bwak bwak bwak", 3375),
			new Boss("The Black Knight Titan", "Kill kill kill!", 14436), new Boss("Bouncer", "Grrr", 14483),
			// custom bosses
			new Boss("Corporeal Beast", null, -1, true, null, NORMAL_ARENA, 8133),
			new Boss("Jad", "Roarrrrrrrrrrrrrrrrrrrrrrrrrr", 2745), new Boss("KalphiteQueen", null, 1158),
			new Boss("King Black Dragon", "Grrrr", 50),
			new Boss("Nomad", "You don't stand a chance!", 7985, true, null, NOMAD_ARENA, 8528), };

	private void startEnduranceMode() {
		if (progress == 256) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You have some dominion factor which you must exchange before", "starting another match.");
			player.getPackets()
					.sendGameMessage("You can't go back into the arena, you must go to the next floor on entrance.");
			return;
		}
		createArena(ENDURANCE);
	}

	public void createArena(final int mode) {
		player.closeInterfaces();
		player.lock();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					boolean needDestroy = mapBaseCoords != null;
					final int[] oldMapBaseCoords = mapBaseCoords;
					mapBaseCoords = RegionBuilder.findEmptyChunkBound(8, 8);
					RegionBuilder.copyAllPlanesMap(BOSSES[getNextBossIndex()].arena[0],
							BOSSES[getNextBossIndex()].arena[1], mapBaseCoords[0], mapBaseCoords[1], 8);
					teleportToArena(mode);
					if (needDestroy) {
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								CoresManager.slowExecutor.execute(new Runnable() {
									@Override
									public void run() {
										try {
											RegionBuilder.destroyMap(oldMapBaseCoords[0], oldMapBaseCoords[1], 8, 8);
										} catch (Exception e) {
											e.printStackTrace();
										} catch (Error e) {
											e.printStackTrace();
										}
									}
								});
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void teleportToArena(int mode) {
		player.setNextFaceWorldTile(new WorldTile(getBaseX() + 11, getBaseY() + 29, 0));
		player.getControlerManager().startControler("DTControler", mode);
		player.unlock();
		player.setNextWorldTile(new WorldTile(getBaseX() + 10, getBaseY() + 29, 2));
		player.getMusicsManager().playMusic(MUSICS[Utils.getRandom(MUSICS.length - 1)]);
	}

	public String getStartFightText(int message) {
		switch (message) {
		case 0:
			return "Kick my ass!";
		case 1:
			return "Please don't hit my face";
		case 2:
			return "Argh!";
		default:
			return "Bring it on!";
		}
	}

	public int getNextBossIndex() {
		if (nextBossIndex < 0 || nextBossIndex >= BOSSES.length)
			selectBoss();
		return nextBossIndex;
	}

	public void startFight(final NPC[] bosses) {
		for (NPC boss : bosses) {
			boss.setCantInteract(true);
			boss.setNextFaceWorldTile(new WorldTile(boss.getX() - 1, boss.getY(), 0));
		}
		player.lock();
		player.setNextWorldTile(new WorldTile(getBaseX() + 25, getBaseY() + 32, 2));
		player.setNextFaceWorldTile(new WorldTile(getBaseX() + 26, getBaseY() + 32, 0));
		final int index = getNextBossIndex();
		WorldTasksManager.schedule(new WorldTask() {

			private int count;

			@Override
			public void run() {
				if (count == 0) {
					player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0,
							1172);
					player.getPackets().sendHideIComponent(1172, 2, true);
					player.getPackets().sendHideIComponent(1172, 7, true);
					player.getPackets().sendIComponentText(1172, 4, player.getDisplayName());
					player.getPackets().sendConfig(1241, 1);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 25),
							Cutscene.getY(player, getBaseY() + 38), 1800);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 25),
							Cutscene.getY(player, getBaseY() + 29), 800);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 32),
							Cutscene.getY(player, getBaseY() + 38), 1800, 6, 6);
				} else if (count == 1) {
					player.setNextForceTalk(new ForceTalk(getStartFightText(Utils.getRandom(1))));
				} else if (count == 3) {
					player.getPackets().sendHideIComponent(1172, 2, false);
					player.getPackets().sendHideIComponent(1172, 5, true);
					player.getPackets().sendIComponentText(1172, 6, BOSSES[index].name);
					player.getPackets().sendConfig(1241, 0);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 35),
							Cutscene.getY(player, getBaseY() + 37), 1800);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 35),
							Cutscene.getY(player, getBaseY() + 28), 800);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 42),
							Cutscene.getY(player, getBaseY() + 37), 1800, 6, 6);
				} else if (count == 4) {
					if (BOSSES[index].text != null)
						bosses[0].setNextForceTalk(new ForceTalk(BOSSES[index].text));
					if (BOSSES[index].voice != -1)
						player.getPackets().sendVoice(BOSSES[index].voice);
				} else if (count == 6) {
					player.getControlerManager().sendInterfaces();
					player.getInterfaceManager().sendInterface(1172);
					player.getPackets().sendHideIComponent(1172, 2, true);
					player.getPackets().sendHideIComponent(1172, 5, true);
					player.getPackets().sendIComponentText(1172, 8, "Fight!");
					player.getPackets().sendHideIComponent(1172, 10, true);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 32),
							Cutscene.getY(player, getBaseY() + 36), 0);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 32),
							Cutscene.getY(player, getBaseY() + 16), 5000);
					player.getPackets().sendVoice(7882);
				} else if (count == 8) {
					if (nextBossIndex != -1 && BOSSES[index].item != null)
						World.addGroundItem(BOSSES[index].item, new WorldTile(getBaseX() + 26, getBaseY() + 33, 2));
					player.closeInterfaces();
					player.getPackets().sendResetCamera();
					for (NPC boss : bosses) {
						boss.setCantInteract(false);
						boss.setTarget(player);
					}
					player.unlock();
					stop();
				}
				count++;
			}

		}, 0, 1);
	}

	public void removeItem() {
		if (nextBossIndex == -1)
			return;
		if (BOSSES[nextBossIndex].item != null) {
			player.getInventory().deleteItem(BOSSES[nextBossIndex].item.getId(),
					BOSSES[nextBossIndex].item.getAmount());
			player.getEquipment().deleteItem(BOSSES[nextBossIndex].item.getId(),
					BOSSES[nextBossIndex].item.getAmount());
			player.getAppearance().generateAppearanceData();
		}
	}

	public void loss(final int mode) {
		/*
		 * if(mapBaseCoords == null) { //died on logout player.setNextWorldTile(new WorldTile(3744, 6425, 0)); player.getControlerManager().removeControlerWithoutCheck(); return; }
		 */
		removeItem();
		nextBossIndex = -1;
		player.lock();
		player.setNextWorldTile(new WorldTile(getBaseX() + 35, getBaseY() + 31, 2));
		player.setNextFaceWorldTile(new WorldTile(player.getX() + 1, player.getY(), 0));

		WorldTasksManager.schedule(new WorldTask() {
			int count;

			@Override
			public void run() {
				if (count == 0) {
					player.setNextAnimation(new Animation(836));
					player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
					player.getInterfaceManager().sendInterface(1172);
					player.getPackets().sendHideIComponent(1172, 2, true);
					player.getPackets().sendHideIComponent(1172, 5, true);
					player.getPackets().sendIComponentText(1172, 8, "Unlucky, you lost!");
					player.getPackets().sendIComponentText(1172, 10,
							"You leave with a dominion factor of: " + dominionFactor);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 35),
							Cutscene.getY(player, getBaseY() + 37), 2500);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 35),
							Cutscene.getY(player, getBaseY() + 28), 800);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 42),
							Cutscene.getY(player, getBaseY() + 37), 2500, 6, 6);
					player.getPackets().sendVoice(7874);
				} else if (count == 4) {
					player.setForceMultiArea(false);
					player.reset();
					player.setNextAnimation(new Animation(-1));
					player.closeInterfaces();
					player.getPackets().sendResetCamera();
					player.unlock();
					destroyArena(false, mode);
					stop();
				}
				count++;
			}
		}, 0, 1);
	}

	public void win(int mode) {
		removeItem();
		int factor = getBossesTotalLevel() * (mode == CLIMBER ? 100 : 10);
		progress++;
		if (mode == CLIMBER) {
			if (progress > maxFloorClimber)
				maxFloorClimber = progress;
		} else if (mode == ENDURANCE) {
			if (progress > maxFloorEndurance)
				maxFloorEndurance = progress;
		}

		killedBossesCount++;
		dominionFactor += factor;
		totalScore += factor;
		if (dominionFactor > MAX_FACTOR) {
			dominionFactor = MAX_FACTOR;
			player.getPackets()
					.sendGameMessage("You've reached the maximum Dominion Factor you can get so you should spend it!");
		}
		nextBossIndex = -1;
		player.lock();
		player.setNextWorldTile(new WorldTile(getBaseX() + 35, getBaseY() + 31, 2));
		player.setNextFaceWorldTile(new WorldTile(getBaseX() + 36, getBaseY() + 31, 0));

		WorldTasksManager.schedule(new WorldTask() {

			private int count;

			@Override
			public void run() {
				if (count == 0) {
					player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
					player.getInterfaceManager().sendInterface(1172);
					player.getPackets().sendHideIComponent(1172, 2, true);
					player.getPackets().sendHideIComponent(1172, 5, true);
					player.getPackets().sendIComponentText(1172, 8, "Yeah! You won!");
					player.getPackets().sendIComponentText(1172, 10,
							"You now have a dominion factor of: " + dominionFactor);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 35),
							Cutscene.getY(player, getBaseY() + 37), 2500);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 35),
							Cutscene.getY(player, getBaseY() + 28), 800);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 42),
							Cutscene.getY(player, getBaseY() + 37), 2500, 6, 6);
					player.getPackets().sendVoice(7897);
				} else if (count == 4) {
					player.reset();
					player.closeInterfaces();
					player.getPackets().sendResetCamera();
					player.unlock();
					stop();
				}
				count++;
			}
		}, 0, 1);

	}

	/*
	 * 4928 15936
	 */
	/*
	 * 4960, 15968
	 */

	public void destroyArena(final boolean logout, int mode) {
		WorldTile tile = new WorldTile(3744, 6425, 0);
		if (logout)
			player.setLocation(tile);
		else {
			player.getControlerManager().removeControlerWithoutCheck();
			player.lock();
			player.setNextWorldTile(tile);
			if (mode == ENDURANCE)
				progress = 0;
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				CoresManager.slowExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							RegionBuilder.destroyMap(mapBaseCoords[0], mapBaseCoords[1], 8, 8);
							if (!logout) {
								mapBaseCoords = null;
								player.unlock();
							}
						} catch (Exception e) {
							e.printStackTrace();
						} catch (Error e) {
							e.printStackTrace();
						}
					}
				});
			}
		}, 1);
	}

	public NPC[] createBosses() {
		NPC[] bosses = new NPC[BOSSES[getNextBossIndex()].ids.length];
		for (int i = 0; i < BOSSES[getNextBossIndex()].ids.length; i++)
			bosses[i] = World.spawnNPC(BOSSES[getNextBossIndex()].ids[i],
					new WorldTile(getBaseX() + 37 + (i * 2), getBaseY() + 31, 2), -1, true, true);
		return bosses;
	}

	/*
	 * 4928 15936
	 */
	/*
	 * 4961, 15968
	 */

	public int getBaseX() {
		return mapBaseCoords[0] << 3;
	}

	public int getBaseY() {
		return mapBaseCoords[1] << 3;
	}

	public void selectBoss() {
		if (nextBossIndex < 0 || nextBossIndex >= BOSSES.length)
			nextBossIndex = Utils.random(BOSSES.length);
	}

	public void openClimberMode() {
		player.getPackets().sendGameMessage("Only endurance mode is currently working.");
		// player.getInterfaceManager().sendScreenInterface(96, 1163);
		// selectBoss();
		// player.getPackets().sendIComponentText(1163, 32, "0"); // you leave
		// with
	}

	public void openEnduranceMode() {
		selectBoss();
		player.getInterfaceManager().sendScreenInterface(96, 1173);
		player.getPackets().sendIComponentText(1173, 25, BOSSES[getNextBossIndex()].name); // current
		player.getPackets().sendIComponentText(1173, 38, String.valueOf(progress + 1)); // current
		player.getPackets().sendIComponentText(1173, 52, "None. Good luck :o."); // current
		player.getPackets().sendIComponentText(1173, 29, String.valueOf(dominionFactor)); // current
		player.getPackets().sendIComponentText(1173, 31,
				dominionFactor == MAX_FACTOR ? "" : String.valueOf(getBossesTotalLevel() * 10)); // on win
		player.getPackets().sendIComponentText(1173, 33, String.valueOf(dominionFactor)); // on death
	}

	public int getBossesTotalLevel() {
		int level = 0;
		for (int id : BOSSES[getNextBossIndex()].ids)
			level = +NPCDefinitions.getNPCDefinitions(id).combatLevel;
		return level;
	}

	public void openSpecialMode() {
		player.getPackets().sendGameMessage("Only endurance mode is currently working.");
		// player.getInterfaceManager().sendScreenInterface(96, 1170);
	}

	public void openFreeStyleMode() {
		player.getPackets().sendGameMessage("Only endurance mode is currently working.");
		// player.getInterfaceManager().sendScreenInterface(96, 1168);
	}

	public void talkToFace() {
		talkToFace(false);
	}

	public void talkToFace(boolean fromDialogue) {
		if (!hasRequiriments()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need at least level 110 combat to use this tower.");
			return;
		}
		if (!talkedWithFace)
			player.getDialogueManager().startDialogue("StrangeFace");
		else {
			if (!fromDialogue)
				player.getPackets().sendVoice(7893);
			player.getInterfaceManager().sendInterface(1160);
		}
	}

	public void openRewards() {
		if (!talkedWithFace) {
			talkToFace();
			return;
		}
		player.getPackets().sendVoice(7893);
		player.getInterfaceManager().sendInterface(1156);
	}

	public void openRewardsChest() {
		if (!talkedWithFace) {
			growFace();
			return;
		}
		progress = 0;
		dominionFactor = 0;
		player.getInterfaceManager().sendInterface(1171);
	}

	public void openBankChest() {
		if (!talkedWithFace) {
			growFace();
			return;
		}
		player.getBank().openBank();
	}

	public void viewScoreBoard() {
//		DTRank.showRanks(player);
	}

	public boolean isTalkedWithFace() {
		return talkedWithFace;
	}

	public void setTalkedWithFace(boolean talkedWithFace) {
		this.talkedWithFace = talkedWithFace;
	}

	public int getProgress() {
		return progress;
	}

	public long getTotalScore() {
		return totalScore;
	}

	public int getDominionFactor() {
		return dominionFactor;
	}

	public Boss getNextBoss() {
		return BOSSES[getNextBossIndex()];
	}

	public int getMaxFloorClimber() {
		return maxFloorClimber;
	}

	public int getMaxFloorEndurance() {
		return maxFloorEndurance;
	}

	public int getKilledBossesCount() {
		return killedBossesCount;
	}

}
