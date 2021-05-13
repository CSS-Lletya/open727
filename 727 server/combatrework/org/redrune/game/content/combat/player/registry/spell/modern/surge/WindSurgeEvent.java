package org.redrune.game.content.combat.player.registry.spell.modern.surge;

import org.redrune.game.content.combat.MagicConstants.MagicBook;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;

import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.ProjectileManager;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class WindSurgeEvent implements CombatSpellEvent{
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 10546;
	}
	
	@Override
	public int hitGfx() {
		return 2700;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 220;
	}
	
	@Override
	public int spellId() {
		return 84;
	}
	
	@Override
	public double exp() {
		return 80;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		player.setNextGraphics(new Graphics(457));
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 462, 30, 26, 52, 0, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this);
	}
}
