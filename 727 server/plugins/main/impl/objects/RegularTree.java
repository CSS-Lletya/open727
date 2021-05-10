package main.impl.objects;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

//How to even start this?
@ObjectSignature(objectId = {61192}, name = {"Tree"})
public class RegularTree implements ObjectType {
    WorldObject tree;
    int optionId;
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        this.tree = object;
        this.optionId = optionId;
        if(isChoppable())
            System.out.println("chop chop");
    }

    private boolean isChoppable() {
        return tree.getDefinitions().getOption(optionId).equalsIgnoreCase("Chop down");
    }
}
