package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;

import com.rs.game.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class FaladorTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 37;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(WATER_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2964, 3379, 0);
	}
	
	@Override
	public int spellId() {
		return 46;
	}
	
	@Override
	public double exp() {
		return 48;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
