package main.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

import main.listener.InventoryType;
import main.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {1050})
public class SantaHatItem implements InventoryType {

	@Override
	public void execute(Player player, Item item, int option) throws Exception {
		System.out.println(option);
	}
}