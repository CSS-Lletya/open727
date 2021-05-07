package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;
import npc.NPC;

@CommandSignature(alias = {"npc"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns a npc with the specified ID")
public final class NPCCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		try {
			NPC.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true,
					true);
			return;
		} catch (NumberFormatException e) {
			player.getPackets().sendPanelBoxMessage(
					"Use: ::npc id(Integer)");
		}
	}
}