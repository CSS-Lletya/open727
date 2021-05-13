package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/29/2017
 */
public class IceBlitzEvent implements CombatSpellEvent {
	
	@Override
	public int spellId() {
		return 21;
	}
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1978;
	}
	
	@Override
	public int hitGfx() {
		return 367;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 260;
	}
	
	@Override
	public double exp() {
		return 46;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		final Entity target = context.getTarget();
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, target, 368, 18, 9, 52, 15, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// only freeze the player if they are unfreezeable when the spell is cast.
			context.getTarget().freeze(player, TimeUnit.SECONDS.toMillis(15), "You have been frozen!");
		}, null);
	}
}
