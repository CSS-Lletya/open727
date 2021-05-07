package npc.qbd;

import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.controlers.Controler;
import com.rs.game.task.Task;
import com.rs.utils.Logger;

import skills.magic.Magic;

/**
 * The Queen Black Dragon controler.
 * @author Emperor
 *
 */
public final class QueenBlackDragonController extends Controler {

	public static final WorldTile OUTSIDE = Settings.RESPAWN_PLAYER_LOCATION;

	/**
	 * The platform steps offsets.
	 */
	private static final int[][][] PLATFORM_STEPS = {
			{ { 88, 86 }, { 88, 87 }, { 88, 88 }, { 88, 89 }, { 88, 90 }, { 88, 91 }, { 89, 91 }, { 89, 90 },
					{ 89, 89 }, { 89, 88 }, { 89, 87 }, { 89, 86 }, { 90, 86 }, { 90, 87 }, { 90, 88 }, { 90, 89 },
					{ 90, 90 }, { 90, 91 }, { 91, 91 }, { 91, 90 }, { 91, 89 }, { 91, 88 }, { 91, 87 }, { 92, 87 },
					{ 92, 88 }, { 92, 89 }, { 92, 90 }, { 92, 91 }, { 93, 91 }, { 93, 90 }, { 93, 89 }, { 93, 88 },
					{ 94, 88 }, { 94, 89 }, { 94, 90 }, { 94, 91 }, { 95, 91 }, { 95, 90 }, { 95, 89 }, { 96, 89 },
					{ 96, 90 }, { 96, 91 }, { 97, 91 }, { 97, 90 }, { 98, 90 }, { 98, 91 }, { 99, 91 } },
			{ { 106, 91 }, { 106, 90 }, { 106, 89 }, { 106, 88 }, { 106, 87 }, { 106, 86 }, { 105, 86 }, { 105, 87 },
					{ 105, 88 }, { 105, 89 }, { 105, 90 }, { 105, 91 }, { 104, 91 }, { 104, 90 }, { 104, 89 },
					{ 104, 88 }, { 104, 87 }, { 104, 86 }, { 103, 87 }, { 103, 88 }, { 103, 89 }, { 103, 90 },
					{ 103, 91 }, { 102, 91 }, { 102, 90 }, { 102, 89 }, { 102, 88 }, { 102, 87 }, { 101, 88 },
					{ 101, 89 }, { 101, 90 }, { 101, 91 }, { 100, 91 }, { 100, 90 }, { 100, 89 }, { 100, 88 },
					{ 99, 88 }, { 99, 89 }, { 99, 90 }, { 98, 89 } },
			{ { 99, 90 }, { 100, 90 }, { 100, 89 }, { 99, 89 }, { 98, 89 }, { 97, 89 }, { 95, 88 }, { 96, 88 },
					{ 97, 88 }, { 98, 88 }, { 99, 88 }, { 99, 87 }, { 98, 87 }, { 97, 87 }, { 96, 87 }, { 96, 86 },
					{ 97, 86 }, { 98, 86 } } };

	/**
	 * The current count of standing on a platform.
	 */
	private int platformStand;

	/**
	 * The Queen Black Dragon NPC.
	 */
	private QueenBlackDragon npc;

	/**
	 * The region base.
	 */
	private int[] regionBase;

	/**
	 * The base location of the region.
	 */
	private WorldTile base;

