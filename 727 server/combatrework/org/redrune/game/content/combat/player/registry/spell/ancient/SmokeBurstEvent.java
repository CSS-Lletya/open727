package org.redrune.game.content.combat.player.registry.spell.ancient;

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
public class SmokeBurstEvent implements CombatSpellEvent {
	
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
		return 389;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 190;
	}
	
	@Override
	public int spellId() {
		return 30;
	}
	
	@Override
	public double exp() {
		return 60;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		context.getSwing().sendMultiSpell(player, context.getTarget(), this, null, null).forEach(spellContext -> {
			if (spellContext.getHit().getDamage() != 0 && RandomFunction.percentageChance(10)) {
				Entity target = spellContext.getTarget();
				if (!target.getPoisonManager().isPoisoned()) {
					target.getPoisonManager().makePoisoned(20);
				}
			}
		});
	}
}
