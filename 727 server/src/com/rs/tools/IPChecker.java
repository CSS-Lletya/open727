package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;

public class IPChecker {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		File[] chars = new File("data/characters").listFiles();
		// String ip = args[0];
		for (File acc : chars) {
			try {
				Player player = (Player) SerializableFilesManager.loadSerializedFile(acc);
				if (player == null || player.getMuted() > 0)
					continue;
				player.setMuted(0);
				SerializableFilesManager.storeSerializableClass(player, acc);
			} catch (Throwable e) {
				System.out.println("failed: " + acc.getName() + ", " + e);
			}
		}
	}

}
