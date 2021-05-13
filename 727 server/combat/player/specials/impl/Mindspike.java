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


@WeaponSpecialSignature(weapons = { ItemNames.MINDSPIKE_AIR, ItemNames.MINDSPIKE_EARTH, ItemNames.MINDSPIKE_FIRE, ItemNames.MINDSPIKE_WATER }, specAmount = 75)
public class Mindspike implements WeaponSpecials {

	/**
	 *All four types (Mindspike (air), Mindspike (water), Mindspike (earth), Mindspike (fire)) have this special attack. Always hits 50 magic damage with a melee-range,
	 * rune-free spell of the same element that the staff is attuned to.
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