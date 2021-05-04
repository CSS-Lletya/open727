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
            new Item(ItemNames.ENHANCED_EXCALIBUR),new Item(ItemNames.EXCALIBUR),new Item(ItemNames.ARMADYL_GODSWORD),new Item(ItemNames.BANDOS_GODSWORD),
            new Item(ItemNames.SARADOMIN_GODSWORD),new Item(ItemNames.ZAMORAK_GODSWORD),new Item(ItemNames.GRANITE_MACE),new Item(ItemNames.GRANITE_MAUL),
            new Item(ItemNames.GUTHIX_BOW),new Item(ItemNames.HAND_CANNON),new Item(ItemNames.KEENBLADE),new Item(ItemNames.KORASIS_SWORD),
            new Item(ItemNames.MAGIC_COMPOSITE_BOW),new Item(ItemNames.MAGIC_SHIELDBOW),new Item(ItemNames.MAGIC_SHORTBOW),new Item(ItemNames.MINDSPIKE_AIR),
            new Item(ItemNames.MINDSPIKE_EARTH),new Item(ItemNames.MINDSPIKE_FIRE),new Item(ItemNames.MINDSPIKE_WATER),new Item(ItemNames.MORRIGANS_JAVELIN, 50),
            new Item(ItemNames.MORRIGANS_THROWING_AXE),new Item(ItemNames.QUICKBOW),new Item(ItemNames.RUNE_CLAW),new Item(ItemNames.RUNE_THROWING_AXE, 50),
            new Item(ItemNames.SARADOMIN_BOW),new Item(ItemNames.SARADOMIN_SWORD),new Item(ItemNames.SEERCULL),new Item(ItemNames.STAFF_OF_LIGHT),
            new Item(ItemNames.STATIUS_WARHAMMER),new Item(ItemNames.VESTAS_LONGSWORD),new Item(ItemNames.VESTAS_SPEAR),new Item(ItemNames.ZAMORAKIAN_SPEAR),
            new Item(ItemNames.ZANIKS_CROSSBOW),


    };
    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.getBank().addItems(testkit, true);
    }
}
