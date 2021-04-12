package com.rs.game.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rs.utils.Utils;

public final class QuestManager implements Serializable {

	private static final long serialVersionUID = -8085932531253271252L;

	public static enum Quests {
		PERIL_OF_ICE_MONTAINS, NOMADS_REQUIEM

	}

	private transient Player player;
	private List<Quests> completedQuests;
	private HashMap<Quests, Integer> questStages;

	public QuestManager() {
		completedQuests = new ArrayList<Quests>();
	}

	public void setPlayer(Player player) {
		this.player = player;
		if (questStages == null)
			questStages = new HashMap<Quests, Integer>();
	}

	public int getQuestStage(Quests quest) {
		if (completedQuests.contains(quest))
			return -1;
		Integer stage = questStages.get(quest);
		return stage == null ? -2 : stage;
	}

	public void setQuestStageAndRefresh(Quests quest, int stage) {
		setQuestStage(quest, stage);
		sendStageData(quest);
	}

	public void setQuestStage(Quests quest, int stage) {
		if (completedQuests.contains(quest))
			return;
		questStages.put(quest, stage);
	}

	public void init() {
		checkCompleted(); // temporary
		for (Quests quest : completedQuests)
			sendCompletedQuestsData(quest);
		for (Quests quest : questStages.keySet())
			sendStageData(quest);
	}

	public void checkCompleted() {
		if (!completedQuests.contains(Quests.PERIL_OF_ICE_MONTAINS) && player.getSkills()
				.hasRequiriments(Skills.CONSTRUCTION, 10, Skills.FARMING, 10, Skills.HUNTER, 10, Skills.THIEVING, 11))
			completeQuest(Quests.PERIL_OF_ICE_MONTAINS);
	}

	public void completeQuest(Quests quest) {
		completedQuests.add(quest);
		questStages.remove(quest);
		sendCompletedQuestsData(quest);
		player.getPackets().sendGameMessage(
				"<col=ff0000>You have completed quest: " + Utils.formatPlayerNameForDisplay(quest.toString()) + ".");
		// message completed quest
	}

	public void sendCompletedQuestsData(Quests quest) {
		switch (quest) {
		case PERIL_OF_ICE_MONTAINS:
			player.getPackets().sendConfigByFile(4684, 150); // air machine instead of dragons + trapdoor opened
			break;
		}
	}

	private void sendStageData(Quests quest) {
		switch (quest) {
		case NOMADS_REQUIEM:
			player.getPackets().sendConfigByFile(6962, 3);
			break;
		}
	}

	public boolean completedQuest(Quests quest) {
		return completedQuests.contains(quest);
	}
}
