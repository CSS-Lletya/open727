package skills.runecrafting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.player.controlers.Controler;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

import skills.Skills;

/**
 * 
 * @author Tyler
 * 
 */
public class SiphonActionCreatures extends Action {

	private static enum Creature {

		AIR_ESSLING(15403, 9.5, 16596, 24215, 16634, 10, 1, 16571, 0.1, 24215),

		MIND_ESSLING(15404, 10, 16596, 24217, 16634, 10, 1, 16571, 0.2, 24215),

		WATER_ESSLING(15405, 12.6, 16596, 24214, 16634, 10, 5, 16571, 0.3, 24215),

		EARTH_ESSLING(15406, 14.3, 16596, 24216, 16634, 10, 9, 16571, 0.4, 24214),

		FIRE_ESSLING(15407, 17.4, 16596, 24213, 16634, 10, 14, 16571, 0.5, 24214),

		BODY_ESSHOUND(15408, 23.1, 16596, 24218, 16650, 10, 20, 16661, 0.7, 24217),

		COSMIC_ESSHOUND(15409, 26.6, 16596, 24223, 16650, 10, 27, 16661, 0.9, 24216),

		CHOAS_ESSHOUND(15410, 30.8, 16596, 24221, 16650, 10, 35, 16661, 1.1, 24213),

		ASTRAL_ESSHOUND(15411, 35.7, 16596, 24224, 16650, 10, 40, 16661, 1.3, 24223),

		NATURE_ESSHOUND(15412, 43.4, 16596, 24220, 16650, 10, 44, 16661, 1.5, 24221),

		LAW_ESSHOUND(15413, 53.9, 16596, 24222, 16650, 10, 54, 16661, 1.7, 24224),

		DEATH_ESSWRAITH(15414, 60, 16596, 24219, 16644, 10, 65, 16641, 2.5, 24220),

		BLOOD_ESSWRAITH(15415, 73.1, 16596, 24225, 16644, 10, 77, 16641, 3, 24222),

		SOUL_ESSWRAITH(15416, 106.5, 16596, 24226, 16644, 10, 90, 16641, 3.5, 24225);

		private int npcId, runeId, playerEmoteId, npcEmoteId, npcLife, levelRequired, deathEmote, chipRune;
		private double xp, pointValue;

		private Creature(int npcId, double xp, int playerEmoteId, int runeId, int npcEmoteId, int npcLife,
				int levelRequired, int deathEmote, double pointValue, int chipRune) {
			this.npcId = npcId;
			this.xp = xp;
			this.playerEmoteId = playerEmoteId;
			this.runeId = runeId;
			this.npcEmoteId = npcEmoteId;
			this.npcLife = npcLife;
			this.levelRequired = levelRequired;
			this.deathEmote = deathEmote;
			this.pointValue = pointValue;
			this.chipRune = chipRune;
		}

		/**
		 * Gets the creatures chipping runes.
		 * @return the chipRune.
		 */
		public int getChippingRunes() {
			return chipRune;
		}

		/**
		 * Gets the creatures deathEmote.
		 * @return the deathEmote
		 */
		public int getDeathEmote() {
			return deathEmote;
		}

		/**
		 * @return the npcEmoteId
		 */
		public int getNpcEmoteId() {
			return npcEmoteId;
		}

		/**
		 * Gets the rune id from the essling enum.
		 * 
		 * @return runeId
		 */
		public int getRuneId() {
			return runeId;
		}

		/**
		 * 
		 * @return the levelRequired
		 */
		public int getLevelRequired() {
			return levelRequired;
		}

	}

	private Creature creatures;
	private NPC creature;
	private boolean started;
	private int npcLife;
	private double points;

	/**
	 * RSRuneCrafting constructor for npcs.
	 * @param creatures
	 * @param creature
	 */
	public SiphonActionCreatures(Creature creatures, NPC creature) {
		this.creatures = creatures;
		this.creature = creature;
	}

	public static boolean siphon(Player player, NPC npc) {
		Creature creature = getCreature(npc.getId());
		if (creature == null)
			return false;
		player.getActionManager().setAction(new SiphonActionCreatures(creature, npc));
		return true;
	}

