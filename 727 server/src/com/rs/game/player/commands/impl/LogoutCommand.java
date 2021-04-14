package com.rs.game.player.commands.impl;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.commands.Command;
import com.rs.game.player.commands.CommandSignature;

@CommandSignature(alias = {"log"}, rights = {Rights.PLAYER}, syntax = "A logout command. How useless.")
public final class LogoutCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.logout(false);
	}
}