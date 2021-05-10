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


@WeaponSpecialSignature(weapons = { ItemNames.STATIUS_WARHAMMER }, specAmount = 35)
public class StatiusWarhammer implements WeaponSpecials {

	/**
	 *Deals up to 25% more damage and decreases opponent's Defence by 30%.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound, needs -30% def implementation!");


		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		combat.delayNormalHit(
				weaponId,
				attackStyle,
				combat.getMeleeHit(
						player,
						combat.getRandomMaxHit(player, weaponId, attackStyle,
								false, true, 1.25, true)));

	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(10505));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(1040));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}