package main.listener;

import com.rs.game.player.Player;

import npc.NPC;

/**
 * 
 * @author Dennis
 *
 */
public interface NPCType {
	
	/**
	 * The functionality to be executed as soon as this execution is called.
	 * @param player the player we are executing this command for.
	 * @param cmd the command that we are executing for this player.
	 */
	void execute(Player player, NPC npc, int option) throws Exception;
}
