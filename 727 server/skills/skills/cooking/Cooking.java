package skills.cooking;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.utils.Utils;

import skills.Skills;

public class Cooking extends Action {

	public enum Cookables {

		/**
		 * Cooked Meat
		 */
		RAW_MEAT(new Item(2132, 1), 1, 200, 30, new Item(2146, 1), new Item(2142, 1), false, false),

		/**
		 * Cooked Chicken
		 */
		RAW_CHICKEN(new Item(2138, 1), 1, 200, 30, new Item(2144, 1), new Item(4291, 1), false, false),

		/**
		 * Ugthanki Meat
		 */
		RAW_UGTHANKI_MEAT(new Item(1859, 1), 1, 200, 30, new Item(2146, 1), new Item(1861, 1), false, false),

		/**
		 * Raw Rabbit
		 */
		RAW_RABBIT(new Item(3226, 1), 1, 200, 30, new Item(7222, 1), new Item(3228, 1), false, true),

		/**
		 * Bird Meat
		 */
		RAW_BIRD_MEAT(new Item(9978, 1), 11, 200, 62, new Item(9982, 1), new Item(9980, 1), true, false),

		/**
		 * Crab meat
		 */
		RAW_CRAB_MEAT(new Item(7518, 1), 21, 200, 100, new Item(7520, 1), new Item(7521, 1), false, false),

		/**
		 * Beast meat
		 */
		RAW_BEAST_MEAT(new Item(9986, 1), 21, 200, 82, new Item(9990, 1), new Item(9988, 1), true, false),

		/**
		 * Chompy
		 */
		RAW_CHOMPY(new Item(2876, 1), 30, 200, 140, new Item(2880, 1), new Item(2878, 1), false, true),

		/**
		 * Jubbly
		 */
		RAW_JUBBLY(new Item(7566, 1), 41, 200, 160, new Item(7570, 1), new Item(7568, 1), true, false),

		/**
		 * CrayFish
		 */
		RAW_CRAYFISH(new Item(13435, 1), 1, 32, 30, new Item(13437, 1), new Item(13433, 1), false, false),

		/**
		 * Shrimp
		 */
		RAW_SHRIMP(new Item(317, 1), 1, 34, 30, new Item(7954, 1), new Item(315, 1), false, false),

		/**
		 * Karambwanji
		 */
		RAW_KARAMBWANJI(new Item(3150, 1), 1, 35, 10, new Item(3148, 1), new Item(3151, 1), false, false),

		/**
		 * Sardine
		 */
		RAW_SARDINE(new Item(327, 1), 1, 38, 40, new Item(323, 1), new Item(325, 1), false, false),

		/**
		 * Anchovies
		 */
		RAW_ANCHOVIES(new Item(321, 1), 1, 34, 30, new Item(323, 1), new Item(319, 1), false, false),

		/**
		 * Karambwan
		 */
		POISON_KARAMBWAN(new Item(3142, 1), 1, 20, 80, new Item(3148, 1), new Item(3151, 1), false, false),

		/**
		 * Herring
		 */
		RAW_HERRING(new Item(345, 1), 5, 37, 50, new Item(357, 1), new Item(347, 1), false, false),

		/**
		 * Mackerel
		 */
		RAW_MACKEREL(new Item(353, 1), 10, 45, 60, new Item(357, 1), new Item(355, 1), false, false),

		/**
		 * Trout
		 */
		RAW_TROUT(new Item(335, 1), 15, 50, 70, new Item(343, 1), new Item(333, 1), false, false),

		/**
		 * Cod
		 */
		RAW_COD(new Item(341, 1), 18, 39, 75, new Item(343, 1), new Item(339, 1), false, false),

		/**
		 * Pike
		 */
		RAW_PIKE(new Item(349, 1), 20, 52, 80, new Item(349, 1), new Item(351, 1), false, false),

		/**
		 * Salmon
		 */
		RAW_SALMON(new Item(331, 1), 25, 58, 90, new Item(343, 1), new Item(329, 1), false, false),

		/**
		 * Slimy Eel
		 */
		RAW_SLIMY_EEL(new Item(3379, 1), 28, 58, 95, new Item(3383, 1), new Item(3381, 1), false, false),

		/**
		 * Tuna
		 */
		RAW_TUNA(new Item(359, 1), 30, 63, 100, new Item(367, 1), new Item(361, 1), false, false),

		/**
		 * Rainbow Fish
		 */
		RAW_RAINBOW_FISH(new Item(10138, 1), 35, 60, 110, new Item(10140, 1), new Item(10136, 1), false, false),

