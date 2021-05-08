package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {1139})
public class ExtrasInterfacePlugin implements RSInterface {
	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		player.getPackets().sendIComponentText(1139, 6, Integer.toString(player.getPlayerDetails().getSpins()));
		switch(componentId) {
			case 14:
				System.out.println("Solomon Store");
				break;
			case 7://Squeal Of Fortune
				if(player.getPlayerDetails().getSpins() > 0)
					player.getSquealOfFortune().start();
				else
					player.getPackets().sendGameMessage("No spins available");
				break;
			case 16:
				System.out.println("Customize button");
				break;
		}
	}
}