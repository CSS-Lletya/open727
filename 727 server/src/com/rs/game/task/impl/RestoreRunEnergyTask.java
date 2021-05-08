package com.rs.game.task.impl;

import com.rs.game.World;
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
		World.players().filter(p -> p.getRunEnergy() < 100 && (p.getWalkSteps().isEmpty())).forEach(p -> {
			double restoreRate = 0.45D;
			double agilityFactor = 0.01 * p.getSkills().getLevel(Skills.AGILITY);
			p.setRunEnergy(p.getRunEnergy() + (restoreRate + agilityFactor));
			p.getPackets().sendRunEnergy();
		});
	}

	@Override
	public void onCancel() {
		World.get().submit(new RestoreRunEnergyTask());
	}
}