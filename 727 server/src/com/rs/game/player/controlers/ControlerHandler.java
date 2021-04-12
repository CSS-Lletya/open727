package com.rs.game.player.controlers;

import java.util.HashMap;

import com.rs.game.minigames.BrimhavenAgility;
import com.rs.game.minigames.RefugeOfFear;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.clanwars.RequestController;
import com.rs.game.minigames.clanwars.WarControler;
import com.rs.game.minigames.creations.StealingCreationGame;
import com.rs.game.minigames.creations.StealingCreationLobby;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.player.controlers.castlewars.CastleWarsPlaying;
import com.rs.game.player.controlers.castlewars.CastleWarsWaiting;
import com.rs.game.player.controlers.events.DeathEvent;
import com.rs.game.player.controlers.fightpits.FightPitsArena;
import com.rs.game.player.controlers.fightpits.FightPitsLobby;
import com.rs.game.player.controlers.pestcontrol.PestControlGame;
import com.rs.game.player.controlers.pestcontrol.PestControlLobby;
import com.rs.utils.Logger;

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
			Class<Controler> value6 = (Class<Controler>) Class.forName(TutorialIsland.class.getCanonicalName());
			handledControlers.put("TutorialIsland", value6);
			Class<Controler> value7 = (Class<Controler>) Class.forName(StartTutorial.class.getCanonicalName());
			handledControlers.put("StartTutorial", value7);
			Class<Controler> value9 = (Class<Controler>) Class.forName(DuelArena.class.getCanonicalName());
			handledControlers.put("DuelArena", value9);
			Class<Controler> value10 = (Class<Controler>) Class.forName(DuelControler.class.getCanonicalName());
			handledControlers.put("DuelControler", value10);
			Class<Controler> value11 = (Class<Controler>) Class.forName(CorpBeastControler.class.getCanonicalName());
			handledControlers.put("CorpBeastControler", value11);
			Class<Controler> value14 = (Class<Controler>) Class.forName(DTControler.class.getCanonicalName());
			handledControlers.put("DTControler", value14);
			Class<Controler> value15 = (Class<Controler>) Class.forName(JailControler.class.getCanonicalName());
			handledControlers.put("JailControler", value15);
			Class<Controler> value17 = (Class<Controler>) Class.forName(CastleWarsPlaying.class.getCanonicalName());
			handledControlers.put("CastleWarsPlaying", value17);
			Class<Controler> value18 = (Class<Controler>) Class.forName(CastleWarsWaiting.class.getCanonicalName());
			handledControlers.put("CastleWarsWaiting", value18);
			// Class<Controler> value99 = (Class<Controler>) Class
			// .forName(DungeonControler.class.getCanonicalName());
			// handledControlers.put("DungeonControler", value99);
			Class<Controler> value20 = (Class<Controler>) Class.forName(NewHomeControler.class.getCanonicalName());
			handledControlers.put("NewHomeControler", value20);
			handledControlers.put("clan_wars_request",
					(Class<Controler>) Class.forName(RequestController.class.getCanonicalName()));
			handledControlers.put("clan_war", (Class<Controler>) Class.forName(WarControler.class.getCanonicalName()));
			handledControlers.put("clan_wars_ffa", (Class<Controler>) Class.forName(FfaZone.class.getCanonicalName()));
			handledControlers.put("NomadsRequiem",
					(Class<Controler>) Class.forName(NomadsRequiem.class.getCanonicalName()));
			handledControlers.put("BorkControler",
					(Class<Controler>) Class.forName(BorkControler.class.getCanonicalName()));
			handledControlers.put("BrimhavenAgility",
					(Class<Controler>) Class.forName(BrimhavenAgility.class.getCanonicalName()));
			handledControlers.put("FightCavesControler",
					(Class<Controler>) Class.forName(FightCaves.class.getCanonicalName()));
			handledControlers.put("FightKilnControler",
					(Class<Controler>) Class.forName(FightKiln.class.getCanonicalName()));
			handledControlers.put("FightPitsLobby",
					(Class<Controler>) Class.forName(FightPitsLobby.class.getCanonicalName()));
			handledControlers.put("FightPitsArena",
					(Class<Controler>) Class.forName(FightPitsArena.class.getCanonicalName()));
			handledControlers.put("PestControlGame",
					(Class<Controler>) Class.forName(PestControlGame.class.getCanonicalName()));
			handledControlers.put("PestControlLobby",
					(Class<Controler>) Class.forName(PestControlLobby.class.getCanonicalName()));
			handledControlers.put("Barrows", (Class<Controler>) Class.forName(Barrows.class.getCanonicalName()));
			handledControlers.put("RefugeOfFear",
					(Class<Controler>) Class.forName(RefugeOfFear.class.getCanonicalName()));
			handledControlers.put("Falconry", (Class<Controler>) Class.forName(Falconry.class.getCanonicalName()));
			handledControlers.put("QueenBlackDragonControler",
					(Class<Controler>) Class.forName(QueenBlackDragonController.class.getCanonicalName()));
			handledControlers.put("HouseControler",
					(Class<Controler>) Class.forName(HouseControler.class.getCanonicalName()));
			handledControlers.put("RuneSpanControler",
					(Class<Controler>) Class.forName(RunespanControler.class.getCanonicalName()));
			handledControlers.put("DeathEvent", (Class<Controler>) Class.forName(DeathEvent.class.getCanonicalName()));
			handledControlers.put("SorceressGarden",
					(Class<Controler>) Class.forName(SorceressGarden.class.getCanonicalName()));
			handledControlers.put("CrucibleControler",
					(Class<Controler>) Class.forName(CrucibleControler.class.getCanonicalName()));
			handledControlers.put("StealingCreationsGame",
					(Class<Controler>) Class.forName(StealingCreationGame.class.getCanonicalName()));
			handledControlers.put("StealingCreationsLobby",
					(Class<Controler>) Class.forName(StealingCreationLobby.class.getCanonicalName()));
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
