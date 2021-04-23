package com.rs.game.npc.combat.rework;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

public interface MobCombatInterface {
	
	public int execute(Player player, NPC mob) throws Exception;
}