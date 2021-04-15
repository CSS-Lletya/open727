package main.listener;

import com.rs.game.player.Player;

public interface RSInterface {
	
	void execute(Player player, int interfaceId, int componentId, int packetId) throws Exception;
}
