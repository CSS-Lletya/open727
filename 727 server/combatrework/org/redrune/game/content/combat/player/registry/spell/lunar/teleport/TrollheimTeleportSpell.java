package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class TrollheimTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 92;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 3, LAW_RUNE, 3, WATER_RUNE, 10);
	}
	
	@Override
	public Location destination() {
		return new Location(2807, 3678, 0);
	}
	
	@Override
	public int spellId() {
		return 75;
	}
	
	@Override
	public double exp() {
		return 101;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
