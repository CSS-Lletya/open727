package com.rs.game.player.actions;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

/**
 * 
 * @author Cjay0091
 * 
 */
public class Smithing extends Action {

	public enum ForgingBar {

		ADAMANT(2361, 70,
				new Item[] { new Item(1211, 1), new Item(1357, 1), new Item(1430, 1), new Item(1145, 1),
						new Item(9380, 1), new Item(1287, 1), new Item(823, 1), new Item(4823, 1), new Item(-1, 1),
						new Item(-1, 1), new Item(-1, 1), new Item(43, 1), new Item(1331, 1), new Item(9429, 1),
						new Item(1301, 1), new Item(867, 1), new Item(1161, 1), new Item(1183, 1), new Item(-1, 1),
						new Item(-1, 1), new Item(1345, 1), new Item(1371, 1), new Item(1111, 1), new Item(1199, 1),
						new Item(3100, 1), new Item(1317, 1), new Item(1091, 1), new Item(1073, 1), new Item(1123, 1),
						new Item(1271, 1) },
				new double[] { 62.5, 125, 187.5, 312.5 }, new int[] { 66, 210, 267 }),

		BRONZE(2349, 0,
				new Item[] { new Item(1205, 1), new Item(1351, 1), new Item(1422, 1), new Item(1139, 1),
						new Item(877, 1), new Item(1277, 1), new Item(819, 1), new Item(4819, 1), new Item(1794, 1),
						new Item(-1, 1), new Item(-1, 1), new Item(39, 1), new Item(1321, 1), new Item(9420, 1),
						new Item(1291, 1), new Item(864, 1), new Item(1155, 1), new Item(1173, 1), new Item(-1, 1),
						new Item(-1, 1), new Item(1337, 1), new Item(1375, 1), new Item(1103, 1), new Item(1189, 1),
						new Item(3095, 1), new Item(1307, 1), new Item(1087, 1), new Item(1075, 1), new Item(1117, 1),
						new Item(1265, 1) },
				new double[] { 12.5, 25, 37.5, 62.5 }, new int[] { 66, 82, 210, 267 }),

		IRON(2351, 15,
				new Item[] { new Item(1203, 1), new Item(1349, 1), new Item(1420, 1), new Item(1137, 1),
						new Item(9377, 1), new Item(1279, 1), new Item(820, 1), new Item(4820, 1), new Item(-1, 1),
						new Item(7225, 1), new Item(-1, 1), new Item(40, 1), new Item(1323, 1), new Item(9423, 1),
						new Item(1293, 1), new Item(863, 1), new Item(1153, 1), new Item(1175, 1), new Item(4540, 1),
						new Item(-1, 1), new Item(1335, 1), new Item(1363, 1), new Item(1101, 1), new Item(1191, 1),
						new Item(3096, 1), new Item(1309, 1), new Item(1081, 1), new Item(1067, 1), new Item(1115, 1),
						new Item(1267, 1) },
				new double[] { 25, 50, 75, 125 }, new int[] { 66, 90, 162, 210, 267 }),

		MITHRIL(2359, 50,
				new Item[] { new Item(1209, 1), new Item(1355, 1), new Item(1428, 1), new Item(1143, 1),
						new Item(9379, 1), new Item(1285, 1), new Item(822, 1), new Item(4822, 1), new Item(-1, 1),
						new Item(-1, 1), new Item(-1, 1), new Item(42, 1), new Item(1329, 1), new Item(9427, 1),
						new Item(1299, 1), new Item(866, 1), new Item(1159, 1), new Item(1181, 1), new Item(-1, 1),
						new Item(9416, 1), new Item(1343, 1), new Item(1369, 1), new Item(1109, 1), new Item(1197, 1),
						new Item(3099, 1), new Item(1315, 1), new Item(1085, 1), new Item(1071, 1), new Item(1121, 1),
						new Item(1273, 1) },
				new double[] { 50, 100, 150, 250 }, new int[] { 66, 170, 210, 267 }),

