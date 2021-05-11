package player.specials.impl;

import java.util.Optional;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.item.ItemNames;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.task.Task;

import npc.NPC;
import player.PlayerCombat;
import player.specials.WeaponSpecialSignature;
import player.specials.WeaponSpecials;


@WeaponSpecialSignature(weapons = { ItemNames.DRAGON_SPEAR }, specAmount = 25)
public class DragonSpear implements WeaponSpecials {

	/**
	 *When this special is used, the target is pushed back one square and stunned for three seconds. This attack does not inflict damage.
	 * Does not work on enemies that take up more than one space.
	 *
	 */
	@Override
	public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
		player.stopAll();
		target.setNextGraphics(new Graphics(80, 5, 60));
		if(player.getRights() == Rights.ADMINISTRATOR)
			player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special, Needs sound, and testing!");


		if (!target.addWalkSteps(target.getX() - player.getX() + target.getX(), target.getY() - player.getY() + target.getY(), 1))
			player.setNextFaceEntity(target);
		target.setNextFaceEntity(player);

		target.setNextFaceEntity(null);
		player.setNextFaceEntity(null);

		if (target instanceof Player) {
			final Player defendingPlayer = (Player) target;
			defendingPlayer.lock();
			defendingPlayer.getWatchMap().get("FOOD").elapsed(3000);
			defendingPlayer.setDisableEquip(true);
			World.get().submit(new Task(5) {
				@Override
				protected void execute() {
					defendingPlayer.setDisableEquip(false);
					defendingPlayer.unlock();
					this.cancel();
				}
			});
		} else {
			NPC n = (NPC) target;
			n.setFreezeDelay(3000);
			n.resetCombat();
			n.setRandomWalk(false);
		}
	}

	@Override
	public Optional<Animation> getAnimation() {
		return Optional.of(new Animation(12017));
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