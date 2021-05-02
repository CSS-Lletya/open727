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


@WeaponSpecialSignature(weapons = { ItemNames.MORRIGANS_JAVELIN }, specAmount = 50)
public class MorrigansJavelin implements WeaponSpecials {
	/**
	 *Inflicts continuous damage in the same way that poison does, but faster and target can still be poisoned.
	 * The additional damage from the special attack occurs at dart-speed until it doubles the damage dealt by the original special attack. Thus, if you hit a 260 with the special, the opponent will be hit by 50s at dart speed until 250 damage has been dealt.
	 * This special attack can only be used in PvP.
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
		//combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(player));
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