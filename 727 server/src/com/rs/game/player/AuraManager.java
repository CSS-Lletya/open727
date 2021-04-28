package com.rs.game.player;

import java.util.HashMap;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class AuraManager {

	private transient Player player;
	private transient boolean warned;
	private long activation;
	private HashMap<Integer, Long> cooldowns;

	public AuraManager() {
		cooldowns = new HashMap<Integer, Long>();
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		if (!isActivated())
			return;
		if (activation - Utils.currentTimeMillis() <= 60000 && !warned) {
			player.getPackets().sendGameMessage("Your aura will deplete in 1 minute.");
			warned = true;
			return;
		}
		if (Utils.currentTimeMillis() < activation)
			return;
		desactive();
		player.getAppearance().generateAppearenceData();
	}

	public void removeAura() {
		if (isActivated())
			desactive();
	}

	public void desactive() {
		activation = 0;
		warned = false;
		player.getPackets().sendGameMessage("Your aura has depleted.");
	}

	public long getCoolDown(int aura) {
		Long coolDown = cooldowns.get(aura);
		if (coolDown == null)
			return 0;
		return coolDown;
	}

	public void activate() {
		Item item = player.getEquipment().getItem(Equipment.SLOT_AURA);
		if (item == null)
			return;
		// temp fix, discard useless once eco since they wont be ingame
		if (item.getDefinitions().containsOption("Discard"))
			return;
		player.stopAll(false);
		int toId = getTransformIntoAura(item.getId());
		if (toId != -1) {
			player.getEquipment().getItem(Equipment.SLOT_AURA).setId(toId);
			player.getEquipment().refresh(Equipment.SLOT_AURA);
			player.getAppearance().generateAppearenceData();
		} else {
			if (activation != 0) {
				// TODO message already activated
				return;
			}
			if (Utils.currentTimeMillis() <= getCoolDown(item.getId())) {
				// TODO message cooldown
				return;
			}
			int tier = getTier(item.getId());
//			if (!player.isExtremeDonator() && isWingedAura(item.getId())) {
//				player.getPackets().sendGameMessage("You need to be extreme donator to use this feature.");
//				return;
//			}
//			int tier = getTier(item.getId());
//			if (!player.isDonator() && !player.isExtremeDonator() && tier > 1) {
//				player.getPackets().sendGameMessage("You need to be a donator to use this feature.");
//				return;
//			}
			activation = Utils.currentTimeMillis() + getActivationTime(item.getId()) * 1000;
			cooldowns.put(item.getId(), activation + getCooldown(item.getId()) * 1000);
			player.setNextAnimation(new Animation(2231));
			player.setNextGraphics(new Graphics(getActiveGraphic(tier)));
			player.getAppearance().generateAppearenceData();
		}
	}

	public int getTransformIntoAura(int aura) {
		switch (aura) {
		case 23896: // infernal
			return 23880;
		case 23880: // infernal
			return 23896;
		case 23898: // serene
			return 23882;
		case 23882: // serene
			return 23898;
		case 23900: // vernal
			return 23884;
		case 23884: // vernal
			return 23900;
		case 23902: // nocturnal
			return 23886;
		case 23886: // nocturnal
			return 23902;
		case 23904: // mystical
			return 23888;
		case 23888: // mystical
			return 23904;
		case 23906: // blazing
			return 23890;
		case 23890: // blazing
			return 23906;
		case 23908: // abyssal
			return 23892;
		case 23892: // abyssal
			return 23908;
		case 23910: // divine
			return 23894;
		case 23894: // divine
			return 23910;
		default:
			return -1;
		}
	}

	public void sendAuraRemainingTime() {
		if (!isActivated()) {
			long cooldown = getCoolDown(player.getEquipment().getAuraId());
			if (Utils.currentTimeMillis() <= cooldown) {
				player.getPackets().sendGameMessage("Currently recharging. <col=ff0000>"
						+ getFormatedTime((cooldown - Utils.currentTimeMillis()) / 1000) + " remaining.");
				return;
			}
			player.getPackets().sendGameMessage("Currently desactivate. It is ready to use.");
			return;
		}
		player.getPackets().sendGameMessage("Currently active. <col=00ff00>"
				+ getFormatedTime((activation - Utils.currentTimeMillis()) / 1000) + " remaining");
	}

	public String getFormatedTime(long seconds) {
		long minutes = seconds / 60;
		long hours = minutes / 60;
		minutes -= hours * 60;
		seconds -= (hours * 60 * 60) + (minutes * 60);
		String minutesString = (minutes < 10 ? "0" : "") + minutes;
		String secondsString = (seconds < 10 ? "0" : "") + seconds;
		return hours + ":" + minutesString + ":" + secondsString;
	}

	public void sendTimeRemaining(int aura) {
		long cooldown = getCoolDown(aura);
		if (cooldown < Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("The aura has finished recharging. It is ready to use.");
			return;
		}
		player.getPackets().sendGameMessage("Currently recharging. <col=ff0000>"
				+ getFormatedTime((cooldown - Utils.currentTimeMillis()) / 1000) + " remaining.");
	}

	public boolean isActivated() {
		return activation != 0;
	}

	/*
	 * 16449 - Corruption 16464 - Greater corruption 16429 - Master corruption 68615 - Supreme corruption
	 * 
	 * 16465 - Salvation 16524 - Greater salvation 16450 - Master salvation 68611 - supreme salvation.
	 * 
	 * 68605 - Harmony. 68610 - Greater harmony. 68607 - Master harmony. 68613 - Supreme harmony.
	 */
	public int getAuraModelId2() {
		int aura = player.getEquipment().getAuraId();
		switch (aura) {
		case 22905: // Corruption.
			return 16449;
		case 22899: // Salvation.
			return 16465;
		case 23848: // Harmony.
			return 68605;
		case 22907: // Greater corruption.
			return 16464;
		case 22901: // Greater salvation.
			return 16524;
		case 23850: // Greater harmony.
			return 68610;
		case 22909: // Master corruption.
			return 16429;
		case 22903: // Master salvation.
			return 16450;
		case 23852: // Master harmony.
			return 68607;
		case 23874: // Supreme corruption.
			return 68615;
		case 23876: // Supreme salvation.
			return 68611;
		case 23854: // Supreme harmony.
			return 68613;
		default:
			Logger.log("AurasManager", "Unknown wings: " + aura);
			return -1;
		}
	}

	public int getAuraModelId() {
		Item weapon = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
		if (weapon == null)
			return 8719;
		String name = weapon.getDefinitions().getName().toLowerCase();
		if (name.contains("dagger"))
			return 8724;
		if (name.contains("whip"))
			return 8725;
		if (name.contains("2h sword") || name.contains("godsword"))
			return 8773;
		if (name.contains("sword") || name.contains("scimitar") || name.contains("korasi"))
			return 8722;
		return 8719;
	}

	public int getActiveGraphic(int tier) {
		if (tier == 2)
			return 1764;
		if (tier == 3)
			return 1763;
		return 370; // default gold
	}

	public boolean hasPoisonPurge() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 20958 || aura == 22268;
	}

	public double getMagicAccurayMultiplier() {
		if (!isActivated() || World.isPvpArea(player))
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20962)
			return 1.03;
		if (aura == 22270)
			return 1.05;
		return 1;
	}

	public double getRangeAccurayMultiplier() {
		if (!isActivated() || World.isPvpArea(player))
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20967)
			return 1.03;
		if (aura == 22272)
			return 1.05;
		return 1;
	}

	public double getWoodcuttingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22280)
			return 1.03;
		if (aura == 22282)
			return 1.05;
		return 1;
	}

	public double getMininingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22284)
			return 1.03;
		if (aura == 22286)
			return 1.05;
		return 1;
	}

	public double getFishingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20966)
			return 1.03;
		if (aura == 22274)
			return 1.05;
		return 1;
	}

	public double getPrayerPotsRestoreMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20965)
			return 1.03;
		if (aura == 22276)
			return 1.05;
		return 1;
	}

	public double getThievingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22288)
			return 1.03;
		if (aura == 22290)
			return 1.05;
		return 1;
	}

	public double getChanceNotDepleteMN_WC() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22292)
			return 1.1;
		return 1;
	}

	public boolean usingEquilibrium() {
		if (!isActivated() || World.isPvpArea(player))
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22294;
	}

	public boolean usingPenance() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22300;
	}

	/**
	 * Gets the prayer experience multiplier.
	 * @return The prayer experience multiplier.
	 */
	public double getPrayerMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		switch (aura) {
		case 22905: // Corruption.
		case 22899: // Salvation.
		case 23848: // Harmony.
			return 1.01;
		case 22907: // Greater corruption.
		case 22901: // Greater salvation.
		case 23850: // Greater harmony.
			return 1.015;
		case 22909: // Master corruption.
		case 22903: // Master salvation.
		case 23852: // Master harmony.
			return 1.02;
		case 23874: // Supreme corruption.
		case 23876: // Supreme salvation.
		case 23854: // Supreme harmony.
			return 1.025;
		}
		return 1.0;
	}

	/**
	 * Gets the amount of prayer points to restore (when getting 500 prayer experience).
	 * @return The prayer restoration multiplier.
	 */
	public double getPrayerRestoration() {
		if (!isActivated())
			return 0;
		int aura = player.getEquipment().getAuraId();
		switch (aura) {
		case 22905: // Corruption.
		case 22899: // Salvation.
		case 23848: // Harmony.
			return 0.03;
		case 22907: // Greater corruption.
		case 22901: // Greater salvation.
		case 23850: // Greater harmony.
			return 0.05;
		case 22909: // Master corruption.
		case 22903: // Master salvation.
		case 23852: // Master harmony.
			return 0.07;
		case 23874: // Supreme corruption.
		case 23876: // Supreme salvation.
		case 23854: // Supreme harmony.
			return 0.1;
		}
		return 0;
	}

	public void checkSuccefulHits(int damage) {
		if (!isActivated() || World.isPvpArea(player))
			return;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22296)
			useInspiration();
		else if (aura == 22298)
			useVampyrism(damage);
	}

	public void useVampyrism(int damage) {
		int heal = (int) (damage * 0.05);
		if (heal > 0)
			player.heal(heal);
	}

	public void useInspiration() {
		Integer atts = (Integer) player.getTemporaryAttributtes().get("InspirationAura");
		if (atts == null)
			atts = 0;
		atts++;
		if (atts == 5) {
			atts = 0;
			player.getCombatDefinitions().restoreSpecialAttack(1);
		}
		player.getTemporaryAttributtes().put("InspirationAura", atts);
	}

	public boolean usingWisdom() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22302;
	}

	/**
	 * Checks if the aura worn is a winged aura.
	 * @return {@code True}.
	 */
	public boolean isWingedAura(int aura) {
		switch (aura) {
		case 22905: // Corruption.
		case 22899: // Salvation.
		case 23848: // Harmony.
		case 22907: // Greater corruption.
		case 22901: // Greater salvation.
		case 23850: // Greater harmony.
		case 22909: // Master corruption.
		case 22903: // Master salvation.
		case 23852: // Master harmony.
		case 23874: // Supreme corruption.
		case 23876: // Supreme salvation.
		case 23854: // Supreme harmony.
			return true;
		}
		return false;
	}

	/*
	 * return seconds
	 */
	public static int getActivationTime(int aura) {
		switch (aura) {
		case 20958:
			return 600; // 10minutes
		case 22268:
			return 1200; // 20minutes
		case 22302:
			return 1800; // 30minutes
		case 22294:
			return 7200; // 2hours
		case 20959:
			return 10800; // 3hours
		default:
			return 3600; // default 1hour
		}
	}

	public static int getCooldown(int aura) {
		switch (aura) {
		case 20962:
		case 22270:
		case 20967:
		case 22272:
		case 22280:
		case 22282:
		case 22284:
		case 22286:
		case 20966:
		case 22274:
		case 20965:
		case 22276:
		case 22288:
		case 22290:
		case 22292:
		case 22296:
		case 22298:
		case 22300:
			return 10800; // 3hours
		case 22294:
			return 14400; // 4hours
		case 20959:
		case 22302:
			return 86400; // 24hours
		default:
			return 10800; // default 3 hours - stated on www.runescape.wikia.com/wiki/Aura
		}
	}

	public static int getTier(int aura) {
		switch (aura) {
		case 23874:
		case 23876:
		case 23854:
			return 4;
		case 22302:
		case 22909:
		case 22903:
		case 23852:
			return 3;
		case 22907:
		case 22901:
		case 23850:
		case 20959:
		case 22270:
		case 22272:
		case 22282:
		case 22286:
		case 22274:
		case 22276:
		case 22290:
		case 22292:
		case 22294:
		case 22296:
		case 22298:
		case 22300:
			return 2;
		default:
			return 1; // default 1
		}
	}
}
