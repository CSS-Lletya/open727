package com.rs.game.task.impl;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.Skills;

public final class RestoreRunEnergyTask extends Task {

	/**
	 * Creates a new {@link RestoreRunEnergyTask}.
	 */
	public RestoreRunEnergyTask() {
		super(2, true);
	}

	@Override
	public void execute() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isRunning() || player.isDead()) {
				continue;
			}
			if (player.runEnergy < 100 && (player.getWalkSteps().isEmpty())) {
				double restoreRate = 0.45D;
				double agilityFactor = 0.01 * player.getSkills().getLevel(Skills.AGILITY);
				player.setRunEnergy(player.runEnergy + (restoreRate + agilityFactor));
				player.getPackets().sendRunEnergy();
			}
		}

	}

	@Override
	public void onCancel() {
		World.get().submit(new RestoreRunEnergyTask());
	}
}