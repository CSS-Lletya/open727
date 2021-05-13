package org.redrune.game.content.combat.player.swing;

import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.calc.MeleeCombatCalculator;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.utility.rs.Hit.HitSplat;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.Player;

import skills.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class MeleeCombatSwing extends CombatTypeSwing {
	
	public MeleeCombatSwing() {
		super(new MeleeCombatCalculator());
	}
	
	// TODO: healing from guthans
	@Override
	public boolean run(Player player, Entity target, int weaponId, int combatStyle, SpecialAttackEvent special) {
		String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.forId(weaponId).getName();
		
		// we check the special attacks
		boolean usingSpecial = special != null;
		
		// the energy required for the special
		final int energyRequired = ItemConstants.getSpecialEnergy(weaponId);
		
		if (usingSpecial) {
			// set the special attack off now...
			player.getCombatDefinitions().setSpecialActivated(false);
			if (player.getCombatDefinitions().getSpecialEnergy() < energyRequired) {
				player.getPackets().sendGameMessage("You don't have enough special attack energy.");
				usingSpecial = false;
			}
		}
		// custom attack send
		if (usingSpecial) {
			special.fire(player, target, this, combatStyle);
			player.getCombatDefinitions().reduceSpecial(energyRequired);
		} else {
			// the hit (randomized)
			final double maxHit = getMaxHit(player, weaponId, combatStyle, 1D);
			
			// the damage
			int damage = randomizeHit(maxHit, getAttackBonus(player, weaponId, combatStyle, false), getDefenceBonus(target, weaponId, combatStyle));
			
			// the delay on the hit
			final int delay = weaponId == 10887 || (weaponName.toLowerCase().contains("maul") && !weaponName.startsWith("Granite")) ? 2 : 1;
			
			// animations
			final int animation = StaticCombatFormulae.getWeaponAttackEmote(weaponId, combatStyle);
			
			// constructs the hit and sets its delay
			final Hit hit = new Hit(player, damage, HitSplat.MELEE_DAMAGE).setMaxHit(maxHit);
			
			// handles the leeches aspect of the hit
			if (target.isPlayer()) {
				target.toPlayer().getManager().getPrayers().handlePrayerEffects(hit);
			} else {
				target.toNPC().handlePrayerEffects(hit);
			}
			
			// sends the hit after the delay
			applyHit(player, target, hit, weaponId, combatStyle, delay);
			
			// the player does the attack animation
			player.sendAnimation(animation);
		}
		return true;
	}
	
	@Override
	public double getAttackBonus(Player player, int weaponId, int combatStyle, boolean specialAttack) {
		return calculator.totalAggressiveBoost(player, weaponId, combatStyle, specialAttack);
	}
	
	@Override
	public double getDefenceBonus(Entity entity, int weaponId, int combatStyle) {
		return calculator.totalDefensiveBoost(entity, weaponId, combatStyle);
	}
	
	@Override
	public double getMaxHit(Player player, int weaponId, int combatStyle, double multiplier) {
		return calculator.maximumDamageAppendable(player, weaponId, combatStyle, multiplier);
	}
	
	@Override
	public void appendExperience(Player player, Entity target, Object... params) {
		int itemId = (int) params[0];
		int combatStyle = (int) params[1];
		int damage = (int) params[2];
		int blockEmote = StaticCombatFormulae.getDefenceEmote(target);
		
		if (damage > 0) {
			int xpSlot = StaticCombatFormulae.getXpStyle(itemId, combatStyle);
			// shared mode
			if (xpSlot == -1) {
				double combatXp = damage / 2.5;
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier(SkillConstants.ATTACK, combatXp / 3);
					player.getSkills().addExperienceNoMultiplier(SkillConstants.STRENGTH, combatXp / 3);
					player.getSkills().addExperienceNoMultiplier(SkillConstants.DEFENCE, combatXp / 3);
				} else {
					player.getSkills().addExperienceWithMultiplier(SkillConstants.ATTACK, combatXp / 3);
					player.getSkills().addExperienceWithMultiplier(SkillConstants.STRENGTH, combatXp / 3);
					player.getSkills().addExperienceWithMultiplier(SkillConstants.DEFENCE, combatXp / 3);
				}
			} else {
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier((short) xpSlot, damage / 2.5);
					player.getSkills().addExperienceNoMultiplier(SkillConstants.HITPOINTS, damage / 7.5);
				} else {
					player.getSkills().addExperienceWithMultiplier((short) xpSlot, damage / 2.5);
					player.getSkills().addExperienceWithMultiplier(SkillConstants.HITPOINTS, damage / 7.5);
				}
			}
		}
		// block emote
		target.sendAwaitedAnimation(blockEmote);
	}
}
