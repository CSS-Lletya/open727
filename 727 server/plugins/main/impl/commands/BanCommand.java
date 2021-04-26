package main.impl.commands;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"ban"}, rights = {Rights.ADMINISTRATOR}, syntax = "Ban a specified Player")
public final class BanCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		String name;
		Player target;
		name = "";
		for (int i = 1; i < cmd.length; i++)
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
		target = World.getPlayerByDisplayName(name);
		if (target == null)
			return;
		HostManager.remove(cmd[1], HostListType.BANNED_IP, true);
	}
}