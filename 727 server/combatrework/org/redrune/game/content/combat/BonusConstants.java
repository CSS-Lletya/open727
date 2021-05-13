package org.redrune.game.content.combat;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public interface BonusConstants {
	
	/**
	 * Attack bonuses
	 */
	int STAB_ATTACK = 0, SLASH_ATTACK = 1, CRUSH_ATTACK = 2, MAGIC_ATTACK = 3, RANGE_ATTACK = 4;
	
	/**
	 * Defence bonuses
	 */
	int STAB_DEFENCE = 5, SLASH_DEFENCE = 6, CRUSH_DEFENCE = 7, MAGIC_DEFENCE = 8, RANGE_DEFENCE = 9, SUMMONING_DEFENCE = 10;
	
	/**
	 * Absorb bonuses
	 */
	int ABSORB_MELEE_BONUS = 11, ABSORB_RANGE_BONUS = 13, ABSORB_MAGE_BONUS = 12;
	
	/**
	 * Strength bonuses
	 */
	int STRENGTH_BONUS = 14, RANGED_STRENGTH_BONUS = 15, MAGIC_DAMAGE_BONUS = 17, PRAYER_BONUS = 16;
	
}
