package com.rs.game.player.content;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

import skills.Skills;

public class Burying {

	public enum Bone {
		NORMAL(526, 100),

		BURNT(528, 100),

		WOLF(2859, 100),

		MONKEY(3183, 125),

		BAT(530, 125),

		BIG(532, 200),

		JOGRE(3125, 200),

		ZOGRE(4812, 250),

		SHAIKAHAN(3123, 300),

		BABY(534, 350),

		WYVERN(6812, 400),

		DRAGON(536, 500),

		FAYRG(4830, 525),

		RAURG(4832, 550),

		DAGANNOTH(6729, 650),

		OURG(4834, 750),

		FROST_DRAGON(18830, 850);

		private int id;
		private double experience;

		private static Map<Integer, Bone> bones = new HashMap<Integer, Bone>();

		static {
			for (Bone bone : Bone.values()) {
				bones.put(bone.getId(), bone);
			}
		}

		public static Bone forId(int id) {
			return bones.get(id);
		}

		private Bone(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}

		public static final Animation BURY_ANIMATION = new Animation(827);

		public static void bury(final Player player, int inventorySlot) {
			final Item item = player.getInventory().getItem(inventorySlot);
			if (item == null || Bone.forId(item.getId()) == null)
				return;
			if (player.getBoneDelay() > Utils.currentTimeMillis())
				return;
			final Bone bone = Bone.forId(item.getId());
			final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
			player.addBoneDelay(3000);
			player.getPackets().sendSound(2738, 0, 1);
			player.setNextAnimation(BURY_ANIMATION);
			player.getPackets().sendGameMessage("You dig a hole in the ground...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage("You bury the " + itemDef.getName().toLowerCase());
					player.getInventory().deleteItem(item.getId(), 1);
					double xp = bone.getExperience() * player.getAuraManager().getPrayerMultiplier();
					player.getSkills().addXp(Skills.PRAYER, xp);
					Double lastPrayer = (Double) player.getTemporaryAttributtes().get("current_prayer_xp");
					if (lastPrayer == null) {
						lastPrayer = 0.0;
					}
					double total = xp + lastPrayer;
					int amount = (int) (total / 500);
					if (amount != 0) {
						double restore = player.getAuraManager().getPrayerRestoration()
								* (player.getSkills().getLevelForXp(Skills.PRAYER) * 10);
						player.getPrayer().restorePrayer((int) (amount * restore));
						total -= amount * 500;
					}
					player.getTemporaryAttributtes().put("current_prayer_xp", total);
					stop();
				}

			}, 2);
		}
	}
}
