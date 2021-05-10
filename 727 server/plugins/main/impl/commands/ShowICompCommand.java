package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.utils.Utils;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"showicomp"}, rights = {Rights.ADMINISTRATOR}, syntax = "Shows interface components from first to an optional second")
public final class ShowICompCommand implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        if (!(cmd.length == 3) && !(cmd.length == 4)) {
            player.getPackets().sendGameMessage("Use: ;;showicomp [interfaceId] [Component ID] [Component ID - optional]");
            return;
        }

        try {
            int interId = Integer.valueOf(cmd[1]);
            int startComponentID = Integer.valueOf(cmd[2]);

            int endComponentID = startComponentID;
            if(cmd.length == 4)
                endComponentID = Integer.valueOf(cmd[3]);

            for (int componentId = startComponentID; componentId <= endComponentID; componentId++) {
                if(componentId < Utils.getInterfaceDefinitionsComponentsSize(interId))
                    player.getPackets().sendHideIComponent(interId, componentId, false);
            }
        } catch (NumberFormatException e) {
            player.getPackets().sendGameMessage("Use: ;;showicomp [interfaceId] [Component ID] [Component ID - optional]");
        }
    }
}
