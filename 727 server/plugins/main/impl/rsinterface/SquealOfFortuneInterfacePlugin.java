package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {1252, 1253})
public class SquealOfFortuneInterfacePlugin implements RSInterface {
    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
        if(interfaceId == 1252) {
            if(componentId == 3) //Goblin icon
                if(player.getPlayerDetails().getSpins() > 0)
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


