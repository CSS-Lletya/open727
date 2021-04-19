package main.listener;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;

/**
 * Represents a command which holds functionality for the execution of
 * it.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public interface ObjectType {
	
	/**
	 * The functionality to be executed as soon as this command is called.
	 * @param player the player we are executing this command for.
	 * @param cmd the command that we are executing for this player.
	 */
	void execute(Player player, WorldObject object, int optionId) throws Exception;
}
