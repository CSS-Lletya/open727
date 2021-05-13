package main.impl.objects;

import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Wilderness wall"})
public class WildernessDitch implements ObjectType {
    private Player player;
    private WorldObject ditch;
    private WorldTile destination;
    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        this.player = player;
        ditch = object;
        if (optionId == 1) {
            int playerY = player.getY();
            int ditchY = object.getY();

            player.stopAll();
            player.lock(4);
            player.setNextAnimation(new Animation(6132));

            if(playerY > ditchY) {//You are starting south
                hopNorth();
                player.getControlerManager().startControler("Wilderness");//allows player attack
            } else {//You are starting north
                hopSouth();
                //removeIcon();//pvp icon
                //removeControler();//turn off wilderness player settings
            }


            player.setNextWorldTile(destination);
            player.faceObject(object);
        }
    }
    
    private void hopNorth() {
        setDestinationNorth();
        player.setNextForceMovement(new ForceMovement(
                new WorldTile(player), 1, destination, 2, ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ForceMovement.SOUTH : ForceMovement.EAST));
        player.resetReceivedDamage();
        player.unlock();
    }

    private void hopSouth() {
        setDestinationSouth();
        player.setNextForceMovement(new ForceMovement(
                new WorldTile(player), 1, destination, 2, 	ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ForceMovement.NORTH : ForceMovement.WEST));
        player.resetReceivedDamage();
    }
    
    private void setDestinationSouth() {
        this.destination = new WorldTile(ditch.getRotation() == 3 || ditch.getRotation() == 1 ? ditch.getX() - 1 : player.getX(),
                ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ditch.getY() + 2 : player.getY(), ditch.getHeight());
    }
    
    private void setDestinationNorth() {
        this.destination = new WorldTile(ditch.getRotation() == 1 || ditch.getRotation() == 3 ? ditch.getX() + 2 : player.getX(),
                ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ditch.getY() - 1 : player.getY(), ditch.getHeight());
    }
}