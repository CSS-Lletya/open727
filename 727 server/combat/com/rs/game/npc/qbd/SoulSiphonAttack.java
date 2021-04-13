package com.rs.game.npc.qbd;

import java.util.Iterator;

import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * The Queen Black Dragon's soul siphon attack.
 * @author Emperor
 *
 */
public final class SoulSiphonAttack implements QueenAttack {

	/**
	 * The siphon graphics.
	 */
	private static final Graphics SIPHON_GRAPHIC = new Graphics(3148);

	@Override
	public int attack(final QueenBlackDragon npc, Player victim) {
		for (Iterator<TorturedSoul> it = npc.getSouls().iterator(); it.hasNext();) {
			TorturedSoul soul = it.next();
			if (soul.isDead()) {
				it.remove();
			}
		}
		if (npc.getSouls().isEmpty()) {
			return 1;
		}
		victim.getPackets()
				.sendGameMessage("<col=9900CC>The Queen Black Dragon starts to siphon the energy of her mages.</col>");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				for (Iterator<TorturedSoul> it = npc.getSouls().iterator(); it.hasNext();) {
					TorturedSoul soul = it.next();
					if (soul.isDead()) {
						it.remove();
						continue;
					}
					soul.setNextGraphics(SIPHON_GRAPHIC);
					soul.applyHit(new Hit(npc, 20, HitLook.REGULAR_DAMAGE));
					npc.getNextHits().add(new Hit(npc, 40, HitLook.HEALED_DAMAGE));
					npc.heal(40);
				}
				if (npc.getSouls().isEmpty()) {
					stop();
					npc.getTemporaryAttributtes().put("_last_soul_summon", npc.getTicks() + Utils.random(120) + 125);
				}
			}
		}, 0, 0);
		npc.getTemporaryAttributtes().put("_last_soul_summon", npc.getTicks() + 999);
		npc.getTemporaryAttributtes().put("_soul_siphon_atk", npc.getTicks() + 50 + Utils.random(40));
		return Utils.random(5, 10);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		Integer tick = (Integer) npc.getTemporaryAttributtes().get("_soul_siphon_atk");
		return tick == null || tick < npc.getTicks();
	}

}