		RUNE(2363, 85,
				new Item[] { new Item(1213, 1), new Item(1359, 1), new Item(1432, 1), new Item(1147, 1),
						new Item(9381, 1), new Item(1289, 1), new Item(824, 1), new Item(4824, 1), new Item(-1, 1),
						new Item(-1, 1), new Item(-1, 1), new Item(44, 1), new Item(1333, 1), new Item(9431, 1),
						new Item(1303, 1), new Item(868, 1), new Item(1163, 1), new Item(1185, 1), new Item(-1, 1),
						new Item(-1, 1), new Item(1347, 1), new Item(1373, 1), new Item(1113, 1), new Item(1201, 1),
						new Item(3101, 1), new Item(1319, 1), new Item(1093, 1), new Item(1079, 1), new Item(1127, 1),
						new Item(1275, 1) },
				new double[] { 75, 150, 225, 375 }, new int[] { 66, 210, 267 }),

		STEEL(2353, 30,
				new Item[] { new Item(1207, 1), new Item(1353, 1), new Item(1424, 1), new Item(1141, 1),
						new Item(9378, 1), new Item(1281, 1), new Item(821, 1), new Item(1539, 1), new Item(-1, 1),
						new Item(-1, 1), new Item(2370, 1), new Item(41, 1), new Item(1325, 1), new Item(9425, 1),
						new Item(1295, 1), new Item(865, 1), new Item(1157, 1), new Item(1177, 1), new Item(4544, 1),
						new Item(-1, 1), new Item(1339, 1), new Item(1365, 1), new Item(1105, 1), new Item(1193, 1),
						new Item(3097, 1), new Item(1311, 1), new Item(1083, 1), new Item(1069, 1), new Item(1119, 1),
						new Item(1269, 1) },
				new double[] { 37.5, 75, 112.5, 187.5 }, new int[] { 66, 98, 162, 210, 267 });

		private static Map<Integer, ForgingBar> bars = new HashMap<Integer, ForgingBar>();

		static {
			for (ForgingBar bar : ForgingBar.values()) {
				bars.put(bar.getBarId(), bar);
			}
		}

		public static ForgingBar forId(int id) {
			return bars.get(id);
		}

		public static ForgingBar getBar(Player player) {
			int smithLevel = player.getSkills().getLevel(Skills.SMITHING);
			for (ForgingBar bar : bars.values()) {
				if (smithLevel < bar.getLevel() || !player.getInventory().containsItem(bar.getBarId(), 1))
					continue;
				return bar;
			}
			return null;
		}

		private int barId;
		private int[] componentChilds;
		private double[] experience;
		private Item items[];
		private int level;

		private ForgingBar(int barId, int level, Item[] items, double[] experience, int[] componentChilds) {
			this.barId = barId;
			this.level = level;
			this.items = items;
			this.componentChilds = componentChilds;
			this.experience = experience;
		}

		public int getBarId() {
			return barId;
		}

		public int[] getComponentChilds() {
			return componentChilds;
		}

		public double[] getExperience() {
			return experience;
		}

		public Item[] getItems() {
			return items;
		}

		public int getLevel() {
			return level;
		}
	}

	private static int HAMMER = 2347;
	private static int SMITHING_INTERFACE = 300;
	private ForgingBar bar;
	private int index;
	private int ticks;

	public Smithing(int ticks, int index) {
		this.index = index;
		this.ticks = ticks;
	}

