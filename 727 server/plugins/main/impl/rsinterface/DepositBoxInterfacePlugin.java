package main.impl.rsinterface;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.decoders.WorldPacketsDecoder;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {11})
public class DepositBoxInterfacePlugin implements RSInterface {

    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
        if (componentId == 17) {
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
                player.getBank().depositItem(slotId, 1, true);
            else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
                player.getBank().depositItem(slotId, 5, true);
            else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
                player.getBank().depositItem(slotId, 10, true);
            else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
                player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
            else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
                player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
                player.getTemporaryAttributtes().remove("bank_isWithdraw");
                player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
            } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
                player.getInventory().sendExamine(slotId);
        } else if (componentId == 18)
            player.getBank().depositAllInventory(false);
        else if (componentId == 22) {//deposit all equipment
            Item[] items = player.getEquipment().getItems().getItemsCopy();
            player.getBank().addItems(items, true);
            player.getEquipment().removeAllEquipment();
            player.getAppearance().generateAppearenceData();
        }
        else if (componentId == 24)
            player.getBank().depositAllBob(false);
        else if (componentId == 20)
            ;//money pouch
    }

}