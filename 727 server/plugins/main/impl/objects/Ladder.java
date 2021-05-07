package main.impl.objects;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.utils.Utils;
import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Ladder"})
public class Ladder implements ObjectType {
    Player player;
    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        this.player = player;
        player.stopAll();
        player.lock(2);
        player.faceObject(object);

        Utils.runLater(climbAnimate(), 900);
        Utils.runLater(changeHeight(), 2000);

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
                player.setLocation(player.getX(), player.getY(), player.getHeight()+1);
            }
        };
    }
}