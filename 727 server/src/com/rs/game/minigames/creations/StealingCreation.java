package com.rs.game.minigames.creations;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

/**
 * @author Richard (Flamable)
 * @author Khaled
 *
 */
public class StealingCreation {

	private static final int[] TOTAL_SKILL_IDS = { Skills.WOODCUTTING, Skills.MINING, Skills.FISHING, Skills.HUNTER,
			Skills.COOKING, Skills.HERBLORE, Skills.CRAFTING, Skills.SMITHING, Skills.FLETCHING, Skills.RUNECRAFTING,
			Skills.CONSTRUCTION };
	private static final int[] TOTAL_COMBAT_IDS = { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.HITPOINTS,
			Skills.RANGE, Skills.MAGIC, Skills.PRAYER, Skills.SUMMONING };
	private static final int[] BASE_ANIMATIONS = { 10603, 10608, 10613, 10618 };
	public static final int[] SACRED_CLAY = { 14182, 14184, 14186, 14188, 14190 };

	private static List<Player> redTeam = new ArrayList<Player>(), blueTeam = new ArrayList<Player>();// no maximum limit on it i beleive
	// plus i wont be using this non but like 5 times
	@SuppressWarnings("unused")
	private static TimerTask lobbyTask, gameTask;
	public static final int[] CLASS_ITEMS_BASE = { 14132, 14122, 14142, 14152, 14172, 14162, 14367, 14357, 14347, 14411,
			14391, 14401, 14337, 14317, 14327, 14297, 14287, 14307, 14192, 14202, 12850, 12851, 14422, 14377, 14421, -1,
			-1, 14215, 14225, 14235, 14245, 14255, 14265, 14275, 14285 };
	public static final WorldTile LOBBY_WORLDTILE = new WorldTile(2968, 9701, 0);

	private static class LobbyTimer extends TimerTask {

		private int minutes;

		@Override
		public void run() {
			if (!hasRequiredPlayers()) {
				this.cancel();
				return;
			} else if (minutes++ == 2) {
				passToGame();
				this.cancel();
				return;
			}
		}
	}

	private static class DynamicRegion {

		int[][] boundChunks = { { 240, 714 /* kiln square */ }, { 240, 715 /* u jellies :D */ },
				{ 241, 714 /* altar */ }, { 240, 715 /* rift, at least 1 */ },
				{ 241, 716 /* agility climb at least 1 */ } };
		boolean[] classCheck = new boolean[4];

	}

	public static void enterTeamLobby(Player player, boolean inRedTeam) {
		if (!canEnter(player, inRedTeam)) {
			return;
		} else if (!hasRequiredPlayers())// saves performance
			CoresManager.fastExecutor.scheduleAtFixedRate(lobbyTask = new LobbyTimer(), 0, 60000);
		player.getControlerManager().startControler("StealingCreationsLobby", inRedTeam);
	}

	public static void passToGame() {
		// createDynamicRegion
		for (Player player : redTeam) {
			player.getControlerManager().startControler("StealingCreationsGame", true);
		}
		for (Player player : blueTeam) {
			player.getControlerManager().startControler("StealingCreationsGame", false);
		}
	}

	public static boolean proccessKilnItems(Player player, int componentId, int index, int itemId, int amount) {
		int clayId = SACRED_CLAY[index];
		if (player.getInventory().containsItem(clayId, 1)) {
			if (player.getInventory()
					.addItem(new Item(
							StealingCreation.CLASS_ITEMS_BASE[componentId - 37]
									+ ((componentId == 57 || componentId == 58 || componentId == 61)
											? 0
											: componentId == 56 ? index
													: componentId >= 64 ? (-index * 2) : (index * 2)),
							(componentId >= 56 && componentId <= 58 ? 15 * (index + 1)
									: componentId == 61 ? index + 1 : 1) * amount))) {
				player.getInventory().deleteItem(itemId, amount);
				return true;
			}
		}
		player.getPackets().sendGameMessage("You have no clay to proccess.");
		return false;
	}

	public static boolean checkSkillRequriments(Player player, int requestedSkill, int index) {
		int level = getLevelForIndex(index);
		if (player.getSkills().getLevel(requestedSkill) <= level) {
			player.getPackets().sendGameMessage("You dont have the requried " + Skills.SKILL_NAME[requestedSkill]
					+ " level for that quality of clay.");
			return false;
		}
		return true;
	}

	public static void startDynamicSkill(Player player, WorldObject object, Animation animation, int baseId,
			int objectIndex) {
		if (!checkSkillRequriments(player, getRequestedObjectSkill(), objectIndex))
			return;
		Item item = getBestItem(player, baseId);
		if (item.getId() == -1)
			animation = new Animation(10602);
		else if (player.getInventory().containsItem(item.getId(), item.getAmount()))
			player.setNextAnimation(animation);
		player.getActionManager().setAction(new CreationSkillsAction(object, animation, item, baseId, objectIndex));
	}

	static Item getBestItem(Player player, int baseId) {
		for (int index = 4; index >= 0; index--) {
			Item item = new Item(baseId + (index * 2), 1);
			if (player.getInventory().getItems().contains(item)) {
				return item;
			}
		}
		return new Item(-1, 1);
	}

	public static int getRequestedObjectSkill() {
		return 0;
	}

	public static int getRequestedKilnSkill(int indexedId) {
		if (indexedId >= 0 && indexedId <= 1 || indexedId >= 6 && indexedId <= 8 || indexedId >= 15 && indexedId <= 17)
			return Skills.SMITHING;
		else if (indexedId >= 2 && indexedId <= 3 || indexedId >= 9 && indexedId <= 14
				|| indexedId >= 18 && indexedId <= 19 || indexedId == 23)
			return Skills.CRAFTING;
		else if (indexedId == 4)
			return Skills.CONSTRUCTION;
		else if (indexedId == 5)
			return Skills.COOKING;
		else if (indexedId >= 20 && indexedId <= 21)
			return Skills.RUNECRAFTING;
		else if (indexedId >= 22 && indexedId <= 24)
			return Skills.SUMMONING;
		else if (indexedId >= 25 && indexedId <= 32)
			return Skills.HERBLORE;
		return 1;
	}

	private static int getLevelForIndex(int index) {
		int level = 0;
		for (int i = 0; i < index; i++) {
			if (i == index)
				return level;
			level += 20;
		}
		return level;
	}

	private static void sendGameEnding() {
		sendGameConfig(556, 1);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				sendGameConfig(561, 1);
				for (Player player : redTeam)
					player.getPackets().sendHideIComponent(809, 18, false);
				for (Player player : blueTeam)
					player.getPackets().sendHideIComponent(809, 18, false);
			}
		});
	}

	private static void sendGameConfig(int id, int value) {
		for (Player player : redTeam)
			player.getPackets().sendGlobalConfig(id, value);
		for (Player player : blueTeam)
			player.getPackets().sendGlobalConfig(id, value);
	}

	private static boolean hasRequiredPlayers() {
		if (redTeam.size() >= 5 && blueTeam.size() >= 5)
			return true;
		return false;
	}

	public static void updateInterfaces() {
		boolean hidden = hasRequiredPlayers();
		for (Player player : redTeam) {
			player.getPackets().sendHideIComponent(804, 2, hidden);
			updateTeamInterface(player, true, hidden);
		}
		for (Player player : blueTeam) {
			player.getPackets().sendHideIComponent(804, 2, hidden);
			updateTeamInterface(player, false, hidden);
		}
	}

	public static void updateTeamInterface(Player player, boolean inRedTeam, boolean hidden) {
		int skillTotal = getTotalLevel(TOTAL_SKILL_IDS, inRedTeam);
		int combatTotal = getTotalLevel(TOTAL_COMBAT_IDS, inRedTeam);
		int otherSkillTotal = getTotalLevel(TOTAL_SKILL_IDS, !inRedTeam);
		int otherCombatTotal = getTotalLevel(TOTAL_COMBAT_IDS, !inRedTeam);
		player.getPackets().sendIComponentText(804, 5, "" + skillTotal);
		player.getPackets().sendIComponentText(804, 4, "" + combatTotal);
		player.getPackets().sendIComponentText(804, 6, "" + otherSkillTotal);
		player.getPackets().sendIComponentText(804, 7, "" + otherCombatTotal);
		for (int i = 33; i < 34; i++)
			player.getPackets().sendIComponentText(804, i,
					"" + (hidden ? 5 - (inRedTeam ? redTeam.size() : blueTeam.size())
							: inRedTeam ? redTeam.size() : blueTeam.size()));
	}

	private static boolean canEnter(Player player, boolean inRedTeam) {
		int skillTotal = getTotalLevel(TOTAL_SKILL_IDS, inRedTeam);
		int combatTotal = getTotalLevel(TOTAL_COMBAT_IDS, inRedTeam);
		int otherSkillTotal = getTotalLevel(TOTAL_SKILL_IDS, !inRedTeam);
		int otherCombatTotal = getTotalLevel(TOTAL_COMBAT_IDS, !inRedTeam);
		if ((skillTotal + combatTotal) > (otherSkillTotal + otherCombatTotal)) {
			player.getPackets().sendGameMessage("This team is too strong for you to join at present.");
			return false;
		} else if (player.getEquipment().wearingArmour() || player.getInventory().getItemsContainerSize() != 0
				|| player.getFamiliar() != null) {
			player.getPackets().sendGameMessage(
					"You may not take any items into Stealing Creation. You can use the nearby bank deposit bank to empty your inventory and storn wore items.");
			return false;
		}
		return true;
	}

	private static int getTotalLevel(int[] ids, boolean inRedTeam) {
		int skillTotal = 0;
		for (Player player : inRedTeam ? redTeam : blueTeam) {
			if (player == null)
				continue;
			for (int skillRequested : ids) {
				skillTotal += player.getSkills().getLevel(skillRequested);
			}
		}
		return skillTotal;
	}

	public static List<Player> getRedTeam() {
		return redTeam;
	}

	public static List<Player> getBlueTeam() {
		return blueTeam;
	}

	public static Animation getAnimationForBase(int baseId, int index) {
		return new Animation(BASE_ANIMATIONS[index] + baseId);
	}
}
