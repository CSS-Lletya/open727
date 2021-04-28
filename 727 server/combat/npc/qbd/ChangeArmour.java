package npc.qbd;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utils.Utils;

/**
 * Handles the Queen Black Dragon's change armour "attack".
 * @author Emperor
 *
 */
public final class ChangeArmour implements QueenAttack {

	@Override
	public int attack(final QueenBlackDragon npc, Player victim) {
		npc.switchState(Utils.random(2) < 1 ? QueenState.CRYSTAL_ARMOUR : QueenState.HARDEN);
		World.get().submit(new Task(40) {
			@Override
			protected void execute() {
				npc.switchState(QueenState.DEFAULT);
				this.cancel();
			}
		});
		npc.getTemporaryAttributtes().put("_last_armour_change", npc.getTicks() + Utils.random(41, 100));
		return Utils.random(4, 10);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		Integer last = (Integer) npc.getTemporaryAttributtes().get("_last_armour_change");
		return last == null || last < npc.getTicks();
	}

}