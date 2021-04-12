package com.rs.game.player.action;

/**
 * @author Jordan Abraham <jordan.abraham1997@gmail.com>
 * @date Nov 28, 2014
 */
public abstract class Action {

	/**
	 * Starts the {@code Action}.
	 */
	protected abstract boolean start();

	/**
	 * Cycles the {@code Action}.
	 */
	protected abstract void cycle();

	/**
	 * Finishes the {@code Action}.
	 */
	protected abstract void finish();

}