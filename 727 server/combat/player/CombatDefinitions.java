package player;

import org.redrune.game.content.combat.MagicConstants.MagicBook;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.ItemBonuses;

import skills.Skills;

public final class CombatDefinitions {

	public static final int STAB_ATTACK = 0, SLASH_ATTACK = 1, CRUSH_ATTACK = 2, RANGE_ATTACK = 4, MAGIC_ATTACK = 3;
	public static final int STAB_DEF = 5, SLASH_DEF = 6, CRUSH_DEF = 7, RANGE_DEF = 9, MAGIC_DEF = 8,
			SUMMONING_DEF = 10;
	public static final int STRENGTH_BONUS = 14, RANGED_STR_BONUS = 15, MAGIC_DAMAGE = 17, PRAYER_BONUS = 16;
	public static final int ABSORVE_MELEE_BONUS = 11, ABSORVE_RANGE_BONUS = 13, ABSORVE_MAGE_BONUS = 12;

	public static final int SHARED = -1;
	public static final String[] BONUS_LABELS = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush",
			"Magic", "Range", "Summoning", "Absorb Melee", "Absorb Magic", "Absorb Ranged", "Strength", "Ranged Str",
			"Prayer", "Magic Damage" };
	private transient Player player;
	private transient boolean usingSpecialAttack;
	private transient int[] bonuses;

	// saving stuff

	private byte attackStyle;
	private byte specialAttackPercentage;
	private boolean autoRelatie;
	private byte sortSpellBook;
	private boolean showCombatSpells;
	private boolean showSkillSpells;
	private boolean showMiscallaneousSpells;
	private boolean showTeleportSpells;
	private boolean defensiveCasting;
	private transient boolean instantAttack;
	private transient boolean dungeonneringSpellBook;
	private byte spellBook;
	private byte autoCastSpell;

	public int getSpellId() {
		Integer tempCastSpell = (Integer) player.getTemporaryAttributtes().get("tempCastSpell");
		if (tempCastSpell != null)
			return tempCastSpell + 256;
		return autoCastSpell;
	}

	public int getAutoCastSpell() {
		return autoCastSpell;
	}

	public void resetSpells(boolean removeAutoSpell) {
		player.getTemporaryAttributtes().remove("tempCastSpell");
		if (removeAutoSpell) {
			setAutoCastSpell(0);
			refreshAutoCastSpell();
		}
	}

	public void setAutoCastSpell(int id) {
		autoCastSpell = (byte) id;
		refreshAutoCastSpell();
	}

	public void refreshAutoCastSpell() {
		refreshAttackStyle();
		player.getPackets().sendConfig(108, getSpellAutoCastConfigValue());
	}

	public int getSpellAutoCastConfigValue() {
		if (dungeonneringSpellBook)
			return 0;
		if (spellBook == 0) {
			switch (autoCastSpell) {
			case 25:
				return 3;
			case 28:
				return 5;
			case 30:
				return 7;
			case 32:
				return 9;
			case 34:
				return 11; // air bolt
			case 39:
				return 13;// water bolt
			case 42:
				return 15;// earth bolt
			case 45:
				return 17; // fire bolt
			case 49:
				return 19;// air blast
			case 52:
				return 21;// water blast
			case 58:
				return 23;// earth blast
			case 63:
				return 25;// fire blast
			case 66: // Saradomin Strike
				return 41;
			case 67:// Claws of Guthix
				return 39;
			case 68:// Flames of Zammorak
				return 43;
			case 70:
				return 27;// air wave
			case 73:
				return 29;// water wave
			case 77:
				return 31;// earth wave
			case 80:
				return 33;// fire wave
			case 84:
				return 47;
			case 87:
				return 49;
			case 89:
				return 51;
			case 91:
				return 53;
			case 99:
				return 145;
			default:
				return 0;
			}
		} else if (spellBook == 1) {
			switch (autoCastSpell) {
			case 28:
				return 63;
			case 32:
				return 65;
			case 24:
				return 67;
			case 20:
				return 69;
			case 30:
				return 71;
			case 34:
				return 73;
			case 26:
				return 75;
			case 22:
				return 77;
			case 29:
				return 79;
			case 33:
				return 81;
			case 25:
				return 83;
			case 21:
				return 85;
			case 31:
				return 87;
			case 35:
				return 89;
			case 27:
				return 91;
			case 23:
				return 93;
			case 36:
				return 95;
			case 37:
				return 99;
			case 38:
				return 97;
			case 39:
				return 101;
			default:
				return 0;
			}
		} else {
			return 0;
		}
	}

	public CombatDefinitions() {
		specialAttackPercentage = 100;
		autoRelatie = true;
		showCombatSpells = true;
		showSkillSpells = true;
		showMiscallaneousSpells = true;
		showTeleportSpells = true;
	}
	
	/**
	 * The id of the spellbook
	 */
	private MagicBook spellbook = MagicBook.REGULAR;
	
	public MagicBook getSpellbook() {
		return spellbook;
	}
	

	public void setSpellBook(int id) {
		if (id == 3)
			dungeonneringSpellBook = true;
		else
			spellBook = (byte) id;
		refreshSpellBookScrollBar_DefCast();
		player.getInterfaceManager().sendMagicBook();
	}

	public void refreshSpellBookScrollBar_DefCast() {
		player.getPackets().sendConfig(439, (dungeonneringSpellBook ? 3 : spellBook) + (defensiveCasting ? 0 : 1 << 8));
	}

	public int getSpellBook() {
		if (dungeonneringSpellBook)
			return 950; // dung book
		else {
			if (spellBook == 0)
				return 192; // normal
			else if (spellBook == 1)
				return 193; // ancients
			else
				return 430; // lunar
		}

	}

	public void switchShowCombatSpells() {
		showCombatSpells = !showCombatSpells;
		refreshSpellBook();
	}

	public void switchShowSkillSpells() {
		showSkillSpells = !showSkillSpells;
		refreshSpellBook();
	}

	public void switchShowMiscallaneousSpells() {
		showMiscallaneousSpells = !showMiscallaneousSpells;
		refreshSpellBook();
	}

	public void switchShowTeleportSkillSpells() {
		showTeleportSpells = !showTeleportSpells;
		refreshSpellBook();
	}

	public void switchDefensiveCasting() {
		defensiveCasting = !defensiveCasting;
		refreshSpellBookScrollBar_DefCast();
	}

	public void setSortSpellBook(int sortId) {
		this.sortSpellBook = (byte) sortId;
		refreshSpellBook();
	}

	public boolean isDefensiveCasting() {
		return defensiveCasting;
	}

	public void refreshSpellBook() {
		if (spellBook == 0) {
			player.getPackets().sendConfig(1376,
					sortSpellBook | (showCombatSpells ? 0 : 1 << 9) | (showSkillSpells ? 0 : 1 << 10)
							| (showMiscallaneousSpells ? 0 : 1 << 11) | (showTeleportSpells ? 0 : 1 << 12));
		} else if (spellBook == 1) {
			player.getPackets().sendConfig(1376,
					sortSpellBook << 3 | (showCombatSpells ? 0 : 1 << 16) | (showTeleportSpells ? 0 : 1 << 17));
		} else if (spellBook == 2) {
			player.getPackets().sendConfig(1376, sortSpellBook << 6 | (showCombatSpells ? 0 : 1 << 13)
					| (showMiscallaneousSpells ? 0 : 1 << 14) | (showTeleportSpells ? 0 : 1 << 15));
		}
	}

	public static final int getMeleeDefenceBonus(int bonusId) {
		if (bonusId == STAB_ATTACK)
			return STAB_DEF;
		if (bonusId == SLASH_DEF)
			return SLASH_DEF;
		return CRUSH_DEF;
	}

	public static final int getMeleeBonusStyle(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			if (weaponId == -2) {
				return CRUSH_ATTACK;
			}
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName.contains("whip"))
				return SLASH_ATTACK;
			if (weaponName.contains("staff of light")) {
				switch (attackStyle) {
				case 0:
					return STAB_ATTACK;
				case 1:
					return SLASH_ATTACK;
				default:
					return CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("staff") || weaponName.contains("granite mace") || weaponName.contains("warhammer")
					|| weaponName.contains("tzhaar-ket-em") || weaponName.contains("tzhaar-ket-om")
					|| weaponName.contains("maul"))
				return CRUSH_ATTACK;
			if (weaponName.contains("godsword") || weaponName.contains("greataxe") || weaponName.contains("2h sword")
					|| weaponName.equals("saradomin sword")) {
				switch (attackStyle) {
				case 2:
					return CRUSH_ATTACK;
				default:
					return SLASH_ATTACK;
				}
			}
			if (weaponName.contains("scimitar") || weaponName.contains("hatchet") || weaponName.contains("claws")
					|| weaponName.contains(" sword") || weaponName.contains("longsword")) {
				switch (attackStyle) {
				case 2:
					return STAB_ATTACK;
				default:
					return SLASH_ATTACK;
				}
			}
			if (weaponName.contains("mace") || weaponName.contains("anchor")) {
				switch (attackStyle) {
				case 2:
					return STAB_ATTACK;
				default:
					return CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
				case 1:
					return SLASH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}
			if (weaponName.contains("spear")) {
				switch (attackStyle) {
				case 1:
					return SLASH_ATTACK;
				case 2:
					return CRUSH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}
			if (weaponName.contains("pickaxe")) {
				switch (attackStyle) {
				case 2:
					return CRUSH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}

			if (weaponName.contains("dagger") || weaponName.contains("rapier")) {
				switch (attackStyle) {
				case 2:
					return SLASH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}

		}
		switch (weaponId) {
		default:
			return CRUSH_ATTACK;
		}
	}

	public static final int getXpStyle(int weaponId, int attackStyle) {
		if (weaponId != -1 && weaponId != -2) {
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName.contains("whip")) {
				switch (attackStyle) {
				case 0:
					return Skills.ATTACK;
				case 1:
					return SHARED;
				case 2:
				default:
					return Skills.DEFENCE;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
				case 0:
					return SHARED;
				case 1:
					return Skills.STRENGTH;
				case 2:
				default:
					return Skills.DEFENCE;
				}
			}
			if (weaponName.contains("staff")) {
				switch (attackStyle) {
				case 0:
					return Skills.ATTACK;
				case 1:
					return Skills.STRENGTH;
				case 2:
				default:
					return Skills.DEFENCE;
				}
			}
			if (weaponName.contains("godsword") || weaponName.contains("sword") || weaponName.contains("2h")) {
				switch (attackStyle) {
				case 0:
					return Skills.ATTACK;
				case 1:
					return Skills.STRENGTH;
				case 2:
					return Skills.STRENGTH;
				case 3:
				default:
					return Skills.DEFENCE;
				}
			}
		}
		switch (weaponId) {
		case -1:
		case -2:
			switch (attackStyle) {
			case 0:
				return Skills.ATTACK;
			case 1:
				return Skills.STRENGTH;
			case 2:
			default:
				return Skills.DEFENCE;
			}
		default:
			switch (attackStyle) {
			case 0:
				return Skills.ATTACK;
			case 1:
				return Skills.STRENGTH;
			case 2:
				return SHARED;
			case 3:
			default:
				return Skills.DEFENCE;
			}
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		bonuses = new int[18];
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public void refreshBonuses() {
		bonuses = new int[18];
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null)
				continue;
			int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
			if (bonuses == null)
				continue;
			for (int id = 0; id < bonuses.length; id++) {
				if (id == RANGED_STR_BONUS && this.bonuses[RANGED_STR_BONUS] != 0)
					continue;
				this.bonuses[id] += bonuses[id];
			}
		}
	}

	public void resetSpecialAttack() {
		decreaseSpecialAttack(0);
		specialAttackPercentage = 100;
		refreshSpecialAttackPercentage();
	}

	public void setSpecialAttack(int special) {
		decreaseSpecialAttack(0);
		specialAttackPercentage = (byte) special;
		refreshSpecialAttackPercentage();
	}

	public void restoreSpecialAttack() {
		if (player.getFamiliar() != null)
			player.getFamiliar().restoreSpecialAttack(15);
		if (specialAttackPercentage == 100)
			return;
		restoreSpecialAttack(10);
		if (specialAttackPercentage == 100 || specialAttackPercentage == 50)
			player.getPackets().sendGameMessage(
					"<col=00FF00>Your special attack energy is now " + specialAttackPercentage + "%.", true);
	}

	public void restoreSpecialAttack(int percentage) {
		if (specialAttackPercentage >= 100 || player.getInterfaceManager().containsScreenInter())
			return;
		specialAttackPercentage += specialAttackPercentage > (100 - percentage) ? 100 - specialAttackPercentage
				: percentage;
		refreshSpecialAttackPercentage();
	}

	public void init() {
		refreshUsingSpecialAttack();
		refreshSpecialAttackPercentage();
		refreshAutoRelatie();
		refreshAttackStyle();
		refreshSpellBook();
		refreshAutoCastSpell();
		refreshSpellBookScrollBar_DefCast();
	}

	public void checkAttackStyle() {
		if (autoCastSpell == 0)
			setAttackStyle(attackStyle);
	}

	public void setAttackStyle(int style) {
		int maxSize = 3;
		int weaponId = player.getEquipment().getWeaponId();
		String name = weaponId == -1 ? "" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		if (weaponId == -1 || PlayerCombat.isRanging(player) != 0 || name.contains("whip") || name.contains("halberd"))
			maxSize = 2;
		if (style > maxSize)
			style = maxSize;
		if (style != attackStyle) {
			attackStyle = (byte) style;
			if (autoCastSpell > 1)
				resetSpells(true);
			else
				refreshAttackStyle();
		} else if (autoCastSpell > 1)
			resetSpells(true);
	}

	public void refreshAttackStyle() {
		player.getPackets().sendConfig(43, autoCastSpell > 0 ? 4 : attackStyle);
	}

	public void sendUnlockAttackStylesButtons() {
		for (int componentId = 7; componentId <= 10; componentId++)
			player.getPackets().sendAccessMask(884, componentId, -1, 0, 6);
	}

	public void switchUsingSpecialAttack() {
		usingSpecialAttack = !usingSpecialAttack;
		refreshUsingSpecialAttack();
	}

	public void decreaseSpecialAttack(int ammount) {
		usingSpecialAttack = false;
		refreshUsingSpecialAttack();
		if (ammount > 0) {
			specialAttackPercentage -= ammount;
			refreshSpecialAttackPercentage();
		}
	}

	public boolean hasRingOfVigour() {
		return player.getEquipment().getRingId() == 19669;
	}

	public int getSpecialAttackPercentage() {
		return specialAttackPercentage;
	}

	public void refreshUsingSpecialAttack() {
		player.getPackets().sendConfig(301, usingSpecialAttack ? 1 : 0);
	}

	public void refreshSpecialAttackPercentage() {
		player.getPackets().sendConfig(300, specialAttackPercentage * 10);
	}

	public void switchAutoRelatie() {
		autoRelatie = !autoRelatie;
		refreshAutoRelatie();
	}

	public void refreshAutoRelatie() {
		player.getPackets().sendConfig(172, autoRelatie ? 0 : 1);
	}

	public boolean isUsingSpecialAttack() {
		return usingSpecialAttack;
	}
	
	public boolean setUsuingSpecilAttack(boolean activate) {
		return usingSpecialAttack = activate;
	}

	public int getAttackStyle() {
		return attackStyle;
	}

	public boolean isAutoRelatie() {
		return autoRelatie;
	}

	public void setAutoRelatie(boolean autoRelatie) {
		this.autoRelatie = autoRelatie;
	}

	public boolean isDungeonneringSpellBook() {
		return dungeonneringSpellBook;
	}

	public void removeDungeonneringBook() {
		if (dungeonneringSpellBook) {
			dungeonneringSpellBook = false;
			player.getInterfaceManager().sendMagicBook();
		}
	}

	public boolean isInstantAttack() {
		return instantAttack;
	}

	public void setInstantAttack(boolean instantAttack) {
		this.instantAttack = instantAttack;
	}
}
