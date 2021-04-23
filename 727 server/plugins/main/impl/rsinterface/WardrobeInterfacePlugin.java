package main.impl.rsinterface;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import main.impl.commands.PlayerDesign;
import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {1028})
public class WardrobeInterfacePlugin implements RSInterface {
    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2) throws Exception {
        if(interfaceId == 1028)
            PlayerDesign.handle(player, componentId, slotId);
    }
}