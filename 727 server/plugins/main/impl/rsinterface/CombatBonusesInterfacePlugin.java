package main.impl.rsinterface;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.ItemExamines;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

/**
 * Doesn't seem to wanna work. No print outs on anything for this.
 * @author Dennis
 *
 */
@RSInterfaceSignature(interfaceId = { 667 })
public class CombatBonusesInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2) throws Exception {
		System.out.println("?");
		if (componentId == 14) {
			if (slotId >= 14)
				return;
			Item item = player.getEquipment().getItem(slotId);
			if (item == null)
				return;

			if (packetId == 3) {
				player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
				if (item.getDefinitions().getValue() <= 1) {
					return;
				}
				System.out.println(item.getName());
//				player.getPackets().sendGameMessage(Colors.blue + "x" + Utils.format(item.getAmount()) + " "
//						+ item.getName() + " value: "
//						+ Utils.format(item.getDefinitions().getValue() * item.getAmount()) + "gp (HA:"
//						+ Utils.format(item.getDefinitions().getHighAlchPrice() * item.getAmount()) + "gp)");
			} else if (packetId == 216) {
				EquipmentInterfacePlugin.sendRemove(player, slotId);
				EquipmentInterfacePlugin.refreshEquipBonuses(player);
			}
		}
		if (componentId == 87) {
			player.stopAll();
		}
		if (componentId == 9) {
			if (slotId >= 14)
				return;
			Item item = player.getEquipment().getItem(slotId);
			if (item == null)
				return;
			if (packetId == 32) {
				player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
				if (item.getDefinitions().getValue() <= 1) {
					return;
				}
				System.out.println(item.getName());
//				player.getPackets().sendGameMessage(Colors.blue + "x" + Utils.format(item.getAmount()) + " "
//						+ item.getName() + " value: "
//						+ Utils.format(item.getDefinitions().getValue() * item.getAmount()) + "gp (HA:"
//						+ Utils.format(item.getDefinitions().getHighAlchPrice() * item.getAmount()) + "gp)");
			}
			if (packetId == 96) {
//				sendItemStats(player, item);
			} else if (packetId == 14) {
				EquipmentInterfacePlugin.sendRemove(player, slotId);
				player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
				EquipmentInterfacePlugin.refreshEquipBonuses(player);
			}
		} else if (componentId == 46) {
//			player.getBank().openBank();
//			player.getPackets().sendIComponentText(762, 47,
//					"Bank Value: " + Utils.formatNumber(player.getBank().getBankValue()) + "gp");
		}
	}
}