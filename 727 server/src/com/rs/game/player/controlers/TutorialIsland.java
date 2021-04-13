package com.rs.game.player.controlers;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Inventory;
import com.rs.net.decoders.WorldPacketsDecoder;

import skills.Skills;
import skills.woodcutting.Woodcutting.TreeDefinitions;

public final class TutorialIsland extends Controler {

	private static final int RUNESCAPE_GUIDE_NPC = 945, SURVIVAL_EXPERT_NPC = 943, MASTER_CHEF_NPC = 942;

	@Override
	public void start() {
		if (getStage() < 6)
			player.getInterfaceManager().openGameTab(16); // sets no selected
															// tab
		refreshStage();
		sendInterfaces();
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You'll be told how to equip items later.");
		return false;
	}

	public int getStage() {
		if (getArguments() == null)
			setArguments(new Object[] { 0 }); // index 0 = stage
		return (Integer) getArguments()[0];
	}

	public void setStage(int stage) {
		getArguments()[0] = stage;
	}

	public void refreshStage() {
		int stage = getStage();
		if (stage == 0 || stage == 1 || stage == 2) {
			NPC guide = findNPC(RUNESCAPE_GUIDE_NPC);
			if (guide != null) // not saving icon as theres no need for more
								// than 1icon
				player.getHintIconsManager().addHintIcon(guide, 0, -1, false);
		} else if (stage == 3)
			player.getHintIconsManager().addHintIcon(3097, 3107, 0, 125, 4, 0, -1, false);
		else if (stage == 4 || stage == 11) {
			NPC survival = findNPC(SURVIVAL_EXPERT_NPC);
			if (survival != null) // not saving icon as theres no need for more
									// than 1icon
				player.getHintIconsManager().addHintIcon(survival, 0, -1, false);
		} else if (stage == 6)
			player.getHintIconsManager().addHintIcon(3099, 3095, 0, 150, 4, 0, -1, false);
		else if (stage == 12) {
			// 233 - 3100 3091 0
			NPC spot = findNPC(952);
			if (spot != null)
				player.getHintIconsManager().addHintIcon(spot, 0, -1, false);
		} else if (stage == 18) {
			player.getHintIconsManager().addHintIcon(3089, 3092, 0, 125, 4, 0, -1, false);
		} else if (stage == 19) {
			player.getHintIconsManager().addHintIcon(3078, 3084, 0, 125, 4, 0, -1, false);
		} else if (stage == 20) {
			NPC chef = findNPC(MASTER_CHEF_NPC);
			if (chef != null)
				player.getHintIconsManager().addHintIcon(chef, 0, -1, false);
		}
		sendInterfaces();
	}

	public NPC findNPC(int id) {
		// as it may be far away
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id)
				continue;
			return npc;
		}
		return null;
	}

	public void updateProgress() {
		setStage(getStage() + 1);
		if (getStage() == 1)
			player.getInterfaceManager().sendSettings();
		else if (getStage() == 2)
			player.getPackets().sendConfig(1021, 0); // unflash
		else if (getStage() == 5) {
			player.getInterfaceManager().sendInventory();
			player.getInventory().unlockInventoryOptions();
			player.getHintIconsManager().removeUnsavedHintIcon();
		} else if (getStage() == 6)
			player.getPackets().sendConfig(1021, 0); // unflash
		else if (getStage() == 7)
			player.getHintIconsManager().removeUnsavedHintIcon();
		else if (getStage() == 10)
			player.getInterfaceManager().sendSkills();
		else if (getStage() == 11)
			player.getPackets().sendConfig(1021, 0); // unflash
		else if (getStage() == 13 || getStage() == 21)
			player.getHintIconsManager().removeUnsavedHintIcon();
		refreshStage();
	}

	public void sendProgress() {
		int stage = getStage();
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, 371);
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		if (stage == 0) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0, "Getting Started");
			player.getPackets().sendIComponentText(372, 1,
					"To start the tutorial use your left mouse button to click on the");
			player.getPackets().sendIComponentText(372, 2,
					Settings.SERVER_NAME + " Guide in this room .He is indicated by a flashing");
			player.getPackets().sendIComponentText(372, 3,
					"yellow arrow above his head. If you can't see him use your");
			player.getPackets().sendIComponentText(372, 4, "keyboard arrow keys to rotate the view.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 1) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0, "");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "Player controls");
			player.getPackets().sendIComponentText(372, 3,
					"Please click on the flashing spanner icon found at the buttom");
			player.getPackets().sendIComponentText(372, 4,
					"right of your screen. This will display your player controls.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
			player.getPackets().sendConfig(1021, 13); // flashing setting tab
		} else if (stage == 2) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0, "Player controls");
			player.getPackets().sendIComponentText(372, 1,
					"On the side panel you can now see a variety of options from");
			player.getPackets().sendIComponentText(372, 2,
					"changing the brightness of the screen and of the volume of");
			player.getPackets().sendIComponentText(372, 3, "music, to selecting whether your player should help");
			player.getPackets().sendIComponentText(372, 4,
					"from other players. Don't worry about these too much for now.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 3) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0, "Interacting with the scenery");
			player.getPackets().sendIComponentText(372, 1,
					"You can interact with many items of the scenery by simply clicking");
			player.getPackets().sendIComponentText(372, 2,
					"on them. Right clicking will also give more options. Feel free to");
			player.getPackets().sendIComponentText(372, 3,
					"try it with the things in this room, then click on the door");
			player.getPackets().sendIComponentText(372, 4,
					"indicated with the yellow arrow to go througth to the next");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 4) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "Moving around");
			player.getPackets().sendIComponentText(372, 1,
					"You can interact with many items of the scenery by simply clicking");
			player.getPackets().sendIComponentText(372, 2,
					"ground will walk you to that point. Talk to the Survival Expert by");
			player.getPackets().sendIComponentText(372, 3,
					"the pond to continue the tutorial. Remember you can rotate");
			player.getPackets().sendIComponentText(372, 4, "the view by pressing the arrow keys.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 5) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "Viewing the items that you were given.");
			player.getPackets().sendIComponentText(372, 1,
					"Click on the flashing backpack icon to the right hand size of");
			player.getPackets().sendIComponentText(372, 2,
					"the main window to view your inventory. Your inventory is a list");
			player.getPackets().sendIComponentText(372, 3, "of everything you have in your backpack.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
			player.getPackets().sendConfig(1021, 5); // flashing inv tab
		} else if (stage == 6) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "Cut down a tree");
			player.getPackets().sendIComponentText(372, 1,
					"You can click on the backpack icon at any time to view the");
			player.getPackets().sendIComponentText(372, 2,
					"items that you currently have in your inventory. You will see");
			player.getPackets().sendIComponentText(372, 3,
					"that you now have an axe in your inventory. Use this to get");
			player.getPackets().sendIComponentText(372, 4, "some logs by clicking on one of the trees in the area.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 7) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "Please wait");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2,
					"Your character is now attemping to cut down the tree. Sit back");
			player.getPackets().sendIComponentText(372, 3, "for a moment while he does all the hard work.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 8) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "Making a fire");
			player.getPackets().sendIComponentText(372, 1,
					"Well done! You managed to cut some logs from the tree! Next,");
			player.getPackets().sendIComponentText(372, 2, "use the tinderbox in your inventory to light the logs.");
			player.getPackets().sendIComponentText(372, 3, "First click on the tinderbox to 'use' it.");
			player.getPackets().sendIComponentText(372, 4, "Then click on the logs in your inventory to light them.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 9) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "Please wait");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "Your character is now attemping to light the fire.");
			player.getPackets().sendIComponentText(372, 3, "This should only take a few seconds.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 10) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "You gained some experience.");
			player.getPackets().sendIComponentText(372, 3,
					"Click on the flashing bar graph icon near the inventory button");
			player.getPackets().sendIComponentText(372, 4, "to see your skill stats.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
			player.getPackets().sendConfig(1021, 3); // flashing skills tab
		} else if (stage == 11) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(2);
			player.getPackets().sendIComponentText(372, 0, "Your skill stats");
			player.getPackets().sendIComponentText(372, 1,
					"Here you will see how good your skills are. As you move your");
			player.getPackets().sendIComponentText(372, 2,
					"mouse over any of the icons in this panel, the small yellow");
			player.getPackets().sendIComponentText(372, 3,
					"popup box will show you the start amount of experience you");
			player.getPackets().sendIComponentText(372, 4,
					"have and how much is needed to get to the next level. Speak to");
			player.getPackets().sendIComponentText(372, 5, "the Survival Expert to continue.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 12) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(3);
			player.getPackets().sendIComponentText(372, 0, "Catch some Shrimp.");
			player.getPackets().sendIComponentText(372, 1,
					"Click on the sparkling fishing spot indicated by the fishing");
			player.getPackets().sendIComponentText(372, 2,
					"arrow. Remember, you can check your inventory by clicking the");
			player.getPackets().sendIComponentText(372, 3, "backpack icon.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 13 || stage == 16) {
			player.getPackets().sendHideIComponent(371, 4, true);
			sendProgress(3);
			player.getPackets().sendIComponentText(372, 0, "");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "Please wait");
			player.getPackets().sendIComponentText(372, 3, "This should only take a few seconds.");
			player.getPackets().sendIComponentText(372, 4,
					"As you gain Fishing experience you'll find that there are many types");
			player.getPackets().sendIComponentText(372, 5, "of fish and many ways to catch them.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 14) {
			player.getPackets().sendHideIComponent(371, 4, true);
			sendProgress(3);
			player.getPackets().sendIComponentText(372, 0, "Cooking your shrimp");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2,
					"Now you have caught some shrimp, let's cook it. First light a fire: chop");
			player.getPackets().sendIComponentText(372, 3,
					"down a tree and then use a tinderbox on the logs. If you've lost");
			player.getPackets().sendIComponentText(372, 4, "your hatchet or tinderbox! Brynna will give you another.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 15 || stage == 17) {
			player.getPackets().sendHideIComponent(371, 4, true);
			sendProgress(3);
			player.getPackets().sendIComponentText(372, 0, "Burning your shrimp");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2,
					"You have just burnt your first shrimp. This is normal. As you get");
			player.getPackets().sendIComponentText(372, 3,
					"more experience in Cooking, you will burn stuff less often. Let's try");
			player.getPackets().sendIComponentText(372, 4,
					"cooking without burning it this time. First catch some more shrimp,");
			player.getPackets().sendIComponentText(372, 5, "then use it on a fire.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 18) {
			player.getPackets().sendHideIComponent(371, 4, true);
			sendProgress(3);
			player.getPackets().sendIComponentText(372, 0, "");
			player.getPackets().sendIComponentText(372, 1, "Well done, you've just cooked your first RuneScape meal.");
			player.getPackets().sendIComponentText(372, 2,
					"If you'd like a recap on anything you've learnt so far speak to the");
			player.getPackets().sendIComponentText(372, 3,
					"Survival Expert. You can now move on the next instructor. Click");
			player.getPackets().sendIComponentText(372, 4,
					"on the gate shown and follow the path. Remember, you can move the");
			player.getPackets().sendIComponentText(372, 5, "camera with the arrow keys.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 19) {
			player.getPackets().sendHideIComponent(371, 4, true);
			sendProgress(4);
			player.getPackets().sendIComponentText(372, 0, "Find your next instructor");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2,
					"Follow the path until you get to the door with the yellow arrow above");
			player.getPackets().sendIComponentText(372, 3,
					"it. Click on the door to open it. Notice the mini map in the top right,");
			player.getPackets().sendIComponentText(372, 4,
					"this shows a top down view of the area arround you. This can also");
			player.getPackets().sendIComponentText(372, 5, "be used tor navigation.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 20) {
			player.getPackets().sendHideIComponent(371, 4, true);
			sendProgress(4);
			player.getPackets().sendIComponentText(372, 0, "Find your next instructor");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2,
					"Talk to the chef indicated. He will teach you the more advanced");
			player.getPackets().sendIComponentText(372, 3,
					"aspects of Cocking such as combiding ingredients. He will also teach");
			player.getPackets().sendIComponentText(372, 4, "you about your Music Player.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 21) {
			player.getPackets().sendHideIComponent(371, 4, true);
			sendProgress(4);
			player.getPackets().sendIComponentText(372, 0, "Making dough");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "This is the base of the meals. To make dough we must mix");
			player.getPackets().sendIComponentText(372, 3,
					"flour and water. First right click the bucket of water and select use,");
			player.getPackets().sendIComponentText(372, 4, "then left click on the pot of flour.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		}
	}

	public void sendProgress(int progress) {
		player.getPackets().sendConfig(406, progress);
	}

	/*
	 * return can use
	 */
	@Override
	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		if (getStage() == 8) {
			if ((itemUsed.getId() == 590 || usedWith.getId() == 590)
					&& (itemUsed.getId() == 1511 || usedWith.getId() == 1511)) {
				updateProgress();
			}
		}
		return true;
	}

	@Override
	public void trackXP(int skillId, int addedXp) {
		if (getStage() == 9 && skillId == Skills.FIREMAKING)
			updateProgress();
		else if ((getStage() == 13 || getStage() == 16) && skillId == Skills.FISHING) {
			updateProgress();
			player.stopAll();
		}
	}

	/*
	 * return can add
	 */
	@Override
	public boolean canAddInventoryItem(int itemId, int amount) {
		if (getStage() == 7 && itemId == TreeDefinitions.NORMAL.getLogsId()) {
			updateProgress();
			player.getDialogueManager().startDialogue("ItemMessage", "You get some logs.", itemId);
		} else if (getStage() == 14 && (itemId == 7954 || itemId == 315)) {
			updateProgress();
			player.getInventory().addItem(7954, 1);
			return false;
		} else if (getStage() == 17 && (itemId == 7954 || itemId == 315)) {
			updateProgress();
			player.getInventory().addItem(315, 1);
			return false;
		}
		return true;
	}

	/*
	 * return process normaly
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (((interfaceId == 548 && componentId == 103) || (interfaceId == 746 && componentId == 51))
				&& getStage() == 1)
			updateProgress();
		else if (((interfaceId == 548 && componentId == 133) || (interfaceId == 746 && componentId == 43))
				&& getStage() == 5)
			updateProgress();
		else if (getStage() == 8 && interfaceId == Inventory.INVENTORY_INTERFACE
				&& packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
			Item item = player.getInventory().getItem(slotId);
			if (item != null && item.getId() == 1511)
				updateProgress();
		} else if (((interfaceId == 548 && componentId == 131) || (interfaceId == 746 && componentId == 41))
				&& getStage() == 10)
			updateProgress();
		return true;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 3014 && object.getX() == 3098 && object.getY() == 3107) {
			int stage = getStage();
			if (stage < 3 || player.getY() != object.getY())
				return false;
			if (stage == 3) {
				updateProgress();
			}
			WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX() - 1, object.getY(), object.getPlane());
			if (World.removeTemporaryObject(object, 1200, false)) {
				World.spawnTemporaryObject(openedDoor, 1200, false);
				player.lock(2);
				player.stopAll();
				player.addWalkSteps(player.getX() >= object.getX() ? object.getX() - 1 : object.getX(), player.getY(),
						-1, false);
			}
			return false;
		} else if (object.getId() == 3033) {
			int stage = getStage();
			if (stage == 6)
				updateProgress();
		} else if (object.getId() == 3015 || object.getId() == 3016) {
			int stage = getStage();
			if (stage < 18 || player.getY() != object.getY())
				return false;
			if (stage == 18)
				updateProgress();
			player.lock(2);
			player.stopAll();
			player.addWalkSteps(player.getX() > object.getX() ? object.getX() : object.getX() + 1, player.getY(), -1,
					false);
			return false;

		} else if (object.getId() == 3017 && object.getX() == 3079 && object.getY() == 3084) {
			int stage = getStage();
			if (stage < 19 || player.getY() != object.getY())
				return false;
			if (stage == 19)
				updateProgress();
			WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX() - 1, object.getY(), object.getPlane());
			if (World.removeTemporaryObject(object, 1200, false)) {
				World.spawnTemporaryObject(openedDoor, 1200, false);
				player.lock(2);
				player.stopAll();
				player.addWalkSteps(player.getX() >= object.getX() ? object.getX() - 1 : object.getX(), player.getY(),
						-1, false);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == RUNESCAPE_GUIDE_NPC) {
			player.getDialogueManager().startDialogue("RuneScapeGuide", RUNESCAPE_GUIDE_NPC, this);
			npc.faceEntity(player);
			npc.resetWalkSteps();
			return false;
		} else if (npc.getId() == SURVIVAL_EXPERT_NPC) {
			player.getDialogueManager().startDialogue("SurvivalExpert", SURVIVAL_EXPERT_NPC, this);
			npc.faceEntity(player);
			npc.resetWalkSteps();
			return false;
		} else if (npc.getId() == 952 && (getStage() == 12 || getStage() == 15))
			updateProgress();
		else if (npc.getId() == MASTER_CHEF_NPC) {
			player.getDialogueManager().startDialogue("MasterChef", MASTER_CHEF_NPC, this);
			npc.faceEntity(player);
			npc.resetWalkSteps();
			return false;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		int stage = getStage();
		boolean rezi = player.getInterfaceManager().hasRezizableScreen();
		player.getPackets().closeInterface(rezi ? 29 : 204);// Attack tab
		if (stage < 10)
			player.getPackets().closeInterface(rezi ? 30 : 206);// Skill tab
		player.getPackets().closeInterface(rezi ? 31 : 207);// Quest tab
		player.getPackets().closeInterface(rezi ? 32 : 205);// Achievement tab
		if (stage < 5)
			player.getPackets().closeInterface(rezi ? 33 : 208);// Inventory tab
		player.getPackets().closeInterface(rezi ? 34 : 209);// Equipment tab
		player.getPackets().closeInterface(rezi ? 35 : 210);// pray tab
		player.getPackets().closeInterface(rezi ? 36 : 211);// magic tab
		player.getPackets().closeInterface(rezi ? 38 : 213);// Friend tab
		player.getPackets().closeInterface(rezi ? 39 : 214);// FriendChat tab
		player.getPackets().closeInterface(rezi ? 40 : 215);// Clan tab
		if (stage == 0)
			player.getPackets().closeInterface(rezi ? 41 : 216);// Settings tab
		player.getPackets().closeInterface(rezi ? 42 : 217);// Emote tab
		player.getPackets().closeInterface(rezi ? 43 : 218);// Music tab
		player.getPackets().closeInterface(rezi ? 44 : 219);// Notes tab
		sendProgress();
	}

	/*
	 * return remove controler
	 */
	@Override
	public boolean login() {
		start();
		return false;
	}

	/*
	 * return remove controler
	 */
	@Override
	public boolean logout() {
		return false;
	}

}
