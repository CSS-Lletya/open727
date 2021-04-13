package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class BorkCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bork" };
	}

	public boolean spawnOrk = false;

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions cdef = npc.getCombatDefinitions();
		if (npc.getHitpoints() <= (cdef.getHitpoints() * 0.4) && !spawnOrk) {
			Player player = (Player) target;
			npc.setNextForceTalk(new ForceTalk("Come to my aid, brothers!"));
			player.getControlerManager().startControler("BorkControler", 1, npc);
			spawnOrk = true;
		}
		npc.setNextAnimation(new Animation(Utils.getRandom(1) == 0 ? cdef.getAttackEmote() : 8757));
		delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, cdef.getMaxHit(), -1, target)));
		return cdef.getAttackDelay();
	}

}
