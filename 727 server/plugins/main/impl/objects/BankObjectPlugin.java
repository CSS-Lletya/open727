package main.impl.objects;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Counter"})
public class BankObjectPlugin implements ObjectType {

    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        if (optionId == 2) {
            player.getBank().openBank();
        }

        if (object.getDefinitions().containsOption("Deposit"))
            System.out.println("Respnding to your Bank option");
    }
}