package player.specials.impl;

import java.util.Optional;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.item.ItemNames;
import com.rs.game.player.Player;

import player.PlayerCombat;
import player.specials.WeaponSpecialSignature;
import player.specials.WeaponSpecials;

@WeaponSpecialSignature(weapons = { ItemNames.ABYSSAL_WHIP, 15442, 15443, 15444, 15441, 23691 }, specAmount = 50)
public class AbyssalWhip implements WeaponSpecials {

	/**
	 *An attack that transfers 25% of the target's run energy to the player (only works on other players).
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(2108, 0, 100));

		player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, needs sound, testing against player!");

		if (target instanceof Player) {
			Player targetPlayer = (Player) target;
			int runEnergyLoss = (int)Math.ceil(targetPlayer.getRunEnergy()*0.25);
			targetPlayer.setRunEnergy(targetPlayer.getRunEnergy() - runEnergyLoss);
			player.setRunEnergy(targetPlayer.getRunEnergy() + runEnergyLoss);
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int damage = combat.getRandomMaxHit(player, weaponId, attackStyle, false, true, 1, true);
		combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(player, damage));
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(11971));
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