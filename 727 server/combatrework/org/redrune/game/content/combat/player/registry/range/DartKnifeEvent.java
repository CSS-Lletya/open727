package org.redrune.game.content.combat.player.registry.range;

import org.redrune.game.content.ProjectileManager;
import org.redrune.cache.parse.ItemDefinitionParser;
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
public class DartKnifeEvent implements BowFireEvent {
	
	@Override
	public String[] bowNames() {
		return arguments("* dart", "* knife");
	}
	
	@Override
	public void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId) {
		sendDamage(attacker, target, swing, weaponId);
		
		final String name = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		
		// diff. projectile types
		if (name.contains("knife")) {
			int speed = 46 + (ProjectileManager.getLocation(attacker).getDistance(target.getLocation()) * 5);
			ProjectileManager.sendProjectile(new Projectile(attacker, target, StaticCombatFormulae.getKnifeThrowGfxId(weaponId), 30, 26, 32, speed, 15, 1));
		} else if (name.contains("dart")) {
			ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(attacker, target, StaticCombatFormulae.getKnifeThrowGfxId(weaponId), 40, 36, 32, 15, 0));
		}
		
		swing.dropAmmo(attacker, target.getLocation(), EquipConstants.SLOT_WEAPON, weaponId, false);
	}
}
