package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.CommandDispatcher;
import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"reloadcommands"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Commands list")
public final class ReloadCommands implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CommandDispatcher.load();
	}
}