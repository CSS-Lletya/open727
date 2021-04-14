package skills.runecrafting;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

import skills.Skills;

/**
 * 
 * @author Tyler
 *
 */
public class SihponActionNodes extends Action {
	Nodes nodes;
	WorldObject node;
	private boolean started;

	/**
	 * 
	 * @param nodes
	 * @param node
	 */
	public SihponActionNodes(Nodes nodes, WorldObject node) {
		this.nodes = nodes;
		this.node = node;
	}

	public enum Nodes {
		CYCLONE(70455, 16596, 19, 1, 24215),

		MIND_STORM(70456, 16596, 20, 1, 24217),

		WATER_POOL(70457, 16596, 25.3, 5, 24214),

		ROCK_FRAGMENT(70458, 16596, 28.6, 9, 24216),

		FIRE_BALL(70459, 16596, 34.8, 14, 24213),

		VINE(70460, 16596, 34.8, 17, 24214, 24216),

		FLESHLY_GROWTH(70461, 16596, Utils.random(30.3, 34.3), 20, 24218),

		FIRE_STORM(70462, 16596, Utils.random(22.8, 41.7), 27, 24213, 24215),

		CHAOTIC_CLOUD(70463, 16596, 61.6, 35, 24221),

		NEBULA(70464, 16596, Utils.random(63.8, 85.6), 40, 24223, 24224),

		SHIFTER(70465, 16596, 86.8, 44, 24220),

		JUMPER(70466, 16596, 107.8, 54, 24222),

		SKULLS(70467, 16596, 120, 65, 24219),

		BLOOD_POOL(70468, 16596, 146.3, 77, 24225),

		BLOODY_SKULLS(70469, 16596, Utils.random(144, 175.5), 83, 24219, 24225),

		LIVING_SOUL(70470, 16596, 213, 90, 24226),

		UNDEAD_SOUL(70471, 16596, Utils.random(144, 255.5), 95, 24219, 24226);

		private int objectId;
		private int emoteId;
		private double xp;
		private int levelRequired;
		private int[] runeId;

		Nodes(int objectId, int emoteId, double xp, int levelRequired, int... runeId) {
			this.objectId = objectId;
			this.emoteId = emoteId;
			this.xp = xp;
			this.levelRequired = levelRequired;
			this.runeId = runeId;
		}

		/**
		 * @return the objectId
		 */
		public int getObjectId() {
			return objectId;
		}

		/**
		 * @param objectId the objectId to set
		 */
		public void setObjectId(int objectId) {
			this.objectId = objectId;
		}

		/**
		 * @return the emoteId
		 */
		public int getEmoteId() {
			return emoteId;
		}

		/**
		 * @param emoteId the emoteId to set
		 */
		public void setEmoteId(int emoteId) {
			this.emoteId = emoteId;
		}

		/**
		 * @return the runeId
		 */
		public int[] getRuneId() {
			return runeId;
		}

		/**
		 * @return the xp
		 */
		public double getXp() {
			return xp;
		}

		/**
		 * @param xp the xp to set
		 */
		public void setXp(int xp) {
			this.xp = xp;
		}

		/**
		 * @return the levelRequired
		 */
		public int getLevelRequired() {
			return levelRequired;
		}

		/**
		 * @param levelRequired the levelRequired to set
		 */
		public void setLevelRequired(int levelRequired) {
			this.levelRequired = levelRequired;
		}
	}

	public static boolean siphon(Player player, WorldObject object) {
		Nodes node = getNode(object.getId());
		if (node == null)
			return false;
		player.getActionManager().setAction(new SihponActionNodes(node, object));
		return true;
	}

	/**
	 * Gets a random transformation id.
	 * @return random node.
	 */
	@SuppressWarnings("unused")
	private static int getRandomTransformationId() {
		return getNode(Utils.getRandom(Nodes.values().length)).getObjectId();
	}

	/**
	 * get the node.
	 * @param id
	 * @return
	 */
	private static Nodes getNode(int id) {
		for (Nodes node : Nodes.values())
			if (node.objectId == id)
				return node;
		return null;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks the players requirements.
	 * 
	 * @param player
	 * @return requirements.
	 */
	public boolean checkAll(final Player player) {
		if (player.getSkills().getLevel(Skills.RUNECRAFTING) < nodes.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a runecrafting level of " + nodes.getLevelRequired() + " to siphon from that node.");
			return false;
		}
		if (!started && !player.withinDistance(node, 6))
			return true;
		if ((World.getRegion(player.getRegionId()).getObject(nodes.getObjectId(), node)) == null) {
			stop(player);
			return false;
		}
		if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsOneItem(nodes.getRuneId())) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		if (!player.getInventory().containsItem(24227, 1)) {
			player.getPackets().sendGameMessage("You don't have any rune essence to siphon from that node.", true);
			return false;
		}
		if (!started) {
			player.resetWalkSteps();
			player.setNextAnimation(new Animation(16596));
			started = true;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@SuppressWarnings("unused")
	private void processNodeDestroy(final Player player) {
		player.getPackets().sendGameMessage("The node you were siphoning from has been depleted of energy.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage("You pick up the essence left by the creature.", true);
				player.setNextAnimation(new Animation(16599));
				player.getInventory().addItem(24227, 50);
				World.destroySpawnedObject(node, true);
				stop();
			}
		}, 2);
	}

	@Override
	public int processWithDelay(final Player player) {
		if (started) {
			int level = player.getSkills().getLevel(Skills.RUNECRAFTING);
			if (level <= 50 && Utils.getRandom(2) == 1) {
				player.getInventory().addItem(nodes.getRuneId()[Utils.getRandom(nodes.getRuneId().length)], 1);
				double totalXp = nodes.getXp();
				if (Runecrafting.hasRcingSuit(player))
					totalXp *= 1.025;
				player.getSkills().addXp(Skills.RUNECRAFTING, totalXp);
				player.setNextGraphics(new Graphics(3071));
			} else if (level >= 50 && Utils.getRandom(1) == 1) {
				player.getInventory().addItem(nodes.getRuneId()[Utils.getRandom(nodes.getRuneId().length)], 1);
				player.getInventory().deleteItem(24227, 1);
				double totalXp = nodes.getXp();
				if (Runecrafting.hasRcingSuit(player))
					totalXp *= 1.025;
				player.getSkills().addXp(Skills.RUNECRAFTING, totalXp);
				player.setNextGraphics(new Graphics(3071));
			}
			player.setNextAnimation(new Animation(nodes.getEmoteId()));
			player.setNextFaceWorldTile(node);
			World.sendProjectile(node, node, player, 3060, 31, 35, 35, 0, 2, 0);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextGraphics(new Graphics(3062));
				}
			}, 1);
		}
		return 1;
	}

	@Override
	public void stop(Player player) {
		player.setNextAnimation(new Animation(16599));
		setActionDelay(player, 3);

	}

}
