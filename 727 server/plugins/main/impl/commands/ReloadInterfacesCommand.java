package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.RSInterfaceDispatcher;
import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"reloadint"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the RS Interface system")
public final class ReloadInterfacesCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		RSInterfaceDispatcher.reload();
	}
}