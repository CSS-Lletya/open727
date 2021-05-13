package org.redrune.game.content.combat.player.registry.spell.ancient.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;

import com.rs.game.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class LassarTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 72;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, WATER_RUNE, 4);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3006, 3471, 0);
	}
	
	@Override
	public int spellId() {
		return 43;
	}
	
	@Override
	public double exp() {
		return 82;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
