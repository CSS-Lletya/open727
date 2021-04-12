package com.rs.game.player.action.impl;

import com.rs.game.player.Player;
import com.rs.game.player.action.Action;

/**
 * @author Jordan Abraham <jordan.abraham1997@gmail.com>
 * @date Nov 28, 2014
 */
public final class Woodcutting extends Action {

	/**
	 * Represents the {@link Player} doing the {@code Woodcutting} to use.
	 */
	private Player player;

	/**
	 * Constructs a new {@code Woodcutting} {@link Object}.
	 * @param player The {@link Player} to construct.
	 */
	public Woodcutting(Player player) {
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.astro.rs2.game.content.action.Action#start()
	 */
	@Override
	protected boolean start() {
		player.sm("You swing your hatchet at the tree.");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.astro.rs2.game.content.action.Action#cycle()
	 */
	@Override
	protected void cycle() {
		player.sm("You get some logs.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.astro.rs2.game.content.action.Action#finish()
	 */
	@Override
	protected void finish() {
		player.sm("Done");
	}

	/**
	 * Gets and returns the constructed {@link Player}.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

}