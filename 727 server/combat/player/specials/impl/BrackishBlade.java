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


@WeaponSpecialSignature(weapons = { ItemNames.BRACKISH_BLADE }, specAmount = 75)
public class BrackishBlade implements WeaponSpecials {
	//TODO:Sounds, graphics, animations, implementation
	/**
	 *Doubles the chance of hitting, and adds 2.5% of the damage dealt to your Strength, Attack and Defence. For example if you hit 200, this means your Strength, Attack and Defence all get a visible 5 level boost.
	 * When using the special attack, if you hit, you will have a guarenteed max hit. Otherwise you will deal zero damage.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(2108, 0, 100));
		player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special!");
		if (target instanceof Player) {
			;
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int damage = 0;//getRandomMaxHit(player, weaponId, attackStyle, )
		//combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(player));int weaponId = player.getEquipment().getWeaponId();
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