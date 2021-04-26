package com.rs.game.npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.combat.rework.MobCombatInterface;
import com.rs.game.npc.combat.rework.MobCombatSignature;
import com.rs.game.player.Player;

/**
 * This is just a basic example of a mob.
 * TODO: Convert old handlers to this new system.
 * @author Dennis
 *
 */
@MobCombatSignature(mobId = {708}, mobName = {})
public class ImpCombat extends MobCombatInterface {

	@Override
	public int execute(Player player, NPC npc) throws Exception {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		if (attackStyle == NPCCombatDefinitions.MELEE) {
			delayHit(npc, 0, player, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, player)));
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, player);
			delayHit(npc, 2, player,
					attackStyle == NPCCombatDefinitions.RANGE ? getRangeHit(npc, damage) : getMagicHit(npc, damage));
			if (defs.getAttackProjectile() != -1)
				World.sendProjectile(npc, player, defs.getAttackProjectile(), 41, 16, 41, 35, 16, 0);
		}
		if (defs.getAttackGfx() != -1)
			npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		return defs.getAttackDelay();
	}
}