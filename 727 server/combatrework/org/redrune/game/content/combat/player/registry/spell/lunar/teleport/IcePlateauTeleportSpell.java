package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class IcePlateauTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 89;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 3, LAW_RUNE, 3, WATER_RUNE, 8);
	}
	
	@Override
	public Location destination() {
		return new Location(2972, 3873, 0);
	}
	
	@Override
	public int spellId() {
		return 51;
	}
	
	@Override
	public double exp() {
		return 96;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
