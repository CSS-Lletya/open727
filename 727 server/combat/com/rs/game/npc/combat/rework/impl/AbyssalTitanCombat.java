package com.rs.game.npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.combat.rework.MobCombatInterface;
import com.rs.game.npc.combat.rework.MobCombatSignature;
import com.rs.game.player.Player;

@MobCombatSignature(mobId = {}, mobName = {"Abbysal Titan"})
public class AbyssalTitanCombat extends MobCombatInterface {

	@Override
	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = 0;
		damage = getRandomMaxHit(npc, 140, NPCCombatDefinitions.MELEE, target);
		npc.setNextAnimation(new Animation(7980));
		npc.setNextGraphics(new Graphics(1490));

		if (target instanceof Player) { // cjay failed dragonkk saved the day
			Player player = (Player) target;
			if (damage > 0 && player.getPrayer().getPrayerpoints() > 0)
				player.getPrayer().drainPrayer(damage / 2);
		}
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		return defs.getAttackDelay();
	}
}