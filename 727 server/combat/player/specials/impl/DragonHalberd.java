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


@WeaponSpecialSignature(weapons = { ItemNames.DRAGON_HALBERD }, specAmount = 30)
public class DragonHalberd implements WeaponSpecials {
	/**
	 *A wide slash with increased strength that strikes multiple enemies if they are lined up correctly (in a multi-combat zone) and will hit
	 * larger monsters twice. Can do well over 400 damage with each swipe.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(2108, 0, 100));
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound, testing!");

		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int damage = 0;//getRandomMaxHit(player, weaponId, attackStyle, )
		if (target.getSize() < 3) {// giant npcs wont get stuned cuz of
			// a stupid hit
			target.setNextGraphics(new Graphics(254, 0, 100));
			target.setNextGraphics(new Graphics(80));
		}
		combat.delayNormalHit(
				weaponId,
				attackStyle,
				combat.getMeleeHit(
						player,
						combat.getRandomMaxHit(player, weaponId, attackStyle,
								false, true, 1.1, true)));
		if (target.getSize() > 1)
			combat.delayHit(
					1,
					weaponId,
					attackStyle,
					combat.getMeleeHit(
							player,
							combat.getRandomMaxHit(player, weaponId,
									attackStyle, false, true, 1.1, true)));
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(1665));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(282));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}