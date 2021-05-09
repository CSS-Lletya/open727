package main.impl.objects;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.utils.Utils;
import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Ladder"})
public class Ladder implements ObjectType {
    Player player;
    WorldObject ladder;
    int optionId;

    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        this.player = player;
        this.ladder = object;
        this.optionId = optionId;
        player.stopAll();
        player.lock(2);
        player.faceObject(object);

        Utils.runLater(climbAnimate(), 800);
        Utils.runLater(changeHeight(), 1300);

    }

    public Runnable climbAnimate() {
        return new Runnable(){
            @Override
            public void run(){
                player.setNextAnimation(new Animation(828));
            }
        };
    }

    public Runnable changeHeight() {
        return new Runnable(){
            @Override
            public void run(){
                if(ladder.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-up"))
                    player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() + 1));
                else if(ladder.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-down"))
                    player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() - 1));
            }
        };
    }
}