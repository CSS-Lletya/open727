package com.rs.game.player.content;

import com.rs.Settings;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

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
		if ((player.getRights() >= 1)
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
			case "test":
				player.logout(false);
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

	public static boolean processNormalCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			@SuppressWarnings("unused")
			String message;
			switch (cmd[0]) {

			}
		}
		return true;
	}
}