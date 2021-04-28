package com.rs.game.task.impl;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

public final class DrainPrayerTask extends Task {
	
	/**
	 * Creates a new {@link DrainPrayerTask}.
	 */
	public DrainPrayerTask() {
		super(1, false);
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
			player.getPrayer().processPrayerDrain();
		}
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new DrainPrayerTask());
	}
}