package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class StrykewyrmCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 9463, 9465, 9467 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.getRandom(10);
		if (attackStyle <= 7) { // melee
			int size = npc.getSize();
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
				// nothing
			} else {
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(npc, 0, target,
						getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
				return defs.getAttackDelay();
			}
		}
		if (attackStyle <= 9) { // mage
			npc.setNextAnimation(new Animation(12794));
			final Hit hit = getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target));
			delayHit(npc, 1, target, hit);
			World.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16, 41, 30, 16, 0);
			if (npc.getId() == 9463) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if (Utils.getRandom(10) == 0 && target.getFreezeDelay() < System.currentTimeMillis()) {
							target.addFreezeDelay(3000);
							target.setNextGraphics(new Graphics(369));
							if (target instanceof Player) {
								Player targetPlayer = (Player) target;
								targetPlayer.stopAll();
							}
						} else if (hit.getDamage() != 0)
							target.setNextGraphics(new Graphics(2315));
					}
				}, 1);
			}
		} else if (attackStyle == 10) { // bury
			final WorldTile tile = new WorldTile(target);
			tile.moveLocation(-1, -1, 0);
			npc.setNextAnimation(new Animation(12796));
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			final int id = npc.getId();
			WorldTasksManager.schedule(new WorldTask() {

				int count;

				@Override
				public void run() {
					if (count == 0) {

						npc.transformIntoNPC(id - 1);
						npc.setForceWalk(tile);
						count++;
					} else if (count == 1 && !npc.hasForceWalk()) {
						npc.transformIntoNPC(id);
						npc.setNextAnimation(new Animation(12795));
						int distanceX = target.getX() - npc.getX();
						int distanceY = target.getY() - npc.getY();
						int size = npc.getSize();
						if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1)
							delayHit(npc, 0, target, new Hit(npc, 300, HitLook.REGULAR_DAMAGE));
						count++;
					} else if (count == 2) {
						npc.getCombat().setCombatDelay(defs.getAttackDelay());
						npc.setTarget(target);
						npc.setCantInteract(false);
						stop();
					}
				}
			}, 1, 1);
		}
		return defs.getAttackDelay();
	}
}
