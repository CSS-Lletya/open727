package org.redrune.game.content.combat.player.registry.spell.modern.curses;

import org.redrune.core.system.SystemManager;
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
public class ChargeEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 0;
	}
	
	@Override
	public int animationId() {
		return 0;
	}
	
	@Override
	public int hitGfx() {
		return 0;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 0;
	}
	
	@Override
	public int spellId() {
		return 83;
	}
	
	@Override
	public double exp() {
		return 180;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		player.sendAnimation(811);
		player.sendGraphics(6);
		player.getVariables().putAttribute(AttributeKey.GOD_CHARGED, SystemManager.getUpdateWorker().getTicks() + 600);
	}
}
