package main.impl.rsinterface;

import com.rs.game.player.Player;
import com.rs.game.player.content.EmotesManager;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {590})
public class EmotesTabInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2) throws Exception {
		if ((interfaceId == 590 && componentId == 8) || interfaceId == 464) {
			player.getEmotesManager().useBookEmote(interfaceId == 464 ? componentId : EmotesManager.getId(slotId, packetId));
		}
	}
}