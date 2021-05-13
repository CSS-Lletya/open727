package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class NorthArdougneTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 76;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 1, WATER_RUNE, 5);
	}
	
	@Override
	public Location destination() {
		return new Location(2614, 3347, 0);
	}
	
	@Override
	public int spellId() {
		return 69;
	}
	
	@Override
	public double exp() {
		return 76;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
