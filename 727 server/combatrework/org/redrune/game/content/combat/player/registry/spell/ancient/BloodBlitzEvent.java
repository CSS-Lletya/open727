package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class BloodBlitzEvent implements CombatSpellEvent {
	
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
		return 375;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 250;
	}
	
	@Override
	public int spellId() {
		return 25;
	}
	
	@Override
	public double exp() {
		return 45;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 374, 18, 9, 52, 15, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this, null, null).consume(detail -> {
			if (detail.getHit().getDamage() != 0) {
				player.getTransmitter().sendMessage("You drain some of your opponents' life points.");
				player.heal(detail.getHit().getDamage() / 4);
			}
		});
	}
}
