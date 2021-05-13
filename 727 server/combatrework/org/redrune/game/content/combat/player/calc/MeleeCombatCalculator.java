package org.redrune.game.content.combat.player.calc;

import org.redrune.game.content.combat.BonusConstants;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatTypeCalculator;

import com.rs.game.Entity;
import com.rs.game.player.Player;

import npc.NPC;
import skills.SkillConstants;

/**
 * This class handles all of the melee combat formula calculations
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public final class MeleeCombatCalculator implements CombatTypeCalculator {
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		final int weaponId = (int) params[0];
		final int styleParam = (int) params[1];
		final boolean specialAttack = (boolean) params[2];
		
		final int style = StaticCombatFormulae.getMeleeBonusStyle(weaponId, styleParam);
		int baseLevel = player.getSkills().getLevelForXp(ATTACK);
		int weaponRequirement = player.getEquipment().getWeaponRequirement(ATTACK);
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		
		final int level = player.getSkills().getLevel(ATTACK);
		final double prayer = player.getManager().getPrayers().getBasePrayerBoost(ATTACK);
		double additional = 1.0; // Black mask/slayer helmet/salve/...
		// we add the spec modifier
		if (specialAttack) {
			additional += StaticCombatFormulae.getSpecialAccuracyModifier(weaponId);
		}
		final int styleBonus = styleParam == 0 ? 3 : styleParam == 2 ? 1 : 0;
		int bonus = player.getEquipment().getBonus(style);
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		return (int) Math.floor((((effective + 8) * (bonus + 64)) / 10) * 1.10);
	}
	
	@Override
	public double totalDefensiveBoost(Entity entity, Object... params) {
		// the weapon id of the player
		final int weaponId = (int) params[0];
		// the attack style of the player
		final int attackStyle = (int) params[1];
		// find the combat style we're on for the selected type
		int meleeBonusStyle = StaticCombatFormulae.getMeleeBonusStyle(weaponId, attackStyle);
		// the bonus index
		final int bonusIndex = StaticCombatFormulae.getMeleeDefenceBonusIndex(meleeBonusStyle);
		// the attack style of the receiver, npcs have default stab attack.
		int targetStyle = BonusConstants.STAB_ATTACK;
		// the defence level
		int defenceLevel;
		// the prayer boost
		double prayer;
		int bonus;
		if (entity.isPlayer()) {
			Player player = entity.toPlayer();
			targetStyle = player.getCombatDefinitions().getAttackStyle();
			defenceLevel = player.getSkills().getLevel(DEFENCE);
			prayer = player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.DEFENCE);
			bonus = player.getEquipment().getBonus(bonusIndex);
		} else {
			NPC npc = entity.toNPC();
			prayer = 1.0;
			defenceLevel = npc.getCombatLevel() / 2;
			bonus = npc.getBonus(bonusIndex);
		}
		// calculate the bonus of the style we're on after all the setting is done
		int styleBonus = targetStyle == 2 ? 1 : targetStyle == 3 ? 3 : 0;
		double effective = Math.floor((defenceLevel * prayer) + styleBonus);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double maximumDamageAppendable(Player player, Object... params) {
		final int weaponId = (int) params[0];
		final int attackStyle = (int) params[1];
		final double multiplier = (double) params[2];
		
		double strengthLvl = player.getSkills().getLevel(STRENGTH);
		int xpStyle = StaticCombatFormulae.getXpStyle(weaponId, attackStyle);
		double styleBonus = xpStyle == STRENGTH ? 3 : xpStyle == -1 ? 1 : 0;
		// if we use the berserk effect
		boolean berserk = player.getEquipment().getItemSlot(SLOT_AMULET) == 11128 && (weaponId == 6528 || weaponId == 6527 || weaponId == 6523 || weaponId == 6526);
		double otherBonus = berserk ? 1.20 : 1;
		double effectiveStrength = 8 + Math.floor((strengthLvl * player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.STRENGTH)) + styleBonus);
		if (StaticCombatFormulae.fullVoidEquipped(player, 11665, 11676)) {
			effectiveStrength = Math.floor(effectiveStrength * 1.1);
		}
		// if we have the dharoks armour set equipped
		if (StaticCombatFormulae.armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok", "dharok", "dharok")) {
			// Example: 99 LP out of 999 LP total. 99 / 999 = 0.1; 2  - 0.1 = 1.9 for a 90% damage boost.
			double dharokMultiplier = 2 - ((double) player.getHitpoints() / (double) player.getMaxHitpoints());
			// multiplying the
			otherBonus *= dharokMultiplier;
		}
		double strengthBonus = player.getEquipment().getBonus(STRENGTH_BONUS);
		double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
		double max = Math.floor(baseDamage * multiplier * otherBonus);
		return (int) max;
	}
}
