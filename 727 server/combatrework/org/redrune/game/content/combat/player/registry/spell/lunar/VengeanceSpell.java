package org.redrune.game.content.combat.player.registry.spell.lunar;

import java.util.concurrent.TimeUnit;

import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.registry.wrapper.context.MagicSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.RegularSpellEvent;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class VengeanceSpell implements RegularSpellEvent {
	
	@Override
	public int spellId() {
		return 37;
	}
	
	@Override
	public double exp() {
		return 112;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
	
	@Override
	public void cast(Player player, MagicSpellContext context) {
		Long lastTimeCast = player.getAttribute("LAST_VENG", -1L);
		if (player.getSkills().getLevel(MAGIC) < 94) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return;
		} else if (player.getSkills().getLevel(DEFENCE) < 40) {
			player.getPackets().sendGameMessage("You need a Defence level of 40 for this spell");
			return;
		} else if (lastTimeCast != null && lastTimeCast + 30_000 > System.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You must wait " + (TimeUnit.MILLISECONDS.toSeconds((lastTimeCast + 30_000) - System.currentTimeMillis())) + " more seconds to cast vengeance.");
			return;
		} else if (player.getAttribute("cast_veng", false)) {
			player.getPackets().sendGameMessage("You already have vengeance cast.");
			return;
		} else if (!CombatRegistry.checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 2, EARTH_RUNE, 10)) {
			return;
		}
		
		// visual effects
		player.setNextGraphics(new Graphics(726, 100, 0));
		player.setNextAnimation(new Animation(4410));
		player.getSkills().addXp(MAGIC, exp());
		
		// storing attributes
		player.putAttribute("cast_veng", true);
		player.putAttribute("LAST_VENG", System.currentTimeMillis());
	}
}
