package com.rs.game.player.controlers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.nomad.Nomad;
import com.rs.game.player.Player;
import com.rs.game.player.QuestManager.Quests;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.content.Magic;
import com.rs.game.player.cutscenes.Cutscene;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class NomadsRequiem extends Controler {

	public static final WorldTile OUTSIDE = new WorldTile(1895, 3177, 0);

	private DungeonPart currentPart;
	private int[] mapBaseChunks;

	public static void enterNomadsRequiem(Player player) {
		player.getControlerManager().startControler("NomadsRequiem");
	}

	@Override
	public void start() {
		enter(DungeonPart.ENTRANCE, 0);
		player.setForceMultiArea(true);
	}

	public int getBaseX() {
		return mapBaseChunks[0] << 3;
	}

	public int getBaseY() {
		return mapBaseChunks[1] << 3;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		case 47981:
			enter(DungeonPart.HALL, 0);
			return false;
		case 47983:
			enter(DungeonPart.ENTRANCE, 1);
			return false;
		case 47974:
			enter(DungeonPart.THRONE, 0);
			return false;
		case 47976:
			enter(DungeonPart.HALL, 1);
			return false;
		case 47980:
			player.lock();
			player.setNextAnimation(new Animation(828));
			FadingScreen.fade(player, new Runnable() {

				@Override
				public void run() {
					player.getControlerManager().forceStop();
					player.unlock();
				}

			});
			return false;
		default:
			return true;
		}
	}

	public static enum DungeonPart {
		ENTRANCE(409, 733, 7, 3, 46, 12, 23, 21), HALL(408, 736, 8, 8, 31, 2, 31, 15), THRONE(418, 730, 5, 5, 16, 2);

		private int chunkX, chunkY, sizeX, sizeY;
		private int[] doorPositions;

		private DungeonPart(int chunkX, int chunkY, int sizeX, int sizeY, int... doorPositions) {
			this.chunkX = chunkX;
			this.chunkY = chunkY;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.doorPositions = doorPositions;
		}

	}

	/*
	 * private void teletoDoor(Player player, int id) { player.setNextWorldTile(new WorldTile(baseDoors[id*2], baseDoors[id*2]+1, 0)); }
	 */

	public void enterDoor(int doorIndex) {
		player.setNextWorldTile(new WorldTile(getBaseX() + currentPart.doorPositions[doorIndex * 2],
				getBaseY() + +currentPart.doorPositions[doorIndex * 2 + 1], 0));
		player.getMusicsManager().playMusic(currentPart == DungeonPart.THRONE ? 727 : 728);
		if (currentPart == DungeonPart.THRONE)
			startThroneScene();
		else if (currentPart == DungeonPart.ENTRANCE && doorIndex == 0
				&& player.getQuestManager().getQuestStage(Quests.NOMADS_REQUIEM) == 0)
			sendFirstScene();
		else {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.unlock();
				}
			}, 1);
		}
	}

	@Override
	public boolean sendDeath() {
		if (currentPart == DungeonPart.THRONE) {
			Dialogue.closeNoContinueDialogue(player);
			List<Integer> indexes = World.getRegion(player.getRegionId()).getNPCsIndexes();
			if (indexes != null && indexes.size() >= 1) {
				NPC nomad = World.getNPCs().get(indexes.get(0));
				if (nomad != null) {
					player.getPackets().sendVoice(7987);
					nomad.setNextForceTalk(new ForceTalk("Pathetic!"));
				}
			}
		}
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					player.setNextWorldTile(OUTSIDE);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.setForceMultiArea(false);
					player.getPackets().sendMusicEffect(90);
					removeControler();
					destroyCurrentPart();
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	public void sendFirstScene() {
		player.getQuestManager().setQuestStage(Quests.NOMADS_REQUIEM, 1);
		final NPC nomad = World.spawnNPC(8531, new WorldTile(getBaseX() + 41, getBaseY() + 11, 0), -1, true, true);
		nomad.setRun(true);
		nomad.setDirection(Utils.getFaceDirection(1, 0));
		WorldTasksManager.schedule(new WorldTask() {
			private int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.getPackets().sendVoice(7985);
					nomad.setNextForceTalk(new ForceTalk("You don't stand a chance " + player.getDisplayName() + "!"));
					Dialogue.sendNPCDialogueNoContinue(player, nomad.getId(), 9827,
							"You don't stand a chance " + player.getDisplayName() + "!");
				} else if (stage == 6) {
					player.setNextFaceWorldTile(nomad);
					player.setNextForceTalk(new ForceTalk("What? Who are you?"));
				} else if (stage == 12) {
					/*
					 * nomad.setNextAnimation(new Animation(12729)); nomad.setNextGraphics(new Graphics(1576));
					 */
					WorldTile walk = new WorldTile(getBaseX() + 35, getBaseY() + 11, 0);
					nomad.addWalkSteps(walk.getX(), walk.getY(), -1, false);
					nomad.setNextForceTalk(new ForceTalk("You'll find out... soon."));
					Dialogue.sendNPCDialogueNoContinue(player, nomad.getId(), 9827, "You'll find out... soon.");
				} else if (stage == 16) {
					nomad.finish();
					player.getDialogueManager().startDialogue("SimplePlayerMessage",
							"Maybe I should follow that guy..");
					Dialogue.closeNoContinueDialogue(player);
					player.unlock();
					stop();
				}
				stage++;
			}
		}, 1, 0);
	}

	public void startThroneScene() {
		final WorldObject object = new WorldObject(48072, 10, 0, getBaseX() + 14, getBaseY() + 20, 0);
		World.spawnObject(object, false);
		final NomadsRequiem requiem = this;
		WorldTasksManager.schedule(new WorldTask() {

			private int stage;

			@Override
			public void run() {
				if (stage == 1) {
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 19),
							Cutscene.getY(player, getBaseY() + 14), 3000);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 17),
							Cutscene.getY(player, getBaseY() + 5), 2000);
					player.setRun(false);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 23),
							Cutscene.getY(player, getBaseY() + 8), 2500, 4, 4);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 17),
							Cutscene.getY(player, getBaseY() + 14), 2000, 2, 2);
					player.addWalkSteps(getBaseX() + 16, getBaseY() + 19, -1, false);
				} else if (stage == 10) {
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 16),
							Cutscene.getY(player, getBaseY() + 8), 2500, 4, 4);
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 16),
							Cutscene.getY(player, getBaseY() + 14), 2000, 2, 2);
				} else if (stage == 15) {
					player.getPackets().sendCameraLook(Cutscene.getX(player, getBaseX() + 16),
							Cutscene.getY(player, getBaseY() + 21), 800, 6, 6);
					player.getPackets().sendCameraPos(Cutscene.getX(player, getBaseX() + 16),
							Cutscene.getY(player, getBaseY() + 14), 1800, 6, 6);
				} else if (stage == 20) {
					player.getDialogueManager().startDialogue("NomadThrone", 8528, requiem);
					player.getPackets().sendResetCamera();
					stop();
				}
				stage++;
			}
		}, 0, 0);
	}

	@Override
	public void forceClose() {
		leave(false);
	}

	@Override
	public boolean logout() {
		leave(true);
		return true;
	}

	@Override
	public boolean login() {
		leave(false);
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		if (type != Magic.OBJECT_TELEPORT)
			leave(false);
	}

	public void leave(boolean logout) {
		if (logout)
			player.setLocation(OUTSIDE);
		else {
			player.setNextWorldTile(OUTSIDE);
			player.setForceMultiArea(false);
		}
		if (mapBaseChunks != null && currentPart != null)
			destroyCurrentPart();
	}

	public void continueThroneScene() {
		final WorldObject object = new WorldObject(48072, 10, 0, getBaseX() + 14, getBaseY() + 20, 0);
		WorldTasksManager.schedule(new WorldTask() {

			private int stage;

			@Override
			public void run() {
				if (stage == 1) {
					World.sendObjectAnimation(player, object, new Animation(12703));
				} else if (stage == 4) {
					player.setNextAnimation(new Animation(7272));
				} else if (stage == 8) {
					WorldTile throne = new WorldTile(getBaseX() + 16, getBaseY() + 20, 0);
					Nomad nomad = (Nomad) World.spawnNPC(8528, throne, -1, true, true);
					nomad.setDirection(Utils.getFaceDirection(0, -1));
					nomad.setThroneTile(throne);
					nomad.setTarget(player);
					World.destroySpawnedObject(object, false);
					player.getPackets()
							.sendSpawnedObject(new WorldObject(48073, 10, 0, getBaseX() + 14, getBaseY() + 22, 0));
					player.unlock();
					player.setRun(true);
					stop();
				}
				stage++;
			}
		}, 0, 0);
	}

	public void enter(final DungeonPart part, final int doorIndex) {
		player.lock();
		final long time = FadingScreen.fade(player);
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final int[] oldMapBaseChunks = mapBaseChunks;
					final DungeonPart oldPart = currentPart;
					mapBaseChunks = RegionBuilder.findEmptyChunkBound(part.sizeX, part.sizeY);
					currentPart = part;
					RegionBuilder.copyAllPlanesMap(part.chunkX, part.chunkY, mapBaseChunks[0], mapBaseChunks[1],
							part.sizeX, part.sizeY);

					FadingScreen.unfade(player, time, new Runnable() {
						@Override
						public void run() {
							destroyPart(oldMapBaseChunks, oldPart);
							enterDoor(doorIndex);
						}
					});

				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		});
	}

	public void destroyCurrentPart() {
		destroyPart(mapBaseChunks, currentPart);
	}

	public void destroyPart(final int[] mapBaseChunks, final DungeonPart part) {
		// since it will change after
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(mapBaseChunks[0], mapBaseChunks[1], part.sizeX, part.sizeY);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}

}
