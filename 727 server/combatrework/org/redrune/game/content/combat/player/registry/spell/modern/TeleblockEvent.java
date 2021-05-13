package org.redrune.game.content.combat.player.registry.spell.modern;

import org.redrune.game.content.combat.MagicConstants.MagicBook;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.utility.AttributeKey;

import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.ProjectileManager;
import com.rs.game.player.Player;
import com.rs.game.player.Prayer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class TeleblockEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 10503;
	}
	
	@Override
	public int hitGfx() {
		return -1;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 30;
	}
	
	@Override
	public int spellId() {
		return 86;
	}
	
	@Override
	public double exp() {
		return 80;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		Entity target = context.getTarget();
		Player targetPlayer = target.toPlayer();
		if (target.isPlayer() && targetPlayer.getVariables().getAttribute(AttributeKey.TELEBLOCKED_UNTIL, -1L) >= System.currentTimeMillis()) {
			player.getPackets().sendGameMessage("That player is already affected by this spell.");
			return;
		}
		player.setNextGraphics(new Graphics(1841));
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 1842, 30, 26, 52, 0, 0));
		
		context.getSwing().sendSpell(player, context.getTarget(), this, null, () -> {
			if (target.isPlayer() && targetPlayer.getVariables().getAttribute(AttributeKey.TELEBLOCKED_UNTIL, -1L) >= System.currentTimeMillis()) {
				return;
			}
			if (target.isPlayer()) {
				boolean protecting = targetPlayer.getManager().getPrayers().prayerOn(Prayer.PROTECT_FROM_MAGIC) || targetPlayer.getManager().getPrayers().prayerOn(Prayer.DEFLECT_MAGIC);
				targetPlayer.getVariables().putAttribute(AttributeKey.TELEBLOCKED_UNTIL, System.currentTimeMillis() + (protecting ? 150_000 : 300_000));
			}
			target.sendGraphics(1843);
		});
	}
}
