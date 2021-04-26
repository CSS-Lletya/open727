package com.rs.game.player;

import java.util.Optional;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.task.impl.ActorDeath;

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
		if (getActor() instanceof Player) {
			Player killer = (Player) getActor();
			killer.setAttackedByDelay(4);
		}
		getActor().getPackets().sendGameMessage("Oh dear, you have died.");
		getActor().setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
		getActor().setNextAnimation(new Animation(-1));
		getActor().heal(getActor().getMaxHitpoints());
		final int maxPrayer = getActor().getSkills().getLevelForXp(Skills.PRAYER) * 10;
		getActor().getPrayer().restorePrayer(maxPrayer);
		getActor().setDead(false);
		getActor().setNextAnimation(new Animation(Animation.RESET_ANIMATION));
		getActor().unlock();
	}
}