package com.rs.game.player;

import java.util.Optional;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.task.impl.ActorDeath;
import com.rs.net.host.HostManager;

import skills.Skills;

public class PlayerDeath extends ActorDeath<Player> {

	public PlayerDeath(Player actor) {
		super(actor);
	}

	@Override
	public void preDeath() {
		getActor().lock();
		getActor().setNextAnimation(new Animation(836));
	}

	@Override
	public void death() {
		if (!getActor().getControlerManager().sendDeath())
			return;
		getActor().setDead(true);
		getActor().getPoisonDamage().set(0);
		getActor().setAntifireDetail(Optional.empty());	
		getActor().stopAll();
		if (getActor().getFamiliar() != null)
			getActor().getFamiliar().sendDeath(getActor());
	}

	@Override
	public void postDeath() {
		getActor().getPackets().sendMusicEffect(90);
		getActor().getPackets().sendGameMessage("Oh dear, you have died.");
		getActor().setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
		getActor().setNextAnimation(new Animation(-1));
		getActor().heal(getActor().getMaxHitpoints());
		final int maxPrayer = getActor().getSkills().getLevelForXp(Skills.PRAYER) * 10;
		getActor().getPrayer().restorePrayer(maxPrayer);
		getActor().setDead(false);
		getActor().setNextAnimation(new Animation(Animation.RESET_ANIMATION));
		getActor().unlock();
		if (getActor() instanceof Player) {
			Player killer = (Player) getActor();
			killer.setAttackedByDelay(4);
			if(HostManager.same(getActor(), killer)) {
				killer.getPackets().sendGameMessage("You don't receive any points because you and " + getActor().getDisplayName() + " are connected from the same network.");
				return;
			}
		}
	}
}