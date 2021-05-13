package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {1092})
public class LodestoneNetworkInterfacePlugin implements RSInterface {
    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
        player.getLodeStones().handleButtons(componentId);
    }
}
