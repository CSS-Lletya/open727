package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class WaterbirthTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 72;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 2, LAW_RUNE, 1, WATER_RUNE, 1);
	}
	
	@Override
	public Location destination() {
		return new Location(2527, 3739, 0);
	}
	
	@Override
	public int spellId() {
		return 47;
	}
	
	@Override
	public double exp() {
		return 72;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
