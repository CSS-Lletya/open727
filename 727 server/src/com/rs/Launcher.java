package com.rs;

import java.util.concurrent.TimeUnit;

import com.alex.store.Index;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.discord.DiscordBot;
import com.rs.game.player.AccountCreation;
import com.rs.json.impl.ObjectSpawnLoader;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import mysql.Database;

public final class Launcher {

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.out.println("USE: guimode(boolean) debug(boolean) hosted(boolean)");
			return;
		}
		Settings.HOSTED = Boolean.parseBoolean(args[2]);
		Settings.DEBUG = true;//Boolean.parseBoolean(args[1]);
		long currentTime = Utils.currentTimeMillis();
		if (Settings.discordRelay) {
			bot = new DiscordBot();
		}
		if (Settings.mysqlEnabled) {
			getDB().init();
		}
		GameLoader.get().getBackgroundLoader().waitForPendingTasks().shutdown();
		new ObjectSpawnLoader().initialize();

		Logger.log("Launcher",
				"Server took " + (Utils.currentTimeMillis() - currentTime) + " milli seconds to launch.");
		addAccountsSavingTask();
		addCleanMemoryTask();
	}

	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < Settings.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					World.players().forEach(p -> {
						AccountCreation.savePlayer(p);
					});
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			for (Region region : World.getRegions().values())
				region.removeMapFromMemory();
		}
		for (Index index : Cache.STORE.getIndexes())
			index.resetCachedFiles();
		System.gc();
	}

	public static void shutdown() {
		try {
			ServerChannelHandler.shutdown();
			CoresManager.shutdown();
		} finally {
			System.exit(0);
		}
	}

	public static void restart() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
		System.gc();
		try {
			Runtime.getRuntime().exec(
					"java -server -Xms2048m -Xmx20000m -cp bin;/data/libs/netty-3.2.7.Final.jar;/data/libs/FileStore.jar Launcher false false true false");
			System.exit(0);
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static DiscordBot bot;
	
	public static DiscordBot getDiscordBot() {
		return bot;
	}
	
	public static Database dbInstance = new Database();
	
	public static Database getDB() {
		return dbInstance;
	}
}