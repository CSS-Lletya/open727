package com.rs.game.npc.qbd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * Represents the Queen Black Dragon.
 * @author Emperor
 *
 */
public final class QueenBlackDragon extends NPC {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -7709946348178377601L;

	/**
	 * The attacks for the first phase.
	 */
	private static final QueenAttack[] PHASE_1_ATTACKS = { new FireBreathAttack(), new MeleeAttack(), new RangeAttack(),
			new FireWallAttack() };

	/**
	 * The attacks for the second phase.
	 */
	private static final QueenAttack[] PHASE_2_ATTACKS = { new FireBreathAttack(), new MeleeAttack(), new RangeAttack(),
			new FireWallAttack(), new ChangeArmour(), new SoulSummonAttack() };

	/**
	 * The attacks for the third phase.
	 */
	private static final QueenAttack[] PHASE_3_ATTACKS = { new FireBreathAttack(), new MeleeAttack(), new RangeAttack(),
			new FireWallAttack(), new ChangeArmour(), new SoulSummonAttack(), new SoulSiphonAttack() };

	/**
	 * The attacks for the last phase.
	 */
	private static final QueenAttack[] PHASE_4_ATTACKS = { // Super fire attack twice, to make it more often.
			new FireBreathAttack(), new TimeStopAttack(), new MeleeAttack(), new SuperFireAttack(), new RangeAttack(),
			new FireWallAttack(), new SuperFireAttack(), new ChangeArmour(), new SoulSummonAttack(),
			new SoulSiphonAttack(), new TimeStopAttack() };

	/**
	 * The rewards (average ratio: 28,5).
	 */
	private static final int[][] REWARDS = { { 1149, 1, 1, 57 }, // Dragon helm
			{ 1305, 1, 1, 29 }, // Dragon longsword
			{ 1215, 1, 1, 32 }, // Dragon dagger
			{ 1249, 1, 1, 17 }, // Dragon spear
			{ 24365, 1, 1, 12 }, // Dragon kiteshield
			{ 560, 500, 500, 31 }, // Death rune
			{ 565, 500, 500, 30 }, // Blood rune
			{ 566, 20, 100, 21 }, // Soul rune
			{ 9342, 150, 150, 12 }, // Onyx bolts
			{ 7936, 1480, 3500, 16 }, // Pure essence
			{ 453, 300, 580, 18 }, // Coal
			{ 449, 50, 50, 16 }, // Adamantite
			{ 451, 30, 100, 13 }, // Runite ore
			{ 5316, 1, 1, 21 }, // Magic seed
			{ 5300, 5, 5, 18 }, // Snapdragon seed
			{ 5321, 3, 3, 26 }, // Watermelon seed
			{ 5304, 1, 5, 8 }, // Torstol seed
			{ 371, 200, 200, 24 }, // Raw swordfish
			{ 15272, 1, 9, 53 }, // Rocktail
			{ 6689, 1, 10, 57 }, // Saradomin brew (2)
			{ 3028, 1, 10, 34 }, // Super restore (2)
			{ 995, 3000, 200000, 35 }, // Coins
			{ 1513, 90, 120, 38 }, // Magic logs
			{ 1515, 150, 500, 53 }, // Yew logs
			{ 219, 10, 10, 19 }, // Grimy torstol
			{ 2485, 50, 50, 18 }, // Grimy lantadyme
			{ 1631, 1, 9, 35 }, // Uncut dragonstone
			{ 2366, 1, 1, 21 }, // Shield left half
			{ 9194, 30, 30, 17 }, // Onyx bolt tips
			{ 24346, 1, 1, 15 }, // Royal torsion spring
			{ 24344, 1, 1, 15 }, // Royal sight
			{ 24342, 1, 1, 15 }, // Royal frame
			{ 24340, 1, 1, 15 }, // Royal bolt stabiliser
			{ 24352, 1, 1, 5 }, // Dragonbone upgrade kit
			{ 11286, 1, 1, 4 }, // Draconic visage current: 850
	};

	/**
	 * The waking up animation.
	 */
	private static final Animation WAKE_UP_ANIMATION = new Animation(16714);

	/**
	 * The sleeping animation.
	 */
	private static final Animation SLEEP_ANIMATION = new Animation(16742);

	/**
	 * The player.
	 */
	private final Player attacker;

	/**
	 * The queen state.
	 */
	private QueenState state = QueenState.SLEEPING;

	/**
	 * The amount of ticks passed.
	 */
	private int ticks;

	/**
	 * The next attack tick count.
	 */
	private int nextAttack;

	/**
	 * The current attacks.
	 */
	private QueenAttack[] attacks;

