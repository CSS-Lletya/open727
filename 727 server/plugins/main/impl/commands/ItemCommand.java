package main.impl.commands;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"item"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawn an Item")
public final class ItemCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		@SuppressWarnings("unused")
		String name;
		if (cmd.length < 2) {
			player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
			return;
		}
		try {
			int itemId = Integer.valueOf(cmd[1]);
			ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
			if (defs.isLended())
				return;
			if (defs.isOverSized()) {
				player.getPackets().sendGameMessage("The item appears to be oversized.");
				return;
			}
			name = defs == null ? "" : defs.getName().toLowerCase();
			player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
		} catch (NumberFormatException e) {
			player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
		}
	}
}