package org.redrune.game.content.combat.player.calc;

import org.redrune.game.content.combat.player.CombatTypeCalculator;

import com.rs.game.Entity;
import com.rs.game.player.Player;

import npc.NPC;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class MagicCombatCalculator implements CombatTypeCalculator {
	
	@Override
	public double totalAggressiveBoost(Player player, Object... params) {
		// the prayer level bonus
		final int level = player.getSkills().getLevel(MAGIC);
		// the prayer bonus
		final double prayer = player.getManager().getPrayers().getBasePrayerBoost(MAGIC);
		// the calculated boost
		double effective = Math.floor(level * prayer);
		// the bonus from your equipment
		int bonus = player.getEquipment().getBonus(MAGIC_ATTACK);
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}
	
	@Override
	public double totalDefensiveBoost(Entity entity, Object... params) {
		// the targets defence level
		int defenceLevel;
		// the targets magic level
		int magicLevel;
		// the targets prayer boost
		double prayer;
		// the targets magic defence bonus
		double bonus;
		if (entity.isPlayer()) {
			Player player = entity.toPlayer();
			defenceLevel = player.getSkills().getLevel(DEFENCE);
			magicLevel = player.getSkills().getLevel(MAGIC);
			prayer = entity.toPlayer().getManager().getPrayers().getBasePrayerBoost(MAGIC);
			bonus = player.getEquipment().getBonus(MAGIC_DEFENCE);
		} else {
			NPC npc = entity.toNPC();
			int combatLevel = npc.getCombatLevel();
			defenceLevel = combatLevel / 2;
			magicLevel = combatLevel / 2;
			prayer = 1.0;
			bonus = npc.getBonus(MAGIC_DEFENCE);
		}
		// the effective calculation
		double effective = Math.floor((defenceLevel * prayer) * 0.3) + (magicLevel * 0.7);
		// the equipment calculation [based on magic defence]
		int equipment = (int) (bonus + 5);
		return (int) Math.floor(((effective + 8) * (equipment + 64)) / 10);
	}
	
	/**
	 * The magic max damage is set by the spell
	 *
	 * @param player
	 * 		The player
	 * @param params
	 * 		The parameters
	 * @return -1
	 */
	@Override
	public double maximumDamageAppendable(Player player, Object... params) {
		return -1;
	}
}