		/**
		 * Cave eel
		 */
		RAW_CAVE_EEL(new Item(5001, 1), 38, 40, 115, new Item(5006, 1), new Item(5003, 1), false, false),

		/**
		 * Lobster
		 */
		RAW_LOBSTER(new Item(377, 1), 40, 66, 120, new Item(381, 1), new Item(379, 1), false, false),

		/**
		 * Bass
		 */
		RAW_BASS(new Item(363, 1), 43, 80, 130, new Item(367, 1), new Item(365, 1), false, false),

		/**
		 * SwordFish
		 */
		RAW_SWORDFISH(new Item(371, 1), 45, 86, 140, new Item(375, 1), new Item(373, 1), false, false),

		/**
		 * Lava Eel
		 */
		RAW_LAVA_EEL(new Item(2148, 1), 53, 53, 30, new Item(-1, 1), new Item(2149, 1), false, false),

		/**
		 * MonkFish
		 */
		RAW_MONKFISH(new Item(7944, 1), 62, 90, 150, new Item(7948, 1), new Item(7946, 1), false, false),

		/**
		 * Shark
		 */
		RAW_SHARK(new Item(383, 1), 80, 100, 210, new Item(387, 1), new Item(385, 1), false, false),

		/**
		 * Sea turtle
		 */
		RAW_SEA_TURTLE(new Item(395, 1), 82, 200, 212, new Item(399, 1), new Item(397, 1), false, false),

		/**
		 * Cavefish
		 */
		RAW_CAVEFISH(new Item(15264, 1), 88, 100, 214, new Item(15268, 1), new Item(15266, 1), false, false),

		/**
		 * Manta Ray
		 */
		RAW_MANTA_RAY(new Item(389, 1), 91, 200, 216, new Item(393, 1), new Item(391, 1), false, false),

		/**
		 * RockTail
		 */
		RAW_ROCKTAIL(new Item(15270, 1), 92, 100, 225, new Item(15274, 1), new Item(15272, 1), false, false),

		/**
		 * RedBerry Pie
		 */
		RAW_REDBERRY_PIE(new Item(2321, 1), 10, 200, 78, new Item(2329, 1), new Item(2325, 1), false, false),

		/**
		 * Meat pie
		 */
		RAW_MEAT_PIE(new Item(2319, 1), 20, 200, 110, new Item(2329, 1), new Item(2327, 1), false, false),

		/**
		 * Mud pie
		 */
		RAW_MUD_PIE(new Item(7168, 1), 29, 200, 128, new Item(2329, 1), new Item(7170, 1), false, false),

		/**
		 * Apple pie
		 */
		RAW_APPLE_PIE(new Item(2317, 1), 30, 200, 130, new Item(2329, 1), new Item(2323, 1), false, false),

		/**
		 * Garden Pie
		 */
		RAW_GARDEN_PIE(new Item(7176, 1), 34, 200, 138, new Item(2329, 1), new Item(7178, 1), false, false),

		/**
		 * Fish Pie
		 */
		RAW_FISH_PIE(new Item(7186, 1), 47, 200, 164, new Item(2329, 1), new Item(7188, 1), false, false),

		/**
		 * Admiral Pie
		 */
		RAW_ADMIRAL_PIE(new Item(7196, 1), 70, 200, 210, new Item(2329, 1), new Item(7198, 1), false, false),

		/**
		 * Wild Pie
		 */
		RAW_WILD_PIE(new Item(7206, 1), 85, 200, 240, new Item(2329, 1), new Item(7208, 1), false, false),

		/**
		 * Summer pie
		 */
		RAW_SUMMER_PIE(new Item(7216, 1), 95, 200, 260, new Item(2329, 1), new Item(7218, 1), false, false),

		/**
		 * Fish Cake
		 */
		RAW_FISHCAKE(new Item(7529, 1), 31, 200, 100, new Item(7531, 1), new Item(7530, 1), false, false),

		/**
		 * Potato
		 */
		RAW_POTATO(new Item(1942, 1), 7, 200, 15, new Item(6699, 1), new Item(6701, 1), false, false);

		private static Map<Short, Cookables> ingredients = new HashMap<Short, Cookables>();

		public static Cookables forId(short itemId) {
			return ingredients.get(itemId);
		}

		static {
			for (Cookables ingredient : Cookables.values()) {
				ingredients.put((short) ingredient.getRawItem().getId(), ingredient);
			}
		}

		private Item raw;
		private int lvl;
		private int burningLvl;
		private int xp;
		private Item burnt;
		private Item total;
		private boolean spitRoast;
		private boolean fireOnly;

		private Cookables(Item raw, int lvl, int burningLvl, int exp, Item burnt, Item total, boolean spitRoast,
				boolean fireOnly) {
			this.raw = raw;
			this.lvl = lvl;
			this.burningLvl = burningLvl;
			this.xp = exp;
			this.burnt = burnt;
			this.total = total;
			this.spitRoast = spitRoast;
			this.fireOnly = fireOnly;
		}

		public Item getRawItem() {
			return raw;
		}

		public int getLvl() {
			return lvl;
		}

		public Item getBurntId() {
			return burnt;
		}

		public Item getProduct() {
			return total;
		}

		public int getXp() {
			return xp;
		}

		public int getBurningLvl() {
			return burningLvl;
		}

		public Item getTotal() {
			return total;
		}

		public boolean isSpitRoast() {
			return spitRoast;
		}

		public boolean isFireOnly() {
			return fireOnly;
		}
	}

	private int amount;
	private Cookables cook;
	private Item item;
	private WorldObject object;
	private Animation FIRE_COOKING = new Animation(897), RANGE_COOKING = new Animation(897);

	public Cooking(WorldObject object, Item item, int amount) {
		this.amount = amount;
		this.item = item;
		this.object = object;
	}

	@Override
	public boolean start(Player player) {
		if ((this.cook = Cookables.forId((short) item.getId())) == null) {
			return false;
		}
		if (cook.isFireOnly() && !object.getDefinitions().name.equals("Fire")) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on a fire.");
		} else if (cook.isSpitRoast() && object.getId() != 11363) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You may only cook this on an iron spit.");
			return false;
		} else if (player.getSkills().getLevel(Skills.COOKING) < cook.getLvl()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a cooking level of " + cook.getLvl() + " to cook this food.");
			return false;
		}
		player.getPackets().sendGameMessage(
				"You attempt to cook the " + cook.getProduct().getDefinitions().getName().toLowerCase() + ".");
		player.faceObject(object);
		return true;
	}

	private boolean isBurned(Cookables cook, Player player) {
		int level = player.getSkills().getLevel(Skills.COOKING);
		if (player.getEquipment().getGlovesId() == 775) {
			if (level >= (cook.getBurningLvl() - (cook.getProduct().getId() == 391 ? 0 : 6)))
				return false;
		}
		int levelsToStopBurn = cook.getBurningLvl() - level;
		if (levelsToStopBurn > 20) {
			levelsToStopBurn = 20;
		}
		return Utils.random(34) <= levelsToStopBurn;
	}

	public static Cookables isCookingSkill(Item item) {
		return Cookables.forId((short) item.getId());
	}

	@Override
	public boolean process(Player player) {
		if (!World.getRegion(object.getRegionId()).containsObject(object.getId(), object))
			return false;
		if (!player.getInventory().containsItem(item.getId(), 1)) {
			return false;
		}
		if (!player.getInventory().containsItem(cook.getRawItem().getId(), 1)) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.COOKING) < cook.getLvl()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a level of " + cook.getLvl() + " to cook this.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		amount--;
		player.setNextAnimation(object.getDefinitions().name.contains("fire") ? FIRE_COOKING : RANGE_COOKING);
		if ((player.getSkills().getLevel(Skills.COOKING) >= cook.getBurningLvl()) ? false : isBurned(cook, player)) {
			player.getInventory().deleteItem(item.getId(), 1);
			player.getInventory().addItem(cook.getBurntId().getId(), cook.getBurntId().getAmount());
			player.getPackets().sendGameMessage("Oops! You accidently burnt the "
					+ cook.getProduct().getDefinitions().getName().toLowerCase() + ".", true);
		} else {
			player.getInventory().deleteItem(item.getId(), 1);
			player.getInventory().addItem(cook.getProduct().getId(), cook.getProduct().getAmount());
			player.getSkills().addXp(Skills.COOKING, cook.getXp());
			player.getPackets().sendGameMessage(
					"You successfully cook the " + cook.getProduct().getDefinitions().getName().toLowerCase() + ".",
					true);
		}
		if (amount > 0) {
			player.getPackets().sendGameMessage(
					"You attempt to cook the " + cook.getProduct().getDefinitions().getName().toLowerCase() + ".");
			return 3;
		}
		return -1;
	}

	@Override
	public void stop(final Player player) {
		this.setActionDelay(player, 3);
	}
}
