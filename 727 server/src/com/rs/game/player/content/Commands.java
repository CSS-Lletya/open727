package com.rs.game.player.content;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.pet.Pets;
import com.rs.game.player.cutscenes.HomeCutScene;
import com.rs.utils.Encrypt;
import com.rs.utils.Utils;

import skills.Skills;

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
	@SuppressWarnings("unused")
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
			}	
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static boolean processHeadModCommands(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String name;
			Player target;

			switch (cmd[0]) {

			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static boolean processSupportCommands(Player player, String[] cmd, boolean console, boolean clientCommand) {
		String name;
		Player target;
		if (clientCommand) {

		} else {
			switch (cmd[0]) {
			}
		}
		return false;
	}

	public static boolean processModCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			switch (cmd[0]) {
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