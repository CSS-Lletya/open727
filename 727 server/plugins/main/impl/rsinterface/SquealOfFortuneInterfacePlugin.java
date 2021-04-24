package main.impl.rsinterface;

import com.rs.Settings;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.utils.Utils;
import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RSInterfaceSignature(interfaceId = {1252, 1253})
public class SquealOfFortuneInterfacePlugin implements RSInterface {
    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2) throws Exception {
        if(interfaceId == 1252) {
            if(componentId == 3) //Goblin icon
                if(player.getSpins() > 0)
                    player.getSquealOfFortune().start();
                else
                    player.getPackets().sendGameMessage("No spins available");
            else if(componentId == 5) {//X Icon
                player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
                player.getPackets().sendGameMessage("You closed the Squeal of fortune interface, you can access SOF in the extras tab");
            }
        }
        player.getSquealOfFortune().handleSOFButtons(player, componentId);
    }
}


