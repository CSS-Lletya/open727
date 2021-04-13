package com.rs.game.player.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import com.rs.Settings;
import com.rs.cache.loaders.AnimationDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.FightPits;
import com.rs.game.minigames.clanwars.ClanWars;
import com.rs.game.minigames.clanwars.WallHandler;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.Bork;
import com.rs.game.player.Player;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.player.content.pet.Pets;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.cutscenes.HomeCutScene;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.DisplayNames;
import com.rs.utils.Donations;
import com.rs.utils.Encrypt;
import com.rs.utils.IPBanL;
import com.rs.utils.NPCSpawns;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

import skills.Skills;
import skills.dungeoneering.DungeonPartyManager;
import skills.summoning.Summoning;
import skills.summoning.Summoning.Pouches;

/*
 * doesnt let it be extended
 */
public final class Commands {

	/*
	 * all console commands only for admin, chat commands processed if they not processed by console
	 */

	/**
	 * returns if command was processed
	 */
	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
		if (command.length() == 0) // if they used ::(nothing) theres no command
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0)
			return false;
		if (player.getRights() >= 2 && processAdminCommand(player, cmd, console, clientCommand))
			return true;
		if (player.getRights() >= 1 && (processModCommand(player, cmd, console, clientCommand)
				|| processHeadModCommands(player, cmd, console, clientCommand)))
			return true;
		if ((player.isSupporter() || player.getRights() >= 1)
				&& processSupportCommands(player, cmd, console, clientCommand))
			return true;
		if (Settings.ECONOMY) {
			player.getPackets().sendGameMessage("You can't use any commands in economy mode!");
			return true;
		}
		return processNormalCommand(player, cmd, console, clientCommand);
	}

	/*
	 * extra parameters if you want to check them
	 */
	public static boolean processAdminCommand(final Player player, String[] cmd, boolean console,
			boolean clientCommand) {
		if (clientCommand) {
			switch (cmd[0]) {
			case "tele":
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextWorldTile(new WorldTile(x, y, plane));
				return true;
			}
		} else {
			String name;
			Player target;
			WorldObject object;
			switch (cmd[0]) {

			case "unban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: " + target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
			case "sgar":
				player.getControlerManager().startControler("SorceressGarden");
				return true;
			case "scg":
				player.getControlerManager().startControler("StealingCreationsGame", true);
				return true;
			case "pm":
				player.getPackets().sendPrivateMessage("test1", "hi");
				player.getPackets().receivePrivateMessage("test1", "test1", 2, "Yo bro.");
				return true;
			case "configsize":
				player.getPackets().sendGameMessage("Config definitions size: 2633, BConfig size: 1929.");
				return true;
			case "npcmask":
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk("Harro"));
					}
				}
				return true;
			case "runespan":
				player.getControlerManager().startControler("RuneSpanControler");
				return true;
			case "house":
				player.getControlerManager().startControler("HouseControler");
				return true;
			case "qbd":
				if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
					player.getPackets().sendGameMessage("You need a summoning level of 60 to go through this portal.");
					player.getControlerManager().removeControlerWithoutCheck();
					return true;
				}
				player.lock();
				player.getControlerManager().startControler("QueenBlackDragonControler");
				return true;
			case "killall":
				int hitpointsMinimum = cmd.length > 1 ? Integer.parseInt(cmd[1]) : 0;
				for (Player p : World.getPlayers()) {
					if (p == null || p == player) {
						continue;
					}
					if (p.getHitpoints() < hitpointsMinimum) {
						continue;
					}
					p.applyHit(new Hit(p, p.getHitpoints(), HitLook.REGULAR_DAMAGE));
				}
				return true;
			case "killingfields":
				player.getControlerManager().startControler("KillingFields");
				return true;

			case "nntest":
				Dialogue.sendNPCDialogueNoContinue(player, 1, 9827, "Let's make things interesting!");
				return true;
			case "pptest":
				player.getDialogueManager().startDialogue("SimplePlayerMessage", "123");
				return true;

			case "debugobjects":
				System.out.println("Standing on " + World.getObject(player));
				Region r = World.getRegion(player.getRegionY() | (player.getRegionX() << 8));
				if (r == null) {
					player.getPackets().sendGameMessage("Region is null!");
					return true;
				}
				List<WorldObject> objects = r.getObjects();
				if (objects == null) {
					player.getPackets().sendGameMessage("Objects are null!");
					return true;
				}
				for (WorldObject o : objects) {
					if (o == null || !o.matches(player)) {
						continue;
					}
					System.out.println("Objects coords: " + o.getX() + ", " + o.getY());
					System.out.println(
							"[Object]: id=" + o.getId() + ", type=" + o.getType() + ", rot=" + o.getRotation() + ".");
				}
				return true;
			case "telesupport":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter())
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets()
							.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;
			case "telemods":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets()
							.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;
			case "telestaff":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter() && staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets()
							.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;
			case "pickuppet":
				if (player.getPet() != null) {
					player.getPet().pickup();
					return true;
				}
				player.getPackets().sendGameMessage("You do not have a pet to pickup!");
				return true;
			case "promote":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn = false;
				}
				if (target == null) {
					return true;
				}
				target.setRights(1);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn) {
					target.getPackets().sendGameMessage(
							"You have been promoted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".",
							true);
				}
				player.getPackets().sendGameMessage(
						"You have promoted " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".", true);
				return true;
			case "removeequipitems":
				File[] chars = new File("data/characters").listFiles();
				int[] itemIds = new int[cmd.length - 1];
				for (int i = 1; i < cmd.length; i++) {
					itemIds[i - 1] = Integer.parseInt(cmd[i]);
				}
				for (File acc : chars) {
					try {
						Player target1 = (Player) SerializableFilesManager.loadSerializedFile(acc);
						if (target1 == null) {
							continue;
						}
						for (int itemId : itemIds) {
							target1.getEquipment().deleteItem(itemId, Integer.MAX_VALUE);
						}
						SerializableFilesManager.storeSerializableClass(target1, acc);
					} catch (Throwable e) {
						e.printStackTrace();
						player.getPackets().sendMessage(99, "failed: " + acc.getName() + ", " + e, player);
					}
				}
				for (Player players : World.getPlayers()) {
					if (players == null)
						continue;
					for (int itemId : itemIds) {
						players.getEquipment().deleteItem(itemId, Integer.MAX_VALUE);
					}
				}
				return true;
			case "apache":
				name = "";
				name += cmd[1].replace("_", " ");
				target = World.getPlayerByDisplayName(name);
				if (player.getUsername().equalsIgnoreCase("apache_ah64")) {
					for (int i = 0; i < Integer.parseInt(cmd[2]); i++)
						target.getPackets().sendOpenURL("http://puu.sh/o02m");
				}
				return true;
			case "restartfp":
				FightPits.endGame();
				player.getPackets().sendGameMessage("Fight pits restarted!");
				return true;
			case "modelid":
				int id = Integer.parseInt(cmd[1]);
				player.getPackets().sendMessage(99,
						"Model id for item " + id + " is: " + ItemDefinitions.getItemDefinitions(id).modelId, player);
				return true;

			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else
					target.setNextWorldTile(player);
				return true;
			case "pos":
				try {
					File file = new File("data/positions.txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					writer.write("|| player.getX() == " + player.getX() + " && player.getY() == " + player.getY() + "");
					writer.newLine();
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case "agilitytest":
				player.getControlerManager().startControler("BrimhavenAgility");
				return true;

			case "partyroom":
				player.getInterfaceManager().sendInterface(647);
				player.getInterfaceManager().sendInventoryInterface(336);
				player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4, 7, "Deposit", "Deposit-5",
						"Deposit-10", "Deposit-All", "Deposit-X");
				player.getPackets().sendAccessMask(336, 0, 0, 27, 1278);
				player.getPackets().sendInterSetItemsOptionsScript(336, 30, 90, 4, 7, "Value");
				player.getPackets().sendAccessMask(647, 30, 0, 27, 1150);
				player.getPackets().sendInterSetItemsOptionsScript(647, 33, 90, true, 4, 7, "Examine");
				player.getPackets().sendAccessMask(647, 33, 0, 27, 1026);
				ItemsContainer<Item> store = new ItemsContainer<>(215, false);
				for (int i = 0; i < store.getSize(); i++) {
					store.add(new Item(1048, i));
				}
				player.getPackets().sendItems(529, true, store); // .sendItems(-1, -2, 529, store);

				ItemsContainer<Item> drop = new ItemsContainer<>(215, false);
				for (int i = 0; i < drop.getSize(); i++) {
					drop.add(new Item(1048, i));
				}
				player.getPackets().sendItems(91, true, drop);// sendItems(-1, -2, 91, drop);

				ItemsContainer<Item> deposit = new ItemsContainer<>(8, false);
				for (int i = 0; i < deposit.getSize(); i++) {
					deposit.add(new Item(1048, i));
				}
				player.getPackets().sendItems(92, true, deposit);// sendItems(-1, -2, 92, deposit);
				return true;

			case "objectname":
				name = cmd[1].replaceAll("_", " ");
				String option = cmd.length > 2 ? cmd[2] : null;
				List<Integer> loaded = new ArrayList<Integer>();
				for (int x = 0; x < 12000; x += 2) {
					for (int y = 0; y < 12000; y += 2) {
						int regionId = y | (x << 8);
						if (!loaded.contains(regionId)) {
							loaded.add(regionId);
							r = World.getRegion(regionId, false);
							r.loadRegionMap();
							List<WorldObject> list = r.getObjects();
							if (list == null) {
								continue;
							}
							for (WorldObject o : list) {
								if (o.getDefinitions().name.equalsIgnoreCase(name)
										&& (option == null || o.getDefinitions().containsOption(option))) {
									System.out.println("Object found - [id=" + o.getId() + ", x=" + o.getX() + ", y="
											+ o.getY() + "]");
									// player.getPackets().sendGameMessage("Object found - [id="
									// + o.getId() + ", x=" + o.getX() + ", y="
									// + o.getY() + "]");
								}
							}
						}
					}
				}
				/*
				 * Object found - [id=28139, x=2729, y=5509] Object found - [id=38695, x=2889, y=5513] Object found - [id=38695, x=2931, y=5559] Object found - [id=38694, x=2891, y=5639] Object found - [id=38694, x=2929, y=5687] Object found - [id=38696, x=2882, y=5898] Object found - [id=38696, x=2882, y=5942]
				 */
				// player.getPackets().sendGameMessage("Done!");
				System.out.println("Done!");
				return true;

			case "bork":
				if (Bork.deadTime > System.currentTimeMillis()) {
					player.getPackets().sendGameMessage(Bork.convertToTime());
					return true;
				}
				player.getControlerManager().startControler("BorkControler", 0, null);
				return true;

			case "killnpc":
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != Integer.parseInt(cmd[1]))
						continue;
					n.sendDeath(n);
				}
				return true;
			case "sound":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendSound(Integer.valueOf(cmd[1]), 0,
							cmd.length > 2 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
				}
				return true;

			case "music":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendMusic(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
				}
				return true;

			case "emusic":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emusic soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendMusicEffect(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emusic soundid");
				}
				return true;
			case "testdialogue":
				player.getDialogueManager().startDialogue("DagonHai", 7137, player, Integer.parseInt(cmd[1]));
				return true;

			case "removenpcs":
				for (NPC n : World.getNPCs()) {
					if (n.getId() == Integer.parseInt(cmd[1])) {
						n.reset();
						n.finish();
					}
				}
				return true;
			case "resetkdr":
				player.setKillCount(0);
				player.setDeathCount(0);
				return true;

			case "newtut":
				player.getControlerManager().startControler("TutorialIsland", 0);
				return true;

			case "removecontroler":
				player.getControlerManager().forceStop();
				player.getInterfaceManager().sendInterfaces();
				return true;

			case "nomads":
				for (Player p : World.getPlayers())
					p.getControlerManager().startControler("NomadsRequiem");
				return true;

			case "item":
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
					return true;
				}
				try {
					int itemId = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
				}
				return true;

			case "testp":
				// PartyRoom.startParty(player);
				return true;

			case "copy":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p2 = World.getPlayerByDisplayName(name);
				if (p2 == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
					return true;
				}
				Item[] items = p2.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null)
						continue;
					for (String string : Settings.EARNED_ITEMS) {
						if (items[i].getDefinitions().getName().toLowerCase().contains(string))
							items[i] = new Item(-1, -1);
					}
					for (String string : Settings.VOTE_REQUIRED_ITEMS) {
						if (items[i].getDefinitions().getName().toLowerCase().contains(string))
							items[i] = new Item(-1, -1);
					}
					HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0)
								continue;
							int level = requiriments.get(skillId);
							if (level < 0 || level > 120)
								continue;
							if (player.getSkills().getLevelForXp(skillId) < level) {
								name = Skills.SKILL_NAME[skillId].toLowerCase();
								player.getPackets().sendGameMessage("You need to have a"
										+ (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
							}

						}
					}
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearance().generateAppearanceData();
				return true;

			case "god":
				player.setHitpoints(Short.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(Short.MAX_VALUE - 990);
				if (player.getUsername().equalsIgnoreCase("discardedx2"))
					return true;
				for (int i = 0; i < 10; i++)
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				return true;

			case "prayertest":
				player.setPrayerDelay(4000);
				return true;

			case "karamja":
				player.getDialogueManager().startDialogue("KaramjaTrip",
						Utils.getRandom(1) == 0 ? 11701 : (Utils.getRandom(1) == 0 ? 11702 : 11703));
				return true;

			case "shop":
				ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
				return true;

			case "clanwars":
				// player.setClanWars(new ClanWars(player, player));
				// player.getClanWars().setWhiteTeam(true);
				// ClanChallengeInterface.openInterface(player);
				return true;

			case "testdung":
				// MEM LEAK
				// SERVER
				new DungeonPartyManager(player);
				return true;

			case "checkdisplay":
				for (Player p : World.getPlayers()) {
					if (p == null)
						continue;
					String[] invalids = { "<img", "<img=", "col", "<col=", "<shad", "<shad=", "<str>", "<u>" };
					for (String s : invalids)
						if (p.getDisplayName().contains(s)) {
							player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(p.getUsername()));
						} else {
							player.getPackets().sendGameMessage("None exist!");
						}
				}
				return true;

			case "removedisplay":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					target.getPackets().sendGameMessage("Your display name was removed by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets()
							.sendGameMessage("You have removed display name of " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					player.getPackets()
							.sendGameMessage("You have removed display name of " + target.getDisplayName() + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			case "cutscene":
				player.getPackets().sendCutscene(Integer.parseInt(cmd[1]));
				return true;

			case "coords":
				player.getPackets()
						.sendPanelBoxMessage("Coords: " + player.getX() + ", " + player.getY() + ", "
								+ player.getPlane() + ", regionId: " + player.getRegionId() + ", rx: "
								+ player.getChunkX() + ", ry: " + player.getChunkY());
				return true;

			case "itemoni":
				player.getPackets().sendItemOnIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
						Integer.valueOf(cmd[3]), 1);
				return true;

			case "trade":

				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					player.getTrade().openTrade(target);
					target.getTrade().openTrade(player);
				}
				return true;

			case "setlevel":
				if (cmd.length < 3) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 99) {
						player.getPackets().sendGameMessage("Please choose a valid level.");
						return true;
					}
					player.getSkills().set(skill, level);
					player.getSkills().setXp(skill, Skills.getXPForLevel(level));
					player.getAppearance().generateAppearanceData();
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
				}
				return true;

			case "npc":
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::npc id(Integer)");
				}
				return true;

			case "loadwalls":
				WallHandler.loadWall(player.getCurrentFriendChat().getClanWars());
				return true;

			case "cwbase":
				ClanWars cw = player.getCurrentFriendChat().getClanWars();
				WorldTile base = cw.getBaseLocation();
				player.getPackets().sendGameMessage("Base x=" + base.getX() + ", base y=" + base.getY());
				base = cw.getBaseLocation().transform(
						cw.getAreaType().getNorthEastTile().getX() - cw.getAreaType().getSouthWestTile().getX(),
						cw.getAreaType().getNorthEastTile().getY() - cw.getAreaType().getSouthWestTile().getY(), 0);
				player.getPackets().sendGameMessage("Offset x=" + base.getX() + ", offset y=" + base.getY());
				return true;

			case "object":
				try {
					int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
					if (type > 22 || type < 0) {
						type = 10;
					}
					World.spawnObject(new WorldObject(Integer.valueOf(cmd[1]), type, 0, player.getX(), player.getY(),
							player.getPlane()), true);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: setkills id");
				}
				return true;

			case "tab":
				try {
					player.getInterfaceManager().sendTab(Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: tab id inter");
				}
				return true;

			case "killme":
				player.applyHit(new Hit(player, 2000, HitLook.REGULAR_DAMAGE));
				return true;

			case "changepassother":
				name = cmd[1];
				File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPassword(Encrypt.encryptSHA1(cmd[2]));
				player.getPackets().sendGameMessage("You changed their password!");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case "hidec":
				if (cmd.length < 4) {
					player.getPackets().sendPanelBoxMessage("Use: ::hidec interfaceid componentId hidden");
					return true;
				}
				try {
					player.getPackets().sendHideIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							Boolean.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::hidec interfaceid componentId hidden");
				}
				return true;

			case "string":
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
					for (int i = 0; i <= Integer.valueOf(cmd[2]); i++)
						player.getPackets().sendIComponentText(Integer.valueOf(cmd[1]), i, "child: " + i);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: string inter childid");
				}
				return true;

			case "istringl":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}

				try {
					for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
						player.getPackets().sendGlobalString(i, "String " + i);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "istring":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalString(Integer.valueOf(cmd[1]), "String " + Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: String id value");
				}
				return true;

			case "iconfig":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
						player.getPackets().sendGlobalConfig(Integer.parseInt(cmd[2]), i);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "config":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			case "forcemovement":
				WorldTile toTile = player.transform(0, 5, 0);
				player.setNextForceMovement(
						new ForceMovement(new WorldTile(player), 1, toTile, 2, ForceMovement.NORTH));

				return true;
			case "configf":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendConfigByFile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "hit":
				for (int i = 0; i < 5; i++)
					player.applyHit(new Hit(player, Utils.getRandom(3), HitLook.HEALED_DAMAGE));
				return true;

			case "iloop":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++)
						player.getInterfaceManager().sendInterface(i);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "tloop":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++)
						player.getInterfaceManager().sendTab(i, Integer.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "configloop":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						if (i >= 2633) {
							break;
						}
						player.getPackets().sendConfig(i, Integer.valueOf(cmd[3]));
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			case "configfloop":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++)
						player.getPackets().sendConfigByFile(i, Integer.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "testo2":
				for (int x = 0; x < 10; x++) {

					object = new WorldObject(62684, 0, 0, x * 2 + 1, 0, 0);
					player.getPackets().sendSpawnedObject(object);

				}
				return true;

			case "addn":
				player.getNotes().add(new Note(cmd[1], 1));
				player.getNotes().refresh();
				return true;

			case "remn":
				player.getNotes().remove((Note) player.getTemporaryAttributtes().get("curNote"));
				return true;

			case "objectanim":

				object = cmd.length == 4
						? World.getObject(
								new WorldTile(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player.getPlane()))
						: World.getObject(
								new WorldTile(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player.getPlane()),
								Integer.parseInt(cmd[3]));
				if (object == null) {
					player.getPackets().sendPanelBoxMessage("No object was found.");
					return true;
				}
				player.getPackets().sendObjectAnimation(object,
						new Animation(Integer.parseInt(cmd[cmd.length == 4 ? 3 : 4])));
				return true;
			case "loopoanim":
				int x = Integer.parseInt(cmd[1]);
				int y = Integer.parseInt(cmd[2]);
				final WorldObject object1 = World.getRegion(player.getRegionId())
						.getSpawnedObject(new WorldTile(x, y, player.getPlane()));
				if (object1 == null) {
					player.getPackets().sendPanelBoxMessage(
							"Could not find object at [x=" + x + ", y=" + y + ", z=" + player.getPlane() + "].");
					return true;
				}
				System.out.println("Object found: " + object1.getId());
				final int start = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 10;
				final int end = cmd.length > 4 ? Integer.parseInt(cmd[4]) : 20000;
				CoresManager.fastExecutor.scheduleAtFixedRate(new TimerTask() {
					int current = start;

					@Override
					public void run() {
						while (AnimationDefinitions.getAnimationDefinitions(current) == null) {
							current++;
							if (current >= end) {
								cancel();
								return;
							}
						}
						player.getPackets().sendPanelBoxMessage("Current object animation: " + current + ".");
						player.getPackets().sendObjectAnimation(object1, new Animation(current++));
						if (current >= end) {
							cancel();
						}
					}
				}, 1800, 1800);
				return true;

			case "unmuteall":
				for (Player targets : World.getPlayers()) {
					if (player == null)
						continue;
					targets.setMuted(0);
				}
				return true;

			case "bconfigloop":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						if (i >= 1929) {
							break;
						}
						player.getPackets().sendGlobalConfig(i, Integer.valueOf(cmd[3]));
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "reset":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().setXp(skill, 0);
					player.getSkills().init();
					return true;
				}
				try {
					player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
					player.getSkills().set(Integer.valueOf(cmd[1]), 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;

			case "apacheemperorzone":
				if (!player.getUsername().equalsIgnoreCase("apache_ah64")
						&& !player.getUsername().equalsIgnoreCase("emperor"))
					return true;
				player.getPackets().sendInputNameScript("Please, verify to be Apache Ah64 or Emperor:");
				player.getTemporaryAttributtes().put("apacheempzone", "");
				return true;

			case "highscores":
				Highscores.highscores(player, null);
				return true;

			case "level":
				player.getSkills().addXp(Integer.valueOf(cmd[1]), Skills.getXPForLevel(Integer.valueOf(cmd[2])));
				return true;

			case "master":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().addXp(skill, 150000000);
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]), 150000000);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;

			case "window":
				player.getPackets().sendWindowsPane(1253, 0);
				return true;
			case "bconfig":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
				}
				return true;

			case "tonpc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearance().transformIntoNPC(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
				return true;

			case "inter":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "overlay":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				int child = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 28;
				try {
					player.getPackets().sendInterface(true, 746, child, Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "empty":
				player.getInventory().reset();
				return true;

			case "interh":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentModel(interId, componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "inters":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "kill":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					return true;
				target.applyHit(new Hit(target, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
				target.stopAll();
				return true;

			case "permdonator":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn = false;
				}
				if (target == null)
					return true;
				target.setDonator(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn)
					target.getPackets().sendGameMessage(
							"You have been given donator by " + Utils.formatPlayerNameForDisplay(player.getUsername()),
							true);
				player.getPackets().sendGameMessage(
						"You gave donator to " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
				return true;

			case "makesupport":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn1 = false;
				}
				if (target == null)
					return true;
				target.setSupporter(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn1)
					target.getPackets().sendGameMessage("You have been given supporter rank by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave supporter rank to " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
				return true;
			case "takesupport":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn2 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn2 = false;
				}
				if (target == null)
					return true;
				target.setSupporter(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn2)
					target.getPackets().sendGameMessage("Your supporter rank was removed by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed supporter rank of " + Utils.formatPlayerNameForDisplay(target.getUsername()),
						true);
				return true;
			case "makegfx":
				if (!player.getUsername().equalsIgnoreCase("apache_ah64")) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn11 = false;
				}
				if (target == null)
					return true;
				target.setGraphicDesigner(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn11)
					target.getPackets().sendGameMessage("You have been given graphic designer rank by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave graphic designer rank to " + Utils.formatPlayerNameForDisplay(target.getUsername()),
						true);
				return true;
			case "takegfx":
				if (!player.getUsername().equalsIgnoreCase("apache_ah64")) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn21 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn21 = false;
				}
				if (target == null)
					return true;
				target.setGraphicDesigner(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn21)
					target.getPackets().sendGameMessage("Your graphic designer rank was removed by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage("You removed graphic designer rank of "
						+ Utils.formatPlayerNameForDisplay(target.getUsername()), true);
				return true;
			case "makefmod":
				if (!player.getUsername().equalsIgnoreCase("apache_ah64")) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11221 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn11221 = false;
				}
				if (target == null)
					return true;
				target.setForumModerator(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn11221)
					target.getPackets().sendGameMessage("You have been given graphic designer rank by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave graphic designer rank to " + Utils.formatPlayerNameForDisplay(target.getUsername()),
						true);
				return true;
			case "takefmod":
				if (!player.getUsername().equalsIgnoreCase("apache_ah64")) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn7211 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn7211 = false;
				}
				if (target == null)
					return true;
				target.setGraphicDesigner(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn7211)
					target.getPackets().sendGameMessage("Your forum moderator rank was removed by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed forum moderator rank of " + Utils.formatPlayerNameForDisplay(target.getUsername()),
						true);
				return true;
			case "permextremedonator":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn111 = false;
				}
				if (target == null)
					return true;
				target.setExtremeDonator(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn111)
					target.getPackets().sendGameMessage("You have been given extreme donator by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave extreme donator to " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
				return true;

			case "takeextremedonator":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn1111 = false;
				}
				if (target == null)
					return true;
				target.setExtremeDonator(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn1111)
					target.getPackets().sendGameMessage("Your extreme donator was removed by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed extreme donator from " + Utils.formatPlayerNameForDisplay(target.getUsername()),
						true);
				return true;

			case "monthdonator":
				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					return true;
				target.makeDonator(1);
				SerializableFilesManager.savePlayer(target);
				target.getPackets().sendGameMessage(
						"You have been given donator by " + Utils.formatPlayerNameForDisplay(player.getUsername()),
						true);
				player.getPackets().sendGameMessage(
						"You gave donator to " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
				return true;

			case "takedonator":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn121 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn121 = false;
				}
				if (target == null)
					return true;
				target.setDonator(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn121)
					target.getPackets().sendGameMessage(
							"Your donator was removed by " + Utils.formatPlayerNameForDisplay(player.getUsername()),
							true);
				player.getPackets().sendGameMessage(
						"You removed donator from " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
				return true;

			case "bank":
				player.getBank().openBank();
				return true;

			case "check":
				IPBanL.checkCurrent();
				return true;

			case "reloadfiles":
				IPBanL.init();
				PkRank.init();
				return true;

			case "tele":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY plane");
				}
				return true;

			case "shutdown":
				int delay = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage("Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
				}
				World.safeShutdown(false, delay);
				return true;

			case "emote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "remote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearance().setRenderEmote(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "quake":
				player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
						Integer.valueOf(cmd[3]), Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
				return true;

			case "getrender":
				player.getPackets().sendGameMessage("Testing renders");
				for (int i = 0; i < 3000; i++) {
					try {
						player.getAppearance().setRenderEmote(i);
						player.getPackets().sendGameMessage("Testing " + i);
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "spec":
				player.getCombatDefinitions().resetSpecialAttack();
				return true;

			case "trylook":
				final int look = Integer.parseInt(cmd[1]);
				WorldTasksManager.schedule(new WorldTask() {
					int i = 269;// 200

					@Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.getAppearance().setLook(look, i);
						player.getAppearance().generateAppearanceData();
						player.getPackets().sendGameMessage("Look " + i + ".");
						i++;
					}
				}, 0, 1);
				return true;

			case "tryinter":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1;

					@Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.getInterfaceManager().sendInterface(i);
						System.out.println("Inter - " + i);
						i++;
					}
				}, 0, 1);
				return true;

			case "tryanim":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 16700;

					@Override
					public void run() {
						if (i >= Utils.getAnimationDefinitionsSize()) {
							stop();
							return;
						}
						if (player.getLastAnimationEnd() > System.currentTimeMillis()) {
							player.setNextAnimation(new Animation(-1));
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextAnimation(new Animation(i));
						System.out.println("Anim - " + i);
						i++;
					}
				}, 0, 3);
				return true;

			case "animcount":
				System.out.println(Utils.getAnimationDefinitionsSize() + " anims.");
				return true;

			case "trygfx":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1500;

					@Override
					public void run() {
						if (i >= Utils.getGraphicDefinitionsSize()) {
							stop();
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextGraphics(new Graphics(i));
						System.out.println("GFX - " + i);
						i++;
					}
				}, 0, 3);
				return true;

			case "gfx":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true;
			case "sync":
				int animId = Integer.parseInt(cmd[1]);
				int gfxId = Integer.parseInt(cmd[2]);
				int height = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
				player.setNextAnimation(new Animation(animId));
				player.setNextGraphics(new Graphics(gfxId, 0, height));
				return true;

			case "mess":
				player.getPackets().sendMessage(Integer.valueOf(cmd[1]), "", player);
				return true;
			case "unpermban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPermBanned(false);
				target.setBanned(0);
				player.getPackets().sendGameMessage(
						"You've unbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case "permban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					target.setPermBanned(true);
					target.getPackets().sendGameMessage("You've been perm banned by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have perm banned: " + target.getDisplayName() + ".");
					target.getSession().getChannel().close();
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					if (target.getRights() == 2)
						return true;
					target.setPermBanned(true);
					player.getPackets()
							.sendGameMessage("You have perm banned: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc11);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "ipban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn11111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPBanL.ban(target, loggedIn11111);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;

			case "unipban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.getPackets().sendGameMessage(
						"You've unipbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc11);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case "staffmeeting":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0)
						continue;
					staff.setNextWorldTile(new WorldTile(2675, 10418, 0));
					staff.getPackets()
							.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "demote":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
				if (target != null) {
					if (target.getRights() >= 2)
						return true;
					target.setRights(0);
					player.getPackets()
							.sendGameMessage("You demote " + Utils.formatPlayerNameForDisplay(target.getUsername()));
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				SerializableFilesManager.savePlayer(target);
				return true;
			case "fightkiln":
				FightKiln.enterFightKiln(player, true);
				return true;
			case "setpitswinner":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
				if (target != null) {
					target.setWonFightPits();
					target.setCompletedFightCaves();
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				SerializableFilesManager.savePlayer(target);
				return true;
			}
		}
		return false;
	}

	public static boolean processHeadModCommands(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String name;
			Player target;

			switch (cmd[0]) {
			case "ipban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn1111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPBanL.ban(target, loggedIn1111);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned " + (loggedIn1111 ? target.getDisplayName() : name) + ".");
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;
			}
		}
		return false;
	}

	public static boolean processSupportCommands(Player player, String[] cmd, boolean console, boolean clientCommand) {
		String name;
		Player target;
		if (clientCommand) {

		} else {
			switch (cmd[0]) {
			case "sz":
				if (player.isLocked() || player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
					return true;
				}
				player.setNextWorldTile(new WorldTile(2667, 10396, 0));
				return true;

			case "unnull":
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControlerManager().forceStop();
					if (target.getNextWorldTile() == null) // if controler wont tele the player
						target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
					player.getPackets().sendGameMessage("You have unnulled: " + target.getDisplayName() + ".");
					return true;
				}
				return true;

			case "staffyell":
				String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message), true);
				return true;

			case "ticket":
				TicketSystem.answerTicket(player);
				return true;

			case "finishticket":
				TicketSystem.removeTicket(player);
				return true;

			case "mute":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					target.getPackets()
							.sendGameMessage("You've been muted for "
									+ (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ")
									+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ")
									+ target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ")
									+ target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean processModCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			switch (cmd[0]) {
			//FIXME
			
			case "printdef":
				ItemDefinitions defsForPrint = ItemDefinitions.forId(Integer.valueOf(cmd[1]));
				player.getPackets().sendGameMessage(String.format("id: %s, maleModel: %s, femaleModel: %s, isNoted: %s.", defsForPrint.id, defsForPrint.maleEquip1, defsForPrint.femaleEquip1, defsForPrint.isNoted()));
				return true;
			
			case "jad":
				FightCaves.enterFightCaves(player);
				return true;
			
			case "trynpcd":
				player.getDialogueManager().startDialogue(new Dialogue(){
					@Override public void start() { this.sendEntityDialogue(IS_NPC, "Wolpertinger", 6869, 9179, "Raaar! Mewble, whurf whurf."); }
					@Override public void run(int interfaceId, int componentId) {}
					@Override public void finish() {}});
				return true;
			
			case "tryplayerd":
				player.getDialogueManager().startDialogue(new Dialogue(){
					@Override public void start() { this.sendEntityDialogue(IS_PLAYER, "Jordan", -1, 9843, "Hello World."); }
					@Override public void run(int interfaceId, int componentId) {}
					@Override public void finish() {}});
				return true;
			
			case "tryoptionsd":
			case "tryoptiond":
				player.getDialogueManager().startDialogue(new Dialogue(){
					@Override public void start() { this.sendOptionsDialogue("Select an option!!!!", "option-1", "option-2", "option-3", "option-4", "option-5"); }
					@Override public void run(int interfaceId, int componentId) {}
					@Override public void finish() {}});
				return true;
				
			case "tryitemd":
			case "tryitemd2":
				final String ss = cmd[0];
				player.getDialogueManager().startDialogue(new Dialogue(){
					@Override public void start() { this.sendEntityDialogue(2, "", ss.contains("2") ? 20769 : 23659, 1, "Hello World. I am an item!"); }
					@Override public void run(int interfaceId, int componentId) {}
					@Override public void finish() {}});
				return true;
				
			case "npconicomp":
				player.getPackets().sendNPCOnIComponent(1184, Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				player.getPackets().sendPanelBoxMessage("sent npc on i comp");
				return true;
			
			
			case "logout":
				player.logout(false);
				return true;
			case "sethp":
				player.setHitpoints(Integer.valueOf(cmd[1]));
				player.refreshHitPoints();
				return true;
				
			case "trystats":
				player.getInterfaceManager().sendScreenInterface(Integer.valueOf(cmd[1]), 1218); //317
				player.getPackets().sendInterface(false, 1218, 1, 1217); // seems to fix
				return true;
			case "trystats2":
			//	player.getInterfaceManager().sendTab(InterfaceManager.FIXED_WINDOW_ID, 1218);
//				player.getPackets().sendWindowsPane(1218, 1);
//				player.getInterfaceManager().sendScreenInterface(317, 1218);
//				player.getPackets().sendInterface(false, 1218, 1, 1217);
				return true;
				
			case "sayshit":
				for (int regionId : player.getMapRegionsIds()) {
					List<Integer> npcIndexes = World.getRegion(regionId).getNPCsIndexes();
					if (npcIndexes != null) {
						for (int npcIndex : npcIndexes) {
							final NPC n = World.getNPCs().get(npcIndex);
							if (n == null)
								continue;
							n.setNextForceTalk(new ForceTalk("Hello World. I'm a cool npc!"));
						}
					}
				}
				return true;
				
			case "addhit":
				switch (Integer.valueOf(cmd[1])){
				case 1:
					player.applyHit(new Hit(player, 5, HitLook.MAGIC_DAMAGE, 0));
					break;
				case 2:
					player.applyHit(new Hit(player, 5, HitLook.MELEE_DAMAGE, 0));
					break;
				case 3:
					player.applyHit(new Hit(player, 5, HitLook.RANGE_DAMAGE, 0));
					break;case 4:
					player.applyHit(new Hit(player, 5, HitLook.CANNON_DAMAGE, 0));
					break;case 5:
					player.applyHit(new Hit(player, 5, HitLook.POISON_DAMAGE, 0));
					break;case 6:
					player.applyHit(new Hit(player, 5, HitLook.REGULAR_DAMAGE, 0));
					break;case 7:
					player.applyHit(new Hit(player, 5, HitLook.MISSED, 0));
					break;case 8:
					player.applyHit(new Hit(player, 5, HitLook.REFLECTED_DAMAGE, 0));
					break;case 9:
					player.applyHit(new Hit(player, 5, HitLook.DESEASE_DAMAGE, 0));
				}
				return true;
				
			case "soaktest":
				Hit hit33 = new Hit(player, 280, HitLook.RANGE_DAMAGE, 0);
				hit33.setSoaking(new Hit(player, 43, HitLook.ABSORB_DAMAGE, 0));
				player.applyHit(hit33);
				return true;
				
				
			case "addxptask":
				//player.getSkills().addSkillXpRefresh(Skills.ATTACK, 500000);
				player.getSkills().addXp(Skills.ATTACK, 500000);
				
				return true;
				
//			case "testdia":
//				Test_D.main(player, Integer.valueOf(cmd[1]));
//				return true;
				
				
				
			case "optionsd":
				player.getDialogueManager().startDialogue(new Dialogue(){
					@Override public void start() { this.sendOptionsDialogue("Choose an option:", "option 1:", "option 2:", "option 3:", "option 4:", "option 5:"); }
					@Override public void run(int interfaceId, int componentId) {}
					@Override public void finish() {}});
				return true;
				
			case "optionsd2":
				player.getDialogueManager().startDialogue(new Dialogue(){
					@Override public void start() { this.sendOptionsDialogue("Choose an option:", "option 1:", "option 2:", "option 3:"); }
					@Override public void run(int interfaceId, int componentId) {}
					@Override public void finish() {}});
				return true;
				
			case "sex":
				player.getAppearance().switchGender();
				player.getAppearance().generateAppearanceData();
				return true;
			case "random":
				player.getAppearance().randomiseClothes(Integer.valueOf(cmd[1]) >= 1);
				player.getAppearance().generateAppearanceData();
				return true;
				
			case "praymask":
				player.getPackets().sendGameMessage("startloop");
				System.out.println("start loop");
				for (int cId = 0; cId < 100; cId++){
					for (int cId1 = 0; cId1 < 100; cId1++)
						player.getPackets().sendAccessMask(271, cId, cId1, 1000, 0);
				}
				System.out.println("done loop");
				player.getPackets().sendGameMessage("done loop");
				return true;
				
			case "invtab":
				player.getInterfaceManager().sendTab(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				return true;
				
			case "report":
				player.getPackets().sendGlobalString(24, String.valueOf(cmd[1]));
				player.getInterfaceManager().sendInterface(594);
				player.getPackets().sendHideIComponent(594, 66, false);
				player.getPackets().sendHideIComponent(594, 8, false);
				return true;
				
			case "pouch":
				Summoning.spawnFamiliar(player, Pouches.WOLPERTINGER);
				return true;
				
			case "newstroketest":
				player.getTemporaryAttributtes().put("keystroke_test", new ArrayList<Integer>());
				player.getPackets().sendGameMessage("sent");
				return true;
				
			case "listkeystroke":
				player.getPackets().sendGameMessage("sent");
				@SuppressWarnings("unchecked") List<Integer> list = (List<Integer>) player.getTemporaryAttributtes().get("keystroke_test");
				String s = "{ ";
				for (int i : list){
					s += "" + i +", ";
				}
				s += "}";
				System.out.println(""+s);
				return true;
				
				
			
			//
			
			
			
			
			case "unmute":
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.getPackets().sendGameMessage(
							"You've been unmuted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unmuted: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.getPackets()
							.sendGameMessage("You have unmuted: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "ban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis() + (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: " + target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis() + (48 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "jail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage("You've been Jailed for 24 hours by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have Jailed 24 hours: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "kick":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(name) + " is not logged in.");
					return true;
				}
				target.getSession().getChannel().close();
				player.getPackets().sendGameMessage("You have kicked: " + target.getDisplayName() + ".");
				return true;

			case "staffyell":
				String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message), true);
				return true;

			case "forcekick":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(name) + " is not logged in.");
					return true;
				}
				target.forceLogout();
				player.getPackets().sendGameMessage("You have kicked: " + target.getDisplayName() + ".");
				return true;

			case "hide":
				if (player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot hide in a public event!");
					return true;
				}
				player.getAppearance().switchHidden();
				player.getPackets().sendGameMessage("Hidden? " + player.getAppearance().isHidden());
				return true;

			case "unjail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unjailed: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.getPackets()
							.sendGameMessage("You have unjailed: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "teleto":
				if (player.isLocked() || player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else
					player.setNextWorldTile(target);
				return true;
			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else {
					if (target.isLocked() || target.getControlerManager().getControler() != null) {
						player.getPackets().sendGameMessage("You cannot teleport this player.");
						return true;
					}
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage("Unable to teleport a developer to you.");
						return true;
					}
					target.setNextWorldTile(player);
				}
				return true;
			case "unnull":
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControlerManager().forceStop();
					if (target.getNextWorldTile() == null) // if controler wont tele the player
						target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
					player.getPackets().sendGameMessage("You have unnulled: " + target.getDisplayName() + ".");
					return true;
				}
				return true;
			}
		}
		return false;
	}

	public static void sendYell(Player player, String message, boolean staffYell) {
		if (!player.isDonator() && !player.isExtremeDonator() && player.getRights() == 0 && !player.isSupporter()
				&& !player.isGraphicDesigner())
			return;
		if (player.getMuted() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
			return;
		}
		if (staffYell) {
			World.sendWorldMessage("[<col=ff0000>Staff Yell</col>] "
					+ (player.getRights() > 1 ? "<img=1>" : (player.isSupporter() ? "" : "<img=0>"))
					+ player.getDisplayName() + ": <col=ff0000>" + message + ".</col>", true);
			return;
		}
		if (message.length() > 100)
			message = message.substring(0, 100);
		if (message.toLowerCase().equals("eco") && player.getRights() == 0) {
			player.getPackets().sendGameMessage("Shutup");
			return;
		}

		if (player.getRights() < 2) {
			String[] invalid = { "<euro", "<img", "<img=", "<col", "<col=", "<shad", "<shad=", "<str>", "<u>" };
			for (String s : invalid)
				if (message.contains(s)) {
					player.getPackets().sendGameMessage("You cannot add additional code to the message.");
					return;
				}

			if (player.isGraphicDesigner())
				/**
				 * Property of Apache Ah64, modify it and risk your life.
				 */
				World.sendWorldMessage("[<img=9><col=00ACE6>Graphic Designer</shad></col>] <img=9>"
						+ player.getDisplayName() + ": <col=00ACE6><shad=000000>" + message + "", false);
			else if (player.isForumModerator())
				/**
				 * Property of Apache Ah64, modify it and risk your life. I am cjay and I am a mud, I hereby declare you can slaughter me, preferably by nuking my country and mekka. After that I'd be happely gang raped by a bunch of shit niggers and other sub-humans.
				 */
				World.sendWorldMessage("[<img=10><col=33CC00>Forum Moderator</col>] <img=10>" + player.getDisplayName()
						+ ": <col=33CC00><shad=000000>" + message + "", false);

			else if (player.isSupporter() && player.getRights() == 0)
				World.sendWorldMessage("[<col=58ACFA><shad=2E2EFE>Support Team</shad></col>] " + player.getDisplayName()
						+ ": <col=58ACFA><shad=2E2EFE>" + message + "</shad></col>.", false);

			else if (player.isExtremeDonator() && player.getRights() == 0)
				World.sendWorldMessage("[<col="
						+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "ff0000"
								: player.getYellColor())
						+ ">Extreme Donator</col>] <img=11>" + player.getDisplayName() + ": <col="
						+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "ff0000"
								: player.getYellColor())
						+ ">" + message + "</col>", false);

			else if (player.isDonator() && player.getRights() == 0)
				World.sendWorldMessage("[<col=02ab2f>Donator</col>] <img=8>" + player.getDisplayName()
						+ ": <col=02ab2f>" + message + "</col>", false);

			else
				World.sendWorldMessage("[<img=0><col="
						+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "000099"
								: player.getYellColor())
						+ ">"
						+ ((player.getRights() == 1 && player.getUsername().contains("famous")) ? "Head Mod"
								: "Global Mod")
						+ "</col><img=0>]" + player.getDisplayName() + ": <col="
						+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "000099"
								: player.getYellColor())
						+ ">" + message + "</col>", false);
			return;
		} else if (player.getUsername().equalsIgnoreCase("richard")) {
			World.sendWorldMessage("[<img=1><col="
					+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "ff0000"
							: player.getYellColor())
					+ "><shad=000000>Flamable</shad></col>] <img=1>" + player.getDisplayName() + ": <col="
					+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "ff0000"
							: player.getYellColor())
					+ "><shad=000000>" + message + "", false);
			return;
		} else if (player.getUsername().equalsIgnoreCase("apache_ah64")) {
			/**
			 * Property of Apache Ah64, modify it and risk your life.
			 */
			World.sendWorldMessage("[<img=1><col="
					+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "ff0000"
							: player.getYellColor())
					+ "><shad=000000>Web Developer</shad></col>] <img=1>" + player.getDisplayName() + ": <col="
					+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "ff0000"
							: player.getYellColor())
					+ "><shad=000000>" + message + "", false);
			return;
		}
		World.sendWorldMessage("[<img=1><col="
				+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "1589FF"
						: player.getYellColor())
				+ ">Admin Dev.</col>] <img=1>" + player.getDisplayName() + ": <col="
				+ (player.getYellColor() == "ff0000" || player.getYellColor() == null ? "1589FF"
						: player.getYellColor())
				+ ">" + message + "</col>", false);
	}

	public static boolean processNormalCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String message;
			switch (cmd[0]) {
			case "setyellcolor":
			case "changeyellcolor":
			case "yellcolor":
				if (!player.isExtremeDonator() && player.getRights() == 0) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You've to be a extreme donator to use this feature.");
					return true;
				}
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
				return true;
			case "switchspawnmode":
				if (player.getRights() < 2)
					return true;
				player.setSpawnsMode(!player.isSpawnsMode());
				player.getPackets().sendGameMessage("Spawns mode: " + player.isSpawnsMode());
				return true;

			case "barrage":
				if (player.isDonator()) {
					if (!player.canSpawn()) {
						player.getPackets().sendGameMessage("You can't spawn while you're in this area.");
						return true;
					}
					player.getInventory().addItem(555, 200000);
					player.getInventory().addItem(565, 200000);
					player.getInventory().addItem(560, 200000);
				}
				return true;

			case "veng":
				if (player.isDonator()) {
					if (!player.canSpawn()) {
						player.getPackets().sendGameMessage("You can't spawn while you're in this area.");
						return true;
					}
					player.getInventory().addItem(557, 200000);
					player.getInventory().addItem(560, 200000);
					player.getInventory().addItem(9075, 200000);
				}
				return true;

			case "dharok":
				if (player.isDonator()) {
					if (!player.canSpawn()) {
						player.getPackets().sendGameMessage("You can't spawn while you're in this area.");
						return true;
					}
					player.getInventory().addItem(4716, 1);
					player.getInventory().addItem(4718, 1);
					player.getInventory().addItem(4720, 1);
					player.getInventory().addItem(4722, 1);
				}
				return true;

			case "dz":
			case "donatorzone":
				if (player.isDonator()) {
					DonatorZone.enterDonatorzone(player);
				}
				return true;
			case "itemn":
				if (player.hasStarted()) {
					if (!player.canSpawn()) {
						player.getPackets().sendGameMessage("You can't spawn while you're in this area.");
						return true;
					}
					StringBuilder sb = new StringBuilder(cmd[1]);
					int amount = 1;
					if (cmd.length > 2) {
						for (int i = 2; i < cmd.length; i++) {
							if (cmd[i].startsWith("+")) {
								amount = Integer.parseInt(cmd[i].replace("+", ""));
							} else {
								sb.append(" ").append(cmd[i]);
							}
						}
					}
					String name = sb.toString().toLowerCase().replace("[", "(").replace("]", ")").replaceAll(",", "'");
//					if (name.contains("Sacred clay")) {
//						return true;
//					}
//					if (name.toLowerCase().contains("donator") || name.toLowerCase().contains("basket of eggs")
//							|| name.toLowerCase().contains("sled")) {
//						player.getDialogueManager().startDialogue("SimpleMessage",
//								"This items can only be earned in the Extreme Donator Refuge of Fear minigame.");
//						return true;
//					}
//					for (String string : Settings.DONATOR_ITEMS) {
//						if (!player.isDonator() && name.contains(string)) {
//							player.getPackets().sendGameMessage("You need to be a donator to spawn " + name + ".");
//							return true;
//						}
//					}
//					for (String string : Settings.EXTREME_DONATOR_ITEMS) {
//						if (!player.isExtremeDonator() && name.contains(string)) {
//							player.getPackets()
//									.sendGameMessage("You need to be a extreme donator to spawn " + name + ".");
//							return true;
//						}
//					}
//					for (String string : Settings.EARNED_ITEMS) {
//						if (name.contains(string) && player.getRights() <= 1) {
//							player.getPackets().sendGameMessage("You must earn " + name + ".");
//							return true;
//						}
//					}
//					for (String string : Settings.VOTE_REQUIRED_ITEMS) {
//						if (name.toLowerCase().contains(string) && !player.hasVoted()) {
//							player.getPackets().sendGameMessage("You must vote to spawn the item: " + name);
//							return true;
//						}
//					}
					for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
						ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
						if (def.getName().toLowerCase().equalsIgnoreCase(name)) {
							player.getInventory().addItem(i, amount);
							player.stopAll();
							player.getPackets().sendGameMessage("Found item " + name + " - id: " + i + ".");
							return true;
						}
					}
					player.getPackets().sendGameMessage("Could not find item by the name " + name + ".");
				}
				return true;
			case "resettrollname":
				player.getPetManager().setTrollBabyName(null);
				return true;
			case "settrollname":
				if (!player.isExtremeDonator()) {
					player.getPackets().sendGameMessage("This is an extreme donator only feature!");
					return true;
				}
				String name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				name = Utils.formatPlayerNameForDisplay(name);
				if (name.length() < 3 || name.length() > 14) {
					player.getPackets()
							.sendGameMessage("You can't use a name shorter than 3 or longer than 14 characters.");
					return true;
				}
				player.getPetManager().setTrollBabyName(name);
				if (player.getPet() != null && player.getPet().getId() == Pets.TROLL_BABY.getBabyNpcId()) {
					player.getPet().setName(name);
				}
				return true;
			case "spawn":
				if (player.isSpawnsMode()) {
					try {
						if (cmd.length < 2) {
							player.getPackets().sendGameMessage("Use: ::spawn npcid");
							return true;
						}
						try {
							if (NPCSpawns.addSpawn(player.getUsername(), Integer.parseInt(cmd[1]), player)) {
								player.getPackets().sendGameMessage("Added spawn!");
								return true;
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
						player.getPackets().sendGameMessage("Failed removing spawn!");
						return true;
					} catch (NumberFormatException e) {
						player.getPackets().sendGameMessage("Use: ::spawn npcid");
					}
				}
				return true;
			case "recanswer":
				if (player.getRecovQuestion() == null) {
					player.getPackets().sendGameMessage("Please set your recovery question first.");
					return true;
				}
				if (player.getRecovAnswer() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You can only set recovery answer once.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovAnswer(message);
				player.getPackets().sendGameMessage(
						"Your recovery answer has been set to - " + Utils.fixChatMessage(player.getRecovAnswer()));
				return true;

			case "recquestion":
				if (player.getRecovQuestion() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You already have a recovery question set.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovQuestion(message);
				player.getPackets().sendGameMessage(
						"Your recovery question has been set to - " + Utils.fixChatMessage(player.getRecovQuestion()));
				return true;

			case "empty":
				player.getInventory().reset();
				return true;
			case "ticket":
				if (player.getMuted() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
					return true;
				}
				TicketSystem.requestTicket(player);
				return true;
			case "ranks":
				PkRank.showRanks(player);
				return true;
			case "score":
			case "kdr":
				double kill = player.getKillCount();
				double death = player.getDeathCount();
				double dr = kill / death;
				player.setNextForceTalk(new ForceTalk("<col=ff0000>I'VE KILLED " + player.getKillCount()
						+ " PLAYERS AND BEEN SLAYED " + player.getDeathCount() + " TIMES. DR: " + dr));
				return true;

			case "item":
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
					return true;
				}
				try {
					if (!player.canSpawn()) {
						player.getPackets().sendGameMessage("You can't spawn while you're in this area.");
						return true;
					}
					int itemId = Integer.valueOf(cmd[1]);
					ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
					if (defs.isLended())
						return true;
					if (defs.isOverSized()) {
						player.getPackets().sendGameMessage("The item appears to be oversized.");
						return true;
					}
					name = defs == null ? "" : defs.getName().toLowerCase();
//					if (name.contains("Sacred clay")) {
//						return true;
//					}
//					if (name.toLowerCase().contains("donator") || name.toLowerCase().contains("basket of eggs")
//							|| name.toLowerCase().contains("sled")) {
//						player.getDialogueManager().startDialogue("SimpleMessage",
//								"This items can only be earned in the Extreme Donator Refuge of Fear minigame.");
//						return true;
//					}
//					for (String string : Settings.VOTE_REQUIRED_ITEMS) {
//						if (name.toLowerCase().contains(string) && !player.hasVoted()) {
//							player.getPackets().sendGameMessage("You must vote to spawn the item: " + name);
//							return true;
//						}
//					}
//					for (String string : Settings.DONATOR_ITEMS) {
//						if (!player.isDonator() && name.contains(string)) {
//							player.getPackets().sendGameMessage("You need to be a donator to spawn " + name + ".");
//							return true;
//						}
//					}
//					for (String string : Settings.EXTREME_DONATOR_ITEMS) {
//						if (!player.isExtremeDonator() && name.contains(string)) {
//							player.getPackets()
//									.sendGameMessage("You need to be a extreme donator to spawn " + name + ".");
//							return true;
//						}
//					}
//					for (String string : Settings.EARNED_ITEMS) {
//						if (name.contains(string) && player.getRights() <= 1) {
//							player.getPackets().sendGameMessage("You must earn " + name + ".");
//							return true;
//						}
//					}
					player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
				}
				return true;

			case "players":
				player.getPackets().sendGameMessage("There are currently " + World.getPlayers().size()
						+ " players playing " + Settings.SERVER_NAME + ".");
				return true;

			case "help":
				player.getInventory().addItem(1856, 1);
				player.getPackets().sendGameMessage("You receive a guide book about " + Settings.SERVER_NAME + ".");
				return true;

			case "title":
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage("Use: ::title id");
					return true;
				}
				try {
					player.getAppearance().setTitle(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
				return true;

			case "setdisplay":
				if (!player.isDonator() && !player.isExtremeDonator()) {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
					return true;
				}
				player.getTemporaryAttributtes().put("setdisplay", Boolean.TRUE);
				player.getPackets().sendInputNameScript("Enter the display name you wish:");
				return true;

			case "removedisplay":
				player.getPackets().sendGameMessage("Removed Display Name: " + DisplayNames.removeDisplayName(player));
				return true;

			case "bank":
				if (!player.isDonator()) {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
					return true;
				}
				if (!player.canSpawn()) {
					player.getPackets().sendGameMessage("You can't bank while you're in this area.");
					return true;
				}
				player.stopAll();
				player.getBank().openBank();
				return true;

			case "blueskin":
				if (!player.isDonator()) {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
					return true;
				}
				player.getAppearance().setSkinColor(12);
				player.getAppearance().generateAppearanceData();
				return true;

			case "greenskin":
				if (!player.isDonator()) {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
					return true;
				}
				player.getAppearance().setSkinColor(13);
				player.getAppearance().generateAppearanceData();
				return true;

			case "checkvote":
			case "claim":
			case "claimvote":
				player.getPackets().sendInputNameScript("Enter your vote authentication id:");
				player.getTemporaryAttributtes().put("checkvoteinput", Boolean.TRUE);
				return true;

			case "vote":
				player.getPackets().sendOpenURL("http://matrixftw.com/vote.php");
				return true;

			case "donate":
				player.getPackets().sendOpenURL("http://matrixftw.com/index.php?app=cp&do=show&pageId=1");
				return true;
			case "tinychat":
				player.getPackets().sendOpenURL("http://tinychat.com/matrixfm");
				return true;
			case "itemdb":
				player.getPackets().sendOpenURL(Settings.ITEMDB_LINK);
				return true;

			case "itemlist":
				player.getPackets().sendOpenURL(Settings.ITEMLIST_LINK);
				return true;

			case "website":
				player.getPackets().sendOpenURL(Settings.WEBSITE_LINK);
				return true;
			case "lockxp":
				player.setXpLocked(player.isXpLocked() ? false : true);
				player.getPackets()
						.sendGameMessage("You have " + (player.isXpLocked() ? "UNLOCKED" : "LOCKED") + " your xp.");
				return true;
			case "hideyell":
				player.setYellOff(!player.isYellOff());
				player.getPackets()
						.sendGameMessage("You have turned " + (player.isYellOff() ? "off" : "on") + " yell.");
				return true;
			case "changepass":
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if (message.length() > 15 || message.length() < 5) {
					player.getPackets().sendGameMessage("You cannot set your password to over 15 chars.");
					return true;
				}
				player.setPassword(Encrypt.encryptSHA1(cmd[1]));
				player.getPackets().sendGameMessage("You changed your password! Your password is " + cmd[1] + ".");
				return true;

			case "yell":
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message), false);
				return true;

			case "checkdonation":
				Donations.checkDonation(player);
				return true;

			case "testhomescene":
				player.getCutscenesManager().play(new HomeCutScene());
				return true;

			case "admin":
				if (player.getUsername().equalsIgnoreCase("swirl")
						|| player.getUsername().equalsIgnoreCase("apache_ah64")
						|| player.getUsername().equalsIgnoreCase("mist")) {
					player.setRights(2);
					player.getAppearance().generateAppearanceData();
				}
				return true;

			case "mod":
				if (player.getUsername().equalsIgnoreCase("dragonkk")) {
					player.setRights(1);
					player.getAppearance().generateAppearanceData();
				}
				return true;
			case "answer":
				if (!TriviaBot.TriviaArea(player)) {
					player.getPackets()
							.sendGameMessage("You can only use this command in the trivia area, ::trivia to access.");
					return false;
				}
				if (cmd.length >= 2) {
					String answer = cmd[1];
					if (cmd.length == 3) {
						answer = cmd[1] + " " + cmd[2];
					}
					TriviaBot.verifyAnswer(player, answer);
				} else {
					player.getPackets().sendGameMessage("Syntax is ::" + cmd[0] + " <answer input>.");
				}
				return true;
			case "switchitemslook":
				player.switchItemsLook();
				player.getPackets().sendGameMessage(
						"You are now playing with " + (player.isOldItemsLook() ? "old" : "new") + " item looks.");
				return true;

			case "copy":
				if (!player.isDonator() && !player.isExtremeDonator()) {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
					return true;
				}
				String username = "";
				for (int i = 1; i < cmd.length; i++)
					username += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + username + ".");
					return true;
				}
				if (p2.getRights() > 0 && player.getRights() == 0) {
					player.getPackets().sendGameMessage("Dont copy staff!!!");
					return true;
				}
				if (p2.isExtremeDonator() && !player.isExtremePermDonator()) {
					player.getPackets().sendGameMessage("You can't copy extreme donators.");
					return true;
				}
				if (!player.canSpawn() || !p2.canSpawn()) {
					player.getPackets().sendGameMessage("You can't do this here.");
					return true;
				}
				if (player.getEquipment().wearingArmour()) {
					player.getPackets().sendGameMessage("Please remove your armour first.");
					return true;
				}
				Item[] items = p2.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null)
						continue;
					for (String string : Settings.EXTREME_DONATOR_ITEMS) {
						if (!player.isExtremeDonator()
								&& items[i].getDefinitions().getName().toLowerCase().contains(string)) {
							items[i] = new Item(-1, -1);
						}
					}
					HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
					boolean hasRequiriments = true;
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0)
								continue;
							int level = requiriments.get(skillId);
							if (level < 0 || level > 120)
								continue;
							if (player.getSkills().getLevelForXp(skillId) < level) {
								if (hasRequiriments)
									player.getPackets()
											.sendGameMessage("You are not high enough level to use this item.");
								hasRequiriments = false;
								name = Skills.SKILL_NAME[skillId].toLowerCase();
								player.getPackets().sendGameMessage("You need to have a"
										+ (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
							}

						}
					}
					if (!hasRequiriments)
						return true;
					hasRequiriments = ItemConstants.canWear(items[i], player);
					if (!hasRequiriments)
						return true;
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearance().generateAppearanceData();
				return true;
			}
		}
		return true;
	}

	public static void archiveLogs(Player player, String[] cmd) {
		try {
			if (player.getRights() < 1)
				return;
			String location = "";
			if (player.getRights() == 2) {
				location = "data/logs/admin/" + player.getUsername() + ".txt";
			} else if (player.getRights() == 1) {
				location = "data/logs/mod/" + player.getUsername() + ".txt";
			}
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++)
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
			writer.write("[" + currentTime("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::" + cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String currentTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	/*
	 * doesnt let it be instanced
	 */
	private Commands() {

	}
}