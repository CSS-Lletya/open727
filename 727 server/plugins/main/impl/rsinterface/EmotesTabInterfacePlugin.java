package main.impl.rsinterface;

import com.rs.game.player.Player;
import com.rs.game.player.content.Emotes;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {590})
public class EmotesTabInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if ((interfaceId == 590 && componentId == 8) || interfaceId == 464) {
			Emotes.Emote.executeEmote(player, (byte) slotId);
		}
	}
}