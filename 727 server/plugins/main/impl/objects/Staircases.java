package main.impl.objects;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Staircase"})
public class Staircases implements ObjectType {
    Player player;
    WorldObject staircase;
    int optionId;
    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        this.player = player;
        this.staircase = object;
        this.optionId = optionId;
        player.stopAll();
        player.lock(1);
        player.faceObject(object);
        Utils.runLater(changeHeight(), 650);
    }

    public Runnable changeHeight() {
        return new Runnable(){
            @Override
            public void run(){
                if(staircase.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-up"))
                    player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() + 1));
                else if(staircase.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-down"))
                    player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() - 1));
            }
        };
    }

}