	private static Creature getCreature(int id) {
		for (Creature creature : Creature.values())
			if (creature.npcId == id)
				return creature;
		return null;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			npcLife = creatures.npcLife;
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	/**
	 * Checks the players requirements.
	 * @param player
	 * @return requirements.
	 */
	public boolean checkAll(final Player player) {
		if (player.isLocked()) {
			return false;
		}
		if (creature.hasFinished())
			return false;
		if (!started && !player.withinDistance(creature, 6))
			return true;
		if (player.getSkills().getLevel(Skills.RUNECRAFTING) < creatures.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"This creature requires level " + creatures.getLevelRequired() + " to siphon.");
			return false;
		}
		if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(creatures.getRuneId(), 1)) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		if (!player.getInventory().containsItem(24227, 1)) {
			player.getPackets().sendGameMessage("You don't have any rune essence to siphon from that creature.", true);
			return false;
		}
		if (!started) {
			creature.resetWalkSteps();
			player.resetWalkSteps();
			player.setNextAnimation(new Animation(creatures.playerEmoteId));
			started = true;
		}
		return true;
	}

	@Override
	public int processWithDelay(final Player player) {
		if (started) {
			int level = player.getSkills().getLevel(Skills.RUNECRAFTING);
			if (Utils.getRandom(level <= 50 ? 2 : 1) == 1) {
				npcLife--;
				player.getInventory().addItem(creatures.getRuneId(), 1);
				player.getInventory().deleteItem(24227, 1);
				double totalXp = creatures.xp;
				if (Runecrafting.hasRcingSuit(player))
					totalXp *= 1.025;
				player.getSkills().addXp(Skills.RUNECRAFTING, totalXp);
				player.setNextGraphics(new Graphics(3071));
				points += creatures.pointValue * 10;
				if (points >= 1) {
					int pointsInt = (int) points;
					points -= pointsInt;
					Controler controler = player.getControlerManager().getControler();
					if (controler instanceof RunespanControler)
						((RunespanControler) controler).addInventoryPoints(pointsInt);
				}
			} else {// When you don't get a rune random chance to get xp.
				player.getSkills().addXp(Skills.RUNECRAFTING, Utils.random(0, 2));
			}
			if (npcLife == 0) {
				processEsslingDeath(player);
				return -1;
			}

			player.setNextAnimation(new Animation(16596));
			creature.setNextAnimation(new Animation(creatures.getNpcEmoteId()));
			creature.setNextFaceWorldTile(player);
			creature.resetWalkSteps();
			player.setNextFaceWorldTile(creature);
			World.sendProjectile(creature, creature, player, 3060, 31, 35, 35, 0, 2, 0);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextGraphics(new Graphics(3062));
				}
			}, 1);
		}
		return 1;
	}

	/**
	 * Process the creatures death.
	 * @param player
	 */
	public void processEsslingDeath(final Player player) {
		creature.setNextAnimation(new Animation(creatures.getDeathEmote()));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage("The creature has been broken down.");
				player.getPackets().sendGameMessage("You pick up the essence left by the creature.", true);
				player.setNextAnimation(new Animation(16599));
				creature.setRespawnTask();
				player.getInventory().addItem(24227, 50);
				stop();
			}
		}, 2);
	}

	@Override
	public void stop(Player player) {
		player.setNextAnimation(new Animation(16599));
		setActionDelay(player, 3);
	}

	/**
	 * Process the chipping of creatures.
	 * @param player
	 * @param npc
	 */
	public static boolean chipCreature(Player player, NPC npc) {
		Creature creature = getCreature(npc.getId());
		if (!player.getInventory().containsItem(creature.getChippingRunes(), 10)) {
			player.getPackets()
					.sendGameMessage("You dont have enough "
							+ ItemDefinitions.getItemDefinitions(creature.getChippingRunes()).getName()
							+ "s to chip away at that creature.");
			return false;
		} else {
			player.getPackets().sendGameMessage(
					"You use some runes to fire a blast of runic energy at the creature, chipping of some rune essense from its body.");
			player.getInventory().deleteItem(creature.getChippingRunes(), 10);
			player.getInventory().addItem(24227, 10);

			World.sendProjectile(player, npc, player, 3060, 31, 35, 35, 0, 2, 0);// Sent to the creature instead of from.
		}
		return false;
	}
}