	@Override
	public void start() {
		player.lock();
		final long time = FadingScreen.fade(player);
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					regionBase = RegionBuilder.findEmptyChunkBound(8, 8);
					base = new WorldTile(regionBase[0] << 3, regionBase[1] << 3, 1);
					RegionBuilder.copyAllPlanesMap(176, 792, regionBase[0], regionBase[1], 8, 8);
					FadingScreen.unfade(player, time, new Runnable() {
						@Override
						public void run() {
							npc = new QueenBlackDragon(player, base.transform(31, 37, 0), base);
							player.setNextWorldTile(base.transform(33, 28, 0));
							player.setLargeSceneView(true);
							player.setForceMultiArea(true);
							player.unlock();
							player.getPackets().sendGlobalConfig(184, 150);
							player.getPackets().sendGlobalConfig(1924, 0);
							player.getPackets().sendGlobalConfig(1925, 0);
							player.getInterfaceManager()
									.sendTab(player.getInterfaceManager().hasRezizableScreen() ? 1 : 0, 1285);// 1133);
							player.getMusicsManager().playMusic(1119); // AWOKEN
						}
					});

				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		});
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (npc == null) {
			return true;
		}
		if (object.getId() == 70790) {
			if (npc.getPhase() < 5) {
				return true;
			}
			player.lock();
			FadingScreen.fade(player, new Runnable() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage(
							"You descend the stairs that appeared when you defeated the Queen Black Dragon.");
					player.getPackets().sendGlobalConfig(184, -1);
					npc.finish();
					RegionBuilder.copyAllPlanesMap(160, 760, regionBase[0], regionBase[1], 8, 8);
					player.setForceNextMapLoadRefresh(true);
					player.loadMapRegions();
					player.getPackets().sendDestroyObject(new WorldObject(70849, (short) 10, (short) 0, base.transform(24, 21, -1)));
					player.getPackets().sendDestroyObject(new WorldObject(70837, (short) 10,  (short) 0, base.transform(22, 24, -1)));
					player.getPackets().sendDestroyObject(new WorldObject(70840, (short) 10, (short) 0, base.transform(34, 24, -1)));
					WorldObject.removeObject(new WorldObject(70775, (short) 10, (short) 0, base.transform(31, 29, -1)), true);
					player.getPackets().sendDestroyObject(new WorldObject(70822, (short) 10, (short) 0, base.transform(21, 35, -1)));
					player.getPackets().sendDestroyObject(new WorldObject(70818, (short) 10, (short) 0, base.transform(39, 35, -1)));
					player.setNextWorldTile(base.transform(31, 36, -1));
					boolean resizable = player.getInterfaceManager().hasRezizableScreen();
					player.getPackets().closeInterface(resizable ? 746 : 548, resizable ? 1 : 0);
					player.unlock();
				}
			});
			return false;
		}
		if (object.getId() == 70813) {
			Magic.sendObjectTeleportSpell(player, true, Settings.RESPAWN_PLAYER_LOCATION);
			return false;
		}
		if (object.getId() == 70814) {
			player.getPackets().sendGameMessage("The gate is locked.");
			return false;
		}
		if (object.getId() == 70815) {
//			player.getDialogueManager().startDialogue("RewardChest", npc);
			//TODO: Dialogue
			return false;
		}
		if (object.getId() == 70817) {
			npc.openRewardChest(false);
			return false;
		}
		if (object.getId() == npc.getActiveArtifact().getId()) {
			player.getMusicsManager().playMusic(1118); // QUEEN BLACK DRAGON
			npc.setSpawningWorms(false);
			npc.setNextAttack(20);
			npc.setActiveArtifact(new WorldObject(object.getId() + 1, (short) 10, (short) 0, object));
			npc.setHitpoints(npc.getMaxHitpoints());
			npc.setCantInteract(false);
			npc.setPhase(npc.getPhase() + 1);
			WorldObject.createObject(npc.getActiveArtifact(), false);
			switch (object.getId()) {
			case 70777:
				player.getPackets().sendGlobalConfig(1924, 2);
				player.getPackets().sendSpawnedObject(new WorldObject(70843, (short) 10, (short) 0, base.transform(24, 21, -1)));
				break;
			case 70780:
				player.getPackets().sendGlobalConfig(1924, 4);
				player.getPackets().sendSpawnedObject(new WorldObject(70845, (short) 10, (short) 0, base.transform(24, 21, -1)));
				break;
			case 70783:
				player.getPackets().sendGlobalConfig(1924, 6);
				player.getPackets().sendSpawnedObject(new WorldObject(70847, (short) 10, (short) 0, base.transform(24, 21, -1)));
				break;
			case 70786:
				player.getPackets().sendGlobalConfig(1924, 8);
				player.getPackets().sendSpawnedObject(new WorldObject(70849, (short) 10, (short) 0, base.transform(24, 21, -1)));
				break;
			}
			return false;
		}
		return true;
	}

	@Override
	public void process() {
		if (npc == null)
			return;
		if (player.getY() < base.getY() + 28) {
			if (npc.hasFinished()) {
				return;
			}
			if (platformStand++ == 3) {
				player.getPackets()
						.sendGameMessage("You are damaged for standing too long on the raw magical platforms.");
				player.applyHit(new Hit(npc, 200, HitLook.REGULAR_DAMAGE));
				platformStand = 0;
			}
		} else {
			platformStand = 0;
		}
	}

	@Override
	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if (npc != null && nextY < base.getY() + 28) {
			if (npc.getPhase() > 1) {
				for (int[] step : PLATFORM_STEPS[0]) {
					if (base.getX() + (step[0] - 64) == nextX && base.getY() + (step[1] - 64) == nextY) {
						return true;
					}
				}
				if (npc.getPhase() > 2) {
					for (int[] step : PLATFORM_STEPS[1]) {
						if (base.getX() + (step[0] - 64) == nextX && base.getY() + (step[1] - 64) == nextY) {
							return true;
						}
					}
					if (npc.getPhase() > 3) {
						for (int[] step : PLATFORM_STEPS[2]) {
							if (base.getX() + (step[0] - 64) == nextX && base.getY() + (step[1] - 64) == nextY) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (npc == null) {
			return true;
		}
		switch (interfaceId) {
		case 1284:
			switch (componentId) {
			case 8:
				player.getBank().addItems(npc.getRewards().toArray(), false);
				npc.getRewards().clear();
				player.getPackets().sendGameMessage("All the items were moved to your bank.");
				break;
			case 9:
				npc.getRewards().clear();
				player.getPackets().sendGameMessage("All the items were removed from the chest.");
				break;
			case 10:
				for (int slot = 0; slot < npc.getRewards().toArray().length; slot++) {
					Item item = npc.getRewards().get(slot);
					if (item == null) {
						continue;
					}
					boolean added = true;
					if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
						added = player.getInventory().addItem(item);
						if (added) {
							npc.getRewards().toArray()[slot] = null;
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (!player.getInventory().addItem(single)) {
								added = false;
								break;
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.getPackets().sendGameMessage(
								"You only had enough space in your inventory to accept some of the items.");
						break;
					}
				}
				break;
			case 7:
				Item item = npc.getRewards().get(slotId);
				if (item == null) {
					return true;
				}
				switch (packetId) {
				case 52:
					player.getPackets().sendGameMessage("It's a " + item.getDefinitions().getName());
					return false;
				case 4:
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 64:
					player.getBank().addItems(new Item[] { npc.getRewards().toArray()[slotId] }, false);
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 61:
					boolean added = true;
					if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
						added = player.getInventory().addItem(item);
						if (added) {
							npc.getRewards().toArray()[slotId] = null;
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (!player.getInventory().addItem(single)) {
								added = false;
								break;
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.getPackets().sendGameMessage(
								"You only had enough space in your inventory to accept some of the items.");
						break;
					}
					break;
				default:
					return true;
				}
				break;
			default:
				return true;
			}
			npc.openRewardChest(false);
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		end(0);
	}

	@Override
	public boolean sendDeath() {
		World.get().submit(new Task(6) {
			@Override
			protected void execute() {
				end(0);
				this.cancel();
			}
		});
		return true;
	}

	@Override
	public boolean logout() {
		end(1);
		return false;
	}

	@Override
	public void forceClose() {
		end(0);
	}

	/**
	 * Ends the controler. 0 - tele 1 - logout
	 */
	private void end(int type) {
		player.setForceMultiArea(false);
		player.setLargeSceneView(false);
		if (type == 0) {
			boolean resizable = player.getInterfaceManager().hasRezizableScreen();
			player.getPackets().closeInterface(resizable ? 746 : 548, resizable ? 1 : 0);
			player.getPackets().sendGlobalConfig(184, -1);
		} else {
			player.setLocation(OUTSIDE);
		}
		removeControler();
		if (npc != null)
			player.getBank().addItems(npc.getRewards().toArray(), false);
		/*
		 * 1200 delay because of leaving
		 */
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(regionBase[0], regionBase[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);

	}

	/**
	 * Gets the base world tile.
	 * @return The base.
	 */
	public WorldTile getBase() {
		return base;
	}

	/**
	 * Gets the npc.
	 * @return The npc.
	 */
	public QueenBlackDragon getNpc() {
		return npc;
	}

}