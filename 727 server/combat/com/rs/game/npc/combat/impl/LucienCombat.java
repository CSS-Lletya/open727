package com.rs.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.HashMap;

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

public class LucienCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 14256 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.getRandom(5);

		if (Utils.getRandom(10) == 0) {
			ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
			final HashMap<String, int[]> tiles = new HashMap<String, int[]>();
			for (Entity t : possibleTargets) {
				if (t instanceof Player) {
					Player p = (Player) t;
					if (!p.getMusicsManager().hasMusic(1008)) {
						p.getMusicsManager().playMusic(581);
						p.getMusicsManager().playMusic(584);
						p.getMusicsManager().playMusic(579);
						p.getMusicsManager().playMusic(1008);
						p.getPackets().sendGameMessage("You've received a reward while fighting Lucius!");
					}
				}
				String key = t.getX() + "_" + t.getY();
				if (!tiles.containsKey(t.getX() + "_" + t.getY())) {
					tiles.put(key, new int[] { t.getX(), t.getY() });
					World.sendProjectile(npc, new WorldTile(t.getX(), t.getY(), npc.getHeight()), 1900, 34, 0, 30, 35,
							16, 0);
				}
			}
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
					for (int[] tile : tiles.values()) {

						World.sendGraphics(null, new Graphics(1896), new WorldTile(tile[0], tile[1], 0));
						for (Entity t : possibleTargets)
							if (t.getX() == tile[0] && t.getY() == tile[1])
								t.applyHit(new Hit(npc, Utils.getRandom(400) + 400, HitLook.REGULAR_DAMAGE));
					}
					stop();
				}

			}, 5);
		} else if (Utils.getRandom(10) == 0) {
			npc.setNextGraphics(new Graphics(444));
			npc.heal(1000);
		}
		if (attackStyle == 0) { // normal mage move
			npc.setNextAnimation(new Animation(11338));
			delayHit(npc, 2, target,
					getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 2963, 34, 16, 40, 35, 16, 0);
		} else if (attackStyle == 1) { // normal mage move
			npc.setNextAnimation(new Animation(11338));
			delayHit(npc, 2, target, getRangeHit(npc, getRandomMaxHit(npc, 900, NPCCombatDefinitions.RANGE, target)));
			World.sendProjectile(npc, target, 1904, 34, 16, 30, 35, 16, 0);

			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					target.setNextGraphics(new Graphics(1910));
				}

			}, 2);

		} else if (attackStyle == 2) {
			npc.setNextAnimation(new Animation(11318));
			npc.setNextGraphics(new Graphics(1901));
			World.sendProjectile(npc, target, 1899, 34, 16, 30, 95, 16, 0);
			delayHit(npc, 4, target,
					getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
		} else if (attackStyle == 3) {
			npc.setNextAnimation(new Animation(11373));
			npc.setNextGraphics(new Graphics(1898));
			target.setNextGraphics(new Graphics(2954));
			delayHit(npc, 2, target,
					getRegularHit(npc, target.getMaxHitpoints() - 1 > 900 ? 900 : target.getMaxHitpoints() - 1));
		} else if (attackStyle == 4) {
			/*
			 * 11364 - even better k0 move. fire balls from sky into everyone 80% max hp or gfx 2600 everyone near dies
			 */
			npc.setNextAnimation(new Animation(11364));
			npc.setNextGraphics(new Graphics(2600));
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (Entity t : npc.getPossibleTargets()) {
						t.applyHit(new Hit(npc, (int) (t.getHitpoints() * Math.random()), HitLook.REGULAR_DAMAGE, 0));
					}
					npc.getCombat().addCombatDelay(3);
					npc.setCantInteract(false);
					npc.setTarget(target);
				}

			}, 4);
			return 0;
		} else if (attackStyle == 5) {
			npc.setCantInteract(true);
			npc.setNextAnimation(new Animation(11319));
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					npc.setCantInteract(false);
					npc.setTarget(target);
					int size = npc.getSize();
					int[][] dirs = Utils.getCoordOffsetsNear(size);
					for (int dir = 0; dir < dirs[0].length; dir++) {
						final WorldTile tile = new WorldTile(new WorldTile(target.getX() + dirs[0][dir],
								target.getY() + dirs[1][dir], target.getHeight()));
						if (World.canMoveNPC(tile.getHeight(), tile.getX(), tile.getY(), size)) { // if found done
							npc.setNextWorldTile(tile);
						}
					}
				}
			}, 3);
			return defs.getAttackDelay();
		}

		return defs.getAttackDelay();
	}
}
