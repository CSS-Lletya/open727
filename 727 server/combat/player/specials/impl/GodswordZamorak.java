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


@WeaponSpecialSignature(weapons = { ItemNames.ZAMORAK_GODSWORD }, specAmount = 60)
public class GodswordZamorak implements WeaponSpecials {

	/**
	 *Freezes the target for 20 seconds if successful, though the target can still attack a player in an adjacent square.
	 * Also increase damage by 10% for the attack
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound, testing and implementation!");

		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int zgsdamage = combat.getRandomMaxHit(player, weaponId, attackStyle,
				false, true, 1.0, true);
		if (zgsdamage != 0 && target.getSize() <= 1) { // freezes small
			// npcs
			target.setNextGraphics(new Graphics(2104));
			target.addFreezeDelay(18000); // 18seconds
		}
		combat.delayNormalHit(weaponId, attackStyle,
				combat.getMeleeHit(player, zgsdamage));
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(7070));
	}

	@Override
	public Optional<Graphics> getGraphics() {
		return Optional.of(new Graphics(1221));
	}

	@Override
	public Optional<Integer> getSound() {
		return Optional.empty();
	}
}