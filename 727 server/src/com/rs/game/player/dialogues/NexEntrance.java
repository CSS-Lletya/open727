package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.minigames.ZarosGodwars;

public final class NexEntrance extends Dialogue {

	@Override
	public void start() {
		sendDialogue("The room beyond this point is a prison!", "There is no way out other than death or teleport.",
				"Only those who endure dangerous encounters should proceed.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue("There are currently " + ZarosGodwars.getPlayersCount()
					+ " people fighting.<br>Do you wish to join them?", "Climb down.", "Stay here.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				player.setNextWorldTile(new WorldTile(2911, 5204, 0));
				player.getControlerManager().startControler("ZGDControler");
			}
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
