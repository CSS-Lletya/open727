package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;
import org.redrune.utility.tool.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class SmokeRushEvent implements CombatSpellEvent {
	
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
		return 385;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 150;
	}
	
	@Override
	public int spellId() {
		return 28;
	}
	
	@Override
	public double exp() {
		return 30;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 386, 18, 9, 52, 15, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this, null, null).consume(spellContext -> {
			if (spellContext.getHit().getDamage() != 0 && RandomFunction.percentageChance(10)) {
				Entity target = spellContext.getTarget();
				if (!target.getPoisonManager().isPoisoned()) {
					target.getPoisonManager().makePoisoned(20);
				}
			}
		});
	}
}
