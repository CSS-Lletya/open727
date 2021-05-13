package org.redrune.game.content.combat.player.registry.spell.lunar.teleport;

import org.redrune.game.GameConstants;
import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class HomeTeleportSpell implements TeleportationSpellEvent{
	
	@Override
	public int levelRequired() {
		return 0;
	}
	
	@Override
	public int[] runesRequired() {
		return new int[0];
	}
	
	@Override
	public Location destination() {
		return GameConstants.HOME_LOCATION;
	}
	
	@Override
	public int spellId() {
		return 39;
	}
	
	@Override
	public double exp() {
		return 0;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
}
