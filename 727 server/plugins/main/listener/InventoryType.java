package main.listener;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * 
 * @author Dennis
 *
 */
public interface InventoryType {
	
	void execute(Player player, Item item, int option) throws Exception;
}
