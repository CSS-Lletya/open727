package org.redrune.game.content.combat.player.registry.spell.modern.curses;

import java.util.concurrent.TimeUnit;

import org.redrune.game.content.combat.MagicConstants.MagicBook;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;

import com.rs.game.Entity;
import com.rs.game.ProjectileManager;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class BindEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 710;
	}
	
	@Override
	public int hitGfx() {
		return -1;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 20;
	}
	
	@Override
	public int spellId() {
		return 36;
	}
	
	@Override
	public double exp() {
		return 60.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		final Entity target = context.getTarget();
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, target, 178, 18, 9, 52, 15, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// we send the graphics here because we don't always freeze them
			context.getTarget().sendGraphics(181);
			// only freeze the player if they are unfreezeable when the spell is cast.
			context.getTarget().freeze(player, TimeUnit.SECONDS.toMillis(5), "You have been frozen!");
		}, null);
	}
}
