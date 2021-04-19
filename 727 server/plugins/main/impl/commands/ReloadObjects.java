package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.ObjectDispatcher;
import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"reloadobjects"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Objects list")
public final class ReloadObjects implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		ObjectDispatcher.load();
	}
}