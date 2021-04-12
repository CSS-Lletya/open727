package com.rs.game.minigames.clanwars;

import com.rs.game.Entity;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.minigames.clanwars.ClanWars.Rules;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.Controler;

/**
 * A controller subclass handling the clan wars requesting.
 * @author Emperor
 *
 */
public final class RequestController extends Controler {

	@Override
	public void start() {
		player.getPackets().sendPlayerOption("Challenge", 1, false);
		sendInterfaces();
		if (!inWarRequest(player)) {
			stop();
		}
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public void forceClose() {
		stop();
	}

	@Override
	public void sendInterfaces() {
	}

	@Override
	public void magicTeleported(int type) {
		stop();
	}

	@Override
	public void moved() {
		if (!inWarRequest(player)) {
			stop();
		}
		decline();
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		ClanWars clanWars = (ClanWars) player.getTemporaryAttributtes().get("clan_wars");
		if (clanWars == null) {
			return true;
		}
		Player other = (Player) player.getTemporaryAttributtes().get("clan_request_p");
		if (other == null || player.getTemporaryAttributtes().get("clan_wars") != other.getTemporaryAttributtes()
				.get("clan_wars")) {
			return true;
		}
		switch (interfaceId) {
		case 791:
			switch (componentId) {
			case 20:// Knock-out.
				clanWars.setVictoryType(-1, player, other);
				return false;
			case 25:
				clanWars.setVictoryType(25, player, other);
				return false;
			case 28:
				clanWars.setVictoryType(50, player, other);
				return false;
			case 31:
				clanWars.setVictoryType(100, player, other);
				return false;
			case 34:
				clanWars.setVictoryType(200, player, other);
				return false;
			case 37:
				clanWars.setVictoryType(400, player, other);
				return false;
			case 40:
				clanWars.setVictoryType(750, player, other);
				return false;
			case 43:
				clanWars.setVictoryType(1_000, player, other);
				return false;
			case 46:
				clanWars.setVictoryType(2_500, player, other);
				return false;
			case 49:
				clanWars.setVictoryType(5_000, player, other);
				return false;
			case 52:
				clanWars.setVictoryType(10_000, player, other);
				return false;
			case 56: // Most kills.
				clanWars.setVictoryType(-2, player, other);
				return false;
			case 60:
				clanWars.setTimeLeft(500, player, other);
				return false;
			case 63:
				clanWars.setTimeLeft(1_000, player, other);
				return false;
			case 66:
				clanWars.setTimeLeft(3_000, player, other);
				return false;
			case 69:
				clanWars.setTimeLeft(6_000, player, other);
				return false;
			case 72:
				clanWars.setTimeLeft(9_000, player, other);
				return false;
			case 75:
				clanWars.setTimeLeft(12_000, player, other);
				return false;
			case 78:
				clanWars.setTimeLeft(15_000, player, other);
				return false;
			case 81:
				clanWars.setTimeLeft(18_000, player, other);
				return false;
			case 84:
				clanWars.setTimeLeft(24_000, player, other);
				return false;
			case 87:
				clanWars.setTimeLeft(30_000, player, other);
				return false;
			case 90:
				clanWars.setTimeLeft(36_000, player, other);
				return false;
			case 93:
				clanWars.setTimeLeft(48_000, player, other);
				return false;
			case 100:
				clanWars.setTimeLeft(-1, player, other);
				return false;
			case 116:
				clanWars.switchRule(Rules.ITEMS_LOST, player);
				return false;
			case 120:
				clanWars.switchRule(Rules.NO_MELEE, player);
				return false;
			case 122:
				clanWars.switchRule(Rules.NO_MAGIC, player);
				return false;
			case 128:
				clanWars.switchRule(Rules.NO_RANGE, player);
				return false;
			case 130:
				clanWars.switchRule(Rules.NO_PRAYER, player);
				return false;
			case 132:
				clanWars.switchRule(Rules.NO_FAMILIARS, player);
				return false;
			case 134:
				clanWars.switchRule(Rules.NO_FOOD, player);
				return false;
			case 136:
				clanWars.switchRule(Rules.NO_POTIONS, player);
				return false;
			case 141:
				switch (slotId) {
				case 3: // Classic area
					ClanWars.sendConfig(player, other, 5292, 0);
					clanWars.setAreaType(AreaType.CLASSIC_AREA);
					return false;
				case 7: // Plateau
					ClanWars.sendConfig(player, other, 5292, 1);
					clanWars.setAreaType(AreaType.PLATEAU);
					return false;
				case 11: // Forsaken quarry
					ClanWars.sendConfig(player, other, 5292, 2);
					clanWars.setAreaType(AreaType.FORSAKEN_QUARRY);
					return false;
				case 15: // Blasted forest
					ClanWars.sendConfig(player, other, 5292, 3);
					clanWars.setAreaType(AreaType.BLASTED_FOREST);
					return false;
				case 19: // Turrets.
					ClanWars.sendConfig(player, other, 5292, 4);
					clanWars.setAreaType(AreaType.TURRETS);
					return false;
				}
				return true;
			case 143: // Accept
				clanWars.accept(player);
				player.getPackets().sendConfigByFile(5293, 1);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canPlayerOption1(final Player target) {
		player.stopAll(false);
		player.setCoordsEvent(new CoordsEvent(target, new Runnable() {
			@Override
			public void run() {
				if (!canRequest(player, target, true)) {
					return;
				}
				if (target == null || target.hasFinished() || !target.withinDistance(player, 14)
						|| !(target.getControlerManager().getControler() instanceof RequestController)) {
					player.getPackets().sendGameMessage(
							"Unable to find " + (target == null ? "your target" : target.getDisplayName()));
					return;
				}
				if (target.getInterfaceManager().containsScreenInter()) {
					player.getPackets().sendGameMessage("The other player is busy.");
					return;
				}
				player.getTemporaryAttributtes().put("clan_request_p", target);
				if (target.getTemporaryAttributtes().get("clan_request_p") == player) {
					ClanWars clanWars = new ClanWars(target.getCurrentFriendChat(), player.getCurrentFriendChat());
					if (canRequest(target, player, false)) { // Make sure the target can still challenge.
						clanWars.sendInterface(player, target);
						clanWars.sendInterface(target, player);
						return;
					}
				}
				target.getPackets().sendClanWarsRequestMessage(player);
				player.getPackets().sendGameMessage("Sending challenge request...");
			}
		}, player.getSize()));
		return true;
	}

	@Override
	public boolean canAttack(Entity target) {
		return false;
	}

	@Override
	public boolean sendDeath() {
		// Make sure the player doesn't lose his items on death.
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		case 28213:
			if (player.getCurrentFriendChat() == null || player.getCurrentFriendChat().getClanWars() == null) {
				player.getDialogueManager().startDialogue("clan_wars_view");
				return false;
			}
			ClanWars.enter(player);
			return false;
		case 38698: // Safe FFA portal.
			player.setNextWorldTile(new WorldTile(2815, 5511, 0));
			player.getControlerManager().startControler("clan_wars_ffa", false);
			return false;
		case 38699: // Risk FFA portal.
			player.setNextWorldTile(new WorldTile(3007, 5511, 0));
			player.getControlerManager().startControler("clan_wars_ffa", true);
			return false;
		}
		return true;
	}

	/**
	 * Called when the request controller should unregister.
	 */
	public void stop() {
		removeControler();
		decline();
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
		player.getPackets().sendPlayerOption("null", 1, false);
	}

	/**
	 * Declines the challenge.
	 */
	public void decline() {
		Player other = (Player) player.getTemporaryAttributtes().get("clan_request_p");
		ClanWars war = (ClanWars) player.getTemporaryAttributtes().get("clan_wars");
		if (other == null || war == null) {
			return;
		}
		if (war != null) {
			player.getInterfaceManager().closeScreenInterface();
			other.getInterfaceManager().closeScreenInterface();
			player.getTemporaryAttributtes().remove("clan_request_p");
			other.getTemporaryAttributtes().remove("clan_request_p");
			player.getTemporaryAttributtes().remove("clan_wars");
			other.getTemporaryAttributtes().remove("clan_wars");
		}
	}

	/**
	 * Checks if a player can request a war.
	 * @param player The player.
	 * @param target The player to challenge.
	 * @param message If the player should be messaged if he was unable to challenge.
	 * @return {@code True} if so.
	 */
	private static boolean canRequest(Player player, Player target, boolean message) {
		String text = null;
		if (player.getCurrentFriendChat() == null) {
			text = "You need to be in a friends chat to start a war.";
		} else if (target.getCurrentFriendChat() == null) {
			text = "This player is not in a friends chat.";
		} else if (player.getCurrentFriendChat().getRank(player.getRights(), player.getUsername()) < 3) {
			text = "You need to be a higher rank to start a war.";
		} else if (target.getCurrentFriendChat().getRank(target.getRights(), target.getUsername()) < 3) {
			text = "This player does not have a high enough rank to accept your challenge.";
		} else if (player.getCurrentFriendChat() == target.getCurrentFriendChat()) {
			text = "You can't challenge players in the same friends chat as you are in.";
		} else if (player.getCurrentFriendChat().getClanWars() != null) {
			text = "Your clan is already in a war.";
		} else if (target.getCurrentFriendChat().getClanWars() != null) {
			text = "This player's clan is already in a war.";
		}
		if (message && text != null) {
			player.getPackets().sendGameMessage(text);
		}
		return text == null;
	}

	/**
	 * Checks if a player is in the clan wars request area.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public static boolean inWarRequest(Player player) {
		return player.inArea(2980, 9664, 3004, 9694);
	}
}