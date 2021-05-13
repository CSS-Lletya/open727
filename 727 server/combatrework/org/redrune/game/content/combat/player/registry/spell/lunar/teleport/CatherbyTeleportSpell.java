package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class CatherbyTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 87;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 3, LAW_RUNE, 3, WATER_RUNE, 10);
	}
	
	@Override
	public Location destination() {
		return new Location(2804, 3433, 0);
	}
	
	@Override
	public int spellId() {
		return 44;
	}
	
	@Override
	public double exp() {
		return 93;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
