package main.impl.commands;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"tele"}, rights = {Rights.ADMINISTRATOR}, syntax = "Teleports you to a specified location")
public final class TeleportCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if (cmd.length < 3) {
			player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY");
			return;
		}
		try {
			player.resetWalkSteps();
			player.setNextWorldTile(new WorldTile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
					cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getHeight()));
		} catch (NumberFormatException e) {
			player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY plane");
		}
	}
}