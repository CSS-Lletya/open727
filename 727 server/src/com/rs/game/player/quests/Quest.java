package com.rs.game.player.quests;

import com.rs.game.player.Player;
import com.rs.game.player.quests.QuestManager.Progress;

/**
 * A extendable quest controler.
 * @author Apache Ah64
 */
public abstract class Quest {

	/**
	 * The player reference.
	 */
	protected Player player;

	/**
	 * The quest id.
	 */
	protected int questId;

	/**
	 * The quest progress.
	 */
	protected Progress progress;

	/**
	 * Start the quest.
	 */
	public abstract void start();

	/**
	 * Finish the quest.
	 */
	public abstract void finish();
}