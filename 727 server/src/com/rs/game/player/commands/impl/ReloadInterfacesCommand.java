package com.rs.game.player.commands.impl;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.commands.Command;
import com.rs.game.player.commands.CommandSignature;

import main.RSInterfaceDispatcher;

@CommandSignature(alias = {"reloadint"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the RS Interface system")
public final class ReloadInterfacesCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		RSInterfaceDispatcher.reload();
	}
}