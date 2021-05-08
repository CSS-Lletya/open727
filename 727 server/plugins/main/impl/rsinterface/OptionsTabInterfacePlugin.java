package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {261})
public class OptionsTabInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if (player.getInterfaceManager().containsInventoryInter())
			return;
		if (componentId == 22) {
			if (player.getInterfaceManager().containsScreenInter()) {
				player.getPackets().sendGameMessage(
						"Please close the interface you have open before setting your graphic options.");
				return;
			}
			player.stopAll();
			player.getInterfaceManager().sendInterface(742);
		} else if (componentId == 12)
			player.switchAllowChatEffects();
		else if (componentId == 13) { // chat setup
			player.getInterfaceManager().sendSettings(982);
		} else if (componentId == 14)
			player.switchMouseButtons();
		else if (componentId == 24) // audio options
			player.getInterfaceManager().sendSettings(429);
		else if (componentId == 26)
			System.out.println("dead content");
	}
}