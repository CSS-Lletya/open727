package com.rs.game.task.impl;

import com.rs.game.World;
import com.rs.game.task.Task;
import com.rs.utils.ShopsHandler;

public final class ShopRestockTask extends Task {
	
	/**
	 * Creates a new {@link ShopRestockTask}.
	 */
	public ShopRestockTask() {
		super(30, false);
	}
	
	@Override
	public void execute() {
		ShopsHandler.restoreShops();
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new ShopRestockTask());
	}
}