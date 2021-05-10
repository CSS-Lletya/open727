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


@WeaponSpecialSignature(weapons = { ItemNames.KORASIS_SWORD }, specAmount = 60)
public class KorasisSword implements WeaponSpecials {

	/**
	 *This special will always deal between 50%-150% of your max hit, and in multiway combat, it can hit up to three enemies, with the damage being halved
	 * for each subsequent enemy struck. It is also capable of keeping the Pest queen from using her own special attack, but it must be deployed while she
	 * charges. During the fight with the Pest queen, the special attack only uses 15% of the player's special attack energy, but outside of it, it instead
	 * requires 60%.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		target.setNextGraphics(new Graphics(2108, 0, 100));
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound, graphics, animations and implementation!");
		if (target instanceof Player) {
			;
		}
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int korasiDamage = combat.getMaxHit(player, weaponId, attackStyle,
				false, true, 1);
		double multiplier = 0.5 + Math.random();
		combat.max_hit = (int) (korasiDamage * 1.5);
		korasiDamage *= multiplier;
		combat.delayNormalHit(weaponId, attackStyle,
				combat.getMagicHit(player, korasiDamage));
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(14788));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(1729));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}