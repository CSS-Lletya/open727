package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;

import com.rs.game.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class LumbridgeTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 31;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(EARTH_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3222, 3218, 0);
	}
	
	@Override
	public int spellId() {
		return 43;
	}
	
	@Override
	public double exp() {
		return 41;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
