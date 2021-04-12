package com.rs.game.minigames;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.Controler;
import com.rs.game.player.controlers.CrucibleControler;
import com.rs.utils.Utils;

public class Crucible {

	public static void enterCrucibleEntrance(Player player) {
		if (!player.isTalkedWithMarv()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need to check in with the Crucible's guardians at the other doorway first.");
			return;
		}
		if (player.getSkills().getCombatLevelWithSummoning() < 60) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need to be at least level 60 to enter Crucible.");
			return;
		}
		player.getInterfaceManager().sendInterface(1292);
	}

	public static void enterArena(Player player) {
		travel(player, getBankTile());
		player.getControlerManager().startControler("CrucibleControler");
	}

	public static void leaveArena(Player player) {
		travel(player, new WorldTile(3355, 6119, 0));
		player.getControlerManager().forceStop();
	}

	public static void travel(Player player, WorldTile tile) {
		player.stopAll();
		player.lock(2);
		player.setNextWorldTile(tile);
	}

	private static final List<Player> playersInside = new ArrayList<Player>();
	private static final Object LOCK = new Object();

	public static void removePlayer(Player player, CrucibleControler crucibleControler, boolean logout) {
		synchronized (LOCK) {
			if (!logout) {
				player.setForceMultiArea(false);
				// setImmune(player, 0);
				crucibleControler.setInside(false);
			}
			if (crucibleControler.getTarget() != null) {
				CrucibleControler targetControler = getControler(crucibleControler.getTarget());
				if (targetControler != null) {
					targetControler.setTarget(null);
					playersInside.add(crucibleControler.getTarget());
				}
				crucibleControler.setTarget(null);
			} else
				playersInside.remove(player);
		}
	}

	public static void addPlayer(Player player, CrucibleControler crucibleControler) {
		synchronized (LOCK) {
			player.setForceMultiArea(true);
			crucibleControler.setInside(true);
			setImmune(player, 9);
			Player target = getTarget(player);
			if (target == null || !addTarget(player, target, crucibleControler))
				playersInside.add(player);
		}
	}

	public static boolean addTarget(Player player, Player target, CrucibleControler playerControler) {
		CrucibleControler targetControler = getControler(target);
		if (targetControler == null)
			return false;
		if (!playersInside.remove(target))
			return false;
		playerControler.setTarget(target);
		targetControler.setTarget(player);
		return true;
	}

	public static CrucibleControler getControler(Player player) {
		Controler controler = player.getControlerManager().getControler();
		return (CrucibleControler) (controler instanceof CrucibleControler ? controler : null);
	}

	public static boolean isImmune(Player player) {
		Long immune = (Long) player.getTemporaryAttributtes().get("CrucibleImmune");
		return immune != null && immune > Utils.currentTimeMillis();
	}

	public static boolean isImmune(Player player, long time) {
		Long immune = (Long) player.getTemporaryAttributtes().get("CrucibleImmune");
		return immune != null && immune > Utils.currentTimeMillis() + time;
	}

	public static void setImmune(Player player, int seconds) {
		if (seconds == 0)
			player.getTemporaryAttributtes().remove("CrucibleImmune");
		else
			player.getTemporaryAttributtes().put("CrucibleImmune", (Utils.currentTimeMillis() + seconds * 1000));
	}

	public static Player getTarget(Player toPlayer) {
		int combatLevel = toPlayer.getSkills().getCombatLevelWithSummoning();
		for (Player player : playersInside) {
			if (Math.abs(player.getSkills().getCombatLevelWithSummoning() - combatLevel) <= 10
					&& !isImmune(player, 9000))
				return player;
		}
		return null;
	}

	public static void openFissureTravel(Player player) {
		player.stopAll();
		player.getInterfaceManager().sendInterface(1291);
		player.getTemporaryAttributtes().remove("crucibleBounty");
	}

	public static WorldTile getBankTile() {
		return BANK_FISSURES[Utils.random(BANK_FISSURES.length)].tile;
	}

	private static final Fissures[] BANK_FISSURES = { Fissures.EAST_BANK, Fissures.NORTH_BANK, Fissures.WEST_BANK,
			Fissures.SOUTH_BANK };

	public static Fissures getFissure(int componentId) {
		for (Fissures f : Fissures.values())
			if (f.componentId == componentId)
				return f;
		return null;
	}

	public static boolean isBankFissure(Fissures fissure) {
		for (Fissures f : BANK_FISSURES)
			if (f == fissure)
				return true;
		return false;
	}

	public static Fissures getFissure() {
		while (true) {
			Fissures f = Fissures.values()[Utils.random(Fissures.values().length)];
			if (isBankFissure(f))
				continue;
			return f;
		}
	}

	public static void quickTravel(Player player, CrucibleControler controler) {
		travel(player, getFissure(), controler);
	}

	public static void goBank(Player player, CrucibleControler controler) {
		travel(player, BANK_FISSURES[Utils.random(BANK_FISSURES.length)], controler);
	}

	public static void payBountyFee(Player player, CrucibleControler controler) {
		Fissures fissure = (Fissures) player.getTemporaryAttributtes().remove("crucibleBounty");
		if (fissure == null)
			return;
		travel(player, fissure.tile);
		Crucible.addPlayer(player, controler);
	}

	public static void travel(Player player, Fissures fissure, CrucibleControler controler) {
		if (fissure == null)
			return;
		boolean isInside = controler.isInside();
		if (!isInside) {
			if (isBankFissure(fissure))
				travel(player, fissure.tile);
			else {
				player.getInterfaceManager().sendInterface(1298);
				player.getPackets().sendHideIComponent(1298, 40, true);
				player.getPackets().sendHideIComponent(1298, 41, true);
				player.getPackets().sendIComponentText(1298, 23, "0");
				player.getPackets().sendIComponentText(1298, 5, "0");
				player.getPackets().sendIComponentText(1298, 6, "0");
				player.getPackets().sendIComponentText(1298, 7, "0");
				player.getTemporaryAttributtes().put("crucibleBounty", fissure);
			}
		} else {
			travel(player, fissure.tile);
			if (isBankFissure(fissure))
				Crucible.removePlayer(player, controler, false);
		}
	}

	private static enum Fissures {
		EAST_BANK(3209, 6144, 4), NORTH_BANK(3263, 6198, 5), WEST_BANK(3318, 6144, 6), SOUTH_BANK(3260, 6089,
				7), FISSURE_6(3266, 6132, 8), FISSURE_7(3294, 6118, 9), FISSURE_3(3279, 6151, 10), FISSURE_2(3287, 6173,
						11), FISSURE_1(3259, 6183, 12), FISSURE_4(3248, 6155,
								13), FISSURE_5(3230, 6144, 14), FISSURE_9(3227, 6116, 15), FISSURE_8(3259, 6100, 16);
		private WorldTile tile;
		private int componentId;

		private Fissures(int x, int y, int componentId) {
			tile = new WorldTile(x, y, 0);
			this.componentId = componentId;
		}

	}

}
