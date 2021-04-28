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
 * Handles the super dragonfire attack.
 * @author Emperor
 *
 */
public final class SuperFireAttack implements QueenAttack {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(16745);

	/**
	 * The graphics.
	 */
	private static final Graphics GRAPHIC = new Graphics(3152);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.setNextAnimation(ANIMATION);
		npc.setNextGraphics(GRAPHIC);
		victim.getPackets().sendGameMessage(
				"<col=FFCC00>The Queen Black Dragon gathers her strength to breath extremely hot flames.</col>");
		World.get().submit(new Task(4) {
			int count = 0;
			@Override
			protected void execute() {
				String message = FireBreathAttack.getProtectMessage(victim);
				int hit;
				if (message != null) {
					hit = Utils.random(150 + Utils.random(120), message.contains("prayer") ? 480 : 342);
					victim.getPackets().sendGameMessage(message);
				} else {
					hit = Utils.random(400, 798);
					victim.getPackets().sendGameMessage("You are horribly burned by the dragon's breath!");
				}
				int distance = Utils.getDistance(npc.getBase().transform(33, 31, 0), victim);
				hit /= (distance / 3) + 1;
				victim.setNextAnimation(new Animation(Combat.getDefenceEmote(victim)));
				victim.applyHit(new Hit(npc, hit, HitLook.REGULAR_DAMAGE));
				if (++count == 3) {
					this.cancel();
				}
				this.cancel();
			}
		});
		return Utils.random(8, 15);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

}