package com.rs.game.player.commands.impl;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.commands.Command;
import com.rs.game.player.commands.CommandDispatcher;
import com.rs.game.player.commands.CommandSignature;

@CommandSignature(alias = {"reloadcommands"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Commands list")
public final class ReloadCommands implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CommandDispatcher.load();
	}
}