package com.rs.game.npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.combat.rework.MobCombatInterface;
import com.rs.game.npc.combat.rework.MobCombatSignature;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

@MobCombatSignature(mobId = {}, mobName = {"Ork legion"})
public class OrkLegionCombat extends MobCombatInterface {

	public String[] messages = { "For Bork!", "Die Human!", "To the attack!", "All together now!" };
	
	@Override
	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		if (Utils.getRandom(3) == 0)
			npc.setNextForceTalk(new ForceTalk(messages[Utils.getRandom(messages.length > 3 ? 3 : 0)]));
		delayHit(npc, 0, target, getMeleeHit(npc, defs.getMaxHit()));
		return defs.getAttackDelay();
	}
}