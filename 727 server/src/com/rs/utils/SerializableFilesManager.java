//package com.rs.utils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.util.ConcurrentModificationException;
//
//import com.rs.game.player.Player;
//
//public class SerializableFilesManager {
//
//	private static final String PATH = "data/characters/";
//	private static final String BACKUP_PATH = "data/charactersBackup/";
//
//	public synchronized static final boolean containsPlayer(String username) {
//		return new File(PATH + username + ".p").exists();
//	}
//
//	public synchronized static Player loadPlayer(String username) {
//		try {
//			return (Player) loadSerializedFile(new File(PATH + username + ".p"));
//		} catch (Throwable e) {
//			Logger.handle(e);
//		}
//		try {
//			Logger.log("SerializableFilesManager", "Recovering account: " + username);
//			return (Player) loadSerializedFile(new File(BACKUP_PATH + username + ".p"));
//		} catch (Throwable e) {
//			Logger.handle(e);
//		}
//		return null;
//	}
//
//	public static boolean createBackup(String username) {
//		try {
//			Utils.copyFile(new File(PATH + username + ".p"), new File(BACKUP_PATH + username + ".p"));
//			return true;
//		} catch (Throwable e) {
//			Logger.handle(e);
//			return false;
//		}
//	}
//
//	public synchronized static void savePlayer(Player player) {
//		try {
//			storeSerializableClass(player, new File(PATH + player.getUsername() + ".p"));
//		} catch (ConcurrentModificationException e) {
//			// happens because saving and logging out same time
//		} catch (Throwable e) {
//			Logger.handle(e);
//		}
//	}
//
//	public static final Object loadSerializedFile(File f) throws IOException, ClassNotFoundException {
//		if (!f.exists())
//			return null;
//		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
//		Object object = in.readObject();
//		in.close();
//		return object;
//	}
//
//	public static final void storeSerializableClass(Serializable o, File f) throws IOException {
//
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
//		out.writeObject(o);
//		out.close();
//	}
//
//	private SerializableFilesManager() {
//
//	}
//
//}
