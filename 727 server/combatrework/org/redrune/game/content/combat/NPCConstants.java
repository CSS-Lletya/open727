package org.redrune.game.content.combat;

import java.util.concurrent.TimeUnit;

import static org.redrune.game.content.combat.player.CombatType.*;

/**
 * Constants for npcs
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public interface NPCConstants {
	
	/**
	 * The melee combat style
	 */
	int MELEE_COMBAT_STYLE = MELEE.ordinal();
	
	/**
	 * The range combat style
	 */
	int RANGE_COMBAT_STYLE = RANGE.ordinal();
	
	/**
	 * The magic combat style
	 */
	int MAGIC_COMBAT_STYLE = MAGIC.ordinal();
	
	/**
	 * The passive aggressive type
	 */
	int PASSIVE_AGGRESSIVE = 0;
	
	/**
	 * The force aggressive type
	 */
	int FORCE_AGGRESSIVE = 1;
	
	/**
	 * The amount of time a player has to spend in a region for npcs who are aggressive to it to ignore it
	 */
	long UNAGGRESSIVE_REGION_TIME = TimeUnit.MINUTES.toMillis(10);
	
	/**
	 * The walk types
	 */
	int NO_WALK = 0x0, NORMAL_WALK = 0x2, WATER_WALK = 0x4, FLY_WALK = 0x8;
	
	/**
	 * Gets the changed walk type
	 *
	 * @param id
	 * 		The id of the npc
	 */
	static int getChangedWalkType(int id) {
		switch (id) {
			default:
				return -1;
			case 1597:
				return NO_WALK;
			case 3385:
				return NO_WALK;
			case 4361:
				return NO_WALK;
			case 11248:
				return NORMAL_WALK;
			case 2024:
				return NO_WALK;
			case 1918:
				return NO_WALK;
			case 1334:
				return NO_WALK;
			case 1411:
				return NO_WALK;
			case 33:
				return NO_WALK;
			case 1288:
				return NO_WALK;
			case 945:
				return NO_WALK;
			case 2998:
				return NO_WALK;
			case 1699:
				return NO_WALK;
			case 1658:
				return NO_WALK;
			case 550:
				return NO_WALK;
			case 549:
				return NO_WALK;
			case 1866:
				return NO_WALK;
			case 554:
				return NO_WALK;
			case 5112:
				return NO_WALK;
			case 278:
				return NO_WALK;
			case 519:
				return NO_WALK;
			case 6370:
				return NO_WALK;
			case 211:
				return NO_WALK;
			case 3001:
				return NO_WALK;
			case 4516:
				return NO_WALK;
			case 2258:
				return NO_WALK;
			case 2892:
				return NO_WALK;
			case 4293:
				return NO_WALK;
			case 2894:
				return NO_WALK;
			case 2896:
				return NO_WALK;
			case 2328:
				return NO_WALK;
			case 4288:
				return NO_WALK;
			case 682:
				return NO_WALK;
			case 1303:
				return NO_WALK;
			case 608:
				return NO_WALK;
			case 588:
				return NO_WALK;
			case 1778:
				return NO_WALK;
			case 6970:
				return NO_WALK;
			case 599:
				return NO_WALK;
			case 6539:
				return NO_WALK;
			case 6537:
				return NO_WALK;
			case 4653:
				return NO_WALK;
			case 14332:
				return NORMAL_WALK;
			case 961:
				return NO_WALK;
			case 4247:
				return NO_WALK;
			case 4375:
				return NO_WALK;
			case 2824:
				return NO_WALK;
			case 946:
				return NO_WALK;
			case 947:
				return NO_WALK;
			case 949:
				return NO_WALK;
		}
	}
}
