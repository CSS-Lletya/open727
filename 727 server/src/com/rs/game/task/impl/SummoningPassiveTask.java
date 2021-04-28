package com.rs.game.task.impl;

import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

public final class SummoningPassiveTask extends Task {
	
	/**
	 * Creates a new {@link SummoningPassiveTask}.
	 */
	public SummoningPassiveTask() {
		super(15, false);
	}
	
	@Override
	public void execute() {
		for (Player player : World.getPlayers()) {
			if (player == null || player.getFamiliar() == null || player.isDead() || !player.hasFinished())
				continue;
			if (player.getFamiliar().getOriginalId() == 6814) {
				player.heal(20);
				player.setNextGraphics(new Graphics(1507));
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new SummoningPassiveTask());
	}
}