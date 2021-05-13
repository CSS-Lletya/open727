package org.redrune.game.content.combat.player;

import java.util.ArrayList;
import java.util.List;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.utility.rs.Hit.HitAttributes;
import org.redrune.utility.tool.RandomFunction;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.Region;
import com.rs.game.player.Player;

import npc.NPC;

/**
 * Handles the swing of the combat type.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public abstract class CombatTypeSwing {
	
	/**
	 * Handles the running of the combat type
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target we're attacking
	 * @param id
	 * 		The weapon or spell used
	 * @param combatStyle
	 * 		The combat style used
	 * @param special
	 * 		The special attack
	 */
	public abstract boolean run(Player player, Entity target, int id, int combatStyle, SpecialAttackEvent special);
	
	/**
	 * Gets the attack bonus
	 *
	 * @param player
	 * 		The player
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 * @param specialAttack
	 * 		If we are using a special attack
	 */
	public abstract double getAttackBonus(Player player, int weaponId, int combatStyle, boolean specialAttack);
	
	/**
	 * Gets the defence bonus
	 *
	 * @param entity
	 * 		The entity
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 */
	public abstract double getDefenceBonus(Entity entity, int weaponId, int combatStyle);
	
	/**
	 * Gets the max hit bonus
	 *
	 * @param player
	 * 		The entity
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 * @param multiplier
	 * 		The max hit multiplier
	 */
	public abstract double getMaxHit(Player player, int weaponId, int combatStyle, double multiplier);
	
	/**
	 * The combat calculator used for this swing type
	 */
	protected final CombatTypeCalculator calculator;
	
	public CombatTypeSwing(CombatTypeCalculator calculator) {
		this.calculator = calculator;
	}
	
	/**
	 * Calculates a random hit
	 *
	 * @param maxHit
	 * 		The max hit
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	public int randomizeHit(double maxHit, double attackBonus, double defenceBonus) {
		if (!rollHit(attackBonus, defenceBonus)) {
			//System.out.println("rolled a miss [" + maxHit + ", " + attackBonus + ", " + defenceBonus + "]");
			return 0;
		}
		// the random hit
		int random = RandomFunction.random((int) maxHit);
		// the count index used for re-rolls
		int count = 0;
		// we dont want too low too often, so we reroll
		while (random <= (maxHit * 0.25) && count < 3) {
			random = RandomFunction.random((int) maxHit);
			//System.out.println("rerolled a " + random + " and we got " + random + "[#" + count + "]");
			count++;
		}
		if (random == 0) {
			//System.out.println("Rerolled " + count + " times and got a " + random);
		}
		return random;
	}
	
	/**
	 * Calculates a random hit
	 *
	 * @param minimumHit
	 * 		The minimum damage
	 * @param maxHit
	 * 		The max hit
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	public int randomizeHit(double minimumHit, double maxHit, double attackBonus, double defenceBonus, boolean roll) {
		if (roll && !rollHit(attackBonus, defenceBonus)) {
			//System.out.println("rolled a miss [" + maxHit + ", " + attackBonus + ", " + defenceBonus + "]");
			return 0;
		}
		// the random hit
		int random = (int) RandomFunction.random(minimumHit, maxHit);
		// the count index used for re-rolls
		int count = 0;
		// we dont want too low too often, so we reroll
		while (random <= (maxHit * 0.25) && count < 3) {
			random = (int) RandomFunction.random(minimumHit, maxHit);
			//System.out.println("rerolled a " + random + " and we got " + random + "[#" + count + "]");
			count++;
		}
		if (random == 0) {
			//System.out.println("Rerolled " + count + " times and got a " + random);
		}
		return random;
	}
	
	/**
	 * Calculates the two modifiers and checks if the hit should randomly miss
	 *
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	private boolean rollHit(double attackBonus, double defenceBonus) {
/*		System.out.println("attackBonus = [" + attackBonus + "], defenceBonus = [" + defenceBonus + "]");
		final boolean hasAttack = attackBonus >= 0;
		return hasAttack && (defenceBonus < 0 || RandomFunction.getRandomDouble((attackBonus + defenceBonus)) > defenceBonus);*/
