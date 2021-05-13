package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/28/2017
 */
public class IceBarrageEvent implements CombatSpellEvent {
	
	@Override
	public int spellId() {
		return 23;
	}
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1979;
	}
	
	// we don't store a static gfx because it is modifiable
	@Override
	public int hitGfx() {
		return -1;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 300;
	}
	
	@Override
	public double exp() {
		return 52;
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
		context.getSwing().sendMultiSpell(player, context.getTarget(), this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// only freeze the player if they are unfreezeable when the spell is cast.
			context.getTarget().freeze(player, TimeUnit.SECONDS.toMillis(20), "You have been frozen!");
		}, () -> {
			int gfx;
			int height;
			if (target.getSize() >= 2 || freezeDelayed || frozenTarget) {
				gfx = 1677;
				height = 100;
			} else {
				gfx = 369;
				height = 0;
			}
			target.sendGraphics(gfx, height, 0);
		});
	}
}
