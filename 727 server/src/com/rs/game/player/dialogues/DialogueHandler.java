package com.rs.game.player.dialogues;

import java.util.HashMap;

import com.rs.game.player.dialogues.impl.AncientAltar;
import com.rs.game.player.dialogues.impl.AncientEffigiesD;
import com.rs.game.player.dialogues.impl.Banker;
import com.rs.game.player.dialogues.impl.BarrowsD;
import com.rs.game.player.dialogues.impl.BonfireD;
import com.rs.game.player.dialogues.impl.BootDwarf;
import com.rs.game.player.dialogues.impl.ClanWarsViewing;
import com.rs.game.player.dialogues.impl.ClimbEmoteStairs;
import com.rs.game.player.dialogues.impl.ClimbNoEmoteStairs;
import com.rs.game.player.dialogues.impl.CookingD;
import com.rs.game.player.dialogues.impl.DagonHai;
import com.rs.game.player.dialogues.impl.DiceBag;
import com.rs.game.player.dialogues.impl.DrogoDwarf;
import com.rs.game.player.dialogues.impl.EnchantedGemDialouge;
import com.rs.game.player.dialogues.impl.FightKilnDialogue;
import com.rs.game.player.dialogues.impl.FlamingSkull;
import com.rs.game.player.dialogues.impl.FletchingD;
import com.rs.game.player.dialogues.impl.ForfeitDialouge;
import com.rs.game.player.dialogues.impl.FremennikShipmaster;
import com.rs.game.player.dialogues.impl.GemCuttingD;
import com.rs.game.player.dialogues.impl.GeneralStore;
import com.rs.game.player.dialogues.impl.GrilleGoatsDialogue;
import com.rs.game.player.dialogues.impl.Guildmaster;
import com.rs.game.player.dialogues.impl.Hairdresser;
import com.rs.game.player.dialogues.impl.HerbloreD;
import com.rs.game.player.dialogues.impl.Hura;
import com.rs.game.player.dialogues.impl.KaramjaTrip;
import com.rs.game.player.dialogues.impl.LanderDialouge;
import com.rs.game.player.dialogues.impl.LeatherCraftingD;
import com.rs.game.player.dialogues.impl.LevelUp;
import com.rs.game.player.dialogues.impl.LunarAltar;
import com.rs.game.player.dialogues.impl.MagicPortal;
import com.rs.game.player.dialogues.impl.MakeOverMage;
import com.rs.game.player.dialogues.impl.Man;
import com.rs.game.player.dialogues.impl.Marv;
import com.rs.game.player.dialogues.impl.MasterOfFear;
import com.rs.game.player.dialogues.impl.MiningGuildDwarf;
import com.rs.game.player.dialogues.impl.NexEntrance;
import com.rs.game.player.dialogues.impl.Nurmof;
import com.rs.game.player.dialogues.impl.RewardChest;
import com.rs.game.player.dialogues.impl.RunespanPortalD;
import com.rs.game.player.dialogues.impl.Scavvo;
import com.rs.game.player.dialogues.impl.SetSkills;
import com.rs.game.player.dialogues.impl.SmeltingD;
import com.rs.game.player.dialogues.impl.SorceressGardenNPCs;
import com.rs.game.player.dialogues.impl.Thessalia;
import com.rs.game.player.dialogues.impl.Transportation;
import com.rs.game.player.dialogues.impl.Valaine;
import com.rs.game.player.dialogues.impl.WildernessDitch;
import com.rs.game.player.dialogues.impl.WizardFinix;
import com.rs.game.player.dialogues.impl.ZarosAltar;
import com.rs.utils.Logger;

public final class DialogueHandler {

