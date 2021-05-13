package main.impl.objects;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {69835, 69834, 69833, 69832, 69829, 69837, 69838, 69839, 69840, 69841, 69828, 69827}, name = {})
public class Lodestones implements ObjectType {

    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        player.getLodeStones().activateLodestone(object);
    }
}
