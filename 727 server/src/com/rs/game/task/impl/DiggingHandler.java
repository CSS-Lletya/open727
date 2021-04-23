package com.rs.game.task.impl;

import java.util.function.Consumer;

import com.rs.game.Animation;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

/**
 * Holds functionality which will execute after the player attempts to dig.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * 
 * BROKEN
 */
public final class DiggingHandler extends Task {
	
	/**
	 * The player whom is digging.
	 */
	private final Player player;
	
	/**
	 * The destination after digging.
	 */
	private final Consumer<Player> action;
	
	/**
	 * Constructs a new {@link DiggingHandler}.
	 * @param player {@link #player}.
	 * @param action {@link #action}.
	 */
	public DiggingHandler(Player player, Consumer<Player> action) {
		super(1, false);
		this.player = player;
		this.action = action;
	}
	
	@Override
	public void onSubmit() {
		player.setNextAnimation(new Animation(831));
	}
	
	@Override
	public void execute() {
		cancel();
		player.setNextAnimation(null);
	}
	
	@Override
	public void onCancel() {
		action.accept(player);
	}
	
}
