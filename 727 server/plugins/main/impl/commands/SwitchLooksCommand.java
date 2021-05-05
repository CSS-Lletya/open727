package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"switchlooks"}, rights = {Rights.ADMINISTRATOR}, syntax = "switches item looks")
public final class SwitchLooksCommand implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.switchItemsLook();
    }
}
