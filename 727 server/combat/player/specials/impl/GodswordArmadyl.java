package player.specials.impl;

import java.util.Optional;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.item.ItemNames;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import player.PlayerCombat;
import player.specials.WeaponSpecialSignature;
import player.specials.WeaponSpecials;


@WeaponSpecialSignature(weapons = { ItemNames.ARMADYL_GODSWORD }, specAmount = 50)
public class GodswordArmadyl implements WeaponSpecials {

	/**
	 *Inflicts 25% more damage.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound and testing!");

		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(
						player,	combat.getRandomMaxHit(player, weaponId, attackStyle,
								false, true, 1.375, true)));
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(11989));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(2113));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}