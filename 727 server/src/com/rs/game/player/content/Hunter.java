package com.rs.game.player.content;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.minigames.PuroPuro;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

import skills.Skills;

public class Hunter {

	public static final Animation CAPTURE_ANIMATION = new Animation(6606);

	public enum FlyingEntities {

		BABY_IMPLING(1, 1, 20, 25, 17, new Item[] { new Item(946, 1), new Item(1755, 1), new Item(1734, 1),
				new Item(1733, 1), new Item(2347, 1), new Item(1985, 1) }, null, null, null),

		YOUNG_IMPLING(1, 1, 48, 65, 22,
				new Item[] { new Item(361, 1), new Item(1901, 1), new Item(1539, 5), new Item(1784, 4),
						new Item(1523, 1), new Item(7936, 1), new Item(5970, 1) },
				new Item[] { new Item(855, 1), new Item(1353, 1), new Item(2293, 1), new Item(7178, 1),
						new Item(247, 1), new Item(453, 1), new Item(1777, 1), new Item(231, 1), new Item(2347, 1) },
				new Item[] { new Item(1097, 1), new Item(1157, 1), new Item(8778, 1), new Item(133, 1),
						new Item(2359, 1), },
				null),

		GOURMET_IMPLING(1, 1, 82, 113, 28, null, null, null, null),

		EARTH_IMPLING(1, 1, 126, 177, 36, null, null, null, null),

		ESSENCE_IMPLING(1, 1, 160, 225, 42, null, null, null, null),

		ELECTIC_IMPLING(1, 1, 205, 289, 50, null, null, null, null),

		SPIRIT_IMPLING(1, 1, 227, 321, 54, null, null, null, null),

		NATURE_IMPLING(1, 1, 250, 353, 58, null, null, null, null),

		MAGPIE_IMPLING(1, 1, 289, 409, 65, null, null, null, null),

		NINJA_IMPLING(1, 1, 339, 481, 74, null, null, null, null),

		PIRATE_IMPLING(1, 1, 350, 497, 76, null, null, null, null),

		DRAGON_IMPLING(1, 1, 390, 553, 83, null, null, null, null),

		ZOMBIE_IMPLING(1, 1, 412, 585, 87, null, null, null, null),

		KINGLY_IMPLING(1, 1, 434, 617, 91,
				new Item[] { new Item(1705, Utils.random(3, 11)), new Item(1684, 3),
						new Item(1618, Utils.random(17, 34)), new Item(990, 2) },
				new Item[] { new Item(1631, 1), new Item(1615, 1), new Item(9341, 40 + Utils.getRandom(30)),
						new Item(9342, 57), new Item(15511, 1), new Item(15509, 1), new Item(15505, 1),
						new Item(15507, 1), new Item(15503, 1), new Item(11212, 40 + Utils.random(104)),
						new Item(9193, 62 + Utils.random(8)), new Item(11230, Utils.random(182, 319)),
						new Item(11232, 70), new Item(1306, Utils.random(1, 2)), new Item(1249, 1) },
				null, new Item[] { new Item(7158, 1), new Item(2366, 1), new Item(6571, 1) }),

		BUTTERFLYTEST(1, 1, 434, 617, 91, null, null, null, null) {

			@Override
			public void effect(Player player) {
				// stat boost
			}
		};

		static final Map<Short, FlyingEntities> flyingEntities = new HashMap<Short, FlyingEntities>();

		static {
			for (FlyingEntities impling : FlyingEntities.values())
				flyingEntities.put((short) impling.reward, impling);
		}

		public static FlyingEntities forItem(short reward) {
			return flyingEntities.get(reward);
		}

		private int npcId, level, reward;
		private double puroExperience, rsExperience;
		private Item[] rarleyCommon, common, rare, extremelyRare;
		private Graphics graphics;

		private FlyingEntities(int npcId, int reward, double puroExperience, double rsExperience, int level,
				Graphics graphics, Item[] rarleyCommon, Item[] common, Item[] rare, Item[] extremelyRare) {
			this.npcId = npcId;
			this.reward = reward;
			this.puroExperience = puroExperience;
			this.rsExperience = rsExperience;
			this.level = level;
			this.rarleyCommon = rarleyCommon;
			this.common = common;
			this.rare = rare;
			this.extremelyRare = extremelyRare;
			this.graphics = graphics;
		}

		private FlyingEntities(int npcId, int reward, double puroExperience, double rsExperience, int level,
				Item[] rarleyCommon, Item[] common, Item[] rare, Item[] extremelyRare) {
			this.npcId = npcId;
			this.reward = reward;
			this.puroExperience = puroExperience;
			this.rsExperience = rsExperience;
			this.level = level;
			this.rarleyCommon = rarleyCommon;
			this.common = common;
			this.rare = rare;
			this.extremelyRare = extremelyRare;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getLevel() {
			return level;
		}

		public int getReward() {
			return reward;
		}

		public double getPuroExperience() {
			return puroExperience;
		}

		public double getRsExperience() {
			return rsExperience;
		}

		public Item[] getRarleyCommon() {
			return rarleyCommon;
		}

		public Item[] getCommon() {
			return common;
		}

		public Item[] getRare() {
			return rare;
		}

		public Item[] getExtremelyRare() {
			return extremelyRare;
		}

		public Graphics getGraphics() {
			return graphics;
		}

		public void effect(Player player) {

		}
	}

