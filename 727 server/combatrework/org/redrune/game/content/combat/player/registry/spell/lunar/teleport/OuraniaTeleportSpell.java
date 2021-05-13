package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class OuraniaTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 71;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 1, ASTRAL_RUNE, 2, EARTH_RUNE, 6);
	}
	
	@Override
	public Location destination() {
		return Location.create(2469, 3247, 0);
	}
	
	@Override
	public int spellId() {
		return 54;
	}
	
	@Override
	public double exp() {
		return 69;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
