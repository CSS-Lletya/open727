package com.rs.game.minigames.creations;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.utils.Utils;

public class CreationSkillsAction extends Action {

	private Animation animation;
	private WorldObject object;
	private int baseId;
	private int objectIndex;

	public static enum CreationObjects {

		CLASS_1(10, -1, 1, 1), // doesnt run out copper

		CLASS_2(20, 200, 6, 20), // silver

		CLASS_3(25, 300, 12, 40), // mithril

		CLASS_4(30, 400, 16, 60), // adamant

		CLASS_5(35, 500, 20, 80); // rune

		private int baseTime, randomTime, randomLife, level;

		private CreationObjects(int baseTime, int randomLife, int randomTime, int level) {
			this.baseTime = baseTime;
			this.randomTime = randomTime;
			this.randomLife = randomLife;
			this.level = level;
		}

		public int getBaseTime() {
			return baseTime;
		}

		public int getRandomTime() {
			return randomTime;
		}

		public int getRandomLife() {
			return randomLife;
		}

		public int getLevel() {
			return level;
		}
	}

	private CreationObjects definitions;
	private Item itemUsed;
	private int skillId;

	// 50 == class 4

	public CreationSkillsAction(WorldObject object, Animation animation, Item bestItem, int baseId, int objectIndex) {
		this.skillId = Skills.HUNTER;
		this.object = object;
		this.animation = animation;
		this.itemUsed = bestItem;
		this.baseId = baseId;
		this.objectIndex = objectIndex;
	}

	@Override
	public boolean start(Player player) {
		definitions = CreationObjects.CLASS_5;
		if (player.getSkills().getLevel(StealingCreation.getRequestedObjectSkill()) < definitions.getLevel()
				|| itemUsed == null)
			return false;
		setActionDelay(player, getSkillTimer(player, skillId));
		player.setNextFaceWorldTile(object);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (itemUsed == null)
			return false;
		player.setNextAnimation(animation);
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return -1;
		} else if (Utils.getRandom(definitions.getRandomLife()) == 0) {
			System.out.println("Empty");
		}
		player.getInventory().addItem(new Item(StealingCreation.SACRED_CLAY[objectIndex], 1));
		return getSkillTimer(player, skillId);
	}

	public int getSkillTimer(Player player, int skillId) {
		int playerLevel = player.getSkills().getLevel(Skills.FISHING);
		int fishLevel = definitions.level;
		int modifier = itemUsed.getDefinitions().getWearingSkillRequiriments().get(skillId);
		int randomAmt = Utils.random(4);
		double cycleCount = 1, otherBonus = 0;
		cycleCount = Math.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10) / modifier * 0.25 - randomAmt * 4);
		if (cycleCount < 1)
			cycleCount = 1;
		int delay = (int) cycleCount + 1;
		delay /= player.getAuraManager().getFishingAccurayMultiplier();
		return delay;
	}

	@Override
	public void stop(Player player) {
		this.setActionDelay(player, 3);
	}
}
