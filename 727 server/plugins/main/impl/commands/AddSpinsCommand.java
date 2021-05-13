package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"addspins"}, rights = {Rights.ADMINISTRATOR}, syntax = "Gives Squeal Of Fortune spins")
public final class AddSpinsCommand implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        if(cmd.length != 2 && cmd.length != 1) {
            player.getPackets().sendGameMessage("The format is \";;addspins [amt]\"");
            return;
        }
        int amount = 1;
        if(cmd.length == 2 && cmd[1].matches("\\d+"))
            amount = Integer.parseInt(cmd[1]);

        player.getPlayerDetails().setSpins(player.getPlayerDetails().getSpins() + amount);
        player.getPackets().sendGameMessage("You recieved " + amount + " free spins for Squeal Of Fortune.");
        player.getPackets().sendIComponentText(1139, 6, Integer.toString(player.getPlayerDetails().getSpins()));
    }
}