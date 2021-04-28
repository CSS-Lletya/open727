package npc.qbd;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utils.Utils;

import player.Combat;

/**
 * Handles the Queen Black Dragon's range attack.
 * @author Emperor
 *
 */
public final class RangeAttack implements QueenAttack {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(16718);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.setNextAnimation(ANIMATION);
		World.get().submit(new Task(1) {
			@Override
			protected void execute() {
				int hit;
				if (victim.getPrayer().usingPrayer(1, 8)) {
					victim.setNextAnimation(new Animation(12573));
					victim.setNextGraphics(new Graphics(2229));
					victim.getPackets().sendGameMessage("You are unable to reflect damage back to this creature.");
					hit = 0;
				} else if (victim.getPrayer().usingPrayer(0, 18)) {
					victim.setNextAnimation(new Animation(Combat.getDefenceEmote(victim)));
					hit = 0;
				} else {
					hit = Utils.random(0 + Utils.random(150), 360);
					victim.setNextAnimation(new Animation(Combat.getDefenceEmote(victim)));
				}
				victim.applyHit(new Hit(npc, hit, hit == 0 ? HitLook.MISSED : HitLook.RANGE_DAMAGE));
				this.cancel();
			}
		});
		return Utils.random(4, 15);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

}