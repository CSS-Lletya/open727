package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.others.DreadNip;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import player.poison.PoisonType;

public class DreadNipCombat extends CombatScript {

	private String[] DREADNIP_ATTACK_MESSAGE = { "Your dreadnip stunned its target!",
			"Your dreadnip poisened its target!" };

	@Override
	public Object[] getKeys() {
		return new Object[] { 14416 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		DreadNip dreadNip = (DreadNip) npc;
		if (dreadNip.getTicks() <= 3)
			return 0;
		npc.setNextAnimation(new Animation(-1));
		int attackStyle = Utils.random(2);
		switch (attackStyle) {
		case 0:
			break;
		case 1:
			int secondsDelay = 5 + Utils.getRandom(3);
			target.setFreezeDelay(secondsDelay);
			if (target instanceof Player) {
				Player player = (Player) target;
				player.getActionManager().addActionDelay(secondsDelay);
			} else {
				NPC npcTarget = (NPC) target;
				npcTarget.getCombat().setCombatDelay(npcTarget.getCombat().getCombatDelay() + secondsDelay);
			}
			break;
		case 2:
			target.poison(PoisonType.DEFAULT_NPC);
			break;
		}
		if (attackStyle != 0)
			dreadNip.getOwner().getPackets().sendGameMessage(DREADNIP_ATTACK_MESSAGE[attackStyle - 1]);
		delayHit(npc, 0, target,
				new Hit(npc, getRandomMaxHit(npc, 550, NPCCombatDefinitions.MELEE, target), HitLook.REGULAR_DAMAGE));
		return 5;
	}
}