	/**
	 * The current phase.
	 */
	private int phase;

	/**
	 * The region base location.
	 */
	private final WorldTile base;

	/**
	 * The list of tortured souls.
	 */
	private final List<TorturedSoul> souls = new ArrayList<TorturedSoul>();

	/**
	 * The list of worms.
	 */
	private final List<NPC> worms = new ArrayList<NPC>();

	/**
	 * If the Queen Black Dragon is spawning worms.
	 */
	private boolean spawningWorms;

	/**
	 * The current active artifact.
	 */
	private WorldObject activeArtifact;

	/**
	 * The rewards container.
	 */
	private final ItemsContainer<Item> rewards = new ItemsContainer<>(10, true);

	/**
	 * The last amount of hitpoints.
	 */
	private int lastHitpoints = -1;

	/**
	 * Constructs a new {@code QueenBlackDragon} {@code Object}.
	 * @param attacker The player.
	 * @param tile The world tile to set the queen on.
	 * @param base The dynamic region's base location.
	 */
	public QueenBlackDragon(Player attacker, WorldTile tile, WorldTile base) {
		super(15509, tile, -1, true, true);
		super.setForceMultiArea(true);
		super.setCantFollowUnderCombat(true);
		setCantInteract(true);
		this.base = base;
		this.attacker = attacker;
		this.nextAttack = 40;
		setHitpoints(getMaxHitpoints());
		activeArtifact = new WorldObject(70776, 10, 0, base.transform(33, 31, 0));
		this.setPhase(1);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (hit != null && hit.getDamage() > 1000) {
			hit.setDamage(1000);
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public void setHitpoints(int hitpoints) {
		super.setHitpoints(hitpoints);
		if (attacker == null) {
			return;
		}
		if (lastHitpoints != hitpoints) {
			attacker.getPackets().sendGlobalConfig(1923, getMaxHitpoints() - hitpoints);
			lastHitpoints = hitpoints;
		}
	}

	@Override
	public void sendDeath(Entity source) {
		switch (phase) {
		case 1:
			attacker.getPackets().sendGlobalConfig(1924, 1);
			activeArtifact = new WorldObject(70777, 10, 0, base.transform(33, 31, 0));
			attacker.getPackets().sendGameMessage(
					"The Queen Black Dragon's concentration wavers; the first artefact is now unguarded.");
			break;
		case 2:
			attacker.getPackets().sendGlobalConfig(1924, 3);
			attacker.getPackets().sendSpawnedObject(new WorldObject(70844, 10, 0, base.transform(24, 21, -1)));
			activeArtifact = new WorldObject(70780, 10, 0, base.transform(24, 21, 0));
			attacker.getPackets()
					.sendGameMessage("The Queen Black Dragon's concentration wavers; the second artefact is now");
			attacker.getPackets().sendGameMessage("unguarded.");
			break;
		case 3:
			attacker.getPackets().sendGlobalConfig(1924, 5);
			attacker.getPackets().sendSpawnedObject(new WorldObject(70846, 10, 0, base.transform(24, 21, -1)));
			activeArtifact = new WorldObject(70783, 10, 0, base.transform(42, 21, 0));
			attacker.getPackets()
					.sendGameMessage("The Queen Black Dragon's concentration wavers; the third artefact is now");
			attacker.getPackets().sendGameMessage("unguarded.");
			break;
		case 4:
			attacker.getPackets().sendGlobalConfig(1924, 7);
			attacker.getPackets().sendSpawnedObject(new WorldObject(70848, 10, 0, base.transform(24, 21, -1)));
			activeArtifact = new WorldObject(70786, 10, 0, base.transform(33, 21, 0));
			attacker.getPackets().sendGameMessage(
					"The Queen Black Dragon's concentration wavers; the last artefact is now unguarded.");
			break;
		}
		World.spawnObject(activeArtifact, true);
		setCantInteract(true);
		if (phase < 5) {
			this.setSpawningWorms(true);
			return;
		}
		this.switchState(QueenState.DEFAULT);
		// TODO: Back to sleep roar
	}

	@Override
	public int getMaxHitpoints() {
		return 7500;
	}

	@Override
	public void processNPC() {
		if (ticks > 5 && !attacker.isAtDynamicRegion()) {
			finish();
			return;
		}
		if (ticks == -20) {
			switchState(QueenState.DEFAULT);
			switchState(QueenState.SLEEPING);
			super.setNextAnimation(SLEEP_ANIMATION);
			World.removeObject(new WorldObject(70778, 10, 0, base.transform(33, 31, 0)), true);
			World.spawnObject(new WorldObject(70790, 10, 0, base.transform(31, 29, 0)), true);
			World.spawnObject(new WorldObject(70775, 10, 0, base.transform(31, 29, -1)), true);
		} else if (ticks == -1) {
			return;
		}
		ticks++;
		if (spawningWorms) {
			if ((ticks % 10) == 0) {
				spawnWorm();
			}
			return;
		} else if (ticks == 5) {
			super.setNextAnimation(WAKE_UP_ANIMATION);
		} else if (ticks == 30) {
			setCantInteract(false);
			switchState(QueenState.DEFAULT);
		} else if (ticks == nextAttack) {
			QueenAttack attack;
			while (!(attack = attacks[Utils.random(attacks.length)]).canAttack(this, attacker))
				;
			this.setNextAttack(attack.attack(this, attacker));
		}
	}

	@Override
	public void finish() {
		for (TorturedSoul s : souls) {
			s.finish();
		}
		for (NPC worm : worms) {
			worm.finish();
		}
		super.finish();
	}

	/**
	 * Spawns a grotworm.
	 */
	public void spawnWorm() {
		setNextAnimation(new Animation(16747));
		attacker.getPackets().sendGameMessage("Worms burrow through her rotting flesh.");
		final WorldTile destination = base.transform(28 + Utils.random(12), 28 + Utils.random(6), 0);
		attacker.getPackets().sendProjectile(null, this, destination, 3141, 128, 0, 60, 0, 5, 3,
				super.getDefinitions().size);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getPhase() > 4) {
					return;
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if (getPhase() > 4) {
							return;
						}
						NPC worm = new NPC(15464, destination, -1, true, true);
						worms.add(worm);
						worm.setForceMultiArea(true);
						worm.getCombat().setTarget(attacker);
					}
				}, 5);
				attacker.getPackets().sendGraphics(new Graphics(3142), destination);
			}
		}, 1);
	}

	@Override
	public int getDirection() {
		return 0;
	}

	/**
	 * Gets the attacker.
	 * @return The attacker.
	 */
	public Player getAttacker() {
		return attacker;
	}

	/**
	 * Gets the state.
	 * @return The state.
	 */
	public QueenState getState() {
		return state;
	}

	/**
	 * Switches the queen state.
	 * @param state The state.
	 */
	@SuppressWarnings("incomplete-switch")
	public void switchState(QueenState state) {
		this.state = state;
		if (state.getMessage() != null) {
			String[] messages = state.getMessage().split("(nl)");
			for (String message : messages) {
				attacker.getPackets().sendGameMessage(message.replace("(", "").replace(")", ""));
			}
		}
		super.transformIntoNPC(state.getNpcId());
		switch (state) {
		case DEFAULT:
			attacker.getPackets().sendSpawnedObject(new WorldObject(70822, 10, 0, base.transform(21, 35, -1)));
			attacker.getPackets().sendSpawnedObject(new WorldObject(70818, 10, 0, base.transform(39, 35, -1)));
			break;
		case HARDEN:
			attacker.getPackets().sendSpawnedObject(new WorldObject(70824, 10, 0, base.transform(21, 35, -1)));
			attacker.getPackets().sendSpawnedObject(new WorldObject(70820, 10, 0, base.transform(39, 35, -1)));
			break;
		case CRYSTAL_ARMOUR:
			attacker.getPackets().sendSpawnedObject(new WorldObject(70823, 10, 0, base.transform(21, 35, -1)));
			attacker.getPackets().sendSpawnedObject(new WorldObject(70819, 10, 0, base.transform(39, 35, -1)));
			break;
		}
	}

	/**
	 * Opens the reward chest.
	 * @param replace If the chest should be replaced with an opened one.
	 */
	public void openRewardChest(boolean replace) {
		attacker.getInterfaceManager().sendInterface(1284);
		attacker.getPackets().sendInterSetItemsOptionsScript(1284, 7, 100, 8, 3, "Take", "Bank", "Discard", "Examine");
		attacker.getPackets().sendAccessMask(1284, 7, 0, 10, 0, 1, 2, 3);
		attacker.getPackets().sendItems(100, rewards);
		if (replace) {
			World.spawnObject(new WorldObject(70817, 10, 0, base.transform(30, 28, -1)), true);
		}
	}

	/**
	 * Sets the state.
	 * @param state The state to set.
	 */
	public void setState(QueenState state) {
		this.state = state;
	}

	/**
	 * Gets the nextAttack.
	 * @return The nextAttack.
	 */
	public int getNextAttack() {
		return nextAttack;
	}

	/**
	 * Sets the nextAttack value (current ticks + the given amount).
	 * @param nextAttack The amount.
	 */
	public void setNextAttack(int nextAttack) {
		this.nextAttack = ticks + nextAttack;
	}

	/**
	 * Gets the phase.
	 * @return The phase.
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * Sets the phase.
	 * @param phase The phase to set.
	 */
	public void setPhase(int phase) {
		this.phase = phase;
		switch (phase) {
		case 1:
			this.attacks = PHASE_1_ATTACKS;
			break;
		case 2:
			this.attacks = PHASE_2_ATTACKS;
			break;
		case 3:
			this.attacks = PHASE_3_ATTACKS;
			break;
		case 4:
			this.attacks = PHASE_4_ATTACKS;
			break;
		case 5:
			setCantInteract(true);
			for (TorturedSoul soul : souls) {
				soul.finish();
			}
			for (NPC worm : worms) {
				worm.finish();
			}
			ticks = -22;
			prepareRewards();
			attacker.getPackets().sendSpawnedObject(new WorldObject(70837, 10, 0, base.transform(22, 24, -1)));
			attacker.getPackets().sendSpawnedObject(new WorldObject(70840, 10, 0, base.transform(34, 24, -1)));
			attacker.getPackets().sendGameMessage(
					"<col=33FFFF>The enchantment is restored! The Queen Black Dragon falls back into her cursed</col>");
			attacker.getPackets().sendGameMessage("<col=33FFFF>slumber.</col>");
			break;
		// Qbd is dead roar.
		}
	}

	/**
	 * Prepares the rewards.
	 */
	private void prepareRewards() {
		rewards.add(new Item(536, 5));
		rewards.add(new Item(24372, 1 + Utils.random(6)));
		rewards.add(new Item(24336, 50 + Utils.random(51)));
		if (!attacker.getInventory().containsItem(24368, 1) && attacker.getBank().getItem(24368) == null) {
			rewards.add(new Item(24368));
		} else if (!attacker.getInventory().containsItem(24369, 1) && attacker.getBank().getItem(24369) == null) {
			if (Utils.random(10) == 0) {
				rewards.add(new Item(24369));
			}
		} else if (!attacker.getInventory().containsItem(24370, 1) && attacker.getBank().getItem(24370) == null) {
			if (Utils.random(25) == 0) {
				rewards.add(new Item(24370));
			}
		} else if (!attacker.getInventory().containsItem(24371, 1) && attacker.getBank().getItem(24371) == null) {
			if (Utils.random(40) == 0) {
				rewards.add(new Item(24371));
			}
		}
		List<Item> rewardTable = new ArrayList<Item>();
		for (int[] reward : REWARDS) {
			Item item = new Item(reward[0], reward[1] + Utils.random(reward[2] - reward[1]));
			for (int i = 0; i < reward[3]; i++) {
				rewardTable.add(item);
			}
		}
		Collections.shuffle(rewardTable);
		for (int i = 0; i < 1 + Utils.random(3); i++) {
			rewards.add(rewardTable.get(Utils.random(rewardTable.size())));
		}
	}

	/**
	 * Gets the base.
	 * @return The base.
	 */
	public WorldTile getBase() {
		return base;
	}

	/**
	 * Gets the amount of ticks.
	 * @return The amount of ticks.
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Gets the souls.
	 * @return The souls.
	 */
	public List<TorturedSoul> getSouls() {
		return souls;
	}

	/**
	 * Gets the spawningWorms.
	 * @return The spawningWorms.
	 */
	public boolean isSpawningWorms() {
		return spawningWorms;
	}

	/**
	 * Sets the spawningWorms.
	 * @param spawningWorms The spawningWorms to set.
	 */
	public void setSpawningWorms(boolean spawningWorms) {
		if (!spawningWorms) {
			setNextAnimation(new Animation(16748));
		}
		this.spawningWorms = spawningWorms;
	}

	/**
	 * Gets the worms.
	 * @return The worms.
	 */
	public List<NPC> getWorms() {
		return worms;
	}

	/**
	 * Gets the activeArtifact.
	 * @return The activeArtifact.
	 */
	public WorldObject getActiveArtifact() {
		return activeArtifact;
	}

	/**
	 * Sets the activeArtifact.
	 * @param activeArtifact The activeArtifact to set.
	 */
	public void setActiveArtifact(WorldObject activeArtifact) {
		this.activeArtifact = activeArtifact;
	}

	/**
	 * Gets the rewards.
	 * @return The rewards.
	 */
	public ItemsContainer<Item> getRewards() {
		return rewards;
	}

}