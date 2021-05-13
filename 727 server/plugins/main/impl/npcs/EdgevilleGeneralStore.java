package main.impl.npcs;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.CustomShops;
import com.rs.json.impl.ShopsLoader;
import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

@NPCSignature(name = {"Shopkeeper"}, npcId = {})
public class EdgevilleGeneralStore implements NPCType {

    @Override
    public void execute(Player player, NPC npc, int option) throws Exception {
        ShopsLoader.openShop(player, "General Store");
    }
}
