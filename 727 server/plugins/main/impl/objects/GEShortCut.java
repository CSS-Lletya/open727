package main.impl.objects;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;
import skills.Skills;

@ObjectSignature(objectId = {}, name = {"Underwall tunnel"})
public class GEShortCut implements ObjectType {

	/*
	 * Not perfect animation but meh
	 */
    @Override
    public void execute(Player player, WorldObject object, int optionId) throws Exception {
        if (player.getSkills().getLevel(Skills.AGILITY) < 21) {
            player.getPackets().sendGameMessage("You need 21 to use this shortcut.");
            return;
        }
        if(object.getId() == 9311) {//Outside
        	LinkedTaskSequence intoSequence = new LinkedTaskSequence();
			intoSequence.connect(1, () -> {
				 player.lock();
                 player.addWalkSteps(3141, 3516);
			});
			intoSequence.connect(2, () -> player.setNextAnimation(new Animation(2589)));
			intoSequence.connect(3, () -> {
				  player.setNextAnimation(new Animation(2590));
                  player.setNextWorldTile(new WorldTile(3144, 3514,0));
			});
			intoSequence.connect(4, () -> {
				player.setNextAnimation(new Animation(2591));
				 player.unlock();
			});
			intoSequence.connect(5, () -> player.setNextAnimation(new Animation(Animation.RESET_ANIMATION)));
			intoSequence.start();
        } else if (object.getId() == 9312) {//inside
            LinkedTaskSequence outSequence = new LinkedTaskSequence();
			outSequence.connect(1, () -> {
				 player.lock();
                player.addWalkSteps(3142, 3514);
			});
			outSequence.connect(2, () -> player.setNextAnimation(new Animation(2589)));
			outSequence.connect(3, () -> {
				  player.setNextAnimation(new Animation(2590));
                 player.setNextWorldTile(new WorldTile(3138, 3516, 0));
			});
			outSequence.connect(4, () -> {
				player.setNextAnimation(new Animation(2591));
				 player.unlock();
			});
			outSequence.connect(5, () -> player.setNextAnimation(new Animation(Animation.RESET_ANIMATION)));
			outSequence.start();
        }
    }
}