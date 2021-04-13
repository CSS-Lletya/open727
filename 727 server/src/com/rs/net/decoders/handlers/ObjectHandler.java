package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.Crucible;
import com.rs.game.minigames.FightPits;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Player;
import com.rs.game.player.QuestManager.Quests;
import com.rs.game.player.RouteEvent;
import com.rs.game.player.actions.CowMilkingAction;
import com.rs.game.player.content.Hunter;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.PartyRoom;
import com.rs.game.player.content.Runecrafting;
import com.rs.game.player.controlers.Falconry;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.NomadsRequiem;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.dialogues.MiningGuildDwarf;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.PkRank;
import com.rs.utils.Utils;

import player.PlayerCombat;
import skills.Skills;
import skills.agility.BarbarianOutpostAgility;
import skills.agility.GnomeAgility;
import skills.cooking.Cooking;
import skills.cooking.Cooking.Cookables;
import skills.firemaking.Bonfire;
import skills.hunter.BoxAction.HunterEquipment;
import skills.hunter.BoxAction.HunterNPC;
import skills.mining.EssenceMining;
import skills.mining.Mining;
import skills.mining.MiningBase;
import skills.mining.EssenceMining.EssenceDefinitions;
import skills.mining.Mining.RockDefinitions;
import skills.runecrafting.SihponActionNodes;
import skills.smithing.Smithing.ForgingBar;
import skills.smithing.Smithing.ForgingInterface;
import skills.thieving.Thieving;
import skills.woodcutting.Woodcutting;
import skills.woodcutting.Woodcutting.TreeDefinitions;

public final class ObjectHandler {

	private ObjectHandler() {

	}

	public static void handleOption(final Player player, InputStream stream, int option) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;
		
//		boolean forceRun = stream.readUnsignedByte128() == 1;
//		final int id = stream.readIntLE();
//		int x = stream.readUnsignedShortLE();
//		int y = stream.readUnsignedShortLE128();
		int y = stream.readUnsignedShort();
		int x = stream.readUnsignedShort();
		final int id = stream.readInt();
		boolean forceRun = stream.readUnsignedByte128() == 1;
		
		System.out.println(x+", "+ y +", "+id +", "+forceRun);
		
		
		int rotation = 0;
		if (player.isAtDynamicRegion()) {
			rotation = World.getRotation(player.getPlane(), x, y);
			if (rotation == 1) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
				y += defs.getSizeY() - 1;
			} else if (rotation == 2) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
				x += defs.getSizeY() - 1;
			}
		}
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id)
			return;
		if (player.isAtDynamicRegion() && World.getRotation(player.getPlane(), x, y) != 0) { // temp fix
			ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
			if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
				for (int xs = 0; xs < defs.getSizeX() + 1 && (mapObject == null || mapObject.getId() != id); xs++) {
					for (int ys = 0; ys < defs.getSizeY() + 1 && (mapObject == null || mapObject.getId() != id); ys++) {
						tile.setLocation(x + xs, y + ys, tile.getPlane());
						mapObject = World.getRegion(regionId).getObject(id, tile);
					}
				}
			}
			if (mapObject == null || mapObject.getId() != id)
				return;
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(), (mapObject.getRotation() + rotation % 4), x, y,
						player.getPlane());
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		switch (option) {
		case 1:
			handleOption1(player, object);
			break;
		case 2:
			handleOption2(player, object);
			break;
		case 3:
			handleOption3(player, object);
			break;
		case 4:
			handleOption4(player, object);
			break;
		case 5:
			handleOption5(player, object);
			break;
		case -1:
			handleOptionExamine(player, object);
			break;
		}
	}

	private static void handleOption1(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		final int x = object.getX();
		final int y = object.getY();
		if (SihponActionNodes.siphon(player, object))
			return;
		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick1(object))
					return;
				if (CastleWars.handleObjects(player, id))
					return;
				if (object.getId() == 19205)
					Hunter.createLoggedObject(player, object, true);
				HunterNPC hunterNpc = HunterNPC.forObjectId(id);
				if (hunterNpc != null) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(hunterNpc.getEquipment().getPickUpAnimation());
						player.getInventory().getItems().addAll(hunterNpc.getItems());
						player.getInventory().addItem(hunterNpc.getEquipment().getId(), 1);
						player.getSkills().addXp(Skills.HUNTER, hunterNpc.getXp());
					} else {
						player.getPackets().sendGameMessage("This isn't your trap.");
					}
				} else if (id == HunterEquipment.BOX.getObjectId() || id == 19192) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(new Animation(5208));
						player.getInventory().addItem(HunterEquipment.BOX.getId(), 1);
					} else
						player.getPackets().sendGameMessage("This isn't your trap.");
				} else if (id == HunterEquipment.BRID_SNARE.getObjectId() || id == 19174) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(new Animation(5207));
						player.getInventory().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
					} else
						player.getPackets().sendGameMessage("This isn't your trap.");
				} else if (id == 2350 && (object.getX() == 3352 && object.getY() == 3417 && object.getPlane() == 0))
					player.useStairs(832, new WorldTile(3177, 5731, 0), 1, 2);
				else if (id == 2353 && (object.getX() == 3177 && object.getY() == 5730 && object.getPlane() == 0))
					player.useStairs(828, new WorldTile(3353, 3416, 0), 1, 2);
				else if (id == 11554 || id == 11552)
					player.getPackets().sendGameMessage("That rock is currently unavailable.");
				else if (id == 38279)
					player.getDialogueManager().startDialogue("RunespanPortalD");
				else if (id == 2491)
					player.getActionManager()
							.setAction(new EssenceMining(object,
									player.getSkills().getLevel(Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
											: EssenceDefinitions.Pure_Essence));
				else if (id == 2478)
					Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2, 22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77,
							88, 9, 99, 10);
				else if (id == 2479)
					Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14, 2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98,
							8);
				else if (id == 2480)
					Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2, 38, 3, 57, 4, 76, 5, 95, 6);
				else if (id == 2481)
					Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26, 2, 52, 3, 78, 4);
				else if (id == 2482)
					Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2, 70, 3);
				else if (id == 2483)
					Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46, 2, 92, 3);
				else if (id == 2484)
					Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
				else if (id == 2487)
					Runecrafting.craftEssence(player, 562, 35, 8.5, true, 74, 2);
				else if (id == 17010)
					Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82, 2);
				else if (id == 2486)
					Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
				else if (id == 2485)
					Runecrafting.craftEssence(player, 563, 50, 9.5, true);
				else if (id == 2488)
					Runecrafting.craftEssence(player, 560, 65, 10, true);
				else if (id == 30624)
					Runecrafting.craftEssence(player, 565, 77, 10.5, true);
				else if (id == 2452) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.AIR_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1438, 1))
						Runecrafting.enterAirAltar(player);
				} else if (id == 2455) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.EARTH_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1440, 1))
						Runecrafting.enterEarthAltar(player);
				} else if (id == 2456) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.FIRE_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1442, 1))
						Runecrafting.enterFireAltar(player);
				} else if (id == 2454) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.WATER_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1444, 1))
						Runecrafting.enterWaterAltar(player);
				} else if (id == 2457) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.BODY_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1446, 1))
						Runecrafting.enterBodyAltar(player);
				} else if (id == 2453) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.MIND_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1448, 1))
						Runecrafting.enterMindAltar(player);
				} else if (id == 47120) { // zaros altar
					// recharge if needed
					if (player.getPrayer().getPrayerpoints() < player.getSkills().getLevelForXp(Skills.PRAYER) * 10) {
						player.lock(12);
						player.setNextAnimation(new Animation(12563));
						player.getPrayer()
								.setPrayerpoints((int) ((player.getSkills().getLevelForXp(Skills.PRAYER) * 10) * 1.15));
						player.getPrayer().refreshPrayerPoints();
					}
					player.getDialogueManager().startDialogue("ZarosAltar");
				} else if (id == 19222)
					Falconry.beginFalconry(player);
				else if (id == 36786)
					player.getDialogueManager().startDialogue("Banker", 4907);
				else if (id == 42377 || id == 42378)
					player.getDialogueManager().startDialogue("Banker", 2759);
				else if (id == 42217 || id == 782 || id == 34752)
					player.getDialogueManager().startDialogue("Banker", 553);
				else if (id == 57437)
					player.getBank().openBank();
				else if (id == 42425 && object.getX() == 3220 && object.getY() == 3222) { // zaros portal
					player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5,
							"And you find yourself into a digsite.");
					player.addWalkSteps(3222, 3223, -1, false);
					player.getPackets().sendGameMessage("You examine portal and it aborves you...");
				} else if (id == 9356)
					FightCaves.enterFightCaves(player);
				else if (id == 68107)
					FightKiln.enterFightKiln(player, false);
				else if (id == 68223)
					FightPits.enterLobby(player, false);
				else if (id == 46500 && object.getX() == 3351 && object.getY() == 3415) { // zaros portal
					player.useStairs(-1, new WorldTile(Settings.RESPAWN_PLAYER_LOCATION.getX(),
							Settings.RESPAWN_PLAYER_LOCATION.getY(), Settings.RESPAWN_PLAYER_LOCATION.getPlane()), 2, 3,
							"You found your way back to home.");
					player.addWalkSteps(3351, 3415, -1, false);
				} else if (id == 9293) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
						player.getPackets().sendGameMessage("You need an agility level of 70 to use this obstacle.",
								true);
						return;
					}
					int x = player.getX() == 2886 ? 2892 : 2886;
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							player.setNextAnimation(new Animation(844));
							if (count++ == 1)
								stop();
						}

					}, 0, 0);
					player.setNextForceMovement(
							new ForceMovement(new WorldTile(x, 9799, 0), 3, player.getX() == 2886 ? 1 : 3));
					player.useStairs(-1, new WorldTile(x, 9799, 0), 3, 4);
				} else if (id == 29370 && (object.getX() == 3150 || object.getX() == 3153) && object.getY() == 9906) { // edgeville dungeon cut
					if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
						player.getPackets().sendGameMessage("You need an agility level of 53 to use this obstacle.");
						return;
					}
					final boolean running = player.getRun();
					player.setRunHidden(false);
					player.lock(8);
					player.addWalkSteps(x == 3150 ? 3155 : 3149, 9906, -1, false);
					player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
					WorldTasksManager.schedule(new WorldTask() {
						boolean secondloop;

						@Override
						public void run() {
							if (!secondloop) {
								secondloop = true;
								player.getAppearance().setRenderEmote(295);
							} else {
								player.getAppearance().setRenderEmote(-1);
								player.setRunHidden(running);
								player.getSkills().addXp(Skills.AGILITY, 7);
								stop();
							}
						}
					}, 0, 5);
				}
				// start forinthry dungeon
				else if (id == 18341 && object.getX() == 3036 && object.getY() == 10172)
					player.useStairs(-1, new WorldTile(3039, 3765, 0), 0, 1);
				else if (id == 20599 && object.getX() == 3038 && object.getY() == 3761)
					player.useStairs(-1, new WorldTile(3037, 10171, 0), 0, 1);
				else if (id == 18342 && object.getX() == 3075 && object.getY() == 10057)
					player.useStairs(-1, new WorldTile(3071, 3649, 0), 0, 1);
				else if (id == 20600 && object.getX() == 3072 && object.getY() == 3648)
					player.useStairs(-1, new WorldTile(3077, 10058, 0), 0, 1);
				// nomads requiem
				else if (id == 18425 && !player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM))
					NomadsRequiem.enterNomadsRequiem(player);
				else if (id == 42219) {
					player.useStairs(-1, new WorldTile(1886, 3178, 0), 0, 1);
					if (player.getQuestManager().getQuestStage(Quests.NOMADS_REQUIEM) == -2) // for now, on future talk with npc + quest reqs
						player.getQuestManager().setQuestStageAndRefresh(Quests.NOMADS_REQUIEM, 0);
				} else if (id == 8689)
					player.getActionManager().setAction(new CowMilkingAction());
				else if (id == 42220)
					player.useStairs(-1, new WorldTile(3082, 3475, 0), 0, 1);
				// start falador mininig
				else if (id == 30942 && object.getX() == 3019 && object.getY() == 3450)
					player.useStairs(828, new WorldTile(3020, 9850, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3019 && object.getY() == 9850)
					player.useStairs(833, new WorldTile(3018, 3450, 0), 1, 2);
				else if (id == 31002 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS))
					player.useStairs(833, new WorldTile(2998, 3452, 0), 1, 2);
				else if (id == 31012 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS))
					player.useStairs(828, new WorldTile(2996, 9845, 0), 1, 2);
				else if (id == 30943 && object.getX() == 3059 && object.getY() == 9776)
					player.useStairs(-1, new WorldTile(3061, 3376, 0), 0, 1);
				else if (id == 30944 && object.getX() == 3059 && object.getY() == 3376)
					player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
				else if (id == 2112 && object.getX() == 3046 && object.getY() == 9756) {
					if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage",
								MiningGuildDwarf.getClosestDwarfID(player),
								"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() - 1,
							object.getX(), object.getY() + 1, object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(3046, player.getY() > object.getY() ? object.getY() : object.getY() + 1, -1,
								false);
					}
				} else if (id == 2113) {
					if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage",
								MiningGuildDwarf.getClosestDwarfID(player),
								"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					player.useStairs(-1, new WorldTile(3021, 9739, 0), 0, 1);
				} else if (id == 6226 && object.getX() == 3019 && object.getY() == 9740)
					player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3019 && object.getY() == 9738)
					player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3018 && object.getY() == 9739)
					player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3020 && object.getY() == 9739)
					player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
				else if (id == 30963)
					player.getBank().openBank();
				else if (id == 6045)
					player.getPackets().sendGameMessage("You search the cart but find nothing.");
				else if (id == 5906) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
						player.getPackets().sendGameMessage("You need an agility level of 42 to use this obstacle.");
						return;
					}
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							if (count == 0) {
								player.setNextAnimation(new Animation(2594));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 2) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2),
										object.getY(), 0);
								player.setNextWorldTile(tile);
							} else if (count == 5) {
								player.setNextAnimation(new Animation(2590));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 7) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5),
										object.getY(), 0);
								player.setNextWorldTile(tile);
							} else if (count == 10) {
								player.setNextAnimation(new Animation(2595));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 12) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6),
										object.getY(), 0);
								player.setNextWorldTile(tile);
							} else if (count == 14) {
								stop();
								player.unlock();
							}
							count++;
						}

					}, 0, 0);
					// BarbarianOutpostAgility start
				} else if (id == 20210)
					BarbarianOutpostAgility.enterObstaclePipe(player, object);
				else if (id == 43526)
					BarbarianOutpostAgility.swingOnRopeSwing(player, object);
				else if (id == 43595 && x == 2550 && y == 3546)
					BarbarianOutpostAgility.walkAcrossLogBalance(player, object);
				else if (id == 20211 && x == 2538 && y == 3545)
					BarbarianOutpostAgility.climbObstacleNet(player, object);
				else if (id == 2302 && x == 2535 && y == 3547)
					BarbarianOutpostAgility.walkAcrossBalancingLedge(player, object);
				else if (id == 1948)
					BarbarianOutpostAgility.climbOverCrumblingWall(player, object);
				else if (id == 43533)
					BarbarianOutpostAgility.runUpWall(player, object);
				else if (id == 43597)
					BarbarianOutpostAgility.climbUpWall(player, object);
				else if (id == 43587)
					BarbarianOutpostAgility.fireSpringDevice(player, object);
				else if (id == 43527)
					BarbarianOutpostAgility.crossBalanceBeam(player, object);
				else if (id == 43531)
					BarbarianOutpostAgility.jumpOverGap(player, object);
				else if (id == 43532)
					BarbarianOutpostAgility.slideDownRoof(player, object);

				// rock living caverns
				else if (id == 45077) {
					player.lock();
					if (player.getX() != object.getX() || player.getY() != object.getY())
						player.addWalkSteps(object.getX(), object.getY(), -1, false);
					WorldTasksManager.schedule(new WorldTask() {

						private int count;

						@Override
						public void run() {
							if (count == 0) {
								player.setNextFaceWorldTile(new WorldTile(object.getX() - 1, object.getY(), 0));
								player.setNextAnimation(new Animation(12216));
								player.unlock();
							} else if (count == 2) {
								player.setNextWorldTile(new WorldTile(3651, 5122, 0));
								player.setNextFaceWorldTile(new WorldTile(3651, 5121, 0));
								player.setNextAnimation(new Animation(12217));
							} else if (count == 3) {
								// TODO find emote
								// player.getPackets().sendObjectAnimation(new WorldObject(45078, 0, 3, 3651, 5123, 0), new Animation(12220));
							} else if (count == 5) {
								player.unlock();
								stop();
							}
							count++;
						}

					}, 1, 0);
				} else if (id == 45076)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Gold_Ore));
				else if (id == 5999)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Coal_Ore));
				else if (id == 45078)
					player.useStairs(2413, new WorldTile(3012, 9832, 0), 2, 2);
				else if (id == 45079)
					player.getBank().openDepositBox();
				// champion guild
				else if (id == 24357 && object.getX() == 3188 && object.getY() == 3355)
					player.useStairs(-1, new WorldTile(3189, 3354, 1), 0, 1);
				else if (id == 24359 && object.getX() == 3188 && object.getY() == 3355)
					player.useStairs(-1, new WorldTile(3189, 3358, 0), 0, 1);
				else if (id == 1805 && object.getX() == 3191 && object.getY() == 3363) {
					WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() - 1,
							object.getX(), object.getY(), object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(3191, player.getY() >= object.getY() ? object.getY() - 1 : object.getY(),
								-1, false);
						if (player.getY() >= object.getY())
							player.getDialogueManager().startDialogue("SimpleNPCMessage", 198,
									"Greetings bolt adventurer. Welcome to the guild of", "Champions.");
					}
				}
				// start of varrock dungeon
				else if (id == 29355 && object.getX() == 3230 && object.getY() == 9904) // varrock dungeon climb to bear
					player.useStairs(828, new WorldTile(3229, 3503, 0), 1, 2);
				else if (id == 24264)
					player.useStairs(833, new WorldTile(3229, 9904, 0), 1, 2);
				else if (id == 24366)
					player.useStairs(828, new WorldTile(3237, 3459, 0), 1, 2);
				else if (id == 882 && object.getX() == 3237 && object.getY() == 3458)
					player.useStairs(833, new WorldTile(3237, 9858, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3097 && object.getY() == 9867) // edge dungeon climb
					player.useStairs(828, new WorldTile(3096, 3468, 0), 1, 2);
				else if (id == 26934)
					player.useStairs(833, new WorldTile(3097, 9868, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3088 && object.getY() == 9971)
					player.useStairs(828, new WorldTile(3087, 3571, 0), 1, 2);
				else if (id == 65453)
					player.useStairs(833, new WorldTile(3089, 9971, 0), 1, 2);
				else if (id == 12389 && object.getX() == 3116 && object.getY() == 3452)
					player.useStairs(833, new WorldTile(3117, 9852, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3116 && object.getY() == 9852)
					player.useStairs(833, new WorldTile(3115, 3452, 0), 1, 2);
				else if (id == 69526)
					GnomeAgility.walkGnomeLog(player);
				else if (id == 69383)
					GnomeAgility.climbGnomeObstacleNet(player);
				else if (id == 69508)
					GnomeAgility.climbUpGnomeTreeBranch(player);
				else if (id == 2312)
					GnomeAgility.walkGnomeRope(player);
				else if (id == 4059)
					GnomeAgility.walkBackGnomeRope(player);
				else if (id == 69507)
					GnomeAgility.climbDownGnomeTreeBranch(player);
				else if (id == 69384)
					GnomeAgility.climbGnomeObstacleNet2(player);
				else if (id == 69377 || id == 69378)
					GnomeAgility.enterGnomePipe(player, object.getX(), object.getY());
				else if (Wilderness.isDitch(id)) {// wild ditch
					player.getDialogueManager().startDialogue("WildernessDitch", object);
				} else if (id == 42611) {// Magic Portal
					player.getDialogueManager().startDialogue("MagicPortal");
				} else if (object.getDefinitions().name.equalsIgnoreCase("Obelisk") && object.getY() > 3525) {
					// Who the fuck removed the controler class and the code from SONIC!!!!!!!!!!
					// That was an hour of collecting coords :fp: Now ima kill myself.
				} else if (id == 27254) {// Edgeville portal
					player.getPackets().sendGameMessage("You enter the portal...");
					player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3, "..and are transported to Edgeville.");
					player.addWalkSteps(1598, 4506, -1, false);
				} else if (id == 12202) {// mole entrance
					if (!player.getInventory().containsItem(952, 1)) {
						player.getPackets().sendGameMessage("You need a spade to dig this.");
						return;
					}
					if (player.getX() != object.getX() || player.getY() != object.getY()) {
						player.lock();
						player.addWalkSteps(object.getX(), object.getY());
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								InventoryOptionsHandler.dig(player);
							}

						}, 1);
					} else
						InventoryOptionsHandler.dig(player);
				} else if (id == 12230 && object.getX() == 1752 && object.getY() == 5136) {// mole exit
					player.setNextWorldTile(new WorldTile(2986, 3316, 0));
				} else if (id == 15522) {// portal sign
					if (player.withinDistance(new WorldTile(1598, 4504, 0), 1)) {// PORTAL
						// 1
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Edgeville");
						player.getPackets().sendIComponentText(327, 14, "This portal will take you to edgeville. There "
								+ "you can multi pk once past the wilderness ditch.");
					}
					if (player.withinDistance(new WorldTile(1598, 4508, 0), 1)) {// PORTAL
						// 2
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Mage Bank");
						player.getPackets().sendIComponentText(327, 14, "This portal will take you to the mage bank. "
								+ "The mage bank is a 1v1 deep wilderness area.");
					}
					if (player.withinDistance(new WorldTile(1598, 4513, 0), 1)) {// PORTAL
						// 3
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Magic's Portal");
						player.getPackets().sendIComponentText(327, 14,
								"This portal will allow you to teleport to areas that "
										+ "will allow you to change your magic spell book.");
					}
				} else if (id == 38811 || id == 37929) {// corp beast
					if (object.getX() == 2971 && object.getY() == 4382)
						player.getInterfaceManager().sendInterface(650);
					else if (object.getX() == 2918 && object.getY() == 4382) {
						player.stopAll();
						player.setNextWorldTile(
								new WorldTile(player.getX() == 2921 ? 2917 : 2921, player.getY(), player.getPlane()));
					}
				} else if (id == 37928 && object.getX() == 2883 && object.getY() == 4370) {
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3214, 3782, 0));
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 38815 && object.getX() == 3209 && object.getY() == 3780 && object.getPlane() == 0) {
					if (player.getSkills().getLevelForXp(Skills.WOODCUTTING) < 37
							|| player.getSkills().getLevelForXp(Skills.MINING) < 45
							|| player.getSkills().getLevelForXp(Skills.SUMMONING) < 23
							|| player.getSkills().getLevelForXp(Skills.FIREMAKING) < 47
							|| player.getSkills().getLevelForXp(Skills.PRAYER) < 55) {
						player.getPackets().sendGameMessage(
								"You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
						return;
					}
					player.stopAll();
					player.setNextWorldTile(new WorldTile(2885, 4372, 2));
					player.getControlerManager().forceStop();
					// TODO all reqs, skills not added
				} else if (id == 48803 && player.isKalphiteLairSetted()) {
					player.setNextWorldTile(new WorldTile(3508, 9494, 0));
				} else if (id == 48802 && player.isKalphiteLairEntranceSetted()) {
					player.setNextWorldTile(new WorldTile(3483, 9510, 2));
				} else if (id == 3829) {
					if (object.getX() == 3483 && object.getY() == 9510) {
						player.useStairs(828, new WorldTile(3226, 3108, 0), 1, 2);
					}
				} else if (id == 3832) {
					if (object.getX() == 3508 && object.getY() == 9494) {
						player.useStairs(828, new WorldTile(3509, 9496, 2), 1, 2);
					}
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				} else if (id == 54019 || id == 54020 || id == 55301)
					PkRank.showRanks(player);
				else if (id == 1817 && object.getX() == 2273 && object.getY() == 4680) { // kbd lever
					Magic.pushLeverTeleport(player, new WorldTile(3067, 10254, 0));
				} else if (id == 1816 && object.getX() == 3067 && object.getY() == 10252) { // kbd out lever
					Magic.pushLeverTeleport(player, new WorldTile(2273, 4681, 0));
				} else if (id == 32015 && object.getX() == 3069 && object.getY() == 10256) { // kbd stairs
					player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 1765 && object.getX() == 3017 && object.getY() == 3849) { // kbd out stairs
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3069, 10255, 0));
					player.getControlerManager().forceStop();
				} else if (id == 14315) {
					player.getControlerManager().startControler("PestControlLobby", 1);
				} else if (id == 5959) {
					Magic.pushLeverTeleport(player, new WorldTile(2539, 4712, 0));
				} else if (id == 5960) {
					Magic.pushLeverTeleport(player, new WorldTile(3089, 3957, 0));
				} else if (id == 1814) {
					Magic.pushLeverTeleport(player, new WorldTile(3155, 3923, 0));
				} else if (id == 1815) {
					Magic.pushLeverTeleport(player, new WorldTile(2561, 3311, 0));
				} else if (id == 62675)
					player.getCutscenesManager().play("DTPreview");
				else if (id == 62681)
					player.getDominionTower().viewScoreBoard();
				else if (id == 62678 || id == 62679)
					player.getDominionTower().openModes();
				else if (id == 62688)
					player.getDialogueManager().startDialogue("DTClaimRewards");
				else if (id == 62677)
					player.getDominionTower().talkToFace();
				else if (id == 62680)
					player.getDominionTower().openBankChest();
				else if (id == 48797)
					player.useStairs(-1, new WorldTile(3877, 5526, 1), 0, 1);
				else if (id == 48798)
					player.useStairs(-1, new WorldTile(3246, 3198, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5543)
					player.useStairs(-1, new WorldTile(3861, 5543, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
				else if (id == 48677 && x == 3858 && y == 5543)
					player.useStairs(-1, new WorldTile(3856, 5543, 1), 0, 1);
				else if (id == 48677 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3856, 5533, 1), 0, 1);
				else if (id == 48679)
					player.useStairs(-1, new WorldTile(3875, 5527, 1), 0, 1);
				else if (id == 48688)
					player.useStairs(-1, new WorldTile(3972, 5565, 0), 0, 1);
				else if (id == 48683)
					player.useStairs(-1, new WorldTile(3868, 5524, 0), 0, 1);
				else if (id == 48682)
					player.useStairs(-1, new WorldTile(3869, 5524, 0), 0, 1);
				else if (id == 62676) { // dominion exit
					player.useStairs(-1, new WorldTile(3374, 3093, 0), 0, 1);
				} else if (id == 62674) { // dominion entrance
					player.useStairs(-1, new WorldTile(3744, 6405, 0), 0, 1);
				} else if (id == 3192) {
					PkRank.showRanks(player);
				} else if (id == 65349) {
					player.useStairs(-1, new WorldTile(3044, 10325, 0), 0, 1);
				} else if (id == 32048 && object.getX() == 3043 && object.getY() == 10328) {
					player.useStairs(-1, new WorldTile(3045, 3927, 0), 0, 1);
				} else if (id == 26194) {
					player.getDialogueManager().startDialogue("PartyRoomLever");
				} else if (id == 61190 || id == 61191 || id == 61192 || id == 61193) {
					if (objectDef.containsOption(0, "Chop down"))
						player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL));
				} else if (id == 20573)
					player.getControlerManager().startControler("RefugeOfFear");
				// crucible
				else if (id == 67050)
					player.useStairs(-1, new WorldTile(3359, 6110, 0), 0, 1);
				else if (id == 67053)
					player.useStairs(-1, new WorldTile(3120, 3519, 0), 0, 1);
				else if (id == 67051)
					player.getDialogueManager().startDialogue("Marv", false);
				else if (id == 67052)
					Crucible.enterCrucibleEntrance(player);
				else {
					switch (objectDef.name.toLowerCase()) {
					case "trapdoor":
					case "manhole":
						if (objectDef.containsOption(0, "Open")) {
							WorldObject openedHole = new WorldObject(object.getId() + 1, object.getType(),
									object.getRotation(), object.getX(), object.getY(), object.getPlane());
							// if (World.removeTemporaryObject(object, 60000, true)) {
							player.faceObject(openedHole);
							World.spawnTemporaryObject(openedHole, 60000, true);
							// }
						}
						break;
					case "closed chest":
						if (objectDef.containsOption(0, "Open")) {
							player.setNextAnimation(new Animation(536));
							player.lock(2);
							WorldObject openedChest = new WorldObject(object.getId() + 1, object.getType(),
									object.getRotation(), object.getX(), object.getY(), object.getPlane());
							// if (World.removeTemporaryObject(object, 60000, true)) {
							player.faceObject(openedChest);
							World.spawnTemporaryObject(openedChest, 60000, true);
							// }
						}
						break;
					case "open chest":
						if (objectDef.containsOption(0, "Search"))
							player.getPackets().sendGameMessage("You search the chest but find nothing.");
						break;
					case "spiderweb":
						if (object.getRotation() == 2) {
							player.lock(2);
							if (Utils.getRandom(1) == 0) {
								player.addWalkSteps(player.getX(),
										player.getY() < y ? object.getY() + 2 : object.getY() - 1, -1, false);
								player.getPackets().sendGameMessage("You squeeze though the web.");
							} else
								player.getPackets().sendGameMessage(
										"You fail to squeeze though the web; perhaps you should try again.");
						}
						break;
					case "web":
						if (objectDef.containsOption(0, "Slash")) {
							player.setNextAnimation(
									new Animation(PlayerCombat.getWeaponAttackEmote(player.getEquipment().getWeaponId(),
											player.getCombatDefinitions().getAttackStyle())));
							slashWeb(player, object);
						}
						break;
					case "anvil":
						if (objectDef.containsOption(0, "Smith")) {
							ForgingBar bar = ForgingBar.getBar(player);
							if (bar != null)
								ForgingInterface.sendSmithingInterface(player, bar);
							else
								player.getPackets()
										.sendGameMessage("You have no bars which you have smithing level to use.");
						}
						break;
					case "tin ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Tin_Ore));
						break;
					case "gold ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Gold_Ore));
						break;
					case "iron ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Iron_Ore));
						break;
					case "silver ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Silver_Ore));
						break;
					case "coal rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Coal_Ore));
						break;
					case "clay rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Clay_Ore));
						break;
					case "copper ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Copper_Ore));
						break;
					case "adamantite ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Adamant_Ore));
						break;
					case "runite ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Runite_Ore));
						break;
					case "granite rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Granite_Ore));
						break;
					case "sandstone rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Sandstone_Ore));
						break;
					case "mithril ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Mithril_Ore));
						break;
					case "bank deposit box":
						if (objectDef.containsOption(0, "Deposit"))
							player.getBank().openDepositBox();
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(0, "Bank") || objectDef.containsOption(0, "Use"))
							player.getBank().openBank();
						break;
					// Woodcutting start
					case "tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL));
						break;
					case "evergreen":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.EVERGREEN));
						break;
					case "dead tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.DEAD));
						break;
					case "oak":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.OAK));
						break;
					case "willow":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.WILLOW));
						break;
					case "maple tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAPLE));
						break;
					case "ivy":
						if (objectDef.containsOption(0, "Chop"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.IVY));
						break;
					case "yew":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.YEW));
						break;
					case "magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC));
						break;
					case "cursed magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.CURSED_MAGIC));
						break;
					// Woodcutting end
					case "gate":
					case "large door":
					case "metal door":
						if (object.getType() == 0 && objectDef.containsOption(0, "Open"))
							if (!handleGate(player, object))
								handleDoor(player, object);
						break;
					case "door":
						if (object.getType() == 0
								&& (objectDef.containsOption(0, "Open") || objectDef.containsOption(0, "Unlock")))
							handleDoor(player, object);
						break;
					case "ladder":
						handleLadder(player, object, 1);
						break;
					case "staircase":
						handleStaircases(player, object, 1);
						break;
					case "small obelisk":
						if (objectDef.containsOption(0, "Renew-points")) {
							int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
							if (player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
								player.lock(3);
								player.setNextAnimation(new Animation(8502));
								player.getSkills().set(Skills.SUMMONING, summonLevel);
								player.getPackets().sendGameMessage("You have recharged your Summoning points.", true);
							} else
								player.getPackets().sendGameMessage("You already have full Summoning points.");
						}
						break;
					case "altar":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage("You pray to the gods...", true);
								player.setNextAnimation(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(maxPrayer);
										player.getPackets().sendGameMessage("...and recharged your prayer.", true);
									}
								}, 2);
							} else
								player.getPackets().sendGameMessage("You already have full prayer.");
							if (id == 6552)
								player.getDialogueManager().startDialogue("AncientAltar");
						}
						break;
					default:
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler",
							"clicked 1 at object id : " + id + ", " + object.getX() + ", " + object.getY() + ", "
									+ object.getPlane() + ", " + object.getType() + ", " + object.getRotation() + ", "
									+ object.getDefinitions().name);
			}
		}, false));//objectDef.getSizeX(), Wilderness.isDitch(id) ? 4 : objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption2(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick2(object))
					return;
				else if (object.getDefinitions().name.equalsIgnoreCase("furnace"))
					player.getDialogueManager().startDialogue("SmeltingD", object);
				else if (id == 17010)
					player.getDialogueManager().startDialogue("LunarAltar");
				else if (id == 62677)
					player.getDominionTower().openRewards();
				else if (id == 62688)
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have a Dominion Factor of " + player.getDominionTower().getDominionFactor() + ".");
				else if (id == 68107)
					FightKiln.enterFightKiln(player, true);
				else if (id == 34384 || id == 34383 || id == 14011 || id == 7053 || id == 34387 || id == 34386
						|| id == 34385)
					Thieving.handleStalls(player, object);
				else if (id == 2418)
					PartyRoom.openPartyChest(player);
				else if (id == 2646) {
					World.removeTemporaryObject(object, 50000, true);
					player.getInventory().addItem(1779, 1);
					// crucible
				} else if (id == 67051)
					player.getDialogueManager().startDialogue("Marv", true);
				else {
					switch (objectDef.name.toLowerCase()) {
					case "cabbage":
						if (objectDef.containsOption(1, "Pick") && player.getInventory().addItem(1965, 1)) {
							player.setNextAnimation(new Animation(827));
							player.lock(2);
							World.removeTemporaryObject(object, 60000, false);
						}
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(1, "Bank"))
							player.getBank().openBank();
						break;
					case "gates":
					case "gate":
					case "metal door":
						if (object.getType() == 0 && objectDef.containsOption(1, "Open"))
							handleGate(player, object);
						break;
					case "door":
						if (object.getType() == 0 && objectDef.containsOption(1, "Open"))
							handleDoor(player, object);
						break;
					case "ladder":
						handleLadder(player, object, 2);
						break;
					case "staircase":
						handleStaircases(player, object, 2);
						break;
					default:
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "clicked 2 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane());
			}
		}, false));//objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption3(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick3(object))
					return;
				switch (objectDef.name.toLowerCase()) {
				case "gate":
				case "metal door":
					if (object.getType() == 0 && objectDef.containsOption(2, "Open"))
						handleGate(player, object);
					break;
				case "door":
					if (object.getType() == 0 && objectDef.containsOption(2, "Open"))
						handleDoor(player, object);
					break;
				case "ladder":
					handleLadder(player, object, 3);
					break;
				case "staircase":
					handleStaircases(player, object, 3);
					break;
				default:
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					break;
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 3 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane() + ", ");
			}
		}, false));//objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption4(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick4(object))
					return;
				// living rock Caverns
				if (id == 45076)
					MiningBase.propect(player, "This rock contains a large concentration of gold.");
				else if (id == 5999)
					MiningBase.propect(player, "This rock contains a large concentration of coal.");
				else {
					switch (objectDef.name.toLowerCase()) {
					default:
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 4 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane() + ", ");
			}
		}, false));//objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption5(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick5(object))
					return;
				if (id == -1) {
					// unused
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "fire":
						if (objectDef.containsOption(4, "Add-logs"))
							Bonfire.addLogs(player, object);
						break;
					default:
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 5 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getPlane() + ", ");
			}
		}, false));//objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOptionExamine(final Player player, final WorldObject object) {
		if (Settings.DEBUG) {
			int offsetX = object.getX() - player.getX();
			int offsetY = object.getY() - player.getY();
			System.out.println("Offsets" + offsetX + " , " + offsetY);
		}
		player.getPackets().sendGameMessage("It's an " + object.getDefinitions().name + ".");
		if (Settings.DEBUG)
			if (Settings.DEBUG)

				Logger.log("ObjectHandler",
						"examined object id : " + object.getId() + ", " + object.getX() + ", " + object.getY() + ", "
								+ object.getPlane() + ", " + object.getType() + ", " + object.getRotation() + ", "
								+ object.getDefinitions().name);
	}

	private static void slashWeb(Player player, WorldObject object) {

		if (Utils.getRandom(1) == 0) {
			World.spawnTemporaryObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(),
					object.getX(), object.getY(), object.getPlane()), 60000, true);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else
			player.getPackets().sendGameMessage("You fail to cut through the web.");
	}

	private static boolean handleGate(Player player, WorldObject object) {
		if (World.isSpawnedObject(object))
			return false;
		if (object.getRotation() == 0) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX(), object.getY() - 1, object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX(), object.getY() - 1, object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1, object.getY(), object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1, object.getY(), object.getPlane()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		}
		return false;
	}

	public static boolean handleDoor(Player player, WorldObject object, long timer) {
		if (World.isSpawnedObject(object))
			return false;
		WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
				object.getX(), object.getY(), object.getPlane());
		if (object.getRotation() == 0)
			openedDoor.moveLocation(-1, 0, 0);
		else if (object.getRotation() == 1)
			openedDoor.moveLocation(0, 1, 0);
		else if (object.getRotation() == 2)
			openedDoor.moveLocation(1, 0, 0);
		else if (object.getRotation() == 3)
			openedDoor.moveLocation(0, -1, 0);
		if (World.removeTemporaryObject(object, timer, true)) {
			player.faceObject(openedDoor);
			World.spawnTemporaryObject(openedDoor, timer, true);
			return true;
		}
		return false;
	}

	private static boolean handleDoor(Player player, WorldObject object) {
		return handleDoor(player, object, 60000);
	}

	private static boolean handleStaircases(Player player, WorldObject object, int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue("ClimbNoEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player.getPlane() + 1),
					new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else
			return false;
		return false;
	}

	private static boolean handleLadder(Player player, WorldObject object, int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue("ClimbEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player.getPlane() + 1),
					new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), "Climb up the ladder.",
					"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}

	public static void handleItemOnObject(final Player player, final WorldObject object, final int interfaceId,
			final Item item) {
		final int itemId = item.getId();
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setRouteEvent(new RouteEvent(object, new Runnable() {
			@Override
			public void run() {
				player.faceObject(object);
				if (itemId == 1438 && object.getId() == 2452) {
					Runecrafting.enterAirAltar(player);
				} else if (itemId == 1440 && object.getId() == 2455) {
					Runecrafting.enterEarthAltar(player);
				} else if (itemId == 1442 && object.getId() == 2456) {
					Runecrafting.enterFireAltar(player);
				} else if (itemId == 1444 && object.getId() == 2454) {
					Runecrafting.enterWaterAltar(player);
				} else if (itemId == 1446 && object.getId() == 2457) {
					Runecrafting.enterBodyAltar(player);
				} else if (itemId == 1448 && object.getId() == 2453) {
					Runecrafting.enterMindAltar(player);
				} else if (object.getId() == 733 || object.getId() == 64729) {
					player.setNextAnimation(new Animation(PlayerCombat.getWeaponAttackEmote(-1, 0)));
					slashWeb(player, object);
				} else if (object.getId() == 48803 && itemId == 954) {
					if (player.isKalphiteLairSetted())
						return;
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLair();
				} else if (object.getId() == 48802 && itemId == 954) {
					if (player.isKalphiteLairEntranceSetted())
						return;
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLairEntrance();
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "anvil":
						ForgingBar bar = ForgingBar.forId(itemId);
						if (bar != null)
							ForgingInterface.sendSmithingInterface(player, bar);
						break;
					case "fire":
						if (objectDef.containsOption(4, "Add-logs") && Bonfire.addLog(player, object, item))
							return;
					case "range":
					case "cooking range":
					case "stove":
						Cookables cook = Cooking.isCookingSkill(item);
						if (cook != null) {
							player.getDialogueManager().startDialogue("CookingD", cook, object);
							return;
						}
						player.getDialogueManager().startDialogue("SimpleMessage",
								"You can't cook that on a " + (objectDef.name.equals("Fire") ? "fire" : "range") + ".");
						break;
					default:
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						break;
					}
					if (Settings.DEBUG)
						System.out.println("Item on object: " + object.getId());
				}
			}
		}, false));//objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
}
