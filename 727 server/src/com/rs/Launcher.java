package com.rs;

import java.util.concurrent.TimeUnit;

import com.alex.store.Index;
import com.rs.cache.Cache;
import com.rs.cores.CoresManager;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class Launcher {

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.out.println("USE: guimode(boolean) debug(boolean) hosted(boolean)");
			return;
		}
		Settings.HOSTED = Boolean.parseBoolean(args[2]);
		Settings.DEBUG = true;//Boolean.parseBoolean(args[1]);
		long currentTime = Utils.currentTimeMillis();

		GameLoader.get().getBackgroundLoader().waitForPendingTasks().shutdown();

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

				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	public static void cleanMemory(boolean force) {
		if (force) {
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
}