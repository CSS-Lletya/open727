package player.specials.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.item.ItemNames;
import com.rs.game.player.Player;
import player.PlayerCombat;
import player.specials.WeaponSpecialSignature;
import player.specials.WeaponSpecials;

import java.util.Optional;


@WeaponSpecialSignature(weapons = { ItemNames.VERACS_FLAIL, ItemNames.VERACS_FLAIL_USED, ItemNames.VERACS_FLAIL_25, ItemNames.VERACS_FLAIL_50,
		ItemNames.VERACS_FLAIL_75, ItemNames.VERACS_FLAIL_100}, specAmount = 0)
public class BarrowsVerac implements WeaponSpecials {
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(2108, 0, 100));
		player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special!");
		if (target instanceof Player) {
			;
		}
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.empty();
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.empty();
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}