package org.redrune.game.content.combat.player.registry.spell.modern;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.MagicConstants.MagicBook;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;

import com.rs.game.Entity;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class WindRushEvent implements CombatSpellEvent {
	
	@Override
	public int spellId() {
		return 98;
	}
	
	@Override
	public int delay(Player player) {
		return 3;
	}
	
	@Override
	public int animationId() {
		return 14221;
	}
	
	@Override
	public int hitGfx() {
		return 2700;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 10;
	}
	
	@Override
	public double exp() {
		return 2.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 2699, 30, 26, 52, 0, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this);
	}
}
