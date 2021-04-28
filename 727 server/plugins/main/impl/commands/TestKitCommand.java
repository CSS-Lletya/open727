package main.impl.commands;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"testkit"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns test bank items")
public final class TestKitCommand implements Command {
    static final Item[] testkit = new Item[] {
            //iron arrow, platelegs, platebody
            new Item(884, 1000), new Item(1067, 5), new Item(1115, 5)
    };

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.getBank().addItems(testkit, true);
    }
}
