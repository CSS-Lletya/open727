package org.redrune.game.content.combat.player.registry.spell.modern.curses;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class StunEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 729;
	}
	
	@Override
	public int hitGfx() {
		return 107;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 10;
	}
	
	@Override
	public int spellId() {
		return 82;
	}
	
	@Override
	public double exp() {
		return 90;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		player.sendGraphics(173);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 174, 30, 26, 52, 0, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this, () -> {
			Entity target = context.getTarget();
			if (target.isPlayer()) {
				target.toPlayer().getSkills().drainLevel(SkillConstants.ATTACK, 0.10, 0.10);
			}
		}, null);
	}
}
