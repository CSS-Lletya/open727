package com.rs;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.rs.cache.Cache;
import com.rs.cores.BlockingExecutorService;
import com.rs.cores.CoresManager;
import com.rs.net.ServerChannelHandler;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.utils.Huffman;
import com.rs.utils.Logger;
import com.rs.utils.MapArchiveKeys;
public class GameLoader {

	public GameLoader() {
		load();
	}

	public static GameLoader get() {
		return LOADER;
	}

	public BlockingExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}

	private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

	public void load() {
		try {
			Logger.log("Launcher", "Initializing Cache & Game Network...");
			Cache.init();
			CoresManager.init();
			ServerChannelHandler.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getBackgroundLoader().submit(() -> {
			Huffman.init();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			MapArchiveKeys.init();
		});
		getBackgroundLoader().submit(() -> {
		});
		getBackgroundLoader().submit(() -> {
		});
		getBackgroundLoader().submit(() -> {
		});
		getBackgroundLoader().submit(() -> {
			HostManager.deserialize(HostListType.STARTER_RECEIVED);
			HostManager.deserialize(HostListType.BANNED_IP);
			HostManager.deserialize(HostListType.MUTED_IP);
		});
	}
	
	private static final GameLoader LOADER = new GameLoader();
}