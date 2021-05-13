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


@WeaponSpecialSignature(weapons = { ItemNames.DRAGON_MACE }, specAmount = 25)
public class DragonMace implements WeaponSpecials {

	/**
	 *Drastically increases Strength and decreases accuracy for one hit.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(2108, 0, 100));
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs accuracy decrease and testing!");
		if (target instanceof Player) {
			;
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		combat.delayNormalHit(
				weaponId,
				attackStyle,
				combat.getMeleeHit(
						player,
						combat.getRandomMaxHit(player, weaponId, attackStyle,
								false, true, 1.45, true)));

	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(1060));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(251));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.of(2541);
	}
}