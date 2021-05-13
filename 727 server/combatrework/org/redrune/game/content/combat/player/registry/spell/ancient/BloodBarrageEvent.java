package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.game.content.combat.MagicConstants.MagicBook;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;

import com.rs.game.Entity;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class BloodBarrageEvent implements CombatSpellEvent {
	
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
		return 377;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 290;
	}
	
	@Override
	public int spellId() {
		return 27;
	}
	
	@Override
	public double exp() {
		return 51;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		context.getSwing().sendMultiSpell(player, context.getTarget(), this, null, null).forEach(spellContext -> {
			if (spellContext.getHit().getDamage() != 0) {
				player.getTransmitter().sendMessage("You drain some of your opponents' life points.");
				player.heal(spellContext.getHit().getDamage() / 4);
			}
		});
	}
}
