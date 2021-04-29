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
            //iron arrow, platelegs, platebody, full helm
            new Item(884, 1000), new Item(1067, 5), new Item(1115, 5), new Item(1153, 5),

            //iron kiteshield, dart, sword
            new Item(1191, 5), new Item(807, 500), new Item(1279, 5), new Item(4121, 5)
    };

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.getBank().addItems(testkit, true);
    }
}
