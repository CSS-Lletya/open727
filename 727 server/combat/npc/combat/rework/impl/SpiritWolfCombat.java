package npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.player.Player;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;
import npc.combat.rework.MobCombatInterface;
import npc.combat.rework.MobCombatSignature;
import npc.familiar.Familiar;

@MobCombatSignature(mobId = { 6829, 6828 }, mobName = {})
public class SpiritWolfCombat extends MobCombatInterface {

	@Override
	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			familiar.submitSpecial(familiar.getOwner());
			npc.setNextAnimation(new Animation(8293));
			npc.setNextGraphics(new Graphics(1334));
			World.sendProjectile(npc, target, 1333, 34, 16, 30, 35, 16, 0);
			Entity entityA = (NPC) npc;
			if (target instanceof Entity) {
				if (npc.getCombatDefinitions().getAttackStyle() != NPCCombatDefinitions.SPECIAL)
					target.setAttackedByDelay(3000);// three seconds
				else
					familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot scare that monster.");
			}
			if (target instanceof Player)
				familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot scare a player.");
			else if (entityA instanceof Familiar)
				familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot scare other familiars.");
		} else {
			npc.setNextAnimation(new Animation(6829));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 40, NPCCombatDefinitions.MAGE, target)));
		}
		return defs.getAttackDelay();
	}
}