package com.rs.tools;

import java.io.File;

import com.rs.game.player.Player;
import com.rs.utils.Encrypt;
import com.rs.utils.SerializableFilesManager;

public class PasswordEncrypter {

	public static void main(String[] args) {
		encrypt();
	}

	public static void encrypt() {
		File[] chars = new File("./checkacc/").listFiles();
		for (File acc : chars) {
			try {
				Player player = (Player) SerializableFilesManager.loadSerializedFile(acc);
				if (player == null || player.getPassword() == null)
					continue;
				System.out.println(player.getPassword());
				player.setPassword(Encrypt.encryptSHA1(player.getPassword()));
				System.out.println(player.getPassword());
				SerializableFilesManager.storeSerializableClass(player, acc);
			} catch (Throwable e) {
				System.out.println("failed: " + acc.getName() + ", " + e);
			}
		}
	}
}