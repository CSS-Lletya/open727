package org.redrune.game.content.combat.player.registry.wrapper;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.registry.CombatRegistryEvent;
import org.redrune.game.content.combat.player.swing.RangeCombatSwing;

import com.rs.game.Entity;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public interface BowFireEvent extends CombatRegistryEvent {
	
	/**
	 * The names of the bows that can use this event
	 */
	String[] bowNames();
	
	/**
	 * Fires the bow
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id used
	 * @param ammoId
	 * 		The id of the ammo
	 */
	void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId);
	
	/**
	 * Sends the damage to the target
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id
	 */
	default CombatSwingDetail sendDamage(Player attacker, Entity target, CombatTypeSwing swing, int weaponId, double... modifier) {
		// sends the damage
		return RangeCombatSwing.sendDamage(attacker, target, (RangeCombatSwing) swing, weaponId, modifier.length != 0 ? modifier[0] : 1D, false);
	}
	
	/**
	 * Sends the damage to the target
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id
	 */
	default CombatSwingDetail sendDamage(Player attacker, Entity target, CombatTypeSwing swing, int weaponId, int damage) {
		// sends the damage
		return RangeCombatSwing.sendDamage(attacker, target, (RangeCombatSwing) swing, weaponId, damage);
	}
	
}
