package org.redrune.game.content.combat;

import skills.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public interface MagicConstants extends SkillConstants {
	
	/**
	 * The ids of runes
	 */
	int AIR_RUNE = 556, WATER_RUNE = 555, EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558, BODY_RUNE = 559, NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560, BLOOD_RUNE = 565, SOUL_RUNE = 566, ASTRAL_RUNE = 9075, LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695, DUST_RUNE = 4696, SMOKE_RUNE = 4697, MUD_RUNE = 4698, LAVA_RUNE = 4699, ARMADYL_RUNE = 21773;
	
	enum MagicBook {
		REGULAR(192),
		ANCIENTS(193),
		LUNARS(430);
		
		/**
		 * The id of the interface
		 */
		@SuppressWarnings("unused")
		private final int interfaceId;
		
		MagicBook(int interfaceId) {
			this.interfaceId = interfaceId;
		}
	}
}
