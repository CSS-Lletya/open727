package com.rs.game.player.action;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Jordan Abraham <jordan.abraham1997@gmail.com>
 * @date Nov 28, 2014
 */
public final class ActionQueue {

	/**
	 * Represents a queue of pending {@link Action}'s.
	 */
	private final Deque<Action> ACTION_REPOSITORY = new ArrayDeque<Action>();

	/**
	 * Represents the maximum number of {@link Action}'s we can queue.
	 */
	private final int MAXIMUM_QUEUE_AMOUNT = 28;

	/**
	 * Represents the {@link Action} being queued.
	 */
	private Action action;

	/**
	 * Adds and queues an {@link Action}.
	 * @param action The {@link Action} to add and queue.
	 */
	public void addAction(Action action) {
		dequeAction();
		if (ACTION_REPOSITORY.size() >= MAXIMUM_QUEUE_AMOUNT || !action.start()) {
			dequeAction();
			return;
		}
		ACTION_REPOSITORY.add(action);
		if (!ACTION_REPOSITORY.isEmpty()) {
			queueAction();
		}
	}

	/**
	 * Queues a pending {@link Action}.
	 */
	public void queueAction() {
		if (ACTION_REPOSITORY.isEmpty() || ACTION_REPOSITORY == null) {
			return;
		}
		synchronized (ACTION_REPOSITORY) {
			this.action = ACTION_REPOSITORY.peek();
		}
	}

	/**
	 * Processes a {@link Action} that is queued.
	 */
	public void cycleAction() {
		if (ACTION_REPOSITORY.contains(action)) {
			synchronized (action) {
				action.cycle();
			}
		}
	}

	/**
	 * Deques a pending {@link Action}.
	 */
	public void dequeAction() {
		if (ACTION_REPOSITORY.contains(action)) {
			synchronized (action) {
				action.finish();
			}
			ACTION_REPOSITORY.remove(action);
		}
	}

	/**
	 * Empties the entirety of the {@link Action} repository. Use this during logout.
	 */
	public void emptyActionRepository() {
		if (!ACTION_REPOSITORY.isEmpty()) {
			ACTION_REPOSITORY.clear();
		}
	}

	/**
	 * Gets and returns the {@link Action} being queued.
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

}