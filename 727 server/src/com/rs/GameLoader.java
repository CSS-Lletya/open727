package com.rs;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cores.BlockingExecutorService;
import com.rs.cores.CoresManager;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.npc.combat.CombatScriptsHandler;
import com.rs.game.player.FriendChatsManager;
import com.rs.game.player.controlers.ControlerHandler;
import com.rs.game.player.dialogues.DialogueHandler;
import com.rs.json.GsonHandler;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.Huffman;
import com.rs.utils.ItemBonuses;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.MapArchiveKeys;
import com.rs.utils.MapAreas;
import com.rs.utils.MusicHints;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.ShopsHandler;

import server.database.GameDatabase;
import server.database.passive.PassiveDatabaseWorker;
import skills.fishing.FishingSpotsHandler;

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
			World.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getBackgroundLoader().submit(() -> {
			if (Settings.mysqlEnabled) {
				GameDatabase.initializeWebsiteDatabases();
				PassiveDatabaseWorker.initialize();
			}
			return null;
		});
		getBackgroundLoader().submit(() -> {
			ItemsEquipIds.init();
			Huffman.init();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			MapArchiveKeys.init();
			MapAreas.init();
			NPCCombatDefinitionsL.init();
		});
		getBackgroundLoader().submit(() -> {
			NPCBonuses.init();
			NPCDrops.init();
			ItemExamines.init();
			ItemBonuses.init();
			MusicHints.init();
			ShopsHandler.init();
		});
		getBackgroundLoader().submit(() -> {
			FishingSpotsHandler.init();
			CombatScriptsHandler.init();
			DialogueHandler.init();
			ControlerHandler.init();
			FriendChatsManager.init();
			RegionBuilder.init();
		});
		getBackgroundLoader().submit(() -> {
			GsonHandler.initialize();
		});
	}
	
	private static final GameLoader LOADER = new GameLoader();
}