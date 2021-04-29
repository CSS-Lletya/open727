package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.utils.IntegerInputAction;

import main.listener.Command;
import main.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"test"}, rights = {Rights.PLAYER}, syntax = "Test a Command")
public final class TestCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getPackets().sendInputIntegerScript("How many many", new IntegerInputAction() {
			@Override
			public void handle(int input) {
				System.out.println("was: " + input);
			}
		});
	}
}