package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.registry.wrapper.context.MagicSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.RegularSpellEvent;
import org.redrune.game.node.entity.player.Player;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class VengeanceGroupSpell implements RegularSpellEvent {
	
	@Override
	public int spellId() {
		return 74;
	}
	
	@Override
	public double exp() {
		return 120;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
	
	@Override
	public void cast(Player player, MagicSpellContext context) {
		Long lastTimeCast = player.getAttribute("LAST_VENG", -1L);
		if (player.getSkills().getLevel(MAGIC) < 94) {
			player.getTransmitter().sendMessage("Your Magic level is not high enough for this spell.");
			return;
		} else if (player.getSkills().getLevel(DEFENCE) < 40) {
			player.getTransmitter().sendMessage("You need a Defence level of 40 for this spell");
			return;
		} else if (lastTimeCast != null && lastTimeCast + 30_000 > System.currentTimeMillis()) {
			player.getTransmitter().sendMessage("You must wait " + (TimeUnit.MILLISECONDS.toSeconds((lastTimeCast + 30_000) - System.currentTimeMillis())) + " more seconds to cast vengeance.");
			return;
		} else if (!CombatRegistry.checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 3, EARTH_RUNE, 11)) {
			return;
		}
		// the amount of people the spell affected
		int affectedPeopleCount = 0;
		
		// loop through the local players
		for (Player other : player.getRegion().getPlayers()) {
			// skip the unavailable players first
			if (other == null || other == player || !other.isRenderable() || other.isDead() || other.isDying() || !other.getLocation().withinDistance(player.getLocation(), 4)) {
				continue;
			}
			// lets the caster know the other person needs to have accept aid on
			if (!other.getVariables().isAcceptingAid()) {
				player.getTransmitter().sendMessage(other.getDetails().getDisplayName() + " is not accepting aid.");
				continue;
			}
			// visual
			other.sendGraphics(725, 100, 0);
			// attributes
			other.putAttribute("cast_veng", true);
			other.putAttribute("LAST_VENG", System.currentTimeMillis());
			// increment total amount
			affectedPeopleCount++;
		}
		
		// visuals
		player.sendAnimation(4411);
		player.getSkills().addExperienceWithMultiplier(MAGIC, exp());
		player.getTransmitter().sendMessage("The spell affected " + affectedPeopleCount + " nearby people.");
		
		// store attribute information
		player.putAttribute("LAST_VENG", System.currentTimeMillis());
	}
}
