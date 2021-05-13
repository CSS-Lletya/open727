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
 * @since 8/1/2017
 */
public class IceBurstEvent  implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1979;
	}
	
	@Override
	public int hitGfx() {
		return 363;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 220;
	}
	
	@Override
	public int spellId() {
		return 22;
	}
	
	@Override
	public double exp() {
		return 46;
	}
	
	@Override
	public int gfxHeight() {
		return 0;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		// storing vars before spell is cast
		final Entity target = context.getTarget();
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, target, 366, 43, 21, 52, 15, 0));
		context.getSwing().sendMultiSpell(player, context.getTarget(), this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// only freeze the player if they are unfreezeable when the spell is cast.
			context.getTarget().freeze(player, TimeUnit.SECONDS.toMillis(10), "You have been frozen!");
		}, null);
	
	}
}
