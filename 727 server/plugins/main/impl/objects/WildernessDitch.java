package main.impl.objects;

import com.rs.game.*;
import com.rs.game.player.Player;
import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Wilderness wall"})
public class WildernessDitch implements ObjectType {

    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        if (optionId == 1) {
            int playerY = player.getY();
            int ditchY = object.getY();

            if(playerY < ditchY) {//You are south of the wall
                player.stopAll();
                player.lock(4);
                player.setNextAnimation(new Animation(6132));
                final WorldTile toTile = new WorldTile(object.getRotation() == 3 || object.getRotation() == 1 ? object.getX() - 1 : player.getX(),
                        object.getRotation() == 0 || object.getRotation() == 2 ? object.getY() + 2 : player.getY(), object.getHeight());
                player.setNextForceMovement(new ForceMovement(
                        new WorldTile(player), 1, toTile, 2, 	object.getRotation() == 0 || object.getRotation() == 2 ? ForceMovement.NORTH : ForceMovement.WEST));
                player.setNextWorldTile(toTile);
                player.faceObject(object);
    //        player.getControlerManager().startController("Wilderness");//allows player attack
                player.resetReceivedDamage();
            } else {//You are north of the wall
                player.lock();
                player.setNextAnimation(new Animation(6132));
                final WorldTile toTile = new WorldTile(object.getRotation() == 1 || object.getRotation() == 3 ? object.getX() + 2 : player.getX(),
                        object.getRotation() == 0 || object.getRotation() == 2 ? object.getY() - 1 : player.getY(), object.getHeight());

                player.setNextForceMovement(new ForceMovement(
                        new WorldTile(player), 1, toTile, 2, object.getRotation() == 0 || object.getRotation() == 2 ? ForceMovement.SOUTH : ForceMovement.EAST));

                player.setNextWorldTile(toTile);
                player.faceObject(object);
//               removeIcon();//pvp icon
//              removeControler();//wilderness player settings
                player.resetReceivedDamage();
                player.unlock();
            }


        }
    }
}