	@Override
	public boolean process(Player player) {
		if (!player.getInventory().containsItem(HAMMER, 1)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a hammer in order to work with a bar of "
							+ new Item(bar.getBarId(), 1).getDefinitions().getName().toLowerCase().replace(" bar", "")
							+ ".");
			return false;
		}
		if (!player.getInventory().containsItem(bar.getBarId(),
				ForgingInterface.getActualAmount(
						bar.getLevel() + ForgingInterface.getFixedAmount(bar, bar.getItems()[index]), bar,
						bar.getItems()[index].getId()))) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You do not have sufficient bars!");
			return false;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < ForgingInterface.getLevels(bar, index, player)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of "
					+ ForgingInterface.getLevels(bar, index, player) + " to create this.");
			return false;
		}
		if (player.getInterfaceManager().containsScreenInter()) {
			player.getInterfaceManager().closeScreenInterface();
			return true;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		player.setNextAnimation(new Animation(898));
		player.getInventory().deleteItem(bar.getBarId(),
				ForgingInterface.getActualAmount(
						bar.getLevel() + ForgingInterface.getFixedAmount(bar, bar.getItems()[index]), bar,
						bar.getItems()[index].getId()));
		player.getInventory().addItem(bar.getItems()[index].getId(),
				ForgingInterface.getForgedAmount(bar.getItems()[index].getId()));
		player.getSkills().addXp(Skills.SMITHING, getExperience(player));
		if (ticks > 0) {
			return 3;
		}
		return -1;
	}

	private double getExperience(Player player) {
		int levelRequired = bar.getLevel() + ForgingInterface.getFixedAmount(bar, bar.getItems()[index]);
		int barAmount = ForgingInterface.getActualAmount(levelRequired, bar, bar.getItems()[index].getId());
		return bar.getExperience()[barAmount == 5 ? 3 : barAmount - 1];
	}

	@Override
	public boolean start(Player player) {
		if ((bar = (ForgingBar) player.getTemporaryAttributtes().get("SmithingBar")) == null) {
			return false;
		}
		if (!player.getInventory().containsOneItem(HAMMER, bar.getBarId())) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a hammer in order to work with a bar of "
							+ new Item(bar.getBarId(), 1).getDefinitions().getName().replace("Bar ", "") + ".");
			return false;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < ForgingInterface.getLevels(bar, index, player)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of "
					+ ForgingInterface.getLevels(bar, index, player) + " to create this.");
			return false;
		}
		return true;
	}

	@Override
	public void stop(Player player) {
		this.setActionDelay(player, 3);
	}

	public static class ForgingInterface {

		public static final int componentChilds[] = new int[30];
		public static final int CLICKED_CHILDS[] = { 28, -1, 5, 1 };

		public static void handleIComponents(Player player, int componentId) {
			int slot = -1;
			int ticks = -1;
			for (int i = 3; i <= 6; i++) {
				for (int index = 0; index < componentChilds.length; index++) {
					if (componentChilds[index] + i != componentId)
						continue;
					slot = index;
					ticks = CLICKED_CHILDS[i - 3];
					break;
				}
			}
			if (slot == -1)
				return;
			player.getActionManager().setAction(new Smithing(ticks, slot));
		}

		private static void calculateComponentConfigurations() {
			int base = 18;
			for (int i = 0; i < componentChilds.length; i++) {
				if (base == 250) {
					base = 267;
				}
				componentChilds[i] = base;
				base += 8;
			}
		}

		private static int getBasedAmount(Item item) {
			String def = item.getDefinitions().getName();
			if (def.contains("dagger")) {
				return 1;
			} else if (def.contains("hatchet") || def.contains("mace") || def.contains("iron spit")) {
				return 2;
			} else if (def.contains("bolts") || def.contains("med helm")) {
				return 3;
			} else if (def.contains("sword") || def.contains("dart tip") || def.contains("nails")
					|| def.contains("wire")) {
				return 4;
			} else if (def.contains("arrow") || def.contains("pickaxe") || def.contains("scimitar")) {
				return 5;
			} else if (def.contains("longsword") || def.contains("limbs")) {
				return 6;
			} else if (def.contains("knife") || def.contains("full helm") || def.contains("studs")) {
				return 7;
			} else if (def.contains("sq shield") || def.contains("warhammer") || def.contains("grapple tip")) {
				return 9;
			} else if (def.contains("battleaxe")) {
				return 10;
			} else if (def.contains("chainbody") || def.contains("oil lantern")) {
				return 11;
			} else if (def.contains("kiteshield")) {
				return 12;
			} else if (def.contains("claws")) {
				return 13;
			} else if (def.contains("2h sword")) {
				return 14;
			} else if (def.contains("plateskirt") || def.contains("platelegs")) {
				return 16;
			} else if (def.contains("platebody")) {
				return 18;
			} else if (def.contains("bullseye lantern")) {
				return 19;
			}
			return 1;
		}

		private static int getFixedAmount(ForgingBar bar, Item item) {
			String name = item.getDefinitions().getName();
			int increment = getBasedAmount(item);
			if (name.contains("dagger") && bar != ForgingBar.BRONZE) {
				increment--;
			} else if (name.contains("hatchet") && bar == ForgingBar.BRONZE) {
				increment--;
			}
			return increment;
		}

		public static int getForgedAmount(int id) {
			String name = ItemDefinitions.getItemDefinitions(id).getName();
			if (name.contains("knife")) {
				return 5;
			} else if (name.contains("bolts") || name.contains("dart tip")) {
				return 10;
			} else if (name.contains("arrowheads") || name.contains("nails")) {
				return 15;
			}
			return 1;
		}

		public static String[] getStrings(Player player, ForgingBar bar, int index, int itemId) {
			if (itemId == -1 || index < 0 || index >= bar.getItems().length) {
				return null;
			}
			StringBuilder barName = new StringBuilder();
			StringBuilder levelString = new StringBuilder();
			String name = ItemDefinitions.getItemDefinitions(itemId).getName().toLowerCase();
			String barVariableName = bar.toString().toLowerCase();
			int levelRequired = bar.getLevel() + getFixedAmount(bar, bar.getItems()[index]);
			int barAmount = getActualAmount(levelRequired, bar, itemId);
			if (player.getInventory().getItems().getNumberOf(bar.barId) >= barAmount) {
				barName.append("<col=00FF00>");
			}
			barName.append(barAmount).append(" ").append(barAmount > 1 ? "bars" : "bar");
			if (levelRequired >= 99) {
				levelRequired = 99;
			}
			if (player.getSkills().getLevel(Skills.SMITHING) >= levelRequired) {
				levelString.append("<col=FFFFFF>");
			}
			levelString.append(Utils.formatPlayerNameForDisplay(name.replace(barVariableName + " ", "")));
			return new String[] { levelString.toString(), barName.toString() };
		}

		public static int getLevels(ForgingBar bar, int slot, Player player) {
			int base = bar.getLevel();
			int barAmount = getFixedAmount(bar, bar.getItems()[slot]);
			int level = base + barAmount;
			if (level > 99)
				level = 99;
			return level;
		}

		private static void sendComponentConfigs(Player player, ForgingBar bar) {
			for (int i : bar.getComponentChilds()) {
				player.getPackets().sendHideIComponent(SMITHING_INTERFACE, i - 1, false);
			}
		}

		public static int getActualAmount(int levelRequired, ForgingBar bar, int id) {
			if (levelRequired >= 99) {
				levelRequired = 99;
			}
			int level = levelRequired - bar.getLevel();
			String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
			if (level >= 0 && level <= 4) {
				return 1;
			} else if (level >= 4 && level <= 8) {
				if (name.contains("knife") || name.contains("limb") || name.contains("studs")) {
					return 1;
				}
				return 2;
			} else if (level >= 9 && level <= 16) {
				if (name.contains("grapple")) {
					return 1;
				} else if (name.contains("claws")) {
					return 2;
				}
				return 3;
			} else if (level >= 17) {
				if (name.contains("bullseye")) {
					return 1;
				}
				return 5;
			}
			return 1;
		}

		public static void sendSmithingInterface(Player player, ForgingBar bar) {
			calculateComponentConfigurations();
			player.getTemporaryAttributtes().put("SmithingBar", bar);
			sendComponentConfigs(player, bar);
			for (int i = 0; i < bar.getItems().length; i++) {
				player.getPackets().sendItemOnIComponent(SMITHING_INTERFACE, componentChilds[i],
						bar.getItems()[i].getId(), 1);
				String[] name = getStrings(player, bar, i, bar.getItems()[i].getId());
				if (name != null) {
					player.getPackets().sendIComponentText(300, componentChilds[i] + 1, name[0]);
					player.getPackets().sendIComponentText(300, componentChilds[i] + 2, name[1]);
				}
			}
			player.getPackets().sendIComponentText(300, 14,
					Utils.formatPlayerNameForDisplay(bar.toString().toLowerCase()) + "");
			player.getInterfaceManager().sendInterface(SMITHING_INTERFACE);
		}
	}
}
