package com.rs.utils;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import com.rs.game.player.DominionTower;
import com.rs.game.player.Player;

public final class DTRank implements Serializable {

	private static final long serialVersionUID = 5403480618483552509L;

	private String username;
	private long dominionFactor;
	private int mode;
	private String bossName;
	private int floorId;

	private static DTRank[] ranks;

	private static final String PATH = "data/dtRanks.ser";

	public DTRank(Player player, int mode, String bossName) {
		this.username = player.getUsername();
		this.mode = mode;
		this.bossName = bossName;
		this.floorId = player.getDominionTower().getProgress();
		dominionFactor = player.getDominionTower().getTotalScore();
	}

	public static void showRanks(Player player) {
		player.getInterfaceManager().sendInterface(1158);
		int count = 0;
		for (DTRank rank : ranks) {
			if (rank == null)
				return;
			player.getPackets().sendIComponentText(1158, 9 + count * 5,
					Utils.formatPlayerNameForDisplay(rank.username));
			player.getPackets().sendIComponentText(1158, 10 + count * 5,
					"On " + (rank.mode == DominionTower.CLIMBER ? "climber" : "endurance") + ", reached floor "
							+ rank.floorId + ", killing: " + rank.bossName + ".");
			player.getPackets().sendIComponentText(1158, 11 + count * 5, "DF:<br>" + rank.dominionFactor);
			count++;
		}
	}

	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				ranks = (DTRank[]) SerializableFilesManager.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		ranks = new DTRank[10];
	}

	public static final void save() {
		try {
			SerializableFilesManager.storeSerializableClass(ranks, new File(PATH));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static void sort() {
		Arrays.sort(ranks, new Comparator<DTRank>() {
			@Override
			public int compare(DTRank arg0, DTRank arg1) {
				if (arg0 == null)
					return 1;
				if (arg1 == null)
					return -1;
				if (arg0.dominionFactor < arg1.dominionFactor)
					return 1;
				else if (arg0.dominionFactor > arg1.dominionFactor)
					return -1;
				else
					return 0;
			}

		});
	}

	public static void checkRank(Player player, int mode, String boss) {
		long dominionFactor = player.getDominionTower().getTotalScore();
		for (int i = 0; i < ranks.length; i++) {
			DTRank rank = ranks[i];
			if (rank == null)
				break;
			if (rank.username.equalsIgnoreCase(player.getUsername())) {
				ranks[i] = new DTRank(player, mode, boss);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks.length; i++) {
			DTRank rank = ranks[i];
			if (rank == null) {
				ranks[i] = new DTRank(player, mode, boss);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks.length; i++) {
			if (ranks[i].dominionFactor < dominionFactor) {
				ranks[i] = new DTRank(player, mode, boss);
				sort();
				return;
			}
		}
	}

}