/*

		double mod = 1.33;
		if (victim == null || style == null) {
			return false;
		}
		if (style != null) {
			if (victim instanceof Player && entity instanceof Familiar && ((Player) victim).getPrayer().get(PrayerType.PROTECT_FROM_SUMMONING)) {
				mod = 0;
			}
		}
		double attackBonus = calculateAccuracy(entity) * accuracyMod * mod * getSetMultiplier(entity, Skills.ATTACK);
		double defenceBonus = calculateDefence(victim, entity) * defenceMod * getSetMultiplier(victim, Skills.DEFENCE);
		double chance = 0.0;
		if (attackBonus < defenceBonus) {
			chance = (attackBonus - 1) / (defenceBonus * 2);
		} else {
			chance = 1 - ((defenceBonus + 1) / (attackBonus * 2));
		}
		double ratio = chance * 100;
		double accuracy = Math.floor(ratio);
		double block = Math.floor(101 - ratio);
		double acc = Math.random() * accuracy;
		double def = Math.random() * block;
		return acc > def;*/
		
		double attack = attackBonus * 1.33;
		double defence = defenceBonus * 1.0D;
		
		double chance;
		if (attack < defence) {
			chance = (attack - 1) / (defence * 2);
		} else {
			chance = 1 - ((defence + 1) / (attack * 2));
		}
		double ratio = Math.floor(chance * 100);
		double block = Math.floor(101 - ratio);
		double acc = RandomFunction.getRandomDouble(ratio);
		double def = RandomFunction.getRandomDouble(block);
		int count = 0;
		if (acc < def) {
			do {
				acc = RandomFunction.getRandomDouble(ratio);
				def = RandomFunction.getRandomDouble(block);
				//				System.out.println("low random roll {" + acc + ", " + def + "} #" + count);
				count++;
			} while ((acc > def) && count < 10);
		}
		//		System.out.println("attackBonus = [" + attackBonus + "], defenceBonus = [" + defenceBonus + "], chance=" + chance + ", ratio=" + ratio + ", block=" + block + ", attack=" + attack + ", defence=" + def + ", acc = { " + acc + "}, def = { " + def + "}");
		return acc >= def;
	}
	
	/**
	 * Applies a hit to the receiver
	 *
	 * @param attacker
	 * 		The attacking player
	 * @param receiver
	 * 		The receiver of the hit
	 * @param hit
	 * 		The hit
	 * @param itemId
	 * 		The item id used
	 * @param combatStyle
	 * 		The combat style
	 * @param delay
	 * 		The delay for the hit
	 */
	public void applyHit(Player attacker, Entity receiver, Hit hit, int itemId, int combatStyle, int delay) {
		appendExperience(attacker, receiver, itemId, combatStyle, hit.getDamage());
		SystemManager.getScheduler().schedule(new ScheduledTask(delay) {
			@Override
			public void run() {
				// the attribute is put when the hit actually appears
				hit.getAttributes().put(HitAttributes.WEAPON_USED, itemId);
				// and the hit is applied to the receiver
				receiver.getHitMap().applyHit(hit);
			}
		});
	}
	
	/**
	 * Sends the experience task. The block emote is also sent in this block
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param params
	 * 		The parameters
	 */
	public abstract void appendExperience(Player player, Entity target, Object... params);
	
	/**
	 * Gets the attackable entities in a radius of 1 around the target, with a capacity of 9
	 *
	 * @param source
	 * 		The source player
	 * @param target
	 * 		The target entity
	 */
	public static List<Entity> getAttackableEntities(Player source, Entity target) {
		return getAttackableEntities(source, target, 1, 9);
	}
	
	/**
	 * Constructs a list of all the entities that the player can attack within a distance
	 *
	 * @param source
	 * 		The player we want to find attackable entities for
	 * @param target
	 * 		The base target we're in combat with
	 * @param maxDistance
	 * 		The max distance we should look for targets in
	 * @param capacity
	 * 		The maximum amount of targets we can attack
	 */
	public static List<Entity> getAttackableEntities(Player source, Entity target, int maxDistance, int capacity) {
		List<Entity> possibleTargets = new ArrayList<>();
		if (target == null) {
			return possibleTargets;
		}
		// add the target into the list
		possibleTargets.add(target);
		if (target.isAtMultiArea()) {
			Region region = target.getRegion();
			if (target.isPlayer()) {
				for (Player p2 : region.getPlayers()) {
					// skip the players that can't be rendered
					if (p2 == null || p2.isDead() || !p2.isRenderable()) {
						continue;
					}
					// avoiding duplicates
					if (p2 == source || p2 == target) {
						continue;
					}
					// make sure the player can fight us and is local
					if (!p2.getVariables().isInFightArea() || !p2.isAtMultiArea() || !p2.getLocation().withinDistance(target.getLocation(), maxDistance) || !source.getManager().getActivities().combatAcceptable(p2)) {
						continue;
					}
					// everything is good we can add to the list
					possibleTargets.add(p2);
					// reached max size so we must stop the operation
					if (possibleTargets.size() == capacity) {
						break;
					}
				}
			} else {
				for (NPC n : region.getNpcs()) {
					// skip the npcs that can't be rendered
					if (n == null || n == target || n.isDead() || !n.isRenderable()) {
						continue;
					}
					// make sure the npc can fight us and is local
					if (!n.isAtMultiArea() || !n.getDefinitions().hasAttackOption() || !source.getManager().getActivities().combatAcceptable(n) || !n.getLocation().withinDistance(target.getLocation(), maxDistance)) {
						continue;
					}
					// everything is good we can add to the list
					possibleTargets.add(n);
					// reached max size so we must stop the operation
					if (possibleTargets.size() == capacity) {
						break;
					}
				}
			}
		}
		return possibleTargets;
	}
	
}
