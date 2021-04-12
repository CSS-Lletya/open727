package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;

public class AccChecker {

	public static void main(String[] args) {
		File dir = new File("./checkacc/");
		File[] accs = dir.listFiles();
		for (File acc : accs) {
			Player player = null;
			try {
				player = (Player) SerializableFilesManager.loadSerializedFile(acc);
				System.out.println(player.getIPList());
				System.out.println(player.getPasswordList());
				System.out.println(player.isDonator());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			try {
				SerializableFilesManager.storeSerializableClass(player, acc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}