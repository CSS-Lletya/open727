package skills.slayer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import skills.Skills;

public class Slayer {

	public enum Master {

		SPRIA(8462, "Travel north of Taverly to get here; Burthorpe.", 3, 1, new int[] { 15, 50 }, "Birds", "Chicken",
				"Rats", "Bears", "Bats", "Cow", "Cow calf", "Crawling hands", "Cave slime", "Banshees", "Dwarves",
				"Ghosts", "Goblins", "Spiders", "Monkeys", "Scorpions"),

		MAZCHNA(8464, "Canifis is quite a walk, east of Varrock; Canifis.", 20, 1, new int[] { 40, 70 }, "Bats",
				"Bears", "Crawling hands", "Cave bugs", "Cave slime", "Banshees", "Ghosts", "Moss giant", "Spiders",
				"Cave crawlers", "Cockatrice", "Zombies", "Hill giants", "Hobgoblins", "Icefiends", "Pyrefiends",
				"Skeletons", "Wolves", "Kalphites", "Dogs"),

		VANNAKA(1597, "Use the trapdoor in Edgeville to get here; Edgeville Dungeon.", 40, 1, new int[] { 60, 120 },
				"Bats", "Crawling hands", "Cave bugs", "Cave slime", "Banshees", "Rock slug", "Spiders",
				"Cave crawlers", "Cockatrice", "Zombies", "Hill giants", "Hobgoblins", "Dagannoth", "Icefiends",
				"Pyrefiends", "Gargoyles", "Skeletons", "Wolves", "Kalphites", "Aberrant spectres", "Basilisks",
				"Wild dog", "Bloodvelds", "Dust devils", "Green dragons", "Ice giants", "Ice warriors", "Jellies",
				"Infernal mages", "Lesser demons", "Moss giant", "Fire giant", "Turoth"),

		CHAELDAR(1598, "You must have completed Lost City quest to get here; Zanaris.", 70, 1, new int[] { 60, 170 },
				"Banshees", "Spiders", "Cave crawlers", "Cockatrice", "Rock slug", "Hill giants", "Hobgoblins",
				"Pyrefiends", "Kalphites", "Aberrant spectres", "Basilisks", "Bloodvelds", "Dust devils", "Nechryael",
				"Wild dog", "Blue dragons", "Ice giants", "Gargoyles", "Jellies", "Infernal mages", "Lesser demons",
				"Fire giant", "Turoth", "Kurasks", "Dagannoth", "Cave horrors", "Bronze dragons", "Waterfiends"),

		SUMONA(7780, "Southeast of the north carpet stop in Pollnivneach; Pollnivneach.", 85, 35,
				new int[] { 120, 192 }, "Aberrant spectres", "Abbyssal demons", "Aquanties", "Banshees", "Basiliks",
				"Black demons", "Bloodvelds", "Blue dragons", "Cave crawlers", "Cave horror", "Dragannoths",
				"Desert strykewyrms", "Dust devils", "Elves", "Fire giant", "Gargoyles", "Greater demons", "Hellhounds",
				"Iron Dragons", "Jungle strykewyrms", "Kalphites", "Kurasks", "Mutated jadinko", "Nechryaels",
				"Red Dragons", "Scabarite minions", "Spiritual mages", "Spiritual warriors", "Terror dogs", "Trolls",
				"Turoth", "Warped tortoises"),

		LAPALOK(8467, "I hear there is a Gnome who can direct you here; Shilo Village", 100, 50, new int[] { 130, 200 },
				"Aberrant spectres", "Gargoyles", "Abyssal demons", "Black demons", "Black Dragons", "Bloodvelds",
				"Dogs", 1594, "Dark beasts", "Goraks", "Dagannoth", "Kalphites", "Iron dragons", "Steel dragons",
				"Mithril dragons", "Nechryael", "Spiritual mages", "Suqahs", "Greater demons", "Fire giant",
				"Hellhounds"),

		KURADAL(9084, "", 110, 75, new int[] { 190, 270 }, "Aberrant spectres", "Abbyssal demons", "Aquanties",
				"Black demons", "Black Dragons", "Bloodvelds", "Blue Dragons", "Dagannoth", "Dark beasts",
				"Desert strykewyrms", "Dust devils", "Fire giant", "Gargoyles", "Greater demons", "Hellhound",
				"Ice strykewyrms", "Iron dragons", "Jungle strykewyrms", "Kalphites", "Living rock creatures",
				"Mithril dragons", "Mutated jadinko", "Nechryael", "Skeletal wyverns", "Spiritual mages",
				"Steel dragons", "Suqahs", "Terror dogs", "Tez-Tek", "Warped tortoises", "Vyrewatch", "Waterfiends");

		private int combatLevel;
		private int master;
		private int levelRequired;
		private int[] range;
		private Object[] tasks;
		private String dialouge;

