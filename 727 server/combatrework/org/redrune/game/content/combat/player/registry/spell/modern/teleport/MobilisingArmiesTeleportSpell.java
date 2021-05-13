package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;

import com.rs.game.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class MobilisingArmiesTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 10;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 1, WATER_RUNE, 1, AIR_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2413, 2848, 0);
	}
	
	@Override
	public int spellId() {
		return 37;
	}
	
	@Override
	public double exp() {
		return 19;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
