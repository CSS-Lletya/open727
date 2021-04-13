package com.rs.game.player.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rs.Settings;
import com.rs.game.player.Player;
import com.rs.utils.Logger;

/**
 * Manages the players completed, started and not-started quests.
 * @author Apache Ah64
 */
public class QuestManager {

	/**
	 * The player reference.
	 */
	@SuppressWarnings("unused")
	private Player player;

	/**
	 * An array list holding all quests, completed, started and non-started.
	 */
	private ArrayList<Quest> quests = new ArrayList<Quest>(Settings.QUESTS);

	/**
	 * Construct a new {@code QuestManager} {@code Object}.
	 * @param player
	 */
	public QuestManager(Player player) {
		this.player = player;
	}

	/**
	 * Start a new quest.
	 * @param quest The quest key.
	 * @param parameters The parameters.
	 */
	public void startQuest(Object quest, Object[] parameters) {
		if (quest == null) {
			return;
		}
		Quest questInstance = Quests.getQuest(quest);
		quests.set(questInstance.questId, questInstance);
	}

	/**
	 * Initialize the quest manager.
	 */
	public void initialize() {
		refreshQuests();
	}

	/**
	 * Refresh the progress status of all quests in the quest tab.
	 */
	public void refreshQuests() {

	}

	/**
	 * Get the quests.
	 * @return The array list holding all quests, completed, started and not-started.
	 */
	public ArrayList<Quest> getQuests() {
		return quests;
	}

	/**
	 * Set a new quests array list.
	 * @param quests The array list holding all quests, completed, started and not-started.
	 */
	public void setQuests(ArrayList<Quest> quests) {
		this.quests = quests;
	}

	/**
	 * A enum holding the quest progress types.
	 * @author Apache Ah64
	 */
	public static enum Progress {

		/**
		 * The completed quest progress type.
		 */
		COMPLETED,

		/**
		 * The started quest progress type.
		 */
		STARTED,

		/**
		 * The not-started quest progress type.
		 */
		NOT_STARTED;
	}

	/**
	 * A class holding all quests.
	 * @author Apache Ah64
	 */
	private static class Quests {

		/**
		 * A map holding all quests.
		 */
		private static final Map<Object, Class<Quest>> quests = new HashMap<Object, Class<Quest>>(Settings.QUESTS);

		/**
		 * Get a quest.
		 * @param quest The quest.
		 * @return The quest instance.
		 */
		public static Quest getQuest(Object quest) {
			if (quest instanceof Quest) {
				return (Quest) quest;
			}
			Class<Quest> questClass = quests.get(quest);
			if (questClass == null) {
				return null;
			}
			try {
				return questClass.newInstance();
			} catch (Throwable e) {
				Logger.handle(e);
			}
			return null;
		}

		static {

		}
	}
}