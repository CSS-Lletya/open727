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

@RSInterfaceSignature(interfaceId = { 1266, 1265 })
public class ShopInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2)
			throws Exception {
		System.out.println(componentId);
		Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
		switch(interfaceId) {
//		case 1265:
//			
//			if (componentId == 1) {
//				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
//				if (shop == null) {
//					return;
//				}
//				shop.sendInventory(player);
//			} else if (componentId == 201) {
//				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
//				if (shop == null) {
//					return;
//				}
//				Integer slot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
//				if (slot == null) {
//					return;
//				}
//				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
//					shop.buy(player, slot, 1);
//				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
//					shop.buy(player, slot, 5);
//				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
//					shop.buy(player, slot, 10);
//				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
//					shop.buy(player, slot, 50);
//				}
//			}
//			break;
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
			if (componentId == 201 && packetId == 96) {
				shop.buy(player, slotId, 1);
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
//		case 1265:
//			Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
//			if (shop == null)
//				return;
//			@SuppressWarnings("unused")
//			Integer slot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
//			boolean isBuying = player.getTemporaryAttributtes().get("shop_buying") != null;
//
//			if (componentId == 20 || componentId == 201) {
//				player.getTemporaryAttributtes().put("ShopSelectedSlot", slotId);
////				if (componentId == 20 && !isBuying) {
////					shop.sendInfo(player, slotId, isBuying);
////					player.getPackets().sendConfig(2561, 93);
////					return;
////				}
//				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
//					shop.sendValue(player, slotId);
//				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
//					shop.sell(player, slotId, 1);
//				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
//					shop.sell(player, slotId, 5);
//				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
//					shop.sell(player, slotId, 10);
//				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
//					shop.sell(player, slotId, 50);
//				}
//			}
//			break;
		}
	}
}