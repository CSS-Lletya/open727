package com.rs.game.npc;

import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.task.Task;
import com.rs.game.task.impl.ActorDeath;

public class MobDeath extends ActorDeath<NPC> {

	private final NPCCombatDefinitions definition = getActor().getCombatDefinitions();
	
	public MobDeath(NPC actor) {
		super(actor);
	}
	
	@Override
	public void preDeath() {
		World.get().getTask().cancel(this);
		getActor().setNextAnimation(null);
		getActor().setDead(true);
		getActor().getPoisonDamage().set(0);
		getActor().resetWalkSteps();
		getActor().getCombat().removeTarget();
	}

	@Override
	public void death() {
		World.get().submit(new Task(0) {
			int loop;
			@Override
			protected void execute() {
				if (loop == 0) {
					getActor().setNextAnimation(new Animation(definition.getDeathEmote()));
				} else if (loop >= definition.getDeathDelay()) {
					getActor().drop();
					getActor().reset();
					getActor().setLocation(getActor().getRespawnTile());
					getActor().finish();
					this.cancel();
				}
				loop++;
			}
		});
	}

	@Override
	public void postDeath() {
		if (!getActor().isSpawned())
			getActor().setRespawnTask();
	}
}