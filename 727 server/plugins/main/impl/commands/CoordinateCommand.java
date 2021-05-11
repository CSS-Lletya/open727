package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"coord", "coords"}, rights = {Rights.ADMINISTRATOR}, syntax = "Get your current coordinate")
public final class CoordinateCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getPackets().sendGameMessage("x: "+ player.getX() + " y: " + player.getY() + " h: " + player.getHeight());
	}
}