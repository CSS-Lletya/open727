package com.rs.game.minigames.creations;

import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.content.Magic;
import com.rs.game.player.controlers.Controler;
import com.rs.net.decoders.WorldPacketsDecoder;

public class StealingCreationGame extends Controler {

	private int score, index = 0;

	// X : 1926 Y : 5716

	@Override
	public void start() {
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		// player.getInterfaceManager().sendTab(809, player.getInterfaceManager().hasRezizableScreen() ? 11 : 27);
		sendUpdateScore(30);
	}

	private void sendUpdateScore(int value) {
		score += value;
		player.getPackets().sendConfig(1333, score);
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 39546) {
			if (!player.getInventory().containsOneItem(StealingCreation.SACRED_CLAY)) {
				player.getPackets().sendGameMessage(
						"You try using the processing point, but quickly realize that you have no sacred clay with you.");
				return false;
			}
			calculateKilnIndex();
			player.getInterfaceManager().sendInterface(813);
			player.getPackets().sendHideIComponent(813, 98 + (index * 2), false);
			return false;
		}
		switch (object.getId()) {
		case 39553:
		case 39552:
		case 39551:
		case 39550:
			int calculatedId = 4 - (object.getId() - 39553);
			int baseId = StealingCreation.CLASS_ITEMS_BASE[1];
			String name = null;
			Item item = StealingCreation.getBestItem(player, baseId);
			if (item.getId() != -1)
				name = item.getDefinitions().getName();
			StealingCreation.startDynamicSkill(player, object,
					name == null ? null
							: StealingCreation.getAnimationForBase(Integer.parseInt(
									name.substring(name.indexOf("(")).replace("(class ", "").replace(")", "")) - 1, 1),
					baseId, calculatedId);
			return false;
		case 39586:
		case 39587:
		case 39588:
		case 39589:
			calculatedId = 4 - (object.getId() - 39586);
			baseId = StealingCreation.CLASS_ITEMS_BASE[0];
			name = null;
			item = StealingCreation.getBestItem(player, baseId);
			if (item.getId() != -1)
				name = item.getDefinitions().getName();
			StealingCreation.startDynamicSkill(player, object,
					name == null ? null
							: StealingCreation.getAnimationForBase(Integer.parseInt(
									name.substring(name.indexOf("(")).replace("(class ", "").replace(")", "")) - 1, 0),
					baseId, calculatedId);
			return false;
		case 39574:
		case 39575:
		case 39576:
		case 39577:
			calculatedId = 4 - (object.getId() - 39574);
			baseId = StealingCreation.CLASS_ITEMS_BASE[3];
			name = null;
			item = StealingCreation.getBestItem(player, baseId);
			if (item.getId() != -1)
				name = item.getDefinitions().getName();
			StealingCreation.startDynamicSkill(player, object,
					name == null ? null
							: StealingCreation.getAnimationForBase(Integer.parseInt(
									name.substring(name.indexOf("(")).replace("(class ", "").replace(")", "")) - 1, 3),
					baseId, calculatedId);
			return false;
		case 39569:
		case 39564:
		case 39563:
		case 39562:
			calculatedId = 4 - (object.getId() - (object.getId() == 39569 ? 39566 : 39562));
			baseId = StealingCreation.CLASS_ITEMS_BASE[2];
			name = null;
			item = StealingCreation.getBestItem(player, baseId);
			if (item.getId() != -1)
				name = item.getDefinitions().getName();
			StealingCreation.startDynamicSkill(player, object,
					name == null ? null
							: StealingCreation.getAnimationForBase(Integer.parseInt(
									name.substring(name.indexOf("(")).replace("(class ", "").replace(")", "")) - 1, 2),
					baseId, calculatedId);
			return false;
		}
		return true;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (interfaceId == 813) {
			if (componentId >= 37 && componentId <= 71)
				processKilnExchange(componentId, packetId);
			return true;
		}
		return true;
	}

	private void processKilnExchange(int componentId, int packetId) {
		int itemId = StealingCreation.SACRED_CLAY[index];
		int amount = 0;
		if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			amount = 1;
		else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
			amount = 5;
		else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
			player.getTemporaryAttributtes().put("scIndex", index);
			player.getTemporaryAttributtes().put("scComponentId", componentId);
			player.getTemporaryAttributtes().put("scItemId", itemId);
			player.getTemporaryAttributtes().put("scAmount", amount);
			player.getTemporaryAttributtes().put("kilnX", true);
			player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
		} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
			amount = player.getInventory().getNumerOf(itemId);
		if (StealingCreation.checkSkillRequriments(player, StealingCreation.getRequestedKilnSkill(componentId - 37),
				index)) {
			if ((amount != 0 && StealingCreation.proccessKilnItems(player, componentId, index, itemId, amount)))
				return;
		}
	}

	private void calculateKilnIndex() {
		for (int index = 4; index >= 0; index--) {
			int item = StealingCreation.SACRED_CLAY[index];
			if (player.getInventory().containsOneItem(item)) {
				this.index = index;
				return;
			}
		}
		this.index = 0;
	}

	@Override
	public boolean logout() {
		Magic.sendTeleportSpell(player, -1, -1, -1, -1, 0, 0, StealingCreation.LOBBY_WORLDTILE, 0, true, -1);
		return true;// removes script
	}

	@Override
	public boolean login() {
		return true;// removes script
	}
}
