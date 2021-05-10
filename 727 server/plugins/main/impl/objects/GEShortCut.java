package main.impl.objects;

import java.util.Timer;
import java.util.TimerTask;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;
import skills.Skills;

@ObjectSignature(objectId = {}, name = {"Underwall tunnel"})
public class GEShortCut implements ObjectType {
    Player player;
    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        this.player = player;
        if (player.getSkills().getLevel(Skills.AGILITY) < 21) {
            player.getPackets().sendGameMessage("You need 21 to use this object.");
            return;
        }
        if(object.getId() == 9311) {//Outside
            moveIntoGE();
        } else if (object.getId() == 9312) {//inside
            moveOutOfGE();
        }
    }

    private void moveIntoGE() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            int count = 0;
            @Override
            public void run()
            {
                if (count == 0) {
                    player.lock();
                    player.addWalkSteps(3140, 3516);
                }
                else if (count == 1) {
                    player.setNextAnimation(new Animation(2589));

                }
                else if (count == 2) {
                    player.setNextAnimation(new Animation (2590));
                    player.setNextWorldTile(new WorldTile(3142, 3514, 0));
                }
                else if (count == 3) {
                    player.setNextAnimation(new Animation(2591));
                    player.setNextWorldTile(new WorldTile(3143, 3514, 0));
                    player.unlock();
                }
                else if (count == 4) {
                    player.addWalkSteps(3144, 3514);
                }
                count++;
                if(count == 5)
                    timer.cancel();
            }
        }, 0, 1000);
    }

    private void moveOutOfGE() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            int count = 0;
            @Override
            public void run()
            {
                if (count == 0) {
                    player.lock();
                    player.addWalkSteps(3142, 3514);
                } else if (count == 1) {
                    player.setNextAnimation(new Animation(2589));
                } else if (count == 2) {
                    player.setNextAnimation(new Animation (2590));
                    player.setNextWorldTile(new WorldTile(3140, 3516, 0));
                } else if (count == 3) {
                    player.setNextAnimation(new Animation(2591));
                    player.setNextWorldTile(new WorldTile(3139, 3516, 0));
                    player.unlock();
                } else if (count == 4) {
                    player.addWalkSteps(3138, 3516);
                }
                count++;
                if(count == 5)
                    timer.cancel();
            }
        }, 0, 1000);
    }
}
