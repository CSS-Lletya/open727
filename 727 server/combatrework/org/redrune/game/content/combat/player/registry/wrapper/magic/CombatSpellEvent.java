package org.redrune.game.content.combat.player.registry.wrapper.magic;

import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;

import com.rs.game.Entity;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public interface CombatSpellEvent extends MagicSpellEvent<CombatSpellContext> {
	
	/**
	 * The delay on the spell, only used to find the next combat swing time
	 */
	int delay(Player player);
	
	/**
	 * The animation id for the spell
	 */
	int animationId();
	
	/**
	 * The id of the graphics applied when the hit lands
	 */
	int hitGfx();
	
	/**
	 * The base damage of the spell
	 *
	 * @param player
	 * 		The player casting
	 * @param target
	 * 		The target of the spell
	 */
	int maxHit(Player player, Entity target);
	
	/**
	 * The minimum damage the spell will do. If the spell splashes it must do atleast this damage.
	 *
	 * @param player
	 * 		The player
	 */
	default int minimumHit(Player player) {
		return -1;
	}
	
	/**
	 * The default height of the hit land gfx
	 */
	default int gfxHeight() {
		return 96;
	}
	
}
