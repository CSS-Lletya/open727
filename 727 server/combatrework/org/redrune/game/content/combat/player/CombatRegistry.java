package org.redrune.game.content.combat.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.redrune.game.content.combat.MagicConstants;
import org.redrune.game.content.combat.player.registry.CombatRegistryEvent;
import org.redrune.game.content.combat.player.registry.wrapper.BowFireEvent;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.content.combat.player.registry.wrapper.magic.MagicSpellEvent;
import org.redrune.game.node.entity.player.link.prayer.PrayerEffectRepository;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import skills.SkillConstants;

/**
 * The registry for all combat data that is stored. This is for special attacks and magic spells.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class CombatRegistry implements MagicConstants {
	
	/**
	 * The map of special attacks, the key being the weapon name.
	 */
	private static final Map<String, SpecialAttackEvent> SPECIALS = new HashMap<>();
	
	/**
	 * The map of bows, the key being the weapon name.
	 */
	private static final Map<String, BowFireEvent> BOWS = new HashMap<>();
	
	/**
	 * The list of all spells we can have
	 */
	private static final List<MagicSpellEvent<?>> SPELL_EVENTS = new ArrayList<>();
	
	/**
	 * Registers all special attacks and magic spells
	 */
	public static void registerAll() {
		Utils.getClassesInDirectory(CombatRegistryEvent.class.getPackage().getName() + ".special").stream().filter(SpecialAttackEvent.class::isInstance).forEach(clazz -> {
			SpecialAttackEvent special = (SpecialAttackEvent) clazz;
			for (String name : special.applicableNames()) {
				if (SPECIALS.containsKey(name)) {
					System.out.println("Attempted duplicate registration of special for '" + name + "'");
					continue;
				}
				SPECIALS.put(name, special);
			}
		});
		Utils.getClassesInDirectory(CombatRegistryEvent.class.getPackage().getName() + ".range").stream().filter(BowFireEvent.class::isInstance).forEach(clazz -> {
			BowFireEvent bow = (BowFireEvent) clazz;
			for (String name : bow.bowNames()) {
				if (BOWS.containsKey(name)) {
					System.out.println("Attempted duplicate registration of bow for '" + name + "'");
					continue;
				}
				BOWS.put(name, bow);
			}
		});
		PrayerEffectRepository.registerAll();
		Utils.getClassesInDirectory(CombatRegistryEvent.class.getPackage().getName() + ".spell").stream().filter(MagicSpellEvent.class::isInstance).forEach(clazz -> SPELL_EVENTS.add((MagicSpellEvent) clazz));
		System.out.println("Prepared " + SPECIALS.size() + " special attack weapons, " + BOWS.size() + " bow listeners, and " + SPELL_EVENTS.size() + " spells.");
	}
	
	/**
	 * Clears all data
	 */
	public static void clearAll() {
		SPECIALS.clear();
		BOWS.clear();
		SPELL_EVENTS.clear();
	}
	
	/**
	 * Gets an optional of a special attack event by the id of the weapon used. The id is then translated into a name
	 * and we loop through the map to find the right one.
	 *
	 * @param weaponId
	 * 		The id of the weapon used.
	 */
	public static Optional<SpecialAttackEvent> getSpecial(int weaponId) {
		String name = weaponId == -1 ? "unarmed" : ItemDefinitions.forId(weaponId).getName().toLowerCase();
		for (Entry<String, SpecialAttackEvent> entry : SPECIALS.entrySet()) {
			String specialName = entry.getKey();
			String regex = specialName.replaceAll("\\*", ".*");
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(name);
			
			if (matcher.find()) {
				return Optional.of(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets an optional of a bow fire event by the id of the weapon used. The id is then translated into a name and we
	 * loop through the map to find the right one.
	 *
	 * @param weaponId
	 * 		The id of the weapon used.
	 */
	public static Optional<BowFireEvent> getBow(int weaponId) {
		String name = weaponId == -1 ? "unarmed" : ItemDefinitions.forId(weaponId).getName().toLowerCase();
		for (Entry<String, BowFireEvent> entry : BOWS.entrySet()) {
			String specialName = entry.getKey();
			String regex = specialName.replaceAll("\\*", ".*");
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(name);
			
			if (matcher.find()) {
				return Optional.of(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets a spell event by its id and book.
	 *
	 * @param book
	 * 		The book of the spell
	 * @param spellId
	 * 		The id of the spell
	 */
	public static Optional<MagicSpellEvent<?>> getSpell(MagicBook book, int spellId) {
		return SPELL_EVENTS.stream().filter(spell -> spell.book() == book && spell.spellId() == spellId).findFirst();
	}
	
	/**
	 * Gets a combat spell
	 *
	 * @param book
	 * 		The book the spell should be ok
	 * @param spellId
	 * 		The id of the spell
	 */
	public static Optional<CombatSpellEvent> getCombatSpell(MagicBook book, int spellId) {
		for (MagicSpellEvent spellEvent : SPELL_EVENTS) {
			if (spellEvent.book() != book || spellEvent.spellId() != spellId) {
				continue;
			}
			if (spellEvent instanceof CombatSpellEvent) {
				return Optional.of((CombatSpellEvent) spellEvent);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Fires a spell if it's possible to fire it
	 *
	 * @param player
	 * 		The player
	 * @param book
	 * 		The book of the spell
	 * @param spellId
	 * 		The id of the spell
	 */
	@SuppressWarnings("unchecked")
	private static void fireSpellIfPossible(Player player, MagicBook book, int spellId) {
		Optional<MagicSpellEvent<?>> optional = SPELL_EVENTS.stream().filter(spell -> spell.book() == book && spell.spellId() == spellId).findFirst();
		if (!optional.isPresent()) {
			player.getPackets().sendGameMessage("Spell #" + spellId + " has not yet been added, please report this on the forums.");
			return;
		}
		MagicSpellEvent event = optional.get();
		// with default context
		event.cast(player, new CombatSpellContext(null, null));
	}
	
	/**
	 * Processes the clicking of a spell
	 *
	 * @param player
	 * 		The player
	 * @param spellId
	 * 		The id of the spell
	 */
	public static void processSpell(Player player, int spellId) {
		player.stop(false, false, true, false);
		switch (player.getCombatDefinitions().getSpellbook()) {
			case REGULAR:
				switch (spellId) {
					case 25:
					case 28:
					case 30:
					case 32:
					case 34:
					case 39:
					case 42:
					case 45:
					case 49:
					case 52:
					case 58:
					case 63:
					case 70:
					case 73:
					case 77:
					case 80:
					case 99:
					case 84:
					case 87:
					case 89:
					case 91:
					case 36:
					case 55:
					case 81:
					case 66:
					case 67:
					case 68:
					case 98:
						setAutocastSpell(player, spellId);
						break;
					default:
						fireSpellIfPossible(player, MagicBook.REGULAR, spellId);
						break;
					/*case 27: // crossbow bolt enchant
						if (player.getSkills().getLevel(Skills.MAGIC) < 4) {
							player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
							return;
						}
						player.stopAll();
						player.getInterfaceManager().sendInterface(432);
						break;
					case 24:
						//useHomeTele(player);
						break;
					case 37: // mobi
						//						sendNormalTeleportSpell(player, 10, 19, new WorldTile(2413, 2848, 0), LAW_RUNE, 1, WATER_RUNE, 1, AIR_RUNE, 1);
						break;
					case 40: // varrock
						//						sendNormalTeleportSpell(player, 25, 19, new WorldTile(3212, 3424, 0), FIRE_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
						break;
					case 43: // lumby
						//						sendNormalTeleportSpell(player, 31, 41, new WorldTile(3222, 3218, 0), EARTH_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
						break;
					case 46: // fally
						//						sendNormalTeleportSpell(player, 37, 48, new WorldTile(2964, 3379, 0), WATER_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
						break;
					case 51: // camelot
						//						sendNormalTeleportSpell(player, 45, 55.5, new WorldTile(2757, 3478, 0), AIR_RUNE, 5, LAW_RUNE, 1);
						break;
					case 57: // ardy
						//						sendNormalTeleportSpell(player, 51, 61, new WorldTile(2664, 3305, 0), WATER_RUNE, 2, LAW_RUNE, 2);
						break;
					case 62: // watch
						//						sendNormalTeleportSpell(player, 58, 68, new WorldTile(2547, 3113, 2), EARTH_RUNE, 2, LAW_RUNE, 2);
						break;
					case 69: // troll
						//						sendNormalTeleportSpell(player, 61, 68, new WorldTile(2888, 3674, 0), FIRE_RUNE, 2, LAW_RUNE, 2);
						break;
					case 72: // ape
						//						sendNormalTeleportSpell(player, 64, 76, new WorldTile(2776, 9103, 0), FIRE_RUNE, 2, WATER_RUNE, 2, LAW_RUNE, 2, 1963, 1);
						break;*/
				}
				break;
			case ANCIENTS:
				switch (spellId) {
					case 28:
					case 32:
					case 24:
					case 20:
					case 30:
					case 34:
					case 26:
					case 22:
					case 29:
					case 33:
					case 25:
					case 21:
					case 31:
					case 35:
					case 27:
					case 23:
					case 36:
					case 37:
					case 38:
					case 39:
						setAutocastSpell(player, spellId);
						break;
					default:
						fireSpellIfPossible(player, MagicBook.ANCIENTS, spellId);
						break;
				}
				break;
			case LUNARS:
				switch (spellId) {
					default:
						fireSpellIfPossible(player, MagicBook.LUNARS, spellId);
						break;
				}
				break;
		}
	}
	
	/**
	 * Sets a spell to be autocasted
	 *
	 * @param player
	 * 		The player
	 * @param spellId
	 * 		The spell id
	 */
	public static void setAutocastSpell(Player player, int spellId) {
		if (player.getCombatDefinitions().getAutocastId() == spellId) {
			player.getCombatDefinitions().resetSpells(true);
		} else {
			checkCombatSpell(player, spellId, 0, false);
		}
	}
	
	/**
	 * Checks that we can cast a spell and sets it if we can
	 *
	 * @param player
	 * 		The player
	 * @param spellId
	 * 		The id of the spell
	 * @param set
	 * 		The set value [0 = autocast, 1 = regular cast]
	 * @param delete
	 * 		If we should delete runes
	 */
	public static boolean checkCombatSpell(Player player, int spellId, int set, boolean delete) {
		switch (player.getCombatDefinitions().getSpellbook()) {
			case REGULAR:
				switch (spellId) {
					case 98:
						if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 2)) {
							return false;
						}
						break;
					case 25:
						if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 1, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 26:
						if (!checkSpellRequirements(player, 1, delete, WATER_RUNE, 3, EARTH_RUNE, 2, BODY_RUNE, 1)) {
							return false;
						}
						break;
					case 28:
						if (!checkSpellRequirements(player, 5, delete, WATER_RUNE, 1, AIR_RUNE, 1, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 30:
						if (!checkSpellRequirements(player, 9, delete, EARTH_RUNE, 2, AIR_RUNE, 1, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 31:
						if (!checkSpellRequirements(player, 1, delete, WATER_RUNE, 3, EARTH_RUNE, 2, BODY_RUNE, 1)) {
							return false;
						}
						break;
					case 32:
						if (!checkSpellRequirements(player, 13, delete, FIRE_RUNE, 3, AIR_RUNE, 2, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 34: // air bolt
						if (!checkSpellRequirements(player, 17, delete, AIR_RUNE, 2, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 35: // curse
						if (!checkSpellRequirements(player, 1, delete, WATER_RUNE, 2, EARTH_RUNE, 3, BODY_RUNE, 1)) {
							return false;
						}
						break;
					case 36:// bind
						if (!checkSpellRequirements(player, 20, delete, EARTH_RUNE, 3, WATER_RUNE, 3, NATURE_RUNE, 2)) {
							return false;
						}
						break;
					case 55: // snare
						if (!checkSpellRequirements(player, 50, delete, EARTH_RUNE, 4, WATER_RUNE, 4, NATURE_RUNE, 3)) {
							return false;
						}
						break;
					case 81:// entangle
						if (!checkSpellRequirements(player, 79, delete, EARTH_RUNE, 5, WATER_RUNE, 5, NATURE_RUNE, 4)) {
							return false;
						}
						break;
					case 39: // water bolt
						if (!checkSpellRequirements(player, 23, delete, WATER_RUNE, 2, AIR_RUNE, 2, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 42: // earth bolt
						if (!checkSpellRequirements(player, 29, delete, EARTH_RUNE, 3, AIR_RUNE, 2, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 45: // fire bolt
						if (!checkSpellRequirements(player, 35, delete, FIRE_RUNE, 4, AIR_RUNE, 3, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 49: // air blast
						if (!checkSpellRequirements(player, 41, delete, AIR_RUNE, 3, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 52: // water blast
						if (!checkSpellRequirements(player, 47, delete, WATER_RUNE, 3, AIR_RUNE, 3, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 58: // earth blast
						if (!checkSpellRequirements(player, 53, delete, EARTH_RUNE, 4, AIR_RUNE, 3, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 63: // fire blast
						if (!checkSpellRequirements(player, 59, delete, FIRE_RUNE, 5, AIR_RUNE, 4, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 70: // air wave
						if (!checkSpellRequirements(player, 62, delete, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 73: // water wave
						if (!checkSpellRequirements(player, 65, delete, WATER_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 77: // earth wave
						if (!checkSpellRequirements(player, 70, delete, EARTH_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 80: // fire wave
						if (!checkSpellRequirements(player, 75, delete, FIRE_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 84:
						if (!checkSpellRequirements(player, 81, delete, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 87:
						if (!checkSpellRequirements(player, 85, delete, WATER_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 89:
						if (!checkSpellRequirements(player, 85, delete, EARTH_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 66: // Sara Strike
						if (player.getEquipment().getWeaponId() != 2415) {
							player.getPackets().sendGameMessage("You need to be equipping a Saradomin staff to cast this spell.", true);
							return false;
						}
						if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 67: // Guthix Claws
						if (player.getEquipment().getWeaponId() != 2416) {
							player.getPackets().sendGameMessage("You need to be equipping a Guthix Staff or Void Mace to cast this spell.", true);
							return false;
						}
						if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 68: // Flame of Zammy
						if (player.getEquipment().getWeaponId() != 2417) {
							player.getPackets().sendGameMessage("You need to be equipping a Zamorak Staff to cast this spell.", true);
							return false;
						}
						if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 4, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 91:
						if (!checkSpellRequirements(player, 85, delete, FIRE_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 86: // teleblock
						if (!checkSpellRequirements(player, 85, delete, CHAOS_RUNE, 1, LAW_RUNE, 1, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 99: // Storm of Armadyl
						if (!checkSpellRequirements(player, 77, delete, ARMADYL_RUNE, 1)) {
							return false;
						}
						break;
					case 75:
						if (!checkSpellRequirements(player, 1, delete, EARTH_RUNE, 5, WATER_RUNE, 5, SOUL_RUNE, 1)) {
							return false;
						}
						break;
					case 78:
						if (!checkSpellRequirements(player, 1, delete, EARTH_RUNE, 8, WATER_RUNE, 8, SOUL_RUNE, 1)) {
							return false;
						}
						break;
					case 82:
						if (!checkSpellRequirements(player, 1, delete, EARTH_RUNE, 12, WATER_RUNE, 12, SOUL_RUNE, 1)) {
							return false;
						}
						break;
				}
				break;
			case ANCIENTS:
				switch (spellId) {
					case 28:
						if (!checkSpellRequirements(player, 50, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE, 1)) {
							return false;
						}
						break;
					case 32:
						if (!checkSpellRequirements(player, 52, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 1)) {
							return false;
						}
						break;
					case 24:
						if (!checkSpellRequirements(player, 56, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 20:
						if (!checkSpellRequirements(player, 58, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, WATER_RUNE, 2)) {
							return false;
						}
						break;
					case 30:
						if (!checkSpellRequirements(player, 62, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2)) {
							return false;
						}
						break;
					case 34:
						if (!checkSpellRequirements(player, 64, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 2)) {
							return false;
						}
						break;
					case 26:
						if (!checkSpellRequirements(player, 68, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 22:
						if (!checkSpellRequirements(player, 70, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, WATER_RUNE, 4)) {
							return false;
						}
						break;
					case 29:
						if (!checkSpellRequirements(player, 74, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2)) {
							return false;
						}
						break;
					case 33:
						if (!checkSpellRequirements(player, 76, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, AIR_RUNE, 2, SOUL_RUNE, 2)) {
							return false;
						}
						break;
					case 25:
						if (!checkSpellRequirements(player, 80, delete, DEATH_RUNE, 2, BLOOD_RUNE, 4)) {
							return false;
						}
						break;
					case 21:
						if (!checkSpellRequirements(player, 82, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, WATER_RUNE, 3)) {
							return false;
						}
						break;
					case 31:
						if (!checkSpellRequirements(player, 86, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, FIRE_RUNE, 4, AIR_RUNE, 4)) {
							return false;
						}
						break;
					case 35:
						if (!checkSpellRequirements(player, 88, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, AIR_RUNE, 4, SOUL_RUNE, 3)) {
							return false;
						}
						break;
					case 27:
						if (!checkSpellRequirements(player, 92, delete, DEATH_RUNE, 4, BLOOD_RUNE, 4, SOUL_RUNE, 1)) {
							return false;
						}
						break;
					case 23:
						if (!checkSpellRequirements(player, 94, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, WATER_RUNE, 6)) {
							return false;
						}
						break;
					case 36: // Miasmic rush.
						if (!checkSpellRequirements(player, 61, delete, CHAOS_RUNE, 2, EARTH_RUNE, 1, SOUL_RUNE, 1)) {
							return false;
						}
						int weaponId = player.getEquipment().getWeaponId();
						if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
							player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
							return false;
						}
						break;
					case 38: // Miasmic burst.
						if (!checkSpellRequirements(player, 73, delete, CHAOS_RUNE, 4, EARTH_RUNE, 2, SOUL_RUNE, 2)) {
							return false;
						}
						weaponId = player.getEquipment().getWeaponId();
						if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
							player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
							return false;
						}
						break;
					case 37: // Miasmic blitz.
						if (!checkSpellRequirements(player, 85, delete, BLOOD_RUNE, 2, EARTH_RUNE, 3, SOUL_RUNE, 3)) {
							return false;
						}
						weaponId = player.getEquipment().getWeaponId();
						if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
							player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
							return false;
						}
						break;
					case 39: // Miasmic barrage.
						if (!checkSpellRequirements(player, 97, delete, BLOOD_RUNE, 4, EARTH_RUNE, 4, SOUL_RUNE, 4)) {
							return false;
						}
						weaponId = player.getEquipment().getWeaponId();
						if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
							player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
							return false;
						}
						break;
				}
				break;
			default:
				System.out.println("Spell # " + spellId + " did not have rune requirements in place.");
				return false;
		}
		if (set >= 0) {
			if (set == 0) {
				player.getCombatDefinitions().setAutocastId(spellId);
			} else {
				player.putAttribute("spell_cast_id", spellId);
			}
		}
		return true;
	}
	
	/**
	 * Checks to make sure the player has the requirements to cast a spell
	 *
	 * @param player
	 * 		The player
	 * @param level
	 * 		The level of the spell
	 * @param delete
	 * 		If we should delete runes
	 * @param runes
	 * 		The runes
	 */
	public static boolean checkSpellRequirements(Player player, int level, boolean delete, int... runes) {
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		return checkRunes(player, delete, runes);
	}
	
	/**
	 * Checks that we have enough runes to cast the spell
	 *
	 * @param player
	 * 		The player
	 * @param delete
	 * 		If we should delete the runes
	 * @param runes
	 * 		The runes to delete
	 */
	public static boolean checkRunes(Player player, boolean delete, int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getItemSlot(Equipment.SLOT_SHIELD);
		int runesCount = 0;
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int ammount = runes[runesCount++];
			if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
				continue;
			}
			if (hasStaffOfLight(weaponId) && Utils.getRandom(8) == 0 && runeId != 21773) {
				continue;
			}
			if (!player.getInventory().containsItem(runeId, ammount)) {
				player.getPackets().sendGameMessage("You do not have enough " + ItemDefinitions.forId(runeId).getName().replace("rune", "Rune") + "s to cast this spell.");
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int amount = runes[runesCount++];
				if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
					continue;
				}
				player.getInventory().deleteItem(runeId, amount);
			}
		}
		return true;
	}
	
	/**
	 * Checks if we have infinite runes, based on the staffs
	 *
	 * @param runeId
	 * 		The rune to check for
	 * @param weaponId
	 * 		The weapon
	 * @param shieldId
	 * 		The shield
	 */
	private static boolean hasInfiniteRunes(int runeId, int weaponId, int shieldId) {
		if (runeId == AIR_RUNE) {
			// air staff
			if (weaponId == 1381 || weaponId == 21777) {
				return true;
			}
		} else if (runeId == WATER_RUNE) {
			// water staff
			if (weaponId == 1383 || shieldId == 18346) {
				return true;
			}
		} else if (runeId == EARTH_RUNE) {
			// earth staff
			if (weaponId == 1385) {
				return true;
			}
		} else if (runeId == FIRE_RUNE) {
			// fire staff
			if (weaponId == 1387) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If we have a staff of light
	 *
	 * @param weaponId
	 * 		The id of the weapon equipped
	 */
	private static boolean hasStaffOfLight(int weaponId) {
		return weaponId == 15486 || weaponId == 22207 || weaponId == 22209 || weaponId == 22211 || weaponId == 22213;
	}
	
}
