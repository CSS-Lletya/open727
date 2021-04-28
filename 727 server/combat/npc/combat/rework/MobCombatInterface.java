package npc.combat.rework;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utils.Utils;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;
import npc.familiar.Steeltitan;
import player.CombatDefinitions;
import player.PlayerCombat;
import skills.Skills;

public abstract class MobCombatInterface {
	
	public int execute(Player target, NPC mob) throws Exception {
		return 0;
	}
	
	public static int getRandomMaxHit(NPC npc, int maxHit, int attackStyle, Entity target) {
		int[] bonuses = npc.getBonuses();
		double attack = bonuses == null ? 0
				: attackStyle == NPCCombatDefinitions.RANGE ? bonuses[CombatDefinitions.RANGE_ATTACK]
						: attackStyle == NPCCombatDefinitions.MAGE ? bonuses[CombatDefinitions.MAGIC_ATTACK]
								: bonuses[CombatDefinitions.STAB_ATTACK];
		double defence;
		if (target instanceof Player) {
			Player targetPlayer = (Player) target;
			defence = targetPlayer.getSkills().getLevel(Skills.DEFENCE)
					+ (2 * targetPlayer.getCombatDefinitions().getBonuses()[attackStyle == NPCCombatDefinitions.RANGE
							? CombatDefinitions.RANGE_DEF
							: attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
									: CombatDefinitions.STAB_DEF]);
			defence *= targetPlayer.getPrayer().getDefenceMultiplier();
			if (attackStyle == NPCCombatDefinitions.MELEE) {
				if (targetPlayer.getFamiliar() instanceof Steeltitan)
					defence *= 1.15;
			}
		} else {
			NPC mobTarget = (NPC) target;
			defence = mobTarget.getBonuses() == null ? 0
					: mobTarget.getBonuses()[attackStyle == NPCCombatDefinitions.RANGE ? CombatDefinitions.RANGE_DEF
							: attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
									: CombatDefinitions.STAB_DEF];
			defence *= 2;
		}
		double probability = attack / defence;
		if (probability > 0.90) // max, 90% prob hit so even lvl 138 can miss at lvl 3
			probability = 0.90;
		else if (probability < 0.05) // minimun 5% so even lvl 3 can hit lvl 138
			probability = 0.05;
		if (probability < Math.random())
			return 0;
		return Utils.getRandom(maxHit);
	}
	
	public static Hit getRangeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.RANGE_DAMAGE);
	}

	public static Hit getMagicHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MAGIC_DAMAGE);
	}

	public static Hit getRegularHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.REGULAR_DAMAGE);
	}

	public static Hit getMeleeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MELEE_DAMAGE);
	}

	public static void delayHit(NPC npc, int delay, final Entity target, final Hit... hits) {
		npc.getCombat().addAttackedByDelay(target);
		World.get().submit(new Task(delay) {
			@Override
			protected void execute() {
				for (Hit hit : hits) {
					NPC npc = (NPC) hit.getSource();
					if (npc.isDead() || npc.hasFinished() || target.isDead() || target.hasFinished())
						return;
					target.applyHit(hit);
					npc.getCombat().doDefenceEmote(target);
					if (target instanceof Player) {
						Player targetPlayer = (Player) target;
						targetPlayer.closeInterfaces();
						if (targetPlayer.getCombatDefinitions().isAutoRelatie() && !targetPlayer.getActionManager().hasSkillWorking()
								&& !targetPlayer.hasWalkSteps())
							targetPlayer.getActionManager().setAction(new PlayerCombat(npc));
					} else {
						NPC targetNPC = (NPC) target;
						if (!targetNPC.isUnderCombat() || targetNPC.canBeAttackedByAutoRelatie())
							targetNPC.setTarget(npc);
					}

				}
				this.cancel();
			}
		});
	}
}