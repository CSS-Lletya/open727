package main.impl.commands;

import com.rs.game.World;
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
@CommandSignature(alias = {"update", "shutdown"}, rights = {Rights.ADMINISTRATOR}, syntax = "Shut down the server in a time based length")
public final class ShutdownCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		World.get().safeShutdown(false, Integer.valueOf(cmd[1]));
		World.players().forEach(p -> p.getPackets().sendGameMessage("Shutting down"));
	}
}