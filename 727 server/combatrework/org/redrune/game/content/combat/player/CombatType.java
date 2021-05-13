package org.redrune.game.content.combat.player;

import java.util.Optional;

import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.content.combat.player.swing.MagicCombatSwing;
import org.redrune.game.content.combat.player.swing.MeleeCombatSwing;
import org.redrune.game.content.combat.player.swing.RangeCombatSwing;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public enum CombatType {
	MELEE(new MeleeCombatSwing()) {
		@Override
		public int getDelay(Player player, int weaponId) {
			if (weaponId != -1) {
				String weaponName = ItemDefinitions.forId(weaponId).getName().toLowerCase();
				// interval 0.6
				if (weaponId == 9703) {
					return 1;
				}
				// Interval 1.8
				if (weaponName.contains("saradomin sword") || weaponName.contains(" whip") || weaponName.equals("zamorakian spear") || weaponName.equals("korasi's sword")) {
					return 3;
				}
				// Interval 3.0
				if (weaponName.contains("spear") || weaponName.contains(" sword") || weaponName.contains("longsword") || weaponName.contains("light") || weaponName.contains("hatchet") || weaponName.contains("pickaxe ") || weaponName.contains("mace") || weaponName.contains("hasta") || weaponName.contains("warspear") || weaponName.contains("flail") || weaponName.contains("hammers")) {
					return 4;
				}
				// Interval 3.6
				if (weaponName.contains("godsword") || weaponName.contains("warhammer") || weaponName.contains("battleaxe") || weaponName.contains("maul") || weaponName.equals("dominion sword")) {
					return 5;
				}
				// Interval 4.2
				if (weaponName.contains("greataxe") || weaponName.contains("halberd") || weaponName.contains("2h sword") || weaponName.contains("two handed sword") || weaponName.contains("katana") || weaponName.equals("thok's sword")) {
					return 6;
				}
			}
			switch (weaponId) {
				case 6527: // tzhaar-ket-em
					return 4;
				case 10887: // barrelchest anchor
					return 5;
				case 15403: // balmung
				case 20084: // golden hammer
				case 6528: // tzhaar-ket-om
					return 6;
				default:
					return 3;
			}
		}
	},
	RANGE(new RangeCombatSwing()) {
		@Override
		public int getDelay(Player player, int weaponId) {
			final int attackStyle = player.getCombatDefinitions().getAttackStyle();
			int delay = 6;
			if (weaponId != -1) {
				String weaponName = ItemDefinitions.forId(weaponId).getName().toLowerCase();
				if (weaponName.contains("shortbow") || weaponName.contains("karil's crossbow") || weaponName.contains("sling")) {
					delay = 3;
				} else if (weaponName.contains("crossbow")) {
					delay = 5;
				} else if (weaponName.contains("dart") || weaponName.contains("knife")) {
					delay = 2;
				} else if (weaponName.contains("chinchompa") || weaponName.contains("crystal bow")) {
					delay = 4;
				} else if (weaponName.contains("toktz-xil-ul")) {
					delay = 3;
				} else {
					switch (weaponId) {
						case 15241:
							delay = 7;
							break;
						case 11235: // dark bows
						case 15701:
						case 15702:
						case 15703:
						case 15704:
							delay = 9;
							break;
						case 20171:
							delay = 4;
							break;
						default:
							delay = 6;
							break;
					}
				}
			}
			if (attackStyle == 1) {
				delay--;
			} else if (attackStyle == 2) {
				delay++;
			}
			return delay;
		}
	},
	MAGIC(new MagicCombatSwing()) {
		@Override
		public int getDelay(Player player, int spellId) {
			Optional<CombatSpellEvent> optional = CombatRegistry.getCombatSpell(player.getCombatDefinitions().getSpellbook(), spellId);
			return optional.map(combatSpellEvent -> combatSpellEvent.delay(player)).orElse(-1);
		}
	};
	
	/**
	 * The delay between each swing
	 *
	 * @param player
	 * 		The player
	 * @param id
	 * 		The id of the weapon used, or the id of the spell.
	 */
	public abstract int getDelay(Player player, int id);
	
	/**
	 * The combat swing type
	 */
	private final CombatTypeSwing swing;
	
	public CombatTypeSwing getSwing() {
		return swing;
	}
	
	CombatType(CombatTypeSwing swing) {
		this.swing = swing;
	}
}
