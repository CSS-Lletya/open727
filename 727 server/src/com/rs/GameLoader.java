package com.rs;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cores.BlockingExecutorService;
import com.rs.cores.CoresManager;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.dialogue.DialogueEventRepository;
import com.rs.game.item.AutomaticGroundItem;
import com.rs.game.player.FriendChatsManager;
import com.rs.game.player.controlers.ControlerHandler;
import com.rs.json.GsonHandler;
import com.rs.json.impl.MobDropTableLoader;
import com.rs.net.ServerChannelHandler;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.utils.Huffman;
import com.rs.utils.ItemBonuses;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.MapArchiveKeys;
import com.rs.utils.MapAreas;
import com.rs.utils.MusicHints;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;

import main.CommandDispatcher;
import main.InventoryDispatcher;
import main.NPCDispatcher;
import main.ObjectDispatcher;
import main.RSInterfaceDispatcher;
import npc.combat.rework.NPCCombatDispatcher;
import player.specials.WeaponSpecialDispatcher;

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
			World.get().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			ItemExamines.init();
			ItemBonuses.init();
			AutomaticGroundItem.initialize();
			MusicHints.init();
		});
		getBackgroundLoader().submit(() -> {
			ControlerHandler.init();
			FriendChatsManager.init();
			RegionBuilder.init();
			DialogueEventRepository.init();
		});
		getBackgroundLoader().submit(() -> {
			GsonHandler.initialize();
			CommandDispatcher.load();
			RSInterfaceDispatcher.load();
			ObjectDispatcher.load();
			NPCDispatcher.load();
			NPCCombatDispatcher.load();
			InventoryDispatcher.load();
		});
		getBackgroundLoader().submit(() -> {
			HostManager.deserialize(HostListType.STARTER_RECEIVED);
			HostManager.deserialize(HostListType.BANNED_IP);
			HostManager.deserialize(HostListType.MUTED_IP);
			new MobDropTableLoader().load();
			WeaponSpecialDispatcher.load();
		});
	}
	
	private static final GameLoader LOADER = new GameLoader();
}