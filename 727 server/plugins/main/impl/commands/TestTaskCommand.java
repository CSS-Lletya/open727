package main.impl.commands;

import com.rs.game.ForceTalk;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.task.LinkedTaskSequence;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"task"}, rights = {Rights.PLAYER}, syntax = "Task testing.")
public final class TestTaskCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		LinkedTaskSequence seq = new LinkedTaskSequence();
		
		seq.connect(1, () -> player.setNextForceTalk(new ForceTalk("Taste vengeance!")));
		
		seq.connect(2, () -> player.setNextForceTalk(new ForceTalk("OR NOT")));
		
		seq.connect(3, () -> player.setNextForceTalk(new ForceTalk("OR MAYBE....")));
		
		seq.start();
	}
}