package com.rs.game.minigames.pest;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.controlers.Controler;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class PestControlGame extends Controler {

	private PestControl control;

	@Override
	public void start() {
		control = (PestControl) getArguments()[0];
		player.getTemporaryAttributtes().put("pestPoints", 0);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				sendInterfaces();
			}
		}, 8);
	}

	@Override
	public void sendInterfaces() {
		updatePestPoints();
		player.getPackets().sendIComponentText(408, 1, "" + control.getKnight().getHitpoints());
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 408);
	}

	private void updatePestPoints() {
		player.getPackets().sendIComponentText(408, 11, "" + player.getTemporaryAttributtes().get("pestPoints"));
	}

	@Override
	public void forceClose() {
		player.closeInterfaces();
		player.setNextWorldTile(PestControl.OUTSIDE_AREA);
		player.reset();
		control.getPlayers().remove(player);
	}

	@Override
	public boolean login() {
		forceClose();
		return true;
	}

	@Override
	public boolean logout() {
		forceClose();
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You can't leave the pest control area like this.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You can't leave the pest control area like this.");
		return false;
	}

	@Override
	public boolean keepCombating(Entity target) {
		for (Hit hit : player.getNextHits()) {
			if (hit == null)
				return false;
			player.getTemporaryAttributtes().put("pestPoints", hit.getDamage());
			updatePestPoints();
		}
		return true;
	}

	/**
	 * 
	 * @param player The player.
	 * @param object The object.
	 * @return {@code true} if the object action got handled, {@code false} if not.
	 */
	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		}
		return true;
	}
}
