package com.rs.game.task.impl;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

/**
 * The class that handles the restoration special percentages.
 * @author Artem Batutin <artembatutin@gmail.com></artembatutin@gmail.com>
 */
public final class RestoreSpecialTask extends Task {
	
	/**
	 * Creates a new {@link RestoreSpecialTask}.
	 */
	public RestoreSpecialTask() {
		super(15, false);
	}
	
	@Override
	public void execute() {
		for(Player player : World.getPlayers()) {
			if(player == null) {
				continue;
			}
			if(!player.isRunning()) {
				continue;
			}
			if(player.isDead()) {
				continue;
			}
			if(player.getCombatDefinitions().getSpecialAttackPercentage() < 100) {
				player.getCombatDefinitions().restoreSpecialAttack(5);
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new RestoreSpecialTask());
	}
}
