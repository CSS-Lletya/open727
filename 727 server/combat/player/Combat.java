package player;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Entity;
import com.rs.game.player.Player;

import npc.NPC;
import player.type.CombatEffect;
import player.type.CombatEffectType;

public final class Combat {

	/**
	 * Applies the {@code effect} in any context.
	 * @param effect the effect that must be applied.
	 * @return {@code true} if it was successfully applied, {@code false}
	 * otherwise.
	 */
	public static boolean effect(Entity character, CombatEffectType effect) {
		return CombatEffect.EFFECTS.get(effect).start(character);
	}
	
	public static boolean hasAntiDragProtection(Entity target) {
		if (target instanceof NPC)
			return false;
		Player p2 = (Player) target;
		int shieldId = p2.getEquipment().getShieldId();
		return shieldId == 1540 || shieldId == 11283 || shieldId == 11284;
	}

	public static int getSlayerLevelForNPC(int id) {
		switch (id) {
		case 9463:
			return 93;
		default:
			return 0;
		}
	}

	public static int getDefenceEmote(Entity target) {
		if (target instanceof NPC) {
			NPC n = (NPC) target;
			return n.getCombatDefinitions().getDefenceEmote();
		} else {
			Player p = (Player) target;
			int shieldId = p.getEquipment().getShieldId();
			String shieldName = shieldId == -1 ? null
					: ItemDefinitions.getItemDefinitions(shieldId).getName().toLowerCase();
			if (shieldId == -1 || (shieldName.contains("book") && shieldId != 18346)) {
				int weaponId = p.getEquipment().getWeaponId();
				if (weaponId == -1)
					return 424;
				String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
				if (weaponName != null && !weaponName.equals("null")) {
					if (weaponName.contains("scimitar") || weaponName.contains("korasi sword"))
						return 15074;
					if (weaponName.contains("whip"))
						return 11974;
					if (weaponName.contains("staff of light"))
						return 12806;
					if (weaponName.contains("longsword") || weaponName.contains("darklight")
							|| weaponName.contains("silverlight") || weaponName.contains("excalibur"))
						return 388;
					if (weaponName.contains("dagger"))
						return 378;
					if (weaponName.contains("rapier"))
						return 13038;
					if (weaponName.contains("pickaxe"))
						return 397;
					if (weaponName.contains("mace"))
						return 403;
					if (weaponName.contains("claws"))
						return 404;
					if (weaponName.contains("hatchet"))
						return 397;
					if (weaponName.contains("greataxe"))
						return 12004;
					if (weaponName.contains("wand"))
						return 415;
					if (weaponName.contains("chaotic staff"))
						return 13046;
					if (weaponName.contains("staff"))
						return 420;
					if (weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em"))
						return 403;
					if (weaponName.contains("maul") || weaponName.contains("tzhaar-ket-om"))
						return 1666;
					if (weaponName.contains("zamorakian spear"))
						return 12008;
					if (weaponName.contains("spear") || weaponName.contains("halberd") || weaponName.contains("hasta"))
						return 430;
					if (weaponName.contains("2h sword") || weaponName.contains("godsword")
							|| weaponName.equals("saradomin sword"))
						return 7050;
				}
				return 424;
			}
			if (shieldName != null) {
				if (shieldName.contains("shield"))
					return 1156;
				if (shieldName.contains("defender"))
					return 4177;
			}
			switch (shieldId) {
			case -1:
			default:
				return 424;
			}
		}
	}

	private Combat() {
	}
}