	public interface DynamicFormula {

		public int getExtraProbablity(Player player);

	}

	public static void captureFlyingEntity(final Player player, final NPC npc) {// LOL @ name and each hunter skill should have different formulas I beleive
		final String name = npc.getDefinitions().name.toUpperCase();
		final FlyingEntities instance = FlyingEntities.valueOf(name);
		final boolean isImpling = name.toLowerCase().contains("impling");
		if (!player.getInventory().containsItem(isImpling ? 1 : 11, 1))
			return;
		player.lock(1);
		player.getPackets().sendGameMessage("You swing your net...");
		player.setNextAnimation(CAPTURE_ANIMATION);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (isSuccessful(player, instance.getLevel(), new DynamicFormula() {

					@Override
					public int getExtraProbablity(Player player) {
						if (player.getInventory().containsItem(3000, 1)
								|| player.getEquipment().getItem(3).getId() == 3000)
							return 3;// magic net
						else if (player.getInventory().containsItem(3001, 1)
								|| player.getEquipment().getItem(3).getId() == 3000)
							return 2;// regular net
						return 1;// hands
					}
				})) {
					player.getSkills().addXp(Skills.HUNTER,
							player.getControlerManager().getControler() instanceof PuroPuro
									? instance.getPuroExperience()
									: instance.getRsExperience());
					npc.finish();
					player.getInventory().addItem(new Item(instance.getReward(), 1));
					player.getPackets().sendGameMessage("...and you successfully caputure the " + name.toLowerCase());
				} else {
					if (isImpling) {
						npc.setNextForceTalk(new ForceTalk("Tehee, you missed me!"));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								WorldTile teleTile = npc;
								for (int trycount = 0; trycount < 10; trycount++) {
									teleTile = new WorldTile(npc, 10);
									if (World.canMoveNPC(npc.getPlane(), teleTile.getX(), teleTile.getY(),
											player.getSize()))
										break;
									teleTile = npc;
								}
								npc.setNextWorldTile(teleTile);
							}
						});
					}
					player.getPackets().sendGameMessage("...you stumble and miss the " + name.toLowerCase());
				}
			}
		});
	}

	public static void openJar(Player player, FlyingEntities instance) {
		boolean isImpling = instance.toString().toLowerCase().contains("impling");
		if (isImpling) {
			int chance = Utils.getRandom(100);
			Item item = null;
			if (chance <= 30) {
				item = instance.getRarleyCommon()[Utils.random(instance.getRarleyCommon().length)];
			} else if (chance <= 60) {
				item = instance.getCommon()[Utils.random(instance.getCommon().length)];
			} else if (chance <= 80) {
				item = instance.getRare()[Utils.random(instance.getRare().length)];
			} else if (chance == 100) {
				item = instance.getExtremelyRare()[Utils.random(instance.getExtremelyRare().length)];
			}
			player.getInventory().addItem(item);
		} else
			instance.effect(player);
		if (Utils.random(100) > 93) {
			player.getInventory().deleteItem(new Item(isImpling ? 50 : 51, 1));
			player.getPackets().sendGameMessage("You press too hard on the jar and the glass shatters in your hands.");
		}
	}

	static int[] requiredLogs = new int[] { 1151, 1153, 1155, 1157, 1519, 1521, 13567, 1521, 2862, 10810, 6332, 12581 };

	public static void createLoggedObject(Player player, WorldObject object, boolean kebbits) {
		if (!player.getInventory().containsOneItem(requiredLogs)) {
			player.getPackets().sendGameMessage("You need to have logs to create this trap.");
			return;
		}
		player.lock(3);
		player.getActionManager().setActionDelay(3);
		player.setNextAnimation(new Animation(5208));// all animation for everything :L
		if (World.removeTemporaryObject(object, 300000, false)) {// five minute delay
			World.spawnTemporaryObject(
					new WorldObject(kebbits ? 19206 : -1, object.getType(), object.getRotation(), object), 300000,
					false);// TODO
			Item item = null;
			for (int requiredLog : requiredLogs) {
				if ((item = player.getInventory().getItems().lookup(requiredLog)) != null) {
					player.getInventory().deleteItem(item);
				}
			}
		}
	}

	public static boolean isSuccessful(Player player, int dataLevel, DynamicFormula formula) {
		int hunterlevel = player.getSkills().getLevel(Skills.HUNTER);
		int increasedProbability = formula == null ? 1 : formula.getExtraProbablity(player);
		int level = Utils.random(hunterlevel + increasedProbability) + 1;
		double ratio = level / (Utils.random(dataLevel + 4) + 1);
		if (Math.round(ratio * dataLevel) < dataLevel) {
			return false;
		}
		return true;
	}
}
