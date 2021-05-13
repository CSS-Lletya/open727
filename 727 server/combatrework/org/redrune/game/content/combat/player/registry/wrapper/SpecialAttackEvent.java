package org.redrune.game.content.combat.player.registry.wrapper;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.registry.CombatRegistryEvent;

import com.rs.game.Entity;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public interface SpecialAttackEvent extends CombatRegistryEvent {
	
	/**
	 * The names of weapons that are applied to this special attack
	 */
	String[] applicableNames();
	
	/**
	 * The max hit multiplier
	 */
	double multiplier();
	
	/**
	 * Sends the special attack to the target
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing handler
	 */
	void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle);
	
	/**
	 * If the special attack is instant
	 */
	default boolean isInstant() {
		return false;
	}
	
	/**
	 * If the special attack requires you to be in a fight.
	 */
	default boolean requiresFight() {
		return true;
	}
}
