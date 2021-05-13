package org.redrune.game.content.combat.player.registry.wrapper.magic;

import org.redrune.game.content.combat.MagicConstants.MagicBook;
import org.redrune.game.content.combat.player.registry.CombatRegistryEvent;
import org.redrune.game.content.combat.player.registry.wrapper.context.MagicSpellContext;

import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public interface MagicSpellEvent<T extends MagicSpellContext> extends CombatRegistryEvent {
	
	/**
	 * The id of the spell
	 */
	int spellId();
	
	/**
	 * The base experience of the spell
	 */
	double exp();
	
	/**
	 * The book the spell is on
	 */
	MagicBook book();
	
	/**
	 * Casts the magic spell
	 *
	 * @param player
	 * 		The player
	 * @param context
	 * 		The context
	 */
	void cast(Player player, T context);
	
}
