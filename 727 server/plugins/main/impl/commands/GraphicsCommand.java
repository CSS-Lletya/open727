package main.impl.commands;

import com.rs.game.Graphics;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"gfx", "graphic", "graphics"}, rights = {Rights.ADMINISTRATOR}, syntax = "Perform an graphic")
public final class GraphicsCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1])));
	}
}