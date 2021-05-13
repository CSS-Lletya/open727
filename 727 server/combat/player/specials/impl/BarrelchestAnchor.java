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

@WeaponSpecialSignature(weapons = { ItemNames.BARRELCHEST_ANCHOR }, specAmount = 50)
public class BarrelchestAnchor implements WeaponSpecials {

	/**
	 *Deals 10% more damage, doubles the chance of hitting, and will lower the opponent's Attack, Defence, Ranged, or Magic level by 1% of the damage inflicted.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound, and implementation!");
		if (target instanceof Player) {
			;
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(
						player,
						combat.getRandomMaxHit(player, weaponId, attackStyle,
								false, false, 1.1, true)));
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(5870));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(1027));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}