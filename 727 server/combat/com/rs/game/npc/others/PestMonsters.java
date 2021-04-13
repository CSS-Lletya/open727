package com.rs.game.npc.others;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class PestMonsters extends NPC {

	public PestMonsters(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		final NPC npc = (NPC) source;
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		// deathEffects(npc);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					if (!(npc.getId() == 6142) || (npc.getId() == 6144) || (npc.getId() == 6145)
							|| (npc.getId() == 6143)) // Portals
						setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					drop();
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	/*
	 * /** Death effects of NPCs
	 * 
	 * @param n The npc TODO other monsters
	 */
	/*
	 * private void deathEffects(NPC n) { if (n.getId() == 6142) { for (Player players : PestControl.playersInGame) { players.getPackets().sendIComponentText(408, 13, "DEAD"); players.getPackets ().sendGameMessage("The west portal has been destroyed."); } PestControl.setPortals(0, true); } if (n.getId() == 6144) { for (Player players : PestControl.playersInGame) { players.getPackets().sendIComponentText(408, 15, "DEAD"); players.getPackets ().sendGameMessage("The south-east portal has been destroyed."); } PestControl.setPortals(1, true); } if (n.getId() == 6145) { for (Player players : PestControl.playersInGame) { players.getPackets().sendIComponentText(408, 16, "DEAD"); players.getPackets ().sendGameMessage("The south-west portal has been destroyed."); } PestControl.setPortals(2, true); } if (n.getId() == 6143) { for (Player players : PestControl.playersInGame) { players.getPackets().sendIComponentText(408, 14, "DEAD"); players.getPackets ().sendGameMessage("The east portal has been destroyed."); } PestControl.setPortals(3, true); } }
	 */

}
