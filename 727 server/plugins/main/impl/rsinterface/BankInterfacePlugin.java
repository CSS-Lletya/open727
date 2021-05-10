package main.impl.rsinterface;

import com.rs.game.player.Player;
import com.rs.net.decoders.WorldPacketsDecoder;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 762/*Bank*/, 763/*Inventory*/})
public class BankInterfacePlugin implements RSInterface {

    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
        if (interfaceId == 762) {
            if (componentId == 15)
                player.getBank().switchInsertItems();
            else if (componentId == 19) {
                player.getPackets().sendConfig(115, player.getBank().getWithdrawNotes() ? 1 : 0);
                player.getBank().switchWithdrawNotes();
            } else if (componentId == 33)
                player.getBank().depositAllInventory(true);
            else if (componentId == 37)
                player.getBank().depositAllEquipment(true);
            else if (componentId == 39)
                player.getBank().depositAllBob(true);
            else if (componentId == 35)
                ;
//                player.getBank().depositMoneyPouch(true);
            else if (componentId == 46) {
//                long moneyPouch = player.getMoneyPouch().getTotal();
//                long bankValue = player.getBank().getBankValue();
//                long inventoryValue = player.getInventory().getInventoryValue();
//                long equipmentValue = player.getEquipment().getEquipmentValue();
//                long totalValue = 0;
//                long grandexchangeValue = GrandExchange.getTotalOfferValues(player);
//                long collectionValue = GrandExchange.getTotalCollectionValue(player);
//                player.closeInterfaces();
//                player.getInterfaceManager().sendInterface(629);
//                player.getPackets().sendIComponentText(629, 11, "Information Tab");
//                player.getPackets().sendIComponentText(629, 12, "");
//                player.getPackets().sendIComponentText(629, 41, "Money pouch:");
//                player.getPackets().sendIComponentText(629, 54, Utils.formatDoubledAmount(moneyPouch));
//                player.getPackets().sendIComponentText(629, 42, "Bank:");
//                player.getPackets().sendIComponentText(629, 55, Utils.formatDoubledAmount(bankValue));
//                player.getPackets().sendIComponentText(629, 43, "Inventory:");
//                player.getPackets().sendIComponentText(629, 56, Utils.formatDoubledAmount(inventoryValue));
//                player.getPackets().sendIComponentText(629, 44, "Equipment:");
//                player.getPackets().sendIComponentText(629, 57, Utils.formatDoubledAmount(equipmentValue));
//                player.getPackets().sendIComponentText(629, 45, "Grand Exchange");
//                player.getPackets().sendIComponentText(629, 58, "");
//                player.getPackets().sendIComponentText(629, 46, "Pending Offers:");
//                player.getPackets().sendIComponentText(629, 59, Utils.formatDoubledAmount(grandexchangeValue));
//                player.getPackets().sendIComponentText(629, 47, "Collection Box:");
//                player.getPackets().sendIComponentText(629, 60, Utils.formatDoubledAmount(collectionValue));
//                totalValue = bankValue + inventoryValue + equipmentValue + moneyPouch + collectionValue
//                        + grandexchangeValue;
//                player.getPackets().sendIComponentText(629, 48, "Total wealth:");
//                player.getPackets().sendIComponentText(629, 61, Utils.formatDoubledAmount(totalValue));
//                player.getPackets().sendIComponentText(629, 49, "");
//                player.getPackets().sendIComponentText(629, 62, "");
//                player.getPackets().sendIComponentText(629, 50, "Highest value Wildy kill:");
//                player.getPackets().sendIComponentText(629, 63,
//                        (player.getHighestValuedKill() >= Integer.MAX_VALUE ? "Lots!"
//                                : Utils.getFormattedNumber(player.getHighestValuedKill(), ',')));
//                int bossKills = 0;
//                bossKills += player.getBossKillcount().size();
//                player.getPackets().sendIComponentText(629, 51, "Total boss kills:");
//                player.getPackets().sendIComponentText(629, 64, Utils.getFormattedNumber(bossKills, ','));
//                player.getPackets().sendIComponentText(629, 52, "Slayer tasks completed:");
//                player.getPackets().sendIComponentText(629, 65,
//                        Utils.getFormattedNumber(player.getSlayerManager().getCompletedTasks()));
//                player.getPackets().sendHideIComponent(629, 68, true);
//                player.getPackets().sendHideIComponent(629, 69, true);
            } else if (componentId >= 46 && componentId <= 64) {
                int tabId = 9 - ((componentId - 46) / 2);
                if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
                    player.getBank().setCurrentTab(tabId);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
                    player.getBank().collapse(tabId);
            } else if (componentId == 95) {
                if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
                    player.getBank().withdrawItem(slotId, 1);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
                    player.getBank().withdrawItem(slotId, 5);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
                    player.getBank().withdrawItem(slotId, 10);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
                    player.getBank().withdrawLastAmount(slotId);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
                    player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
                    player.getTemporaryAttributtes().put("bank_isWithdraw", Boolean.TRUE);
                    player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
                } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
                    player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
                    player.getBank().withdrawItemButOne(slotId);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
                    player.getBank().sendExamine(slotId);

            } else if (componentId == 119)
                player.getBank().openEquipmentBonuses(true);
        } else if (interfaceId == 763) {
            if (componentId == 0) {
                if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
                    player.getBank().depositItem(slotId, 1, true);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
                    player.getBank().depositItem(slotId, 5, true);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
                    player.getBank().depositItem(slotId, 10, true);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
                    player.getBank().depositLastAmount(slotId);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
                    player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
                    player.getTemporaryAttributtes().remove("bank_isWithdraw");
                    player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
                } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
                    player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
                else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
                    player.getInventory().sendExamine(slotId);
            }
        }
    }
}