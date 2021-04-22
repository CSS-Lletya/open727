package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

/**
 * Redo this accordingly. You should now 
 * understand how options work.
 * @author Dennis
 *
 */
public class DismissD extends DialogueEventListener {

	public DismissD(Player player) {
		super(player);
	}

	@Override
	public void start() {
		option(player.getPet() != null ? "Free pet?" : "Dismiss Familiar?", "Yes.", 
		() -> {
			if (player.getFamiliar() != null) {
				player.getFamiliar().sendDeath(player);
			} else if (player.getPet() != null) {
				player(9827, "Run along; I'm setting you free.");
				return;
			}
			System.out.println("option 1");
		},
		
		"No.", 
		() -> {
//			player.getPetManager().setNpcId(-1);
//			player.getPetManager().setItemId(-1);
//			player.getPetManager().removeDetails(player.getPet().getItemId());
//			player.getPet().switchOrb(false);
//			player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
//			player.getPackets().sendAccessMask(747, 17, 0, 0, 0);
//			player.getPet().finish();
//			player.setPet(null);
			player.getPackets().sendGameMessage("Your pet runs off until it's out of sight.");
		});
	}
}
