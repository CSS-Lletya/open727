package main.impl.rsinterface;

import com.rs.game.player.Player;
import com.rs.game.player.content.Shop;
import com.rs.net.decoders.WorldPacketsDecoder;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

/**
 * Mainly used to close interfaces that can't be closed properly.
 * 
 * @author Dennis
 *
 */

@RSInterfaceSignature(interfaceId = { 1266, 1265, 449 })
public class ShopInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
		switch(interfaceId) {
		case 1265:
				if (shop == null) {
					return;
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					shop.sendInfo(player, slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					shop.buy(player, slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					shop.buy(player, slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					shop.buy(player, slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					shop.buy(player, slotId, 50);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					shop.buy(player, slotId, 500);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					shop.sendExamine(player, slotId);
				}
			break;
		case 1266:
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				} else {
					if (shop == null) {
						return;
					}
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
						shop.sendValue(player, slotId);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
						shop.sell(player, slotId, 1);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
						shop.sell(player, slotId, 5);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
						shop.sell(player, slotId, 10);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						shop.sell(player, slotId, 50);
					}
				}
			}
			break;
		case 449:
			if (interfaceId == 449) {
				if (componentId == 0)
					shop.sendValue(player, slotId);
				if (componentId == 1)
					shop.sendInventory(player);
			}
			break;
		}
	}
}