		private Master(int master, String dialouge, int combatLevel, int levelRequired, int[] range, Object... tasks) {
			this.combatLevel = combatLevel;
			this.levelRequired = levelRequired;
			this.range = range;
			this.tasks = tasks;
			this.dialouge = dialouge;
			this.master = master;
		}

		public String getDialouge() {
			return dialouge;
		}

		public int getMaster() {
			return master;
		}

		public int getCombatLevel() {
			return combatLevel;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int[] getRange() {
			return range;
		}

		public Object[] getTasks() {
			return tasks;
		}

		public static Master forId(int npcId) {
			for (Master master : Master.values()) {
				if (master.getMaster() == npcId)
					return master;
			}
			return null;
		}
	}

	public static class SlayerTask implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3925639922342382315L;
		private Master master;
		private int index;
		private int amount;

		public SlayerTask(Master master, int index, int amount) {
			this.master = master;
			this.index = index;
			this.amount = amount;
		}

		public void setMaster(Master master) {
			this.master = master;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public Master getMaster() {
			return master;
		}

		public int getIndex() {
			return index;
		}

		public int getAmount() {
			return amount;
		}

		public String getName() {
			return master.getTasks()[index].toString();
		}
	}

	public static enum SlayerMonsters {

		CRAWLING_HAND(1648, 5),

		CAVE_BUG(1832, 7),

		CAVE_CRAWLER(7787, 10),

		BANSHEE(1612, 15),

		CAVE_SLIM(1831, 17),

		ROCK_SLUG(1631, 20),

		DESERT_LIZARD(2804, 22),

		COCKATRICE(1620, 25),

		PYREFIED(1633, 30),

		MORGE(114, 32),

		HARPIE_BUG_SWARM(3163, 33),

		WALL_BEAST(7823, 35),

		KILLERWATT(3201, 37),

		MOLANISK(5751, 39),

		BASILISK(1616, 40),

		TERROR_DOG(5417, 40),

		FEVER_SPIDER(2850, 42),

		INFERNAL_MAGE(1643, 45),

		BRINE_RAT(3707, 47),

		BLOODVELD(1618, 50),

		PHOENIX(8549, 51),

		JELLY(1637, 52),

		TUROTH(1622, 55),

		WARPED_TERRORBIRD(6285, 56),

		WARPED_TORTOISE(6296, 56),

		ZYGOMITE(3346, 57),

		CAVE_HORROR(4353, 58),

		WILD_JADE_FINE(3409, 59),

		ABERRANT_SPECTRE(1604, 60),

		SPIRITUAL_RANGE(6220, 63),

		DUST_DEVIL(1624, 65),

		SPIRITUAL_WARRIOR(6219, 68),

		KURASK(1608, 70),

		SKELETAL_WYVERN(3068, 72),

		GARGOYLE(1610, 75),

		AQUANITE(9172, 78),

		NECHRYAEL(1613, 80),

		SPIRITUAL_MAGE(6221, 83),

		ABYSSAL_DEMON(1615, 85),

		DARK_BEAST(2783, 90);

		private static Map<Integer, SlayerMonsters> monsters = new HashMap<Integer, SlayerMonsters>();

		public static SlayerMonsters forId(int id) {
			return monsters.get(id);
		}

		static {
			for (SlayerMonsters monster : SlayerMonsters.values())
				monsters.put(monster.id, monster);
		}

		private int id;
		private int req;

		private SlayerMonsters(int id, int req) {
			this.id = id;
			this.req = req;
		}

		public int getId() {
			return id;
		}

		public int getRequirement() {
			return req;
		}
	}

	public static boolean checkRequirement(Player player, SlayerMonsters slayer) {
		if (player.getSkills().getLevel(Skills.SLAYER) < slayer.getRequirement()) {
			player.getPackets().sendGameMessage("This monster requires " + slayer.getRequirement() + " to slay.");
			return false;
		}
		return true;
	}

	public static void submitRandomTask(Player player) {
		SlayerTask task = null;
		Master master = player.getSlayerMaster();
		if (master == null) {
			player.setSlayerMaster(Master.SPRIA);
			master = player.getSlayerMaster();
		}
		int index = Utils.getRandom(master.getTasks().length);
		int amount = Utils.random(master.getRange()[0], master.getRange()[1]);
		task = new SlayerTask(master, index, amount);
		if (player.getSlayerTask() == null)
			player.setSlayerTask(task);
	}

	public static void killedTask(Player player, NPC npc) {
		SlayerTask task = player.getSlayerTask();
		task.setAmount(task.getAmount() - 1);
		player.getSkills().addXp(Skills.SLAYER, npc.getCombatDefinitions().getHitpoints() / 10);
		if (task.getAmount() == 0) {
			task = null;
			player.setSlayerTask(task);
		}
	}
}
