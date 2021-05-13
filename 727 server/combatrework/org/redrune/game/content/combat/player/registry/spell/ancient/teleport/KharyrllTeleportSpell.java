package org.redrune.game.content.combat.player.registry.spell.ancient.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;

import com.rs.game.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class KharyrllTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 66;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, BLOOD_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3492, 3471, 0);
	}
	
	@Override
	public int spellId() {
		return 42;
	}
	
	@Override
	public double exp() {
		return 76;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
