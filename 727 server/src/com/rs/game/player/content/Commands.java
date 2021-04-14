package com.rs.game.player.content;

import com.rs.game.player.Player;
import com.rs.game.player.commands.CommandDispatcher;

public final class Commands {

	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
		if (command.length() == 0) // if they used ::(nothing) theres no command
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0)
			return false;
		CommandDispatcher.execute(player, cmd, command);
		return false;
	}
}