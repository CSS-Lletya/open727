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

	//TODO: Needs to be tested against a player
	/**
	 *
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(2108, 0, 100));

		player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special!");

		if (target instanceof Player) {
			Player targetPlayer = (Player) target;
			int runEnergyLoss = (int)Math.ceil(targetPlayer.getRunEnergy()*0.25);
			targetPlayer.setRunEnergy(targetPlayer.getRunEnergy() - runEnergyLoss);
			player.setRunEnergy(targetPlayer.getRunEnergy() + runEnergyLoss);
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		combat.getMeleeHit(player, combat.getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.2, true));
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