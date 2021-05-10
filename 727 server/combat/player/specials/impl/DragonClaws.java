package player.specials.impl;

import java.util.Optional;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.item.ItemNames;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.utils.Utils;

import player.PlayerCombat;
import player.specials.WeaponSpecialSignature;
import player.specials.WeaponSpecials;


@WeaponSpecialSignature(weapons = { ItemNames.DRAGON_CLAW }, specAmount = 50)
public class DragonClaws implements WeaponSpecials {

	/**
	 *Four very quick attacks performed at once with increased accuracy, but the last three hits depend entirely on the first hit. The second hit will
	 * always be half of the first hit, and the 3rd and 4th hits will always add up to the 2nd hit. For example, 200-100-50-50 or 180-90-50-40 or 120-60-30-30.
	 * Effectively, whatever you hit in the first strike, you will get double that hit after all 4 hits are dealt (200 + 100 + 50 + 50 = 400). There is
	 * also the chance that you hit a 0, as your first hit. This does not mean that you'll hit 0's as your first 4 hits (special attack). For example, you
	 * could hit 0-200-100-100, or 0-0-200-200 or even 0-0-0-350 (in this case damage is 1.5 times the original hit). Effectively, if one of the four special
	 * hits inflicts damage, the dragon claws guarantee damage of at least 1.5 times the original hit.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound, testing and implementation!");
		if (target instanceof Player) {
			;
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int[] hits1 = new int[] {0, 1};
		int hit1 = combat.getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
		if (hit1 > 0) {
			hits1 = new int[] {hit1, hit1 / 2, (hit1 / 2) / 2, (hit1 / 2) - ((hit1 / 2) / 2)};
		} else {
			hit1 = combat.getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
			if (hit1 > 0) {
				hits1 = new int[] {0, hit1, hit1 / 2, hit1 - (hit1 / 2)};
			} else {
				hit1 = combat.getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
				if (hit1 > 0) {
					hits1 = new int[] {0, 0, hit1 / 2, (hit1 / 2) + 10};
				} else {
					hit1 = combat.getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
					if (hit1 > 0) {
						hits1 = new int[] {0, 0, 0, (int) (hit1 * 1.5)};
					} else {
						hits1 = new int[] {0, 0, 0, Utils.getRandom(7)};
					}
				}
			}
		}
		for (int i = 0; i < hits1.length; i++) {
			if (i > 1) {
				combat.delayHit(1, weaponId, attackStyle, combat.getMeleeHit(player, hits1[i]));
			} else {
				combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(player, hits1[i]));
			}
		}
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(10961));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(1950));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}