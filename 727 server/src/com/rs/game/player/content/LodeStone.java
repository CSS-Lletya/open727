package com.rs.game.player.content;

import com.rs.game.Graphics;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.HomeTeleport;

public class LodeStone {
	private static final int[] CONFIG_IDS = new int[] { 10900, 10901, 10902,
			10903, 10904, 10905, 10906, 10907, 10908, 10909, 10910, 10911,
			10912, 2448, 358 };

	private transient Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Handles the interface of the lodestone network. Checks if the player is
	 * able to teleport to the selected lodestone.
	 * 
	 * @param componentId
	 */
	public void handleButtons(int componentId) {
		player.stopAll();
		WorldTile stoneTile = null;
		switch (componentId) {
		case 7:// Bandit Camp
			if (player.getActivatedLodestones()[14] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.BANDIT_CAMP_LODE_STONE;
			break;
		case 39:// Lunar Isle
			if (player.getActivatedLodestones()[13] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.LUNAR_ISLE_LODE_STONE;
			break;
		case 40:// AlKarid
			if (player.getActivatedLodestones()[0] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.ALKARID_LODE_STONE;
			break;
		case 41:// Ardougne
			if (player.getActivatedLodestones()[1] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.ARDOUGNE_LODE_STONE;
			break;
		case 42:// Burthorpe
			if (player.getActivatedLodestones()[2] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.BURTHORPE_LODE_STONE;
			break;
		case 43:// Catherby
			if (player.getActivatedLodestones()[3] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.CATHERBAY_LODE_STONE;
			break;
		case 44:// Draynor
			if (player.getActivatedLodestones()[4] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.DRAYNOR_VILLAGE_LODE_STONE;
			break;
		case 45:// Edgeville
			if (player.getActivatedLodestones()[5] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.EDGEVILLE_LODE_STONE;
			break;
		case 46:// Falador
			if (player.getActivatedLodestones()[6] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.FALADOR_LODE_STONE;
			break;
		case 47:// Lumbridge is auto unlocked.
			stoneTile = HomeTeleport.LUMBRIDGE_LODE_STONE;
			break;
		case 48:// Port Sarim
			if (player.getActivatedLodestones()[8] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.PORT_SARIM_LODE_STONE;
			break;
		case 49:// Seers Village
			if (player.getActivatedLodestones()[9] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.SEERS_VILLAGE_LODE_STONE;
			break;
		case 50:// Taverly
			if (player.getActivatedLodestones()[10] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.TAVERLY_LODE_STONE;
			break;
		case 51:// Varrock
			if (player.getActivatedLodestones()[11] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.VARROCK_LODE_STONE;
			break;
		case 52:// Yanille
			if (player.getActivatedLodestones()[12] == false) {
				player.getPackets().sendGameMessage(
						"You must activate this lodestone to teleport to it.");
				return;
			}
			stoneTile = HomeTeleport.YANILLE_LODE_STONE;
			break;
		}
		if (stoneTile != null) {
			player.getActionManager().setAction(new HomeTeleport(stoneTile));
		}
	}

	/**
	 * Checks the object id then sends the necessary config. Activates the
	 * lodestone for the player.
	 * 
	 * @param object
	 */
	public void activateLodestone(WorldObject object) {
		switch (object.getId()) {
		case 69827:// Bandit Camp
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[14], 190);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[14] = true;
			break;
		case 69828:// Lunar Isle
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[13], 190);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[13] = true;
			break;
		case 69829:// AlKarid
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[0], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[0] = true;
			break;
		case 69830:// Ardougne
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[1], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[1] = true;
			break;
		case 69831:// Burthorpe
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[2], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[2] = true;
			break;
		case 69832:// Catherby
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[3], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[3] = true;
			break;
		case 69833:// Draynor
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[4], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[4] = true;
			break;
		case 69834:// Edgeville
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[5], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[5] = true;
			break;
		case 69835:// Falador
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[6], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[6] = true;
			break;
		case 69837:// Port Sarim
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[8], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[8] = true;
			break;
		case 69838:// Seers Village
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[9], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[9] = true;
			break;
		case 69839:// Taverly
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[10], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[10] = true;
			break;
		case 69840:// Varrock
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[11], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[11] = true;
			break;
		case 69841:// Yanille
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[12], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[12] = true;
			break;
		}
	}

	/**
	 * Sends the player their reward for activating the lodestone.
	 * 
	 */
	private void sendReward() {
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPackets().sendGameMessage("You have no free spaces in your inventory. Your reward is on the ground.");
			FloorItem.createGroundItem(new Item(995, 10_000), (WorldTile)player, player, false, 180, false);
			return;
		} else {
			player.getPackets().sendGameMessage("You are rewarded for your efforts");
			player.getInventory().addItem(995, 10_000);
		}
	}

	/**
	 * Checks if the player has unlocked the lodestone during login.
	 * 
	 */
	public void checkActivation() {
		// Lumbridge is auto unlocked.
		player.getPackets().sendConfigByFile(10907, 1);
		for (int x = 0; x <= 12; x++) {
			if (player.getActivatedLodestones()[x] == true) {
				player.getPackets().sendConfigByFile(CONFIG_IDS[x], 1);
			}
		}
		if (player.getActivatedLodestones()[13] == true) {
			player.getPackets().sendConfigByFile(CONFIG_IDS[13], 190);
		}
		if (player.getActivatedLodestones()[14] == true) {
			player.getPackets().sendConfigByFile(CONFIG_IDS[14], 15);
		}
	}

	public void unlockAllLodestones() {
		player.getPackets().sendConfigByFile(CONFIG_IDS[14], 190);
		player.getActivatedLodestones()[14] = true;
		
		player.getPackets().sendConfigByFile(CONFIG_IDS[13], 190);
		player.getActivatedLodestones()[13] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[0], 1);
		player.getActivatedLodestones()[0] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[1], 1);
		player.getActivatedLodestones()[1] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[2], 1);
		player.getActivatedLodestones()[2] = true;
		
		player.getPackets().sendConfigByFile(CONFIG_IDS[3], 1);
		player.getActivatedLodestones()[3] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[4], 1);
		player.getActivatedLodestones()[4] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[5], 1);
		player.getActivatedLodestones()[5] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[6], 1);
		player.getActivatedLodestones()[6] = true;
		
		player.getPackets().sendConfigByFile(CONFIG_IDS[8], 1);
		player.getActivatedLodestones()[8] = true;
		
		player.getPackets().sendConfigByFile(CONFIG_IDS[9], 1);
		player.getActivatedLodestones()[9] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[10], 1);
		player.getActivatedLodestones()[10] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[11], 1);
		player.getActivatedLodestones()[11] = true;
		
		player.getPackets().sendConfigByFile(CONFIG_IDS[12], 1);
		player.getActivatedLodestones()[12] = true;

		player.getPackets().sendConfigByFile(CONFIG_IDS[7], 1);
		player.getActivatedLodestones()[7] = true;

	}
}
