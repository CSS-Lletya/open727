package com.rs.game.task.impl;

import com.rs.game.Graphics;
import com.rs.game.World;
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
		World.players().filter(p -> p.getFamiliar() != null).forEach(p -> {
			if (p.getFamiliar().getOriginalId() == 6814) {
				p.heal(20);
				p.setNextGraphics(new Graphics(1507));
			}
		});
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new SummoningPassiveTask());
	}
}