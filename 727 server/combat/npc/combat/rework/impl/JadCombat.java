package npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utils.Utils;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;
import npc.combat.rework.MobCombatInterface;
import npc.combat.rework.MobCombatSignature;

/**
 * Non melee doesn't hit, find out why.
 * 
 * @author Dennis
 *
 */
@MobCombatSignature(mobId = {}, mobName = { "Jad" })
public class JadCombat extends MobCombatInterface {

	@Override
	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.random(3);
		if (attackStyle == 2) { // melee
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			int size = npc.getSize();
			if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)
				attackStyle = Utils.random(2); // set mage
			else {
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(npc, 1, target,
						getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
				return defs.getAttackDelay();
			}
		}
		if (attackStyle == 1) { // range
			npc.setNextAnimation(new Animation(16202));
			npc.setNextGraphics(new Graphics(2994));
			World.get().submit(new Task(3) {
				@Override
				protected void execute() {
					target.setNextGraphics(new Graphics(3000));
					delayHit(npc, 1, target, getRangeHit(npc,
							getRandomMaxHit(npc, defs.getMaxHit() - 2, NPCCombatDefinitions.RANGE, target)));
					this.cancel();
				}
			});
		} else {
			npc.setNextAnimation(new Animation(16195));
			npc.setNextGraphics(new Graphics(2995));
			World.get().submit(new Task(2) {
				@Override
				protected void execute() {
					target.setNextGraphics(new Graphics(2741, 0, 100));
					delayHit(npc, 0, target, getMagicHit(npc,
							getRandomMaxHit(npc, defs.getMaxHit() - 2, NPCCombatDefinitions.MAGE, target)));
					this.cancel();
				}
			});
		}

		return defs.getAttackDelay() + 2;
	}
}