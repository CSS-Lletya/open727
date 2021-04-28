package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;
import skills.Skills;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"max", "master"}, rights = {Rights.PLAYER}, syntax = "Unlock all skills to 99")
public final class MasterCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		for (int i = 0; i < 24; i++) {
            player.getSkills().set(i, 99);
            player.getSkills().setXp(i, Skills.getXPForLevel(99));
        }
        player.getSkills().set(24, 120);
        player.getSkills().setXp(24, Skills.getXPForLevel(120));
//        for (int i = 0; i < 25; i++)
//            player.getDialogueManager().startDialogue("LevelUp", i);
        player.getAppearance().generateAppearenceData();
	}
}