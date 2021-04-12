package com.rs.game.player.actions.mining;

import com.rs.game.Animation;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

public class LivingMineralMining extends MiningBase {

	private LivingRock rock;

	public LivingMineralMining(LivingRock rock) {
		this.rock = rock;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int oreBaseTime = 50;
		int oreRandomTime = 20;
		int mineTimer = oreBaseTime - player.getSkills().getLevel(Skills.MINING) - Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + oreRandomTime)
			mineTimer = 1 + Utils.getRandom(oreRandomTime);
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}

	private boolean checkAll(Player player) {
		if (!setPickaxe(player)) {
			player.getPackets().sendGameMessage("You need a pickaxe to mine this rock.");
			return false;
		}
		if (!hasPickaxe(player)) {
			player.getPackets().sendGameMessage("You dont have the required level to use this pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		if (!rock.canMine(player)) {
			player.getPackets().sendGameMessage(
					"You must wait at least one minute before you can mine a living rock creature that someone else defeated.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (73 > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage("You need a mining level of 73 to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(emoteId));
		return checkRock(player);
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		rock.takeRemains();
		player.setNextAnimation(new Animation(-1));
		return -1;
	}

	private void addOre(Player player) {
		player.getSkills().addXp(Skills.MINING, 25);
		player.getInventory().addItem(15263, Utils.random(5, 25));
		player.getPackets().sendGameMessage("You manage to mine some living minerals.", true);
	}

	private boolean checkRock(Player player) {
		return !rock.hasFinished();
	}
}
