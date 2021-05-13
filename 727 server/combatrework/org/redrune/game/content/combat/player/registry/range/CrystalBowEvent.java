package org.redrune.game.content.combat.player.registry.range;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.combat.player.registry.wrapper.BowFireEvent;
import org.redrune.game.node.entity.Entity;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class CrystalBowEvent implements BowFireEvent {
	
	@Override
	public String[] bowNames() {
		return arguments("crystal bow");
	}
	
	@Override
	public void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId) {
		sendDamage(attacker, target, swing, weaponId);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(attacker, target, 249, 40, 30, 41, 15, 0));
	}
}
