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


@WeaponSpecialSignature(weapons = { ItemNames.GRANITE_MAUL }, specAmount = 50)
public class GraniteMaul implements WeaponSpecials {

	/**
	 *An extra attack done instantly with no other effects.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(340, 0, 96 << 16));
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound and implementation!");
		if (target instanceof Player) {
			;
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int damage = combat.getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true);
		combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(player, damage));
	}
	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(1667));
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