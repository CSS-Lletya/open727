package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;
import player.Combat;
import player.type.CombatEffectType;
import player.type.PoisonType;

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
		player.poison(PoisonType.DEFAULT_MAGIC);
	}
}