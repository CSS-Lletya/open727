package com.rs.game.task.impl;

import java.util.function.Consumer;

import com.rs.game.Animation;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

/**
 * Handles Player digging Task handling
 * Rest of the event responses should be handled
 * in the specified class (Exmaple Barrows class).
 * @author Dennis
 *
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
	 * A Simple seconds counter object;
	 */
	private byte counter;
	
	/**
	 * Constructs a new {@link DiggingHandler}.
	 * @param player {@link #player}.
	 * @param action {@link #action}.
	 */
	public DiggingHandler(Player player, Consumer<Player> action) {
		super(2, false);
		this.player = player;
		this.action = action;
	}
	
	@Override
	public void execute() {
		counter++;
		player.setNextAnimation(new Animation(831));
		if (counter == getDelay()) {
			player.setNextAnimation(new Animation(Animation.RESET_ANIMATION));
			this.cancel();
		}
	}
	
	@Override
	public void onCancel() {
		action.accept(player);
	}
}