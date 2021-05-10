package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"wardrobe"}, rights = {Rights.ADMINISTRATOR}, syntax = "Shows player appearence options")
public final class WardrobeCommand implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        if (cmd.length > 1) {
            player.getPackets().sendPanelBoxMessage("Use: ;;wardrobe");
            return;
        }
        PlayerDesign.open(player);

    }
}
