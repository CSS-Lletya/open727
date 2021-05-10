package main.impl.rsinterface;

import com.rs.game.player.Player;
import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 13, 14, 759 })
public class BankPinInterfacePlugin implements RSInterface {
    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
        player.getBankPin().handleButtons(interfaceId, componentId);
    }

    public static void finishPinDialogue(Player player, int interfaceId, int componentId) {
        if (interfaceId == 13) {
            player.getBankPin().handleButtons(interfaceId, componentId);
        }
        if (interfaceId == 14) {
            player.getBankPin().handleButtons(interfaceId, componentId);
        }
    }
}
