package main.listener;

import com.rs.game.player.Player;

public interface RSInterface {
	
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception;
}
