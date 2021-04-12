package com.rs.game.player.content;

import java.util.Random;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class Dicing {

	public static void handleRoll(final Player player, int itemId, boolean friends) {
		if (friends) {
			switch (itemId) {
			case 15086:
				friendsRoll(player, itemId, 2072, 1, 6);
				break;
			case 15088:
				friendsRoll(player, itemId, 2074, 1, 12);
				break;
			case 15090:
				friendsRoll(player, itemId, 2071, 1, 8);
				break;
			case 15092:
				friendsRoll(player, itemId, 2070, 1, 10);
				break;
			case 15094:
				friendsRoll(player, itemId, 2073, 1, 12);
				break;
			case 15096:
				friendsRoll(player, itemId, 2068, 1, 20);
				break;
			case 15098:
				friendsRoll(player, itemId, 2075, 1, 100);
				break;
			case 15100:
				friendsRoll(player, itemId, 2069, 1, 4);
				break;
			}
		} else {
			switch (itemId) {
			case 15086:
				privateRoll(player, itemId, 2072, 1, 6);
				break;
			case 15088:
				privateRoll(player, itemId, 2074, 1, 12);
				break;
			case 15090:
				privateRoll(player, itemId, 2071, 1, 8);
				break;
			case 15092:
				privateRoll(player, itemId, 2070, 1, 10);
				break;
			case 15094:
				privateRoll(player, itemId, 2073, 1, 12);
				break;
			case 15096:
				privateRoll(player, itemId, 2068, 1, 20);
				break;
			case 15098:
				privateRoll(player, itemId, 2075, 1, 100);
				break;
			case 15100:
				privateRoll(player, itemId, 2069, 1, 4);
				break;
			}
		}
	}

	public static void privateRoll(final Player player, final int itemId, int graphic, final int lowest,
			final int highest) {
		player.getPackets().sendGameMessage("Rolling...", true);
		player.getInventory().deleteItem(itemId, 1);
		player.setNextAnimation(new Animation(11900));
		player.setNextGraphics(new Graphics(graphic));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInventory().addItem(itemId, 1);
				player.getPackets().sendGameMessage("You rolled <col=db3535>" + getRandom(lowest, highest)
						+ "</col> on " + diceText(itemId) + " die.", true);
			}
		}, 1);
	}

	public static void friendsRoll(final Player player, final int itemId, int graphic, final int lowest,
			final int highest) {
		final FriendChatsManager chat = player.getCurrentFriendChat();
		if (chat == null) {
			player.getPackets().sendGameMessage("You need to be in a friend chat to use this option.");
			return;
		}
		player.lock(2);
		player.getPackets().sendGameMessage("Rolling...");
		player.getInventory().deleteItem(itemId, 1);
		player.setNextAnimation(new Animation(11900));
		player.setNextGraphics(new Graphics(graphic));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInventory().addItem(itemId, 1);
				chat.sendDiceMessage(player,
						"Friends Chat channel-mate <col=db3535>" + player.getDisplayName()
								+ "</col> rolled <col=db3535>" + getRandom(lowest, highest) + "</col> on "
								+ diceText(itemId) + " die.");
			}
		}, 1);
	}

	public static int getRandom(int lowest, int highest) {
		Random r = new Random();
		if (lowest > highest) {
			return -1;
		}
		long range = (long) highest - (long) lowest + 1;
		long fraction = (long) (range * r.nextDouble());
		int numberRolled = (int) (fraction + lowest);
		return numberRolled;
	}

	public static String diceText(int id) {
		switch (id) {
		case 15086:
			return "a six-sided";
		case 15088:
			return "two six-sided";
		case 15090:
			return "an eight-sided";
		case 15092:
			return "a ten-sided";
		case 15094:
			return "a twelve-sided";
		case 15096:
			return "a a twenty-sided";
		case 15098:
			return "the percentile";
		case 15100:
			return "a four-sided";
		}
		return "";
	}

	public static int getGraphic(int id) {
		return 0;
	}
}
