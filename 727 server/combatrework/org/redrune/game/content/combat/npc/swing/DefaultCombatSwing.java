package org.redrune.game.content.combat.npc.swing;

import org.redrune.game.content.combat.npc.NPCCombatSwing;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.ProjectileManager;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public class DefaultCombatSwing implements NPCCombatSwing {
	
	@Override
	public Object[] bindings() {
		return new Object[0];
	}
	
	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions combatDefinitions = npc.getCombatDefinitions();
		int delay = combatDefinitions.getAttackDelay();
		int style = combatDefinitions.getAttackStyle();
		int maxHit = combatDefinitions.getMaxHit();
		int damage = randomMaxHit(npc, maxHit, style, target);
		int attackAnim = combatDefinitions.getAttackEmote();
		int projectile = combatDefinitions.getAttackProjectile();
		int graphics = combatDefinitions.getAttackGfx();
		if (style == MELEE_COMBAT_STYLE) {
			delayHit(npc, 1, target, constructMeleeHit(npc, damage));
		} else {
			// the projectile delay speed
			int projectileDelay = ProjectileManager.getProjectileDelay(npc, target);
			// the additional calculation
			double delayCalc = ProjectileManager.getDelay(npc, target, projectileDelay, 0);
			delayHit(npc, (int) (projectileDelay + delayCalc), target, style == RANGE_COMBAT_STYLE ? constructRangeHit(npc, damage) : constructMagicHit(npc, damage));
			if (projectile != -1) {
				ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(npc, target, projectile, 34, 10, 35, 16, 0));
			}
		}
		if (graphics != -1) {
			npc.setNextGraphics(new Graphics(graphics));
		}
		npc.setNextAnimation(new Animation(attackAnim));
		return delay;
	}
}
