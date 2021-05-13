package org.redrune.game.content.combat.player.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.calc.MagicCombatCalculator;
import org.redrune.game.content.combat.player.registry.wrapper.CombatSwingDetail;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.content.combat.player.registry.wrapper.magic.MagicSpellEvent;
import org.redrune.utility.rs.Hit.HitAttributes;
import org.redrune.utility.rs.Hit.HitSplat;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.ProjectileManager;
import com.rs.game.player.Player;

import skills.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class MagicCombatSwing extends CombatTypeSwing {
	
	public MagicCombatSwing() {
		super(new MagicCombatCalculator());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean run(Player player, Entity target, int spellId, int combatStyle, SpecialAttackEvent special) {
		final boolean regularCast = player.getAttribute("spell_cast_id", -1) != -1;
		// we're not auto-casting so we should reset the spell
		if (regularCast) {
			player.getCombatDefinitions().resetSpells(false);
			player.getManager().getActions().stopAction();
		}
		// we don't have the runes anymore
		if (!CombatRegistry.checkCombatSpell(player, spellId, -1, true)) {
			if (!regularCast) {
				player.getCombatDefinitions().resetSpells(true);
			}
			return false;
		}
		// finds the spell to cast
		Optional<MagicSpellEvent<?>> spellOptional = CombatRegistry.getSpell(player.getCombatDefinitions().getSpellbook(), spellId);
		// spell hasn't been registered yet
		if (!spellOptional.isPresent()) {
			player.getPackets().sendGameMessage("Spell #" + spellId + " has not yet been added, please report this on the forums.");
			return false;
		}
		// spell instance
		MagicSpellEvent spellEvent = spellOptional.get();
		switch (player.getCombatDefinitions().getSpellbook()) {
			case REGULAR:
			case ANCIENTS:
			case LUNARS:
				switch (spellId) {
					default:
						// all spells usually are cast like this
						// context will change for spells that have different parameters.
						spellEvent.cast(player, new CombatSpellContext(target, this));
						break;
				}
				break;
		}
		return true;
	}
	
	@Override
	public double getAttackBonus(Player player, int weaponId, int combatStyle, boolean specialAttack) {
		return 0;
	}
	
	@Override
	public double getDefenceBonus(Entity entity, int weaponId, int combatStyle) {
		return 0;
	}
	
	@Override
	public double getMaxHit(Player player, int weaponId, int combatStyle, double multiplier) {
		return 0;
	}
	
	@Override
	public void appendExperience(Player player, Entity target, Object... params) {
		double magicExp = (double) params[0];
		int damage = (int) params[1];
		
		double combatXp = magicExp * 1 + (damage / 5);
		if (combatXp <= 0) {
			return;
		}
		if (player.getCombatDefinitions().isDefensiveCasting()) {
			double defenceXp = damage / 3;
			if (defenceXp > 0) {
				combatXp -= defenceXp;
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier(SkillConstants.DEFENCE, defenceXp);
				} else {
					player.getSkills().addExperienceWithMultiplier(SkillConstants.DEFENCE, defenceXp);
				}
			}
		}
		if (target.isPlayer()) {
			player.getSkills().addExperienceNoMultiplier(SkillConstants.MAGIC, combatXp);
		} else {
			player.getSkills().addExperienceWithMultiplier(SkillConstants.MAGIC, combatXp);
		}
		
		double hpExp = damage / 7.5;
		if (hpExp > 0) {
			if (target.isPlayer()) {
				player.getSkills().addExperienceNoMultiplier(SkillConstants.HITPOINTS, hpExp);
			} else {
				player.getSkills().addExperienceWithMultiplier(SkillConstants.HITPOINTS, hpExp);
			}
		}
	}
	
	/**
	 * Sends the spell to the target
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param event
	 * 		The magic spell event
	 * @return The damage that landed
	 */
	public CombatSwingDetail sendSpell(Player player, Entity target, CombatSpellEvent event) {
		return sendSpell(player, target, event, null, null);
	}
	
	/**
	 * Sends a multi spell
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param event
	 * 		The event
	 * @param spellCastTask
	 * 		The task for when the spell is cast
	 * @param hitLandTask
	 * 		The task for when the hit lands
	 */
	public List<CombatSwingDetail> sendMultiSpell(Player player, Entity target, CombatSpellEvent event, Runnable spellCastTask, Runnable hitLandTask) {
		// the list consisting of all the entities to attacak, and the first index being the
		// entity we cast the spell on
		List<Entity> entityList = CombatTypeSwing.getAttackableEntities(player, target);
		// the list of all contexts
		List<CombatSwingDetail> detailList = new ArrayList<>();
		if (entityList.size() == 0) {
			return detailList;
		}
		// if the first hit was a splash we don't want to keep trying
		for (int i = 0; i < entityList.size(); i++) {
			Entity entity = entityList.get(i);
			CombatSwingDetail context = sendSpell(player, entity, event, spellCastTask, hitLandTask);
			detailList.add(context);
			if (i == 0 && context.getHit().getDamage() == 0) {
				return detailList;
			}
		}
		return detailList;
	}
	
	/**
	 * Sends the spell to the target
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param event
	 * 		The magic spell event
	 * @param spellCastTask
	 * 		The task that is executed when the spell is cast
	 * @param hitLandTask
	 * 		The task that is executed when the hit lands.
	 * @return The amount of damage that landed
	 */
	public CombatSwingDetail sendSpell(Player player, Entity target, CombatSpellEvent event, Runnable spellCastTask, Runnable hitLandTask) {
		int damage;
		int maxHit = event.maxHit(player, target);
		int minimum = event.minimumHit(player);
		if (minimum != -1) {
			damage = randomizeHit(minimum, maxHit, calculator.totalAggressiveBoost(player), calculator.totalDefensiveBoost(target), false);
		} else {
			damage = randomizeHit(maxHit, calculator.totalAggressiveBoost(player), calculator.totalDefensiveBoost(target));
		}
		appendExperience(player, target, event.exp(), damage);
		// the projectile delay speed
		int projectileDelay = ProjectileManager.getProjectileDelay(player, target);
		// the extra delay calculation
		double delayCalc = ProjectileManager.getDelay(player, target, projectileDelay, 0);
		// the final delay
		final int delay = (int) (projectileDelay + delayCalc);
		
		final Hit hit = new Hit(player, damage, HitSplat.MAGIC_DAMAGE).setMaxHit(maxHit);
		
		// handles the leeches aspect of the hit
		if (target.isPlayer()) {
			target.toPlayer().getManager().getPrayers().handlePrayerEffects(hit);
		} else {
			target.toNPC().handlePrayerEffects(hit);
		}
		
		// uses the spells animation
		if (event.animationId() != -1) {
			player.sendAnimation(event.animationId());
		}
		
		// sends the task that is executed when the spell is used successfully
		if (damage > 0 && spellCastTask != null) {
			spellCastTask.run();
		}
		
		SystemManager.getScheduler().schedule(new ScheduledTask(1, delay) {
			
			@Override
			public void run() {
				// so we don't have to make a new task for blocking.
				if (delay == 2 && getTicksPassed() == 0 || getTicksPassed() == getGoalTicks() - 1) {
					target.sendAwaitedAnimation(StaticCombatFormulae.getDefenceEmote(target));
				} else if (getTicksPassed() == getGoalTicks()) {
					// the attribute is put when the hit actually appears
					hit.getAttributes().put(HitAttributes.WEAPON_USED, player.getEquipment().getWeaponId());
					// and the hit is applied to the receiver
					target.getHitMap().applyHit(hit);
					if (damage == 0) {
						target.sendGraphics(85, 96, 0);
					} else {
						if (event.hitGfx() != -1) {
							target.sendGraphics(event.hitGfx(), event.gfxHeight(), 0);
						}
						if (damage > 0 && hitLandTask != null) {
							hitLandTask.run();
						}
					}
					stop();
				}
			}
		});
		return new CombatSwingDetail(player, target, hit);
	}
	
}
