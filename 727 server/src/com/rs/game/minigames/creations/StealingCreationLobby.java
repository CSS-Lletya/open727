package com.rs.game.minigames.creations;

import com.rs.game.WorldTile;
import com.rs.game.player.controlers.Controler;

/**
 * @author Richard
 * @author Khaled
 *
 */
public class StealingCreationLobby extends Controler {

	@Override
	public void start() {
		if ((boolean) getArguments()[0])
			StealingCreation.getRedTeam().add(player);
		else
			StealingCreation.getRedTeam().add(player);
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(804, player.getInterfaceManager().hasRezizableScreen() ? 11 : 27);// TODO find correct one
		StealingCreation.updateInterfaces();
	}

	// TODO object click for exit

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		player.getControlerManager().forceStop();
	}

	@Override
	public void forceClose() {
		if ((boolean) getArguments()[0])
			StealingCreation.getRedTeam().remove(player);
		else
			StealingCreation.getRedTeam().remove(player);
		StealingCreation.updateInterfaces();
	}
}
