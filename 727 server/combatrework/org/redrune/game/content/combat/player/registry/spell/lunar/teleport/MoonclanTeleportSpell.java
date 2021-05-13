package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class MoonclanTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 69;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 1, EARTH_RUNE, 2);
	}
	
	@Override
	public Location destination() {
		return Location.create(2100, 3915, 0);
	}
	
	@Override
	public int spellId() {
		return 43;
	}
	
	@Override
	public double exp() {
		return 66;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
