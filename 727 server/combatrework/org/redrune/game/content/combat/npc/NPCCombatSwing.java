package org.redrune.game.content.combat.npc;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.combat.BonusConstants;
import org.redrune.game.content.combat.NPCConstants;
import org.redrune.game.content.combat.StaticCombatFormulae;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.player.actions.PlayerCombatAction;
import com.rs.utils.Utils;

import npc.NPC;
import skills.SkillConstants;

/**
 * This is the combat swing interface that handles the npc's combat.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public interface NPCCombatSwing extends NPCConstants {
	
	/**
	 * The array of bindings for the swing
	 */
	Object[] bindings();
	
	/**
	 * Handles the attacking of the swing
	 *
	 * @param npc
	 * 		The npc
	 * @param target
	 * 		The target
	 * @return The delay until the next swing
	 */
	int attack(NPC npc, Entity target);
	
	/**
	 * Gets a random max hit
	 *
	 * @param npc
	 * 		The npc hitting
	 * @param maxHit
	 * 		The max hit
	 * @param attackStyle
	 * 		The style used
	 * @param target
	 * 		The target
	 */
	default int randomMaxHit(NPC npc, int maxHit, int attackStyle, Entity target) {
		int[] bonuses = npc.getBonuses();
		double att = bonuses == null ? 0 : attackStyle == RANGE_COMBAT_STYLE ? bonuses[BonusConstants.RANGE_ATTACK] : attackStyle == MAGIC_COMBAT_STYLE ? bonuses[BonusConstants.MAGIC_ATTACK] : bonuses[BonusConstants.STAB_ATTACK];
		double def;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			def = p2.getSkills().getLevel(SkillConstants.DEFENCE) + (2 * p2.getEquipment().getBonus(attackStyle == RANGE_COMBAT_STYLE ? BonusConstants.RANGE_DEFENCE : attackStyle == MAGIC_COMBAT_STYLE ? BonusConstants.MAGIC_DEFENCE : BonusConstants.STAB_DEFENCE));
			def *= p2.getManager().getPrayers().getBasePrayerBoost(SkillConstants.DEFENCE);
		} else {
			NPC n = (NPC) target;
			def = n.getBonuses() == null ? 0 : n.getBonuses()[attackStyle == RANGE_COMBAT_STYLE ? BonusConstants.RANGE_DEFENCE : attackStyle == MAGIC_COMBAT_STYLE ? BonusConstants.MAGIC_DEFENCE : BonusConstants.STAB_DEFENCE];
			def *= 2;
		}
		double prob = att / def;
		// max, 90% prob hit so even lvl 138 can miss at lvl 3
		if (prob > 0.90) {
			prob = 0.90;
		} else if (prob < 0.05) {
			// minimun 5% so even lvl 3 can hit lvl 138
			prob = 0.05;
		}
		if (prob < Math.random()) {
			return 0;
		}
		return Utils.getRandom(maxHit);
	}
	
	/**
	 * Adds a hit after the time
	 *
	 * @param npc
	 * 		The npc hitting
	 * @param delay
	 * 		The ticks to wait
	 * @param target
	 * 		The target
	 * @param hits
	 * 		The hits to apply
	 */
	default void delayHit(NPC npc, int delay, final Entity target, final Hit... hits) {
		target.addAttackedByDelay(npc);
		SystemManager.getScheduler().schedule(new ScheduledTask(delay) {
			@Override
			public void run() {
				for (Hit hit : hits) {
					NPC npc = hit.getSource().toNPC();
					if (npc == null || npc.isDead() || !npc.isRenderable()) {
						return;
					}
					target.getHitMap().applyHit(hit);
					target.sendAwaitedAnimation(StaticCombatFormulae.getDefenceEmote(target));
					if (target.isPlayer()) {
						Player p2 = target.toPlayer();
						p2.getManager().getInterfaces().closeAll();
						SystemManager.getScheduler().schedule(new ScheduledTask(1) {
							@Override
							public void run() {
								if (p2.getCombatDefinitions().isRetaliating() && !p2.fighting() && !p2.getMovement().hasWalkSteps()) {
									p2.getManager().getActions().startAction(new PlayerCombatAction(npc));
								}
							}
						});
					} else {
						NPC n = target.toNPC();
						if (!npc.fighting()) {
							n.getCombatManager().getCombat().setTarget(npc);
						}
					}
				}
			}
		});
	}
	
	/**
	 * Constructs a melee hit
	 *
	 * @param npc
	 * 		The npc the hit is from
	 * @param damage
	 * 		The damage on the hitsplat
	 */
	default Hit constructMeleeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MELEE_DAMAGE);
	}
	
	/**
	 * Constructs a range hit
	 *
	 * @param npc
	 * 		The npc the hit is from
	 * @param damage
	 * 		The damage on the hitsplat
	 */
	default Hit constructRangeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.RANGE_DAMAGE);
	}
	
	/**
	 * Constructs a magic hit
	 *
	 * @param npc
	 * 		The npc the hit is from
	 * @param damage
	 * 		The damage on the hitsplat
	 */
	default Hit constructMagicHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MAGIC_DAMAGE);
	}
}