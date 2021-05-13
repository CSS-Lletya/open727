package org.redrune.game.content.combat;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.CombatType;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;

import com.google.common.base.Preconditions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Entity;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.actions.PlayerCombatAction;

import skills.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class StaticCombatFormulae {
	
	/**
	 * Gets the type of combat we're engaging in
	 *
	 * @param player
	 * 		The player
	 */
	public static CombatType getCombatType(Player player) {
		// magic gets first priority
		int spellId = player.getAttribute("spell_cast_id", player.getCombatDefinitions().getAutoCastSpell());
		if (spellId != -1) {
			return CombatType.MAGIC;
		}
		// then we check if we have range worn
		int rangeResponse = getRangeResponse(player);
		switch (rangeResponse) {
			case 0: // nothing found that symbolizes range
				return CombatType.MELEE;
			case 1: // invalid ammo
			case 2: // good range
			case 3: // no ammo
				return CombatType.RANGE;
		}
		return CombatType.MELEE;
	}
	
	/**
	 * Gets the range response from the player. The options are as follows: <br> <ul> <li>0 - We are doing melee</li>
	 * <li>1 - The ammo being used is incorrect</li> <li>2 - Range should proceed</li> <li>3 - We do not have ammo to
	 * use.</li> </ul>
	 *
	 * @param player
	 * 		The player to check.
	 */
	public static int getRangeResponse(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1) {
			return 0;
		}
		String name = ItemDefinitions.forId(weaponId).getName().toLowerCase();
		// those dont need arrows
		if (name.contains("knife") || name.contains("dart") || name.contains("javelin") || name.contains("thrownaxe") || name.contains("throwing axe") || name.contains("crystal bow") || name.equalsIgnoreCase("zaryte bow") || name.contains("chinchompa") || name.contains("bolas") || name.contains("sling") || name.contains("toktz-xil-ul")) {
			return 2;
		}
		int ammoId = Equipment.getItemSlot(Equipment.SLOT_ARROWS);
		switch (weaponId) {
			case 15241: // Hand cannon
				switch (ammoId) {
					case -1:
						return 3;
					case 15243: // bronze arrow
						return 2;
					default:
						return 1;
				}
			case 839: // longbow
			case 841: // shortbow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
						return 2;
					default:
						return 1;
				}
			case 843: // oak longbow
			case 845: // oak shortbow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
						return 2;
					default:
						return 1;
				}
			case 847: // willow longbow
			case 849: // willow shortbow
			case 13541: // Willow composite bow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
						return 2;
					default:
						return 1;
				}
			case 851: // maple longbow
			case 853: // maple shortbow
			case 18331: // Maple longbow (sighted)
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
						return 2;
					default:
						return 1;
				}
			case 2883:// ogre bow
				switch (ammoId) {
					case -1:
						return 3;
					case 2866: // ogre arrow
						return 2;
					default:
						return 1;
				}
			case 4827:// Comp ogre bow
				switch (ammoId) {
					case -1:
						return 3;
					case 2866: // ogre arrow
					case 4773: // bronze brutal
					case 4778: // iron brutal
					case 4783: // steel brutal
					case 4788: // black brutal
					case 4793: // mithril brutal
					case 4798: // adamant brutal
					case 4803: // rune brutal
						return 2;
					default:
						return 1;
				}
			case 855: // yew longbow
			case 857: // yew shortbow
			case 10281: // Yew composite bow
			case 14121: // Sacred clay bow
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
			case 6724: // seercull
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
					case 892: // rune arrow
						return 2;
					default:
						return 1;
				}
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
					case 892: // rune arrow
					case 11212: // dragon arrow
						return 2;
					default:
						return 1;
				}
			case 19143: // saradomin bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19152: // saradomin arrow
						return 2;
					default:
						return 1;
				}
			case 19146: // guthix bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19157: // guthix arrow
						return 2;
					default:
						return 1;
				}
			case 19149: // zamorak bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19162: // zamorak arrow
						return 2;
					default:
						return 1;
				}
			case 24338: // Royal crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 24336: // Coral bolts
						return 2;
					default:
						return 1;
				}
			case 24303: // Coral crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 24304: // Coral bolts
						return 2;
					default:
						return 1;
				}
			case 4734: // karil crossbow
			case 4934:
			case 4935:
			case 4936:
			case 4937:
				switch (ammoId) {
					case -1:
						return 3;
					case 4740: // bolt rack
						return 2;
					default:
						return 1;
				}
			case 10156: // hunters crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 10158: // Kebbit bolts
					case 10159: // Long kebbit bolts
						return 2;
					default:
						return 1;
				}
			case 8880: // Dorgeshuun c'bow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 8882: // bone bolts
						return 2;
					default:
						return 1;
				}
			case 14684: // zanik crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9144: // rune bolts
					case 9145: // silver bolts wtf
						return 2;
					default:
						return 1;
				}
			case 767: // phoenix crossbow
			case 837: // crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
						return 2;
					default:
						return 1;
				}
			case 9174: // bronze crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9236: // Opal bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9176: // blurite crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9139: // Blurite bolts
					case 9237: // Jade bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9177: // iron crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9179: // steel crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
						return 2;
					default:
						return 1;
				}
			case 13081: // black crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9181: // Mith crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9145: // silver bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9183: // adam c bow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9145: // silver bolts wtf
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
					case 9242: // Ruby bolts (e)
					case 9243: // Diamond bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9185: // rune c bow
			case 18357: // chaotic crossbow
			case 18358:
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9144: // rune bolts
					case 9145: // silver bolts wtf
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
					case 9242: // Ruby bolts (e)
					case 9243: // Diamond bolts (e)
					case 9244: // Dragon bolts (e)
					case 9245: // Onyx bolts (e)
					case 24116: // Bakriminel bolts
						return 2;
					default:
						return 1;
				}
			default:
				return 0;
		}
	}
	
	/**
	 * Gets the combat style, based on the weapon id and the attack style selected of the weapon.
	 *
	 * @param weaponId
	 * 		The id of the weapon equipped
	 * @param attackStyle
	 * 		The style used.
	 */
	public static int getMeleeBonusStyle(int weaponId, int attackStyle) {
		if (weaponId == -1) {
			return BonusConstants.CRUSH_ATTACK;
		} else {
			if (weaponId == -2) {
				return BonusConstants.CRUSH_ATTACK;
			}
			String weaponName = ItemDefinitions.forId(weaponId).getName().toLowerCase();
			if (weaponName.contains("whip")) {
				return BonusConstants.SLASH_ATTACK;
			}
			if (weaponName.contains("staff of light")) {
				switch (attackStyle) {
					case 0:
						return BonusConstants.STAB_ATTACK;
					case 1:
						return BonusConstants.SLASH_ATTACK;
					default:
						return BonusConstants.CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("staff") || weaponName.contains("granite mace") || weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em") || weaponName.contains("tzhaar-ket-om") || weaponName.contains("maul")) {
				return BonusConstants.CRUSH_ATTACK;
			}
			if (weaponName.contains("godsword") || weaponName.contains("greataxe") || weaponName.contains("2h sword") || weaponName.equals("saradomin sword")) {
				switch (attackStyle) {
					case 2:
						return BonusConstants.CRUSH_ATTACK;
					default:
						return BonusConstants.SLASH_ATTACK;
				}
			}
			if (weaponName.contains("scimitar") || weaponName.contains("hatchet") || weaponName.contains("claws") || weaponName.contains(" sword") || weaponName.contains("longsword")) {
				System.out.println("using style " + attackStyle + " with weapon " + weaponName);
				switch (attackStyle) {
					case 2:
						return BonusConstants.STAB_ATTACK;
					default:
						return BonusConstants.SLASH_ATTACK;
				}
			}
			if (weaponName.contains("mace") || weaponName.contains("anchor")) {
				switch (attackStyle) {
					case 2:
						return BonusConstants.STAB_ATTACK;
					default:
						return BonusConstants.CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
					case 1:
						return BonusConstants.SLASH_ATTACK;
					default:
						return BonusConstants.STAB_ATTACK;
				}
			}
			if (weaponName.contains("spear")) {
				switch (attackStyle) {
					case 1:
						return BonusConstants.SLASH_ATTACK;
					case 2:
						return BonusConstants.CRUSH_ATTACK;
					default:
						return BonusConstants.STAB_ATTACK;
				}
			}
			if (weaponName.contains("pickaxe")) {
				switch (attackStyle) {
					case 2:
						return BonusConstants.CRUSH_ATTACK;
					default:
						return BonusConstants.STAB_ATTACK;
				}
			}
			
			if (weaponName.contains("dagger") || weaponName.contains("rapier")) {
				switch (attackStyle) {
					case 2:
						return BonusConstants.SLASH_ATTACK;
					default:
						return BonusConstants.STAB_ATTACK;
				}
			}
			
		}
		switch (weaponId) {
			default:
				return BonusConstants.CRUSH_ATTACK;
		}
	}
	
	/**
	 * Gets the defence bonus based on the attack bonus
	 *
	 * @param style
	 * 		The attack style
	 */
	public static int getMeleeDefenceBonusIndex(int style) {
		switch (style) {
			case BonusConstants.STAB_ATTACK:
				return BonusConstants.STAB_DEFENCE;
			case BonusConstants.SLASH_ATTACK:
				return BonusConstants.SLASH_DEFENCE;
			case BonusConstants.CRUSH_ATTACK:
				return BonusConstants.CRUSH_DEFENCE;
			default:
				return BonusConstants.STAB_DEFENCE;
		}
	}
	
	/**
	 * Checks if we have full void equipped, with the possible helmet ids
	 *
	 * @param player
	 * 		The player to check on
	 * @param helmetIds
	 * 		The helmet id
	 */
	public static boolean fullVoidEquipped(Player player, int... helmetIds) {
		boolean hasDeflector = player.getEquipment().getItemSlot(Equipment.SLOT_SHIELD) == 19712;
		if (player.getEquipment().getItemSlot(Equipment.SLOT_HANDS) != 8842) {
			if (hasDeflector) {
				hasDeflector = false;
			} else {
				return false;
			}
		}
		int legsId = player.getEquipment().getItemSlot(Equipment.SLOT_LEGS);
		boolean hasLegs = legsId != -1 && (legsId == 8840 || legsId == 19786 || legsId == 19788 || legsId == 19790);
		if (!hasLegs) {
			if (hasDeflector) {
				hasDeflector = false;
			} else {
				return false;
			}
		}
		int torsoId = player.getEquipment().getItemSlot(Equipment.SLOT_CHEST);
		boolean hasTorso = torsoId != -1 && (torsoId == 8839 || torsoId == 10611 || torsoId == 19785 || torsoId == 19787 || torsoId == 19789);
		if (!hasTorso) {
			if (hasDeflector) {
				hasDeflector = false;
			} else {
				return false;
			}
		}
		if (hasDeflector) {
			return true;
		}
		int helmId = player.getEquipment().getItemSlot(Equipment.SLOT_HAT);
		if (helmId == -1) {
			return false;
		}
		boolean hasHelm = false;
		for (int id : helmetIds) {
			if (helmId == id) {
				hasHelm = true;
				break;
			}
		}
		return hasHelm;
	}
	
	/**
	 * Checks if we have an armour set equipped. This uses lowercase naming. <br> Example usage:
	 * <br>armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_chest, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok",
	 * "dharok", "dharok");// armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_AMMY, SLOT_LEGS, SLOT_WEAPON },
	 * "dharok", "dharok", "dharok", "dharok");
	 *
	 * @param player
	 * 		The player
	 * @param slots
	 * 		The slots
	 * @param nameFlags
	 * 		The name flags, with the indexes corresponding to the slots indexes.
	 */
	public static boolean armourSetEquipped(Player player, int[] slots, String... nameFlags) {
		Preconditions.checkArgument(slots.length == nameFlags.length, "Name flags and slot length must be equal!");
		for (int i = 0; i < slots.length; i++) {
			int slot = slots[i];
			player.getEquipment();
			int itemInSlot = Equipment.getItemSlot(slot);
			// theres no item in the slot, so its not possible to match the name
			if (itemInSlot == -1) {
				return false;
			}
			String itemInSlotName = ItemDefinitions.forId(itemInSlot).getName().toLowerCase();
			
			String nameFlag = nameFlags[i].toLowerCase();
			// the item in that slot's name didn't have the expected flag
			if (!itemInSlotName.contains(nameFlag)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the experience style based on the weapon and the attack style selected. -1 means shared
	 *
	 * @param weaponId
	 * 		The id of the weapon
	 * @param attackStyle
	 * 		The players attack style
	 */
	public static int getXpStyle(int weaponId, int attackStyle) {
		if (weaponId != -1 && weaponId != -2) {
			String weaponName = ItemDefinitions.forId(weaponId).getName().toLowerCase();
			if (weaponName.contains("whip")) {
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return -1;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
					case 0:
						return -1;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			}
			if (weaponName.contains("staff")) {
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			}
			if (weaponName.contains("godsword") || weaponName.contains("sword") || weaponName.contains("2h")) {
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
						return SkillConstants.STRENGTH;
					case 3:
					default:
						return SkillConstants.DEFENCE;
				}
			}
		}
		switch (weaponId) {
			case -1:
			case -2:
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			default:
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
						return -1;
					case 3:
					default:
						return SkillConstants.DEFENCE;
				}
		}
	}
	
	/**
	 * Gets the attack emote for a weapon
	 *
	 * @param weaponId
	 * 		The id of the weapon
	 * @param attackStyle
	 * 		The attack style being used
	 */
	public static int getWeaponAttackEmote(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			if (weaponId == -2) {
				// punch/block:14393 kick:14307 spec:14417
				switch (attackStyle) {
					case 1:
						return 14307;
					default:
						return 14393;
				}
			}
			String weaponName = ItemDefinitions.forId(weaponId).getName().toLowerCase();
			if (!weaponName.equals("null")) {
				if (weaponName.contains("crossbow")) {
					return weaponName.contains("karil's crossbow") ? 2075 : 4230;
				}
				if (weaponName.contains("bow")) {
					return 426;
				}
				if (weaponName.contains("chinchompa")) {
					return 2779;
				}
				if (weaponName.contains("staff of light")) {
					switch (attackStyle) {
						case 0:
							return 15072;
						case 1:
							return 15071;
						case 2:
							return 414;
					}
				}
				if (weaponName.contains("staff") || weaponName.contains("wand")) {
					return 419;
				}
				if (weaponId == 6522) {
					return 2614;
				}
				if (weaponName.contains("dart")) {
					return 6600;
				}
				if (weaponName.contains("knife")) {
					return 9055;
				}
				if (weaponName.contains("scimitar") || weaponName.contains("korasi's sword")) {
					switch (attackStyle) {
						case 2:
							return 15072;
						default:
							return 15071;
					}
				}
				if (weaponName.contains("granite mace")) {
					return 400;
				}
				if (weaponName.contains("mace")) {
					switch (attackStyle) {
						case 2:
							return 400;
						default:
							return 401;
					}
				}
				if (weaponName.contains("hatchet") || weaponName.contains("battleaxe")) {
					switch (attackStyle) {
						case 2:
							return 401;
						default:
							return 395;
					}
				}
				if (weaponName.contains("warhammer")) {
					switch (attackStyle) {
						default:
							return 401;
					}
				}
				if (weaponName.contains("claws")) {
					switch (attackStyle) {
						case 2:
							return 1067;
						default:
							return 393;
					}
				}
				if (weaponName.contains("whip")) {
					switch (attackStyle) {
						case 1:
							return 11969;
						case 2:
							return 11970;
						default:
							return 11968;
					}
				}
				if (weaponName.contains("anchor")) {
					switch (attackStyle) {
						default:
							return 5865;
					}
				}
				if (weaponName.contains("tzhaar-ket-em")) {
					switch (attackStyle) {
						default:
							return 401;
					}
				}
				if (weaponId == 20084 || weaponName.contains("tzhaar-ket-om")) {
					switch (attackStyle) {
						default:
							return 13691;
					}
				}
				if (weaponName.contains("halberd")) {
					switch (attackStyle) {
						case 1:
							return 440;
						default:
							return 428;
					}
				}
				if (weaponName.contains("zamorakian spear")) {
					switch (attackStyle) {
						case 1:
							return 12005;
						case 2:
							return 12009;
						default:
							return 12006;
					}
				}
				if (weaponName.equals("training sword")) {
					switch (attackStyle) {
						case 2:
						case 3:
							return 12311;
						default:
							return 12310;
					}
				}
				if (weaponName.contains("spear")) {
					switch (attackStyle) {
						case 1:
							return 440;
						case 2:
							return 429;
						default:
							return 428;
					}
				}
				if (weaponName.contains("flail")) {
					return 2062;
				}
				if (weaponName.contains("javelin")) {
					return 10501;
				}
				if (weaponName.contains("morrigan's throwing axe")) {
					return 10504;
				}
				if (weaponName.contains("pickaxe")) {
					switch (attackStyle) {
						case 2:
							return 400;
						default:
							return 401;
					}
				}
				if (weaponName.contains("dagger")) {
					switch (attackStyle) {
						case 2:
							return 377;
						default:
							return 376;
					}
				}
				if (weaponName.contains("2h sword") || weaponName.equals("dominion sword") || weaponName.equals("thok's sword") || weaponName.equals("saradomin sword")) {
					switch (attackStyle) {
						case 2:
							return 7048;
						case 3:
							return 7049;
						default:
							return 7041;
					}
				}
				if (weaponName.contains(" sword") || weaponName.contains("saber") || weaponName.contains("longsword") || weaponName.contains("light") || weaponName.contains("excalibur")) {
					switch (attackStyle) {
						case 2:
							return 12310;
						default:
							return 12311;
					}
				}
				if (weaponName.contains("rapier") || weaponName.contains("brackish")) {
					switch (attackStyle) {
						case 2:
							return 13048;
						default:
							return 13049;
					}
				}
				if (weaponName.contains("katana")) {
					switch (attackStyle) {
						case 2:
							return 1882;
						default:
							return 1884;
					}
				}
				if (weaponName.contains("godsword")) {
					switch (attackStyle) {
						case 2:
							return 11980;
						case 3:
							return 11981;
						default:
							return 11979;
					}
				}
				if (weaponName.contains("greataxe")) {
					switch (attackStyle) {
						case 2:
							return 12003;
						default:
							return 12002;
					}
				}
				if (weaponName.contains("granite maul")) {
					switch (attackStyle) {
						default:
							return 1665;
					}
				}
				
			}
		}
		switch (weaponId) {
			case 16405:// novite maul
			case 16407:// Bathus maul
			case 16409:// Maramaros maul
			case 16411:// Kratonite maul
			case 16413:// Fractite maul
			case 16415:// Zephyrium maul
			case 16417:// Argonite maul
			case 16419:// Katagon maul
			case 16421:// Gorgonite maul
			case 16423:// Promethium maul
			case 16425:// primal maul
				return 2661; // maul
			case 18353:// chaotic maul
				return 13055;
			case 13883: // morrigan thrown axe
				return 10504;
			case 15241:
				return 12174;
			default:
				switch (attackStyle) {
					case 1:
						return 423;
					default:
						return 422;
				}
		}
	}
	
	/**
	 * Gets the defence emote of an entity
	 *
	 * @param entity
	 * 		The entity
	 */
	public static int getDefenceEmote(Entity entity) {
		if (entity.isPlayer()) {
			Player player = entity.toPlayer();
			int shieldId = player.getEquipment().getItemSlot(Equipment.SLOT_SHIELD);
			String shieldName = shieldId == -1 ? null : ItemDefinitions.forId(shieldId).getName().toLowerCase();
			if (shieldId == -1 || (shieldName.contains("book") && shieldId != 18346)) {
				int weaponId = player.getEquipment().getItemSlot(Equipment.SLOT_WEAPON);
				if (weaponId == -1) {
					return 424;
				}
				String weaponName = ItemDefinitions.forId(weaponId).getName().toLowerCase();
				if (!weaponName.equals("null")) {
					if (weaponName.contains("scimitar") || weaponName.contains("korasi sword")) {
						return 15074;
					}
					if (weaponName.contains("whip")) {
						return 11974;
					}
					if (weaponName.contains("staff of light")) {
						return 12806;
					}
					if (weaponName.contains("longsword") || weaponName.contains("darklight") || weaponName.contains("silverlight") || weaponName.contains("excalibur")) {
						return 388;
					}
					if (weaponName.contains("dagger")) {
						return 378;
					}
					if (weaponName.contains("rapier")) {
						return 13038;
					}
					if (weaponName.contains("pickaxe")) {
						return 397;
					}
					if (weaponName.contains("mace")) {
						return 403;
					}
					if (weaponName.contains("claws")) {
						return 4177;
					}
					if (weaponName.contains("hatchet") || weaponName.contains("battleaxe")) {
						return 397;
					}
					if (weaponName.contains("greataxe")) {
						return 12004;
					}
					if (weaponName.contains("wand")) {
						return 415;
					}
					if (weaponName.contains("chaotic staff")) {
						return 13046;
					}
					if (weaponName.contains("staff")) {
						return 420;
					}
					if (weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em")) {
						return 403;
					}
					if (weaponName.contains("maul") || weaponName.contains("tzhaar-ket-om")) {
						return 1666;
					}
					if (weaponName.contains("zamorakian spear")) {
						return 12008;
					}
					if (weaponName.contains("spear") || weaponName.contains("halberd") || weaponName.contains("hasta")) {
						return 430;
					}
					if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.equals("saradomin sword")) {
						return 7050;
					}
				}
				return 424;
			}
			if (shieldName.contains("shield") || shieldName.contains("toktz-ket-xil")) {
				return 1156;
			}
			if (shieldName.contains("defender")) {
				return 4177;
			}
			switch (shieldId) {
				default:
					return 424;
			}
		} else if (entity.isNPC()) {
			return entity.toNPC().getCombatDefinitions().getDefenceEmote();
		} else {
			System.out.println("Unable to identify entity type: " + entity);
			return -1;
		}
	}
	
	/**
	 * Gets the attack distance the player must be at with their weapon to attack
	 *
	 * @param player
	 * 		The player
	 * @param type
	 * 		The type of combat the player is using
	 */
	public static int getMinimumDistance(Player player, CombatType type) {
		final int weaponId = player.getEquipment().getItemSlot(Equipment.SLOT_WEAPON);
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		final String name = weaponId == -1 ? "null" : ItemDefinitions.forId(weaponId).getName().toLowerCase();
		
		switch (type) {
			// melee must be right next to the player, unless its a halberd
			case MELEE:
				if (name.contains("halberd")) {
					return 1;
				}
				return 0;
			default:
				if (name.contains("dart")) {
					return attackStyle != 2 ? 3 : 5;
				}
				if (name.contains("knife") || name.contains("throwaxe") || name.contains("sling")) {
					return attackStyle != 2 ? 4 : 6;
				}
				if (name.contains("javelin")) {
					return attackStyle != 2 ? 5 : 7;
				}
				if (name.contains("dorgeshuun")) {
					return attackStyle != 2 ? 6 : 8;
				}
				if (name.contains("longbow") || name.contains("dark") || name.contains("chinchompa")) {
					return attackStyle != 2 ? 9 : 10;
				}
				if (name.contains("zaryte") || name.contains("crystal")) {
					return 10;
				}
				return attackStyle != 2 ? 7 : 9;
		}
	}
	
	/**
	 * In the case that a target is above  water, melee will never reach; we must check the clip as if its a range/magic
	 * projectile.
	 *
	 * @param target
	 * 		The target
	 */
	public static boolean checkAttackPathAsRange(Entity target) {
		return false;
	}
	
	/**
	 * Fires combat listeners (post-swing events)
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 */
	public static void fireCombatListeners(Player player, Entity target) {
		if (target.isPlayer()) {
			target.toPlayer().getManager().getInterfaces().closeAll();
		}
		SystemManager.getScheduler().schedule(new ScheduledTask() {
			@Override
			public void run() {
				player.getManager().getInterfaces().closeAll();
			}
		});
		target.addAttackedByDelay(player);
	}
	
	/**
	 * Checks if the entity has anti dragon protection
	 *
	 * @param entity
	 * 		The entity
	 */
	public static boolean hasAntiDragProtection(Entity entity) {
		if (entity.isNPC()) {
			String name = entity.toNPC().getDefinitions().name.toLowerCase();
			return name.contains("fire") || name.contains("dragon");
		} else {
			Player p2 = (Player) entity;
			int shieldId = p2.getEquipment().getItemSlot(Equipment.SLOT_SHIELD);
			return shieldId == 1540 || shieldId == 11283 || shieldId == 11284;
		}
	}
	
	/**
	 * Forces the target to get into combat with the source, 1 tick after the calling of the method. This requires the
	 * target not to be moving and not to be fighting already
	 *
	 * @param source
	 * 		The starter of combat
	 * @param target
	 * 		The target of combat
	 */
	public static void autoRetaliate(Entity source, Entity target) {
		// as long as the target isn't moving or fighting already, they'll retaliate to us
		if ((target.isNPC() || (target.isPlayer() && target.toPlayer().getCombatDefinitions().isRetaliating()) && !target.fighting() && !target.getMovement().isMoving())) {
		
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				if (target.isPlayer()) {
					target.toPlayer().getManager().getActions().startAction(new PlayerCombatAction(source));
				} else {
					target.toNPC().getCombatManager().getCombat().setTarget(source);
				}
			}
		}, 1, TimeUnit.SECONDS);
		}
	}
	
	/**
	 * Checks if the target is in a good distance to fight, based on the combat type.
	 *
	 * @param player
	 * 		The player fighting
	 * @param target
	 * 		The target
	 * @param type
	 * 		The combat type
	 */
	public static boolean isWithinDistance(Player player, Entity target, CombatType type) {
		// the distance change
		int distance = player.getMovement().isRunning() /*&& target.getMovement().isRunning()*/ ? 2 : 1;
		int weaponId = player.getEquipment().getWeaponId();
		String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.forId(weaponId).getName().toLowerCase();
		boolean halberd = weaponName.contains("halberd");
		if (type == CombatType.MELEE && halberd) {
			distance += 1;
		}
		// if we should check closeby tiles [close 1v1 melee only]
		final boolean checkClose = type == CombatType.MELEE && !checkAttackPathAsRange(target);
		// the distance modifier
		final int modifier = player.getMovement().hasWalkSteps() /*&& target.getMovement().hasWalkSteps()*/ ? distance : 0;
		// if we can't clip to the target
		// or the target is too far away
		// or we're colliding with the target
		if (!player.getMovement().clippedProjectileToNode(target, checkClose) || !Misc.isOnRange(player, target, getMinimumDistance(player, type) + modifier) || Misc.colides(player, target)) {
			return false;
		}
		// otherwise we can fight
		return true;
	}
	
	/**
	 * Performing some checks to toggle the special attack bar. It must be done after players stop switching if they are
	 * to make combat smooth.
	 *
	 * @param player
	 * 		The player
	 * @param attempt
	 * 		The attempt number
	 */
	public static void checkSpecialToggle(Player player, final int attempt) {
		Queue<Integer> equipQueue = player.getAttribute("equip_queue", new ConcurrentLinkedQueue<>());
		if (!equipQueue.isEmpty() && attempt <= 3) {
			EngineWorkingSet.getScheduledExecutorService().schedule(() -> checkSpecialToggle(player, attempt + 1), 100, TimeUnit.MILLISECONDS);
			return;
		}
		if (player.removeAttribute("special_attack_toggled", false)) {
			player.getCombatDefinitions().setSpecialActivated(!player.getCombatDefinitions().isSpecialActivated());
		}
		// check instant specs
		if (player.getCombatDefinitions().isSpecialActivated()) {
			Optional<SpecialAttackEvent> optional = CombatRegistry.getSpecial(player.getEquipment().getWeaponId());
			// no optional found
			if (!optional.isPresent()) {
				return;
			}
			SpecialAttackEvent event = optional.get();
			// the event isn't instant
			if (!event.isInstant()) {
				return;
			}
			final boolean energyRequired = player.getCombatDefinitions().getSpecialAttackPercentage() < ItemConstants.getSpecialEnergy(player.getEquipment().getWeaponId());
			// not enough energy
			if (energyRequired) {
				player.getPackets().sendGameMessage("You don't have enough special attack energy.");
				player.getCombatDefinitions().setUsuingSpecilAttack(false);
				return;
			}
			// the combat action
			PlayerCombatAction action = player.getManager().getActions().getAction() instanceof PlayerCombatAction ? (PlayerCombatAction) player.getManager().getActions().getAction() : null;
			Entity target = player.getAttribute("combat_target", action == null ? null : action.getTarget());
			// no target and it was necessary
			if (target == null && event.requiresFight()) {
				player.getCombatDefinitions().setUsuingSpecilAttack(false);
				return;
			}
			// we can't allow a swing on dead target
			if (target != null && target.isDead()) {
				player.getCombatDefinitions().setUsuingSpecilAttack(false);
				return;
			}
			// granite maul is instant and requires combat, others like SOL/DBA don't...
			if (event.requiresFight()) {
				if (!canFight(player, target)) {
					return;
				}
				// just in case [nearly certain all instant specs are melee...]
				CombatType type = StaticCombatFormulae.getCombatType(player);
				if (type == null) {
					return;
				}
				// we are far away
				if (!isWithinDistance(player, target, type)) {
					return;
				}
				event.fire(player, target, type.getSwing(), player.getCombatDefinitions().getAttackStyle());
				// face the target so it looks real
				player.faceEntity(target);
			} else {
				event.fire(player, null, null, player.getCombatDefinitions().getAttackStyle());
			}
			// dropping the special attack amount
			player.getCombatDefinitions().decreaseSpecialAttack(ItemConstants.getSpecialEnergy(player.getEquipment().getWeaponId()));
			// we used spec so it is triggered off
			player.getCombatDefinitions().setUsuingSpecilAttack(false);
		}
	}
	
	/**
	 * Submits a special attack request
	 *
	 * @param player
	 * 		The player submitting
	 */
	public static void submitSpecialRequest(final Player player) {
		player.putAttribute("special_attack_toggled", true);
		EngineWorkingSet.getScheduledExecutorService().schedule(() -> {
			try {
				if (player.isDead()) {
					return;
				}
				checkSpecialToggle(player, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 100, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Gets the graphics id of the thrown weapon
	 *
	 * @param weaponId
	 * 		The id of the weapon
	 */
	public static int getKnifeThrowGfxId(int weaponId) {
		// knives
		if (weaponId == 868) { // rune
			return 218;
		} else if (weaponId == 867) { // addy
			return 217;
		} else if (weaponId == 866) {  // mith
			return 216;
		} else if (weaponId == 869) { // black
			return 215;
		} else if (weaponId == 865) { // steel
			return 214;
		} else if (weaponId == 863) { // iron
			return 213;
		} else if (weaponId == 864) { // bronze
			return 212;
		}
		// darts
		if (weaponId == 806) { // bronze
			return 226;
		} else if (weaponId == 807) { // iron
			return 227;
		} else if (weaponId == 808) { // steel
			return 228;
		} else if (weaponId == 3093) { // black
			return 34;
		} else if (weaponId == 809) { // mithril
			return 229;
		} else if (weaponId == 810) { // addy
			return 230;
		} else if (weaponId == 811) { // rune
			return 231;
		} else if (weaponId == 11230) { // dragon
			return 1122;
		}
		// javelins
		if (weaponId >= 13954 && weaponId <= 13956 || weaponId >= 13879 && weaponId <= 13882) {
			return 1837;
		}
		// thrownaxe
		if (weaponId == 13883 || weaponId == 13957) {
			return 1839;
		}
		// obby rings
		if (weaponId == 6522) {
			return 442;
		}
		if (weaponId == 800) {
			return 43;
		} else if (weaponId == 13954 || weaponId == 13955 || weaponId == 13956 || weaponId == 13879 || weaponId == 13880 || weaponId == 13881 || weaponId == 13882) {
			return 1837;
		}
		return 219;
	}
	
	/**
	 * Gets the graphics id of an arrow
	 *
	 * @param arrowId
	 * 		The arrow
	 */
	public static int getArrowThrowGfxId(int arrowId) {
		if (arrowId == 884) {
			return 18;
		} else if (arrowId == 886) {
			return 20;
		} else if (arrowId == 888) {
			return 21;
		} else if (arrowId == 890) {
			return 22;
		} else if (arrowId == 892) {
			return 24;
		}
		return 19; // bronze default
	}
	
	/**
	 * Gets the projectile id based on the weapon and the arrow
	 *
	 * @param weaponId
	 * 		The weapon
	 * @param arrowId
	 * 		The arrow
	 */
	public static int getArrowProjectileGfxId(int weaponId, int arrowId) {
		if (arrowId == 882) {
			return 9;
		} else if (arrowId == 884) {
			return 10;
		} else if (arrowId == 886) {
			return 11;
		} else if (arrowId == 888) {
			return 12;
		} else if (arrowId == 890) {
			return 13;
		} else if (arrowId == 892) {
			return 15;
		} else if (arrowId == 11212) {
			return 1120;
		} else if (weaponId == 20171) {
			return 1066;
		}
		return 10;// bronze default
	}
	
	/**
	 * Checks if the player can fight the target
	 *
	 * @param source
	 * 		The player
	 * @param target
	 * 		The target
	 */
	public static boolean canFight(Entity source, Entity target) {
		boolean targetInvalid = target == null || target.isDead();
		boolean sourceInvalid = source == null || source.isDead();
		if (sourceInvalid || targetInvalid || !source.withinDistance(target.getLastWorldTile(), 16)) {
			return false;
		}
		// when force walking we ignore all combat states
		if (target.isNPC() && target.toNPC().isForceWalking() || (source.isNPC() && source.toNPC().isForceWalking())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the accuracy multiplier of a weapon when on special
	 *
	 * @param itemId
	 * 		The weapon
	 */
	// TODO: boosts for ammo [rs combat data url has it]
	public static double getSpecialAccuracyModifier(int itemId) {
		if (itemId == -1) {
			return 0;
		}
		String name = ItemDefinitions.forId(itemId).getName().toLowerCase();
		if (name.contains("whip") || name.contains("dragon scimitar") || name.contains("dragon dagger") || name.contains("dragon spear") || name.contains("zamorakian spear") || name.contains("dragon halberd") || name.contains("anchor") || name.contains("magic longbow") || name.contains("magic shortbow") || name.contains("dragon longsword")) {
			return 0.25;
		}
		if (name.contains("dragon mace")) {
			return 0.1;
		}
		if (name.contains("korasi") || name.contains("dragon claws")) {
			return 0.6;
		}
		if (name.contains("armadyl godsword")) {
			return 0.10;
		}
		if (name.contains("godsword")) {
			return 0.10;
		}
		if (name.contains("barrelchest anchor")) {
			return 0.10;
		}
		if (name.contains("granite maul") || name.contains("granite mace")) {
			return 0.095;
		}
		if (name.contains("dark bow") || name.contains("zanik")) {
			return 0.5;
		}
		if (name.contains("morrigan's javel")) {
			return 0.4;
		}
		if (name.contains("vesta's spear") || name.contains("statius' warhammer") || name.contains("statius' warhammer (deg)") || name.contains("morrigan's throw") || name.contains("hand cannon")) {
			return 1.7;
		}
		if (name.contains("vesta's longsword")) {
			return 1.5;
		}
		return 1;
	}
}
