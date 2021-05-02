package main.impl.commands;

import com.rs.game.item.Item;
import com.rs.game.item.ItemNames;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"testkit"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns test bank items")
public final class TestKitCommand implements Command {
    static final Item[] testkit = new Item[]{
            //iron arrow, platelegs, platebody, full helm
            new Item(884, 1000), new Item(1067, 5), new Item(1115, 5), new Item(1153, 5),

            //iron kiteshield, dart, sword
            new Item(1191, 5), new Item(807, 500), new Item(1279, 5), new Item(4121, 5),

            //bronze pick, axe, regular knife, tinderbox
            new Item(1265, 5), new Item(1351, 5), new Item(946, 5), new Item(590, 5),

            new Item(ItemNames.ABYSSAL_VINE_WHIP), new Item(ItemNames.ABYSSAL_WHIP), new Item(ItemNames.BARRELCHEST_ANCHOR),
            new Item(ItemNames.BRACKISH_BLADE),new Item(ItemNames.DARK_BOW),new Item(ItemNames.DARKLIGHT),new Item(ItemNames.DORGESHUUN_CBOW),
            new Item(ItemNames.DRAGON_2H_SWORD),new Item(ItemNames.DRAGON_BATTLEAXE),new Item(ItemNames.DRAGON_CLAW),new Item(ItemNames.DRAGON_DAGGER),
            new Item(ItemNames.DRAGON_HALBERD),new Item(ItemNames.DRAGON_HATCHET),new Item(ItemNames.DRAGON_LONGSWORD),new Item(ItemNames.DRAGON_MACE),
            new Item(ItemNames.DRAGON_PICKAXE),new Item(ItemNames.DRAGON_SCIMITAR),new Item(ItemNames.DRAGON_SPEAR),new Item(ItemNames.DWARVEN_ARMY_AXE),
            new Item(ItemNames.ENHANCED_EXCALIBUR),


    };
    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.getBank().addItems(testkit, true);
    }
}
