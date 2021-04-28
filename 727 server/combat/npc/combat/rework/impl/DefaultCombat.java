package npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.player.Player;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;
import npc.combat.rework.MobCombatInterface;
import npc.combat.rework.MobCombatSignature;

/**
 * This is a default class for mobs that have no special
 * combat abilities such as Man, Woman, Imp, such..
 * (Basically a fallback combat script if we don't have one made)
 * 
 * (use an array of NPC ids or call by array of npc names)
 * (Example: 708, 709, 710 | "imp", "Man", Woman")
 * @author Dennis
 *
 */
@MobCombatSignature(mobId = {}, mobName = {})
public class DefaultCombat extends MobCombatInterface {

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