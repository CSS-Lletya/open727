package org.redrune.game.content.combat.player.registry.spell.ancient.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;

import com.rs.game.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class CarrallangarTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 84;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, SOUL_RUNE, 2);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3217, 3677, 0);
	}
	
	@Override
	public int spellId() {
		return 45;
	}
	
	@Override
	public double exp() {
		return 94;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
