package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {1139})
public class ExtrasInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2) throws Exception {
		switch(componentId) {
		case 14:
			System.out.println("Solomon Store");
			break;
		case 7:
			System.out.println("SOF");
			break;
		case 16:
			System.out.println("Customize button");
			break;
		}
	}
}