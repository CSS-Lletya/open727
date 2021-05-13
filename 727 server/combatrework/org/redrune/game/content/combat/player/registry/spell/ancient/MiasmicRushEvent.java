package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class MiasmicRushEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 10513;
	}
	
	@Override
	public int hitGfx() {
		return 1847;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 200;
	}
	
	@Override
	public int spellId() {
		return 36;
	}
	
	@Override
	public double exp() {
		return 35;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		final Entity target = context.getTarget();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, target, 1846, 18, 9, 52, 15, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this, null, () -> {
			if (!target.isPlayer() || target.getAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
				return;
			}
			Player p = target.toPlayer();
			p.getTransmitter().sendMessage("You feel slowed down.");
			target.putAttribute(AttributeKey.MIASMIC_IMMUNITY, true);
			target.putAttribute(AttributeKey.MIASMIC_EFFECT, true);
			SystemManager.getScheduler().schedule(new ScheduledTask(1, 35) {
				@Override
				public void run() {
					if (getTicksPassed() == 20) {
						target.removeAttribute(AttributeKey.MIASMIC_EFFECT);
					} else if (getTicksPassed() == 35) {
						target.removeAttribute(AttributeKey.MIASMIC_IMMUNITY);
					}
				}
			});
		});
	}
}
