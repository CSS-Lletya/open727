package com.rs.game.player.content.agility;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;

public class Agility {

	public static boolean hasLevel(Player player, int level) {
		if (player.getSkills().getLevel(Skills.AGILITY) < level) {
			player.getPackets().sendGameMessage("You need an agility level of " + level + " to use this obstacle.",
					true);
			return false;
		}
		return true;
	}

}
