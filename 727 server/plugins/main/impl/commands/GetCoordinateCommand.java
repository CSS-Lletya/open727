package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"coordinate"}, rights = {Rights.ADMINISTRATOR}, syntax = "Get current player tile")
public final class GetCoordinateCommand implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.getPackets().sendGameMessage("X: " + Integer.toString(player.getX()) + " Y: " + Integer.toString(player.getY())
                + " Plane: " + Integer.toString(player.getHeight()));
    }
}
