package com.rs.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.rs.game.player.Player;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.Highscores;

public final class DisplayNames {

	private static ArrayList<String> cachedNames;

	private static final String PATH = "data/displayNames.ser";

	private DisplayNames() {

	}

	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				cachedNames = (ArrayList<String>) SerializableFilesManager.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		cachedNames = new ArrayList<String>();
	}

	public static void save() {
		try {
			SerializableFilesManager.storeSerializableClass(cachedNames, new File(PATH));
		} catch (IOException e) {
			Logger.handle(e);
		}
	}

	public static boolean setDisplayName(Player player, String displayName) {
		synchronized (cachedNames) {
			if ((SerializableFilesManager.containsPlayer(Utils.formatPlayerNameForProtocol(displayName))
					|| cachedNames.contains(displayName) || !cachedNames.add(displayName)))
				return false;
			if (player.hasDisplayName())
				cachedNames.remove(player.getDisplayName());
		}
		String displayname = player.getDisplayName();
		player.setDisplayName(displayName);
		FriendChatsManager.refreshChat(player);
		Highscores.highscores(player, displayname);
		player.getAppearance().generateAppearanceData();
		return true;
	}

	public static boolean removeDisplayName(Player player) {
		if (!player.hasDisplayName())
			return false;
		synchronized (cachedNames) {
			cachedNames.remove(player.getDisplayName());
		}
		player.setDisplayName(null);
		player.getAppearance().generateAppearanceData();
		return true;
	}
}
