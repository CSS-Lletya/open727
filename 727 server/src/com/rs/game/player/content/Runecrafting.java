package com.rs.game.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;

public final class Runecrafting {

	private Runecrafting() {

	}

	public final static int[] LEVEL_REQ = { 1, 25, 50, 75 };
	public final static int RUNE_ESSENCE = 1436, PURE_ESSENCE = 7936, AIR_TIARA = 5527, MIND_TIARA = 5529,
			WATER_TIARA = 5531, BODY_TIARA = 5533, EARTH_TIARA = 5535, FIRE_TIARA = 5537, COSMIC_TIARA = 5539,
			NATURE_TIARA = 5541, CHAOS_TIARA = 5543, LAW_TIARA = 5545, DEATH_TIARA = 5547, BLOOD_TIARA = 5549,
			SOUL_TIARA = 5551, ASTRAL_TIARA = 9106, OMNI_TIARA = 13655;

	public static boolean isTiara(int id) {
		return id == AIR_TIARA || id == MIND_TIARA || id == WATER_TIARA || id == BODY_TIARA || id == EARTH_TIARA
				|| id == FIRE_TIARA || id == COSMIC_TIARA || id == NATURE_TIARA || id == CHAOS_TIARA || id == LAW_TIARA
				|| id == DEATH_TIARA || id == BLOOD_TIARA || id == SOUL_TIARA || id == ASTRAL_TIARA || id == OMNI_TIARA;
	}

	public static void enterAirAltar(Player player) {
		enterAltar(player, new WorldTile(2841, 4829, 0));
	}

	private static void enterAltar(Player player, WorldTile dest) {
		player.getPackets().sendGameMessage("A mysterious force grabs hold of you.");
		player.useStairs(-1, dest, 0, 1);
	}

	public static void enterMindAltar(Player player) {
		enterAltar(player, new WorldTile(2792, 4827, 0));
	}

	public static void enterWaterAltar(Player player) {
		enterAltar(player, new WorldTile(3482, 4838, 0));
	}

	public static void enterEarthAltar(Player player) {
		enterAltar(player, new WorldTile(2655, 4830, 0));
	}

	public static void enterFireAltar(Player player) {
		enterAltar(player, new WorldTile(2574, 4848, 0));
	}

	public static void enterBodyAltar(Player player) {
		enterAltar(player, new WorldTile(2522, 4825, 0));
	}

	public static void craftEssence(Player player, int rune, int level, double experience, boolean pureEssOnly,
			int... multipliers) {
		int actualLevel = player.getSkills().getLevel(Skills.RUNECRAFTING);
		if (actualLevel < level) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a runecrafting level of " + level + " to craft this rune.");
			return;
		}
		int runes = player.getInventory().getItems().getNumberOf(PURE_ESSENCE);
		if (runes > 0)
			player.getInventory().deleteItem(PURE_ESSENCE, runes);
		if (!pureEssOnly) {
			int normalEss = player.getInventory().getItems().getNumberOf(RUNE_ESSENCE);
			if (normalEss > 0) {
				player.getInventory().deleteItem(RUNE_ESSENCE, normalEss);
				runes += normalEss;
			}
		}
		if (runes == 0) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You don't have " + (pureEssOnly ? "pure" : "rune") + " essence.");
			return;
		}
		double totalXp = experience * runes;
		if (hasRcingSuit(player))
			totalXp *= 1.025;
		player.getSkills().addXp(Skills.RUNECRAFTING, totalXp);
		for (int i = multipliers.length - 2; i >= 0; i -= 2) {
			if (actualLevel >= multipliers[i]) {
				runes *= multipliers[i + 1];
				break;
			}
		}
		player.setNextGraphics(new Graphics(186));
		player.setNextAnimation(new Animation(791));
		player.lock(5);
		player.getInventory().addItem(rune, runes);
		player.getPackets().sendGameMessage("You bind the temple's power into "
				+ ItemDefinitions.getItemDefinitions(rune).getName().toLowerCase() + "s.");
	}

	public static boolean hasRcingSuit(Player player) {
		if (player.getEquipment().getHatId() == 21485 && player.getEquipment().getChestId() == 21484
				&& player.getEquipment().getLegsId() == 21486 && player.getEquipment().getBootsId() == 21487)
			return true;
		return false;
	}

	public static void locate(Player p, int xPos, int yPos) {
		String x = "";
		String y = "";
		int absX = p.getX();
		int absY = p.getY();
		if (absX >= xPos)
			x = "west";
		if (absY > yPos)
			y = "South";
		if (absX < xPos)
			x = "east";
		if (absY <= yPos)
			y = "North";
		p.getPackets().sendGameMessage("The talisman pulls towards " + y + "-" + x + ".", false);
	}

	public static void checkPouch(Player p, int i) {
		if (i < 0)
			return;
		p.getPackets().sendGameMessage(

				"This pouch has " + p.getPouches()[i] + " rune essences in it.", false);
	}

	public static final int[] POUCH_SIZE = { 3, 6, 9, 12 };

	public static void fillPouch(Player p, int i) {
		if (i < 0)
			return;
		if (LEVEL_REQ[i] > p.getSkills().getLevel(Skills.RUNECRAFTING)) {
			p.getPackets().sendGameMessage("You need a runecrafting level of " + LEVEL_REQ[i] + " to fill this pouch.",
					false);
			return;
		}
		int essenceToAdd = POUCH_SIZE[i] - p.getPouches()[i];
		if (essenceToAdd > p.getInventory().getItems().getNumberOf(1436))
			essenceToAdd = p.getInventory().getItems().getNumberOf(1436);
		if (essenceToAdd > POUCH_SIZE[i] - p.getPouches()[i])
			essenceToAdd = POUCH_SIZE[i] - p.getPouches()[i];
		if (essenceToAdd > 0) {
			p.getInventory().deleteItem(1436, essenceToAdd);
			p.getPouches()[i] += essenceToAdd;
		}
		if (!p.getInventory().containsOneItem(1436)) {
			p.getPackets().sendGameMessage("You don't have any essence with you.", false);
			return;
		}
		if (essenceToAdd == 0) {
			p.getPackets().sendGameMessage("Your pouch is full.", false);
			return;
		}
	}

	public static void emptyPouch(Player p, int i) {
		if (i < 0)
			return;
		int toAdd = p.getPouches()[i];
		if (toAdd > p.getInventory().getFreeSlots())
			toAdd = p.getInventory().getFreeSlots();
		if (toAdd > 0) {
			p.getInventory().addItem(1436, toAdd);
			p.getPouches()[i] -= toAdd;
		}
		if (toAdd == 0) {
			p.getPackets().sendGameMessage("Your pouch has no essence left in it.", false);
			return;
		}
	}
}