package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {182})
public class LogoutButton implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId) throws Exception {
		if (componentId == 6)
			System.out.println("Lobby");
		if (componentId == 13)
			player.logout(false);
	}
}