package main.impl.commands;

import com.rs.game.Animation;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"anim", "an", "animation"}, rights = {Rights.ADMINISTRATOR}, syntax = "Perform an animation")
public final class AnimationCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
	}
}