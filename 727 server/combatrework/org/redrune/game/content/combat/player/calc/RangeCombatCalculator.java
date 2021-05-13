package org.redrune.game.content.combat.player.calc;

import org.redrune.game.content.combat.BonusConstants;
import org.redrune.game.content.combat.EquipConstants;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatTypeCalculator;

import com.rs.game.Entity;
import com.rs.game.player.Player;

import npc.NPC;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class RangeCombatCalculator implements CombatTypeCalculator {
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		final int attackStyle = (int) params[0];
		final boolean specialAttack = (boolean) params[1];
		final int weaponId = player.getEquipment().getWeaponId();
		int baseLevel = player.getSkills().getLevelForXp(RANGE);
		int weaponRequirement = player.getEquipment().getWeaponRequirement(RANGE);
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		int level = player.getSkills().getLevel(RANGE);
		double prayer = player.getManager().getPrayers().getBasePrayerBoost(RANGE);
		double additional = 1.0; // Slayer helmet/salve/...
		if (specialAttack) {
			additional += StaticCombatFormulae.getSpecialAccuracyModifier(weaponId == -1 ? player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS) : weaponId);
		}
		int styleBonus = 0;
		if (attackStyle == 0) {
			styleBonus = 3;
		}
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		int bonus = player.getEquipment().getBonus(BonusConstants.RANGE_ATTACK);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double totalDefensiveBoost(Entity entity, Object... params) {
		int style = (int) params[0];
		int styleBonus = (style == 2 ? 1 : style == 3 ? 3 : 0);
		int defenceLevel;
		int bonus;
		double prayer;
		if (entity.isPlayer()) {
			Player player = entity.toPlayer();
			defenceLevel = player.getSkills().getLevel(DEFENCE);
			prayer = player.getManager().getPrayers().getBasePrayerBoost(DEFENCE);
			bonus = player.getEquipment().getBonus(RANGE_DEFENCE);
		} else {
			NPC npc = entity.toNPC();
			defenceLevel = npc.getCombatLevel() / 2;
			prayer = 1.0;
			bonus = npc.getBonus(RANGE_DEFENCE);
		}
		double effective = Math.floor((defenceLevel * prayer) + styleBonus);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double maximumDamageAppendable(Player player, Object... params) {
		final int weaponId = (int) params[0];
		final int attackStyle = (int) params[1];
		final double multiplier = (double) params[2];
		final boolean voidEquipped = StaticCombatFormulae.fullVoidEquipped(player, 11664, 11675);
		final boolean pernixEquipped = StaticCombatFormulae.armourSetEquipped(player, new int[] { EquipConstants.SLOT_HAT, EquipConstants.SLOT_CHEST, EquipConstants.SLOT_LEGS }, "pernix", "pernix", "pernix");
		
		int level = player.getSkills().getLevel(RANGE);
		int bonus = player.getEquipment().getBonus(BonusConstants.RANGED_STRENGTH_BONUS);
		double prayer = player.getManager().getPrayers().getBasePrayerBoost(PRAYER);
		
		double cumulativeStr = Math.floor(level * prayer);
		double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
		cumulativeStr += (8 + styleBonus);
		if (voidEquipped) {
			cumulativeStr *= StaticCombatFormulae.fullVoidEquipped(player, 11675) ? 1.125 : 1.1;
		}
		if (pernixEquipped) {
			cumulativeStr += 150;
		}
		final double effective = (((14 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865))) / 10 + 1) * multiplier;
		double maxHit = (Math.round(effective) * 10);
		return maxHit;
		
/*		final int weaponId = (int) params[0];
		final int attackStyle = (int) params[1];
		final double multiplier = (double) params[2];
		
		double rangedLvl = player.getSkills().getLevel(SkillConstants.RANGE);
		double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
		double effectiveStrength = Math.floor(rangedLvl + player.getManager().getPrayers().getBasePrayerBoost(SkillConstants.PRAYER)) + styleBonus;
		
		// void range equipped?
		if (StaticCombatFormulae.fullVoidEquipped(player, 11664, 11675)) {
			effectiveStrength += Math.floor((player.getSkills().getLevelForXp(SkillConstants.RANGE) / 5) + 1.6);
		}
		
		double strengthBonus = player.getEquipment().getBonus(BonusConstants.RANGED_STRENGTH_BONUS);
		
		String weaponName = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		if (weaponName.toLowerCase().contains("crystal bow")) {
			strengthBonus = 0;
		}
		double baseDamage = 5 + (((effectiveStrength + 8) * (strengthBonus + 64)) / 64);
		int maxHit = (int) Math.floor(baseDamage * multiplier);
		
		// full pernix equipped?
		if (StaticCombatFormulae.armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_CHEST, SLOT_LEGS }, "pernix", "pernix", "pernix")) {
			maxHit += 150;
		}
		return maxHit;*/
	}
}
