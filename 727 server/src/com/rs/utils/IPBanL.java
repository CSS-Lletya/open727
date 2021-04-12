package com.rs.utils;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.game.player.Player;

public final class IPBanL {

	public static CopyOnWriteArrayList<String> ipList;

	private static final String PATH = "data/bannedIPS.ser";
	private static boolean edited;

	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				ipList = (CopyOnWriteArrayList<String>) SerializableFilesManager.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		ipList = new CopyOnWriteArrayList<String>();
	}

	public static final void save() {
		if (!edited)
			return;
		try {
			SerializableFilesManager.storeSerializableClass(ipList, new File(PATH));
			edited = false;
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static boolean isBanned(String ip) {
		return ipList.contains(ip);
	}

	public static void ban(Player player, boolean loggedIn) {
		player.setPermBanned(true);
		if (loggedIn) {
			ipList.add(player.getSession().getIP());
			player.getSession().getChannel().disconnect();
		} else {
			ipList.add(player.getLastIP());
			SerializableFilesManager.savePlayer(player);
		}
		edited = true;
	}

	public static void unban(Player player) {
		player.setPermBanned(false);
		player.setBanned(0);
		ipList.remove(player.getLastIP());
		edited = true;
		save();
	}

	public static void checkCurrent() {
		for (String list : ipList) {
			System.out.println(list);
		}
	}

	public static CopyOnWriteArrayList<String> getList() {
		return ipList;
	}

}