	private static final HashMap<Object, Class<Dialogue>> handledDialogues = new HashMap<Object, Class<Dialogue>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Dialogue> value1 = (Class<Dialogue>) Class.forName(LevelUp.class.getCanonicalName());
			handledDialogues.put("LevelUp", value1);
			Class<Dialogue> value2 = (Class<Dialogue>) Class.forName(ZarosAltar.class.getCanonicalName());
			handledDialogues.put("ZarosAltar", value2);
			Class<Dialogue> value3 = (Class<Dialogue>) Class.forName(ClimbNoEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbNoEmoteStairs", value3);
			Class<Dialogue> value4 = (Class<Dialogue>) Class.forName(Banker.class.getCanonicalName());
			handledDialogues.put("Banker", value4);
			Class<Dialogue> value5 = (Class<Dialogue>) Class.forName(DestroyItemOption.class.getCanonicalName());
			handledDialogues.put("DestroyItemOption", value5);
			Class<Dialogue> value6 = (Class<Dialogue>) Class.forName(FremennikShipmaster.class.getCanonicalName());
			handledDialogues.put("FremennikShipmaster", value6);
			Class<Dialogue> value8 = (Class<Dialogue>) Class.forName(NexEntrance.class.getCanonicalName());
			handledDialogues.put("NexEntrance", value8);
			Class<Dialogue> value9 = (Class<Dialogue>) Class.forName(MagicPortal.class.getCanonicalName());
			handledDialogues.put("MagicPortal", value9);
			Class<Dialogue> value10 = (Class<Dialogue>) Class.forName(LunarAltar.class.getCanonicalName());
			handledDialogues.put("LunarAltar", value10);
			Class<Dialogue> value11 = (Class<Dialogue>) Class.forName(AncientAltar.class.getCanonicalName());
			handledDialogues.put("AncientAltar", value11);
			// TODO 12 and 13
			Class<Dialogue> value12 = (Class<Dialogue>) Class.forName(FletchingD.class.getCanonicalName());
			handledDialogues.put("FletchingD", value12);
			Class<Dialogue> value16 = (Class<Dialogue>) Class.forName(SimpleMessage.class.getCanonicalName());
			handledDialogues.put("SimpleMessage", value16);
			Class<Dialogue> value17 = (Class<Dialogue>) Class.forName(ItemMessage.class.getCanonicalName());
			handledDialogues.put("ItemMessage", value17);
			Class<Dialogue> value18 = (Class<Dialogue>) Class.forName(ClimbEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbEmoteStairs", value18);
			Class<Dialogue> value20 = (Class<Dialogue>) Class.forName(GemCuttingD.class.getCanonicalName());
			handledDialogues.put("GemCuttingD", value20);
			Class<Dialogue> value21 = (Class<Dialogue>) Class.forName(CookingD.class.getCanonicalName());
			handledDialogues.put("CookingD", value21);
			Class<Dialogue> value22 = (Class<Dialogue>) Class.forName(HerbloreD.class.getCanonicalName());
			handledDialogues.put("HerbloreD", value22);
			Class<Dialogue> value23 = (Class<Dialogue>) Class.forName(BarrowsD.class.getCanonicalName());
			handledDialogues.put("BarrowsD", value23);
			Class<Dialogue> value24 = (Class<Dialogue>) Class.forName(SmeltingD.class.getCanonicalName());
			handledDialogues.put("SmeltingD", value24);
			Class<Dialogue> value25 = (Class<Dialogue>) Class.forName(LeatherCraftingD.class.getCanonicalName());
			handledDialogues.put("LeatherCraftingD", value25);
			Class<Dialogue> value26 = (Class<Dialogue>) Class.forName(EnchantedGemDialouge.class.getCanonicalName());
			handledDialogues.put("EnchantedGemDialouge", value26);
			Class<Dialogue> value27 = (Class<Dialogue>) Class.forName(ForfeitDialouge.class.getCanonicalName());
			handledDialogues.put("ForfeitDialouge", value27);
			Class<Dialogue> value28 = (Class<Dialogue>) Class.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value28);
			Class<Dialogue> value29 = (Class<Dialogue>) Class.forName(WildernessDitch.class.getCanonicalName());
			handledDialogues.put("WildernessDitch", value29);
			Class<Dialogue> value30 = (Class<Dialogue>) Class.forName(SimpleNPCMessage.class.getCanonicalName());
			handledDialogues.put("SimpleNPCMessage", value30);
			Class<Dialogue> value31 = (Class<Dialogue>) Class.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value31);
			Class<Dialogue> value34 = (Class<Dialogue>) Class.forName(AncientEffigiesD.class.getCanonicalName());
			handledDialogues.put("AncientEffigiesD", value34);
			Class<Dialogue> value36 = (Class<Dialogue>) Class.forName(SetSkills.class.getCanonicalName());
			handledDialogues.put("SetSkills", value36);
			Class<Dialogue> value37 = (Class<Dialogue>) Class.forName(DismissD.class.getCanonicalName());
			handledDialogues.put("DismissD", value37);
			Class<Dialogue> value39 = (Class<Dialogue>) Class.forName(MakeOverMage.class.getCanonicalName());
			handledDialogues.put("MakeOverMage", value39);
			Class<Dialogue> value40 = (Class<Dialogue>) Class.forName(KaramjaTrip.class.getCanonicalName());
			handledDialogues.put("KaramjaTrip", value40);
			Class<Dialogue> value42 = (Class<Dialogue>) Class.forName(DagonHai.class.getCanonicalName());
			handledDialogues.put("DagonHai", value42);
			handledDialogues.put("clan_wars_view",
					(Class<Dialogue>) Class.forName(ClanWarsViewing.class.getCanonicalName()));
			handledDialogues.put("DiceBag", (Class<Dialogue>) Class.forName(DiceBag.class.getCanonicalName()));
			handledDialogues.put("DrogoDwarf", (Class<Dialogue>) Class.forName(DrogoDwarf.class.getCanonicalName()));
			handledDialogues.put("GeneralStore",
					(Class<Dialogue>) Class.forName(GeneralStore.class.getCanonicalName()));
			handledDialogues.put("Nurmof", (Class<Dialogue>) Class.forName(Nurmof.class.getCanonicalName()));
			handledDialogues.put("BootDwarf", (Class<Dialogue>) Class.forName(BootDwarf.class.getCanonicalName()));
			handledDialogues.put("MiningGuildDwarf",
					(Class<Dialogue>) Class.forName(MiningGuildDwarf.class.getCanonicalName()));
			handledDialogues.put("Man", (Class<Dialogue>) Class.forName(Man.class.getCanonicalName()));
			handledDialogues.put("Guildmaster", (Class<Dialogue>) Class.forName(Guildmaster.class.getCanonicalName()));
			handledDialogues.put("Scavvo", (Class<Dialogue>) Class.forName(Scavvo.class.getCanonicalName()));
			handledDialogues.put("Valaine", (Class<Dialogue>) Class.forName(Valaine.class.getCanonicalName()));
			handledDialogues.put("Hura", (Class<Dialogue>) Class.forName(Hura.class.getCanonicalName()));
			handledDialogues.put("LanderD", (Class<Dialogue>) Class.forName(LanderDialouge.class.getCanonicalName()));
			handledDialogues.put("MasterOfFear",
					(Class<Dialogue>) Class.forName(MasterOfFear.class.getCanonicalName()));
			handledDialogues.put("SimplePlayerMessage",
					(Class<Dialogue>) Class.forName(SimplePlayerMessage.class.getCanonicalName()));
			handledDialogues.put("BonfireD", (Class<Dialogue>) Class.forName(BonfireD.class.getCanonicalName()));
			handledDialogues.put("FightKilnDialogue",
					(Class<Dialogue>) Class.forName(FightKilnDialogue.class.getCanonicalName()));
			handledDialogues.put("RewardChest", (Class<Dialogue>) Class.forName(RewardChest.class.getCanonicalName()));
			handledDialogues.put("WizardFinix", (Class<Dialogue>) Class.forName(WizardFinix.class.getCanonicalName()));
			handledDialogues.put("RunespanPortalD",
					(Class<Dialogue>) Class.forName(RunespanPortalD.class.getCanonicalName()));
			handledDialogues.put("SorceressGardenNPCs",
					(Class<Dialogue>) Class.forName(SorceressGardenNPCs.class.getCanonicalName()));
			handledDialogues.put("Marv", (Class<Dialogue>) Class.forName(Marv.class.getCanonicalName()));
			handledDialogues.put("FlamingSkull",
					(Class<Dialogue>) Class.forName(FlamingSkull.class.getCanonicalName()));
			handledDialogues.put("Hairdresser", (Class<Dialogue>) Class.forName(Hairdresser.class.getCanonicalName()));
			handledDialogues.put("Thessalia", (Class<Dialogue>) Class.forName(Thessalia.class.getCanonicalName()));
			handledDialogues.put("GrilleGoatsD",
					(Class<Dialogue>) Class.forName(GrilleGoatsDialogue.class.getCanonicalName()));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void reload() {
		handledDialogues.clear();
		init();
	}

	public static final Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue)
			return (Dialogue) key;
		Class<Dialogue> classD = handledDialogues.get(key);
		if (classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}

	private DialogueHandler() {

	}
}
