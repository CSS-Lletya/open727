package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.dialogue.container.Test_D;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.others.FireSpirit;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.npc.pet.Pet;
import com.rs.game.npc.slayer.Strykewyrm;
import com.rs.game.player.Player;
import com.rs.game.player.RouteEvent;
import com.rs.game.player.content.PlayerLook;
import com.rs.game.player.dialogues.FremennikShipmaster;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ShopsHandler;

import skills.fishing.Fishing;
import skills.fishing.Fishing.FishingSpots;
import skills.mining.LivingMineralMining;
import skills.mining.MiningBase;
import skills.runecrafting.SiphonActionCreatures;
import skills.thieving.PickPocketAction;
import skills.thieving.PickPocketableNPC;

public class NPCHandler {

	public static void handleExamine(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		if (forceRun)
			player.setRun(forceRun);
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		if (player.getRights() > 1) {
			player.getPackets().sendGameMessage("NPC - [id=" + npc.getId() + ", loc=[" + npc.getX() + ", " + npc.getY()
					+ ", " + npc.getPlane() + "]].");
		}
		player.getPackets().sendNPCMessage(0, npc, "It's a " + npc.getDefinitions().name + ".");
		if (player.isSpawnsMode()) {
			try {
				if (NPCSpawns.removeSpawn(npc)) {
					player.getPackets().sendGameMessage("Removed spawn!");
					return;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			player.getPackets().sendGameMessage("Failed removing spawn!");
		}
		if (Settings.DEBUG)
			Logger.log("NPCHandler", "examined npc: " + npcIndex + ", " + npc.getId());
	}

	public static void handleOption1(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		final int npcId = npc.getId();
		/*if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2))
				return;
			npc.faceEntity(player);
			player.getDialogueManager().startDialogue("Banker", npc.getId());
			return;
		}*/
		if (SiphonActionCreatures.siphon(player, npc))
			return;
	
		player.setRouteEvent(new RouteEvent(npc, new Runnable() {
			@Override
			public void run() {
				
				npc.resetWalkSteps();
				
				if (npcId == 6537){
					player.dialog(new Test_D(player, npc));
					return;
				}
				
				
				player.faceEntity(npc);
				if (!player.getControlerManager().processNPCClick1(npc))
					return;
				FishingSpots spot = FishingSpots.forId(npc.getId() | 1 << 24);
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return; // its a spot, they wont face us
				} else if (npc.getId() >= 8837 && npc.getId() <= 8839) {
					player.getActionManager().setAction(new LivingMineralMining((LivingRock) npc));
					return;
				}
				else if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")){
					player.getDialogueManager().startDialogue("Banker", npc.getId());
					return;
				}
				npc.faceEntity(player);
				if (npc.getId() == 3709)
					player.getDialogueManager().startDialogue("MrEx", npc.getId());
				else if (npc.getId() == 5532)
					player.getDialogueManager().startDialogue("SorceressGardenNPCs", npc);
				else if (npc.getId() == 5563)
					player.getDialogueManager().startDialogue("SorceressGardenNPCs", npc);
				else if (npc.getId() == 5559)
					player.sendDeath(npc);
				else if (npc.getId() == 15451 && npc instanceof FireSpirit) {
					FireSpirit spirit = (FireSpirit) npc;
					spirit.giveReward(player);
				} else if (npc.getId() == 949)
					player.getDialogueManager().startDialogue("QuestGuide", npc.getId(), null);
				else if (npc.getId() >= 1 && npc.getId() <= 6 || npc.getId() >= 7875 && npc.getId() <= 7884)
					player.getDialogueManager().startDialogue("Man", npc.getId());
				else if (npc.getId() == 198)
					player.getDialogueManager().startDialogue("Guildmaster", npc.getId());
				else if (npc.getId() == 9462)
					Strykewyrm.handleStomping(player, npc);
				else if (npc.getId() == 9707)
					player.getDialogueManager().startDialogue("FremennikShipmaster", npc.getId(), true);
				else if (npc.getId() == 9708)
					player.getDialogueManager().startDialogue("FremennikShipmaster", npc.getId(), false);
				else if (npc.getId() == 11270)
					ShopsHandler.openShop(player, 19);
				else if (npc.getId() == 6537)
					player.getDialogueManager().startDialogue("SetSkills", npc.getId());
				else if (npc.getId() == 2676)
					player.getDialogueManager().startDialogue("MakeOverMage", npc.getId(), 0);
				else if (npc.getId() == 598)
					player.getDialogueManager().startDialogue("Hairdresser", npc.getId());
				else if (npc.getId() == 548)
					player.getDialogueManager().startDialogue("Thessalia", npc.getId());
				else if (npc.getId() == 659)
					player.getDialogueManager().startDialogue("PartyPete");
				else if (npc.getId() == 579)
					player.getDialogueManager().startDialogue("DrogoDwarf", npc.getId());
				else if (npc.getId() == 582) // dwarves general store
					player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 31);
				else if (npc.getId() == 528 || npc.getId() == 529) // edge
					player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 1);
				else if (npc.getId() == 522 || npc.getId() == 523) // varrock
					player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 8);
				else if (npc.getId() == 520 || npc.getId() == 521) // lumbridge
					player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 4);
				else if (npc.getId() == 594)
					player.getDialogueManager().startDialogue("Nurmof", npc.getId());
				else if (npc.getId() == 665)
					player.getDialogueManager().startDialogue("BootDwarf", npc.getId());
				else if (npc.getId() == 382 || npc.getId() == 3294 || npc.getId() == 4316)
					player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), false);
				else if (npc.getId() == 3295)
					player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), true);
				else if (npc.getId() == 537)
					player.getDialogueManager().startDialogue("Scavvo", npc.getId());
				else if (npc.getId() == 536)
					player.getDialogueManager().startDialogue("Valaine", npc.getId());
				else if (npc.getId() == 4563) // Crossbow Shop
					player.getDialogueManager().startDialogue("Hura", npc.getId());
				else if (npc.getId() == 2617)
					player.getDialogueManager().startDialogue("TzHaarMejJal", npc.getId());
				else if (npc.getId() == 2618)
					player.getDialogueManager().startDialogue("TzHaarMejKah", npc.getId());
				else if (npc.getId() == 15149)
					player.getDialogueManager().startDialogue("MasterOfFear", 0);
				else if (npc instanceof Pet) {
					Pet pet = (Pet) npc;
					if (pet != player.getPet()) {
						player.getPackets().sendGameMessage("This isn't your pet.");
						return;
					}
					player.setNextAnimation(new Animation(827));
					pet.pickup();
				} else {
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					if (Settings.DEBUG)
						System.out.println("cliked 1 at npc id : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY()
								+ ", " + npc.getPlane());
				}
			}
		}, npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")));
	}

	public static void handleOption2(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		
		player.setRouteEvent(new RouteEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				player.faceEntity(npc);
				if (npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
					player.faceEntity(npc);
					if (!player.withinDistance(npc, 2))
						return;
					npc.faceEntity(player);
					player.getBank().openBank();
					return;
				}
				FishingSpots spot = FishingSpots.forId(npc.getId() | (2 << 24));
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return;
				}
				PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
				if (pocket != null) {
					player.getActionManager().setAction(new PickPocketAction(npc, pocket));
					return;
				}
				if (npc instanceof Familiar) {
					if (npc.getDefinitions().hasOption("store")) {
						if (player.getFamiliar() != npc) {
							player.getPackets().sendGameMessage("That isn't your familiar.");
							return;
						}
						player.getFamiliar().store();
					} else if (npc.getDefinitions().hasOption("cure")) {
						if (player.getFamiliar() != npc) {
							player.getPackets().sendGameMessage("That isn't your familiar.");
							return;
						}
						if (!player.getPoison().isPoisoned()) {
							player.getPackets().sendGameMessage("Your arent poisoned or diseased.");
							return;
						} else {
							player.getFamiliar().drainSpecial(2);
							player.addPoisonImmune(120);
						}
					}
					return;
				}
				npc.faceEntity(player);
				if (!player.getControlerManager().processNPCClick2(npc))
					return;
				if (npc.getId() == 9707)
					FremennikShipmaster.sail(player, true);
				else if (npc.getId() == 9708)
					FremennikShipmaster.sail(player, false);
				else if (npc.getId() == 13455 || npc.getId() == 2617 || npc.getId() == 2618 || npc.getId() == 15194)
					player.getBank().openBank();
				else if (npc.getId() == 528 || npc.getId() == 529)
					ShopsHandler.openShop(player, 1);
				else if (npc.getId() == 519)
					ShopsHandler.openShop(player, 2);
				else if (npc.getId() == 520 || npc.getId() == 521)
					ShopsHandler.openShop(player, 4);
				else if (npc.getId() == 538)
					ShopsHandler.openShop(player, 6);
				else if (npc.getId() == 522 || npc.getId() == 523)
					ShopsHandler.openShop(player, 8);
				else if (npc.getId() == 546)
					ShopsHandler.openShop(player, 10);
				else if (npc.getId() == 11475)
					ShopsHandler.openShop(player, 9);
				else if (npc.getId() == 551)
					ShopsHandler.openShop(player, 13);
				else if (npc.getId() == 550)
					ShopsHandler.openShop(player, 14);
				else if (npc.getId() == 549)
					ShopsHandler.openShop(player, 15);
				else if (npc.getId() == 548)
					ShopsHandler.openShop(player, 18); // thesalia
				else if (npc.getId() == 2233 || npc.getId() == 3671)
					ShopsHandler.openShop(player, 20);
				else if (npc.getId() == 970)
					ShopsHandler.openShop(player, 21);
				else if (npc.getId() == 579) // Drogo's mining Emporium
					ShopsHandler.openShop(player, 30);
				else if (npc.getId() == 582) // dwarves general store
					ShopsHandler.openShop(player, 31);
				else if (npc.getId() == 594) // Nurmof's Pickaxe Shop
					ShopsHandler.openShop(player, 32);
				else if (npc.getId() == 537) // Scavvo's Rune Store
					ShopsHandler.openShop(player, 12);
				else if (npc.getId() == 536) // Valaine's Shop of Champions
					ShopsHandler.openShop(player, 17);
				else if (npc.getId() == 4563) // Crossbow Shop
					ShopsHandler.openShop(player, 33);
				else if (npc.getId() == 15149)
					player.getDialogueManager().startDialogue("MasterOfFear", 3);
				else if (npc.getId() == 2676)
					PlayerLook.openMageMakeOver(player);
				else if (npc.getId() == 598)
					PlayerLook.openHairdresserSalon(player);
				else if (npc instanceof Pet) {
					if (npc != player.getPet()) {
						player.getPackets().sendGameMessage("This isn't your pet!");
						return;
					}
					Pet pet = player.getPet();
					player.getPackets().sendMessage(99,
							"Pet [id=" + pet.getId() + ", hunger=" + pet.getDetails().getHunger() + ", growth="
									+ pet.getDetails().getGrowth() + ", stage=" + pet.getDetails().getStage() + "].",
							player);
				} else {
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					if (Settings.DEBUG)
						System.out.println("cliked 2 at npc id : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY()
								+ ", " + npc.getPlane());
				}
			}
		}, npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")));
	}

	public static void handleOption3(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		player.setRouteEvent(new RouteEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!player.getControlerManager().processNPCClick3(npc))
					return;
				player.faceEntity(npc);
				if (npc.getId() >= 8837 && npc.getId() <= 8839) {
					MiningBase.propect(player, "You examine the remains...",
							"The remains contain traces of living minerals.");
					return;

				}
				npc.faceEntity(player);
				if ((npc.getId() == 8462 || npc.getId() == 8464 || npc.getId() == 1597 || npc.getId() == 1598
						|| npc.getId() == 7780 || npc.getId() == 8467 || npc.getId() == 9084))
					ShopsHandler.openShop(player, 29);
				else if (npc.getId() == 548)
					PlayerLook.openThessaliasMakeOver(player);
				else if (npc.getId() == 5532) {
					npc.setNextForceTalk(new ForceTalk("Senventior Disthinte Molesko!"));
					player.getControlerManager().startControler("SorceressGarden");

				} else
					player.getPackets().sendGameMessage("Nothing interesting happens.");
			}
		}, npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")));
		if (Settings.DEBUG)
			System.out.println("cliked 3 at npc id : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", "
					+ npc.getPlane());
	}
}
