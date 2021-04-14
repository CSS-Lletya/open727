package com.rs.game.player.controlers;

import java.util.HashMap;

import com.rs.game.minigames.Barrows;
import com.rs.game.minigames.BorkControler;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.clanwars.RequestController;
import com.rs.game.minigames.clanwars.WarControler;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.minigames.pest.PestControlGame;
import com.rs.game.minigames.pest.PestControlLobby;
import com.rs.game.npc.corp.CorpBeastControler;
import com.rs.game.npc.godwars.GodWars;
import com.rs.game.npc.godwars.zaros.ZGDControler;
import com.rs.game.npc.others.Kalaboss;
import com.rs.game.npc.qbd.QueenBlackDragonController;
import com.rs.utils.Logger;

import skills.construction.HouseControler;
import skills.hunter.Falconry;
import skills.runecrafting.RunespanControler;

public class ControlerHandler {

	private static final HashMap<Object, Class<Controler>> handledControlers = new HashMap<Object, Class<Controler>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Controler> value1 = (Class<Controler>) Class.forName(Wilderness.class.getCanonicalName());
			handledControlers.put("Wilderness", value1);
			Class<Controler> value2 = (Class<Controler>) Class.forName(Kalaboss.class.getCanonicalName());
			handledControlers.put("Kalaboss", value2);
			Class<Controler> value4 = (Class<Controler>) Class.forName(GodWars.class.getCanonicalName());
			handledControlers.put("GodWars", value4);
			Class<Controler> value5 = (Class<Controler>) Class.forName(ZGDControler.class.getCanonicalName());
			handledControlers.put("ZGDControler", value5);
			Class<Controler> value9 = (Class<Controler>) Class.forName(DuelArena.class.getCanonicalName());
			handledControlers.put("DuelArena", value9);
			Class<Controler> value10 = (Class<Controler>) Class.forName(DuelControler.class.getCanonicalName());
			handledControlers.put("DuelControler", value10);
			Class<Controler> value11 = (Class<Controler>) Class.forName(CorpBeastControler.class.getCanonicalName());
			handledControlers.put("CorpBeastControler", value11);
			Class<Controler> value15 = (Class<Controler>) Class.forName(JailControler.class.getCanonicalName());
			handledControlers.put("JailControler", value15);
			handledControlers.put("clan_wars_request",
					(Class<Controler>) Class.forName(RequestController.class.getCanonicalName()));
			handledControlers.put("clan_war", (Class<Controler>) Class.forName(WarControler.class.getCanonicalName()));
			handledControlers.put("clan_wars_ffa", (Class<Controler>) Class.forName(FfaZone.class.getCanonicalName()));
			handledControlers.put("BorkControler",
					(Class<Controler>) Class.forName(BorkControler.class.getCanonicalName()));
			handledControlers.put("PestControlGame",
					(Class<Controler>) Class.forName(PestControlGame.class.getCanonicalName()));
			handledControlers.put("PestControlLobby",
					(Class<Controler>) Class.forName(PestControlLobby.class.getCanonicalName()));
			handledControlers.put("Barrows", (Class<Controler>) Class.forName(Barrows.class.getCanonicalName()));
			handledControlers.put("Falconry", (Class<Controler>) Class.forName(Falconry.class.getCanonicalName()));
			handledControlers.put("QueenBlackDragonControler",
					(Class<Controler>) Class.forName(QueenBlackDragonController.class.getCanonicalName()));
			handledControlers.put("HouseControler",
					(Class<Controler>) Class.forName(HouseControler.class.getCanonicalName()));
			handledControlers.put("RuneSpanControler",
					(Class<Controler>) Class.forName(RunespanControler.class.getCanonicalName()));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void reload() {
		handledControlers.clear();
		init();
	}

	public static final Controler getControler(Object key) {
		if (key instanceof Controler)
			return (Controler) key;
		Class<Controler> classC = handledControlers.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
}
