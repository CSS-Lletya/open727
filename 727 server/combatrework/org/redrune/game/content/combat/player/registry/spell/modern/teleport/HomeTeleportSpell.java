package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;

import com.rs.Settings;
import com.rs.game.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class HomeTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 0;
	}
	
	@Override
	public int[] runesRequired() {
		return new int[0];
	}
	
	@Override
	public WorldTile destination() {
		return Settings.RESPAWN_PLAYER_LOCATION;
	}
	
	@Override
	public int spellId() {
		return 24;
	}
	
	@Override
	public double exp() {
		return 0;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
