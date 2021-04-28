package npc.combat.rework.impl;

import com.rs.game.Animation;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;
import npc.combat.rework.MobCombatInterface;
import npc.combat.rework.MobCombatSignature;
import npc.others.DreadNip;
import player.type.PoisonType;

@MobCombatSignature(mobId = {14416}, mobName = {})
public class DreadNipCombat extends MobCombatInterface {

	private String[] DREADNIP_ATTACK_MESSAGE = { "Your dreadnip stunned its target!",
	"Your dreadnip poisened its target!" };
	
	@Override
	public int execute(Player target, NPC npc) throws Exception {
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
				npc.getCombat().setCombatDelay(npc.getCombat().getCombatDelay() + secondsDelay);
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