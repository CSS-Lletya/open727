package npc.qbd;

import java.util.Iterator;

import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utils.Utils;

import npc.NPC;

/**
 * Handles the Queen Black Dragon's time stop attack.
 * @author Emperor
 *
 */
public final class TimeStopAttack implements QueenAttack {

	/**
	 * The messages the soul says.
	 */
	private static final ForceTalk[] MESSAGES = {
			new ForceTalk("Kill me, mortal... quickly! HURRY! BEFORE THE SPELL IS COMPLETE!"),
			new ForceTalk("Time is short!"), new ForceTalk("She is pouring her energy into me... hurry!"),
			new ForceTalk("The spell is nearly complete!") };

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		for (Iterator<TorturedSoul> it = npc.getSouls().iterator(); it.hasNext();) {
			TorturedSoul soul = it.next();
			if (soul.isDead()) {
				it.remove();
			}
		}
		if (npc.getSouls().isEmpty()) {
			return 1;
		}
		final TorturedSoul soul = npc.getSouls().get(Utils.random(npc.getSouls().size()));
		soul.setNextWorldTile(
				Utils.random(2) == 0 ? npc.getBase().transform(24, 28, 0) : npc.getBase().transform(42, 28, 0));
		soul.setNextGraphics(TorturedSoul.TELEPORT_GRAPHIC);
		soul.setNextAnimation(TorturedSoul.TELEPORT_ANIMATION);
		soul.setLocked(true);
		World.get().submit(new Task(3) {
			int stage = -1;
			@Override
			protected void execute() {
				stage++;
				if (stage == 8) {
					this.cancel();
					npc.getTemporaryAttributtes().put("_time_stop_atk", npc.getTicks() + Utils.random(50) + 40);
					for (TorturedSoul s : npc.getSouls()) {
						s.setLocked(false);
					}
					for (NPC worm : npc.getWorms()) {
						worm.setLocked(false);
					}
					victim.unlock();
					victim.getPackets().sendGlobalConfig(1925, 0);
					return;
				} else if (stage == 4) {
					for (TorturedSoul s : npc.getSouls()) {
						s.setLocked(true);
					}
					for (NPC worm : npc.getWorms()) {
						worm.setLocked(true);
					}
					victim.lock();
					soul.setLocked(false);
					victim.getPackets().sendGlobalConfig(1925, 1);
					victim.getPackets().sendGameMessage(
							"<col=33900>The tortured soul has stopped time for everyone except himself and the Queen Black</col>");
					victim.getPackets().sendGameMessage("<col=33900>Dragon.</col>");
					return;
				} else if (stage > 3) {
					return;
				}
				if (soul.isDead()) {
					this.cancel();
					return;
				}
				soul.setNextForceTalk(MESSAGES[stage]);
				this.cancel();
			}
		});
		npc.getTemporaryAttributtes().put("_time_stop_atk", 9999999);
		return Utils.random(5, 10);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		if (npc.getSouls().isEmpty()) {
			return false;
		}
		Integer tick = (Integer) npc.getTemporaryAttributtes().get("_time_stop_atk");
		return tick == null || tick < npc.getTicks();
	}

}