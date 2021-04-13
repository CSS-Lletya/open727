package com.rs.game.npc.combat.impl;

import java.util.List;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;

public class YtMejKotCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Yt-MejKot" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		delayHit(npc, 0, target,
				getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), defs.getAttackStyle(), target)));
		if (npc.getHitpoints() < npc.getMaxHitpoints() / 2) {
			if (npc.getTemporaryAttributtes().remove("Heal") != null) {
				npc.setNextGraphics(new Graphics(2980, 0, 100));
				List<Integer> npcIndexes = World.getRegion(npc.getRegionId()).getNPCsIndexes();
				if (npcIndexes != null) {
					for (int npcIndex : npcIndexes) {
						NPC n = World.getNPCs().get(npcIndex);
						if (n == null || n.isDead() || n.hasFinished())
							continue;
						n.heal(100);
					}
				}
			} else
				npc.getTemporaryAttributtes().put("Heal", Boolean.TRUE);
		}
		return defs.getAttackDelay();
	}
}
