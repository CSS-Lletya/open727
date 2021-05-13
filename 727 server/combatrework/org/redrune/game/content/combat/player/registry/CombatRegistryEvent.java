package org.redrune.game.content.combat.player.registry;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public interface CombatRegistryEvent {
	
	/**
	 * Converts a varargs parameter to the String[] array
	 *
	 * @param varArgs
	 * 		The var args
	 */
	default String[] arguments(String... varArgs) {
		return varArgs;
	}
	
	/**
	 * Converts a varargs parameter to the String[] array
	 *
	 * @param varArgs
	 * 		The var args
	 */
	default int[] arguments(int... varArgs) {
		return varArgs;
	}
}
