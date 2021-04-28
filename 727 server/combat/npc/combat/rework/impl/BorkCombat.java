package npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;
import npc.combat.rework.MobCombatInterface;
import npc.combat.rework.MobCombatSignature;

@MobCombatSignature(mobId = {}, mobName = {"Bork"})
public class BorkCombat extends MobCombatInterface {

	public boolean spawnOrk = false;
	
	@Override
	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.getHitpoints() <= (defs.getHitpoints() * 0.4) && !spawnOrk) {
			Player player = (Player) target;
			npc.setNextForceTalk(new ForceTalk("Come to my aid, brothers!"));
			player.getControlerManager().startControler("BorkControler", 1, npc);
			spawnOrk = true;
		}
		npc.setNextAnimation(new Animation(Utils.getRandom(1) == 0 ? defs.getAttackEmote() : 8757));
		delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), -1, target)));
		return defs.getAttackDelay();
	}
}