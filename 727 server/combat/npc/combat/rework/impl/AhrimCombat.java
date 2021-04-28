package npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;
import npc.combat.rework.MobCombatInterface;
import npc.combat.rework.MobCombatSignature;
import skills.Skills;

@MobCombatSignature(mobId = {}, mobName = {"Ahrim"})
public class AhrimCombat extends MobCombatInterface {

	@Override
	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		if (damage != 0 && target instanceof Player && Utils.random(3) == 0) {
			target.setNextGraphics(new Graphics(400, 0, 100));
			Player targetPlayer = (Player) target;
			int currentLevel = targetPlayer.getSkills().getLevel(Skills.STRENGTH);
			targetPlayer.getSkills().set(Skills.STRENGTH, currentLevel < 5 ? 0 : currentLevel - 5);
		}
		World.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16, 41, 35, 16, 0);
		npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
		delayHit(npc, 2, target, getMagicHit(npc, damage));
		return defs.getAttackDelay();
	}
}