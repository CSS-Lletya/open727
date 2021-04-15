package main.impl.rsinterface;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {679})
public class InventoryInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2) throws Exception {
		if (componentId == 0) {
			if (slotId > 27 || player.getInterfaceManager().containsInventoryInter())
				return;
			Item item = player.getInventory().getItem(slotId);
			if (item == null || item.getId() != slotId2)
				return;
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
				InventoryOptionsHandler.handleItemOption1(player, slotId, slotId2, item);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
				InventoryOptionsHandler.handleItemOption2(player, slotId, slotId2, item);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
				InventoryOptionsHandler.handleItemOption3(player, slotId, slotId2, item);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
				InventoryOptionsHandler.handleItemOption4(player, slotId, slotId2, item);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
				InventoryOptionsHandler.handleItemOption5(player, slotId, slotId2, item);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
				InventoryOptionsHandler.handleItemOption6(player, slotId, slotId2, item);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
				InventoryOptionsHandler.handleItemOption7(player, slotId, slotId2, item);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
				InventoryOptionsHandler.handleItemOption8(player, slotId, slotId2, item);
		}
	}
}