package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {182})
public class LogoutButton implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if (player.getInterfaceManager().containsInventoryInter()){
			System.out.println("failed cont invy inter");
			return;
		}
		if (componentId == 6 || componentId == 13)
			if (!player.hasFinished())
				player.logout(componentId == 6);
	}
}