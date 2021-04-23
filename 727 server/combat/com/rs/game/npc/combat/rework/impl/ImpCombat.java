package com.rs.game.npc.combat.rework.impl;

import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.combat.rework.MobCombatInterface;
import com.rs.game.npc.combat.rework.MobCombatSignature;
import com.rs.game.player.Player;

/**
 * This is just a test class,
 * Read NPCCombatDispatcher for more
 * information regarding combat.
 * @author Dennis
 *
 */
@MobCombatSignature(mobId = {}, mobName = {"Imp"})
public class ImpCombat implements MobCombatInterface {

	@Override
	public int execute(Player player, NPC mob) throws Exception {
		final NPCCombatDefinitions defs = mob.getCombatDefinitions();
		System.out.println("SUP");
		return defs.getAttackDelay();
	}
}