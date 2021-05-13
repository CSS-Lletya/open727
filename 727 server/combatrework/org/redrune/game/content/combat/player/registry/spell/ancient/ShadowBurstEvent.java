package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class ShadowBurstEvent implements CombatSpellEvent {
	
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
		return 382;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 200;
	}
	
	@Override
	public int spellId() {
		return 34;
	}
	
	@Override
	public double exp() {
		return 37;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		context.getSwing().sendMultiSpell(player, context.getTarget(), this, null, null).forEach(spellDetail -> {
			if (spellDetail.getHit().getDamage() != 0) {
				if (spellDetail.getTarget().isPlayer()) {
					spellDetail.getTarget().toPlayer().getSkills().drainLevel(SkillConstants.ATTACK, 0.05, 0.10);
				}
			}
		});
	}
	
	
}
