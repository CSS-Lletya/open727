package player.specials.impl;

import com.rs.game.Entity;
import com.rs.game.player.Player;

import player.specials.WeaponSpecialSignature;
import player.specials.WeaponSpecials;

@WeaponSpecialSignature(weapons = {4151}, specAmount = 100)
public class AbyssalWhip implements WeaponSpecials {

	@Override
	public void execute(Player player, Entity target) throws Exception {
		System.out.println("Sup");
	}
}
