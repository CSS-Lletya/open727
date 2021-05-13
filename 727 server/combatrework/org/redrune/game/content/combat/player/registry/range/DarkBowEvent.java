package org.redrune.game.content.combat.player.registry.range;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.registry.wrapper.BowFireEvent;
import org.redrune.game.content.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.Projectile;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class DarkBowEvent implements BowFireEvent {
	
	@Override
	public String[] bowNames() {
		return arguments("dark bow");
	}
	
	@Override
	public void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId) {
		int speed = 46 + (attacker.getLocation().getDistance(target.getLocation()) * 5);
		int speed2 = 55 + (attacker.getLocation().getDistance(target.getLocation()) * 10);
		attacker.sendGraphics(StaticCombatFormulae.getArrowThrowGfxId(ammoId), 100, 0);
		
		for (int i = 1; i <= 2; i++) {
			sendDamage(attacker, target, swing, weaponId);
			Projectile projectile = new Projectile(attacker, target, StaticCombatFormulae.getArrowProjectileGfxId(weaponId, ammoId), 41, 35, 41, i == 1 ? speed : speed2, i == 1 ? 5 : 25, 0);
			ProjectileManager.sendProjectile(projectile);
			swing.dropAmmo(attacker, target.getLocation(), EquipConstants.SLOT_ARROWS, ammoId, false);
		}
	}
}
