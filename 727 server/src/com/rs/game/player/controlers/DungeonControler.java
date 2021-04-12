package com.rs.game.player.controlers;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.dungeoneering.DungeonManager;
import com.rs.game.player.content.dungeoneering.DungeonPartyPlayer;
import com.rs.game.player.content.dungeoneering.DungeonUtils;
import com.rs.game.player.content.dungeoneering.RoomReference;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class DungeonControler extends Controler {

	private DungeonManager dungeon;

	@Override
	public void start() {
		dungeon = (DungeonManager) getArguments()[0];
		setArguments(null); // because arguments save on char and we dont want
							// to save dungmanager
		showDeaths();
	}

	public void showDeaths() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, 945);
	}

	@Override
	public void sendInterfaces() {
		showDeaths();
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					player.setNextWorldTile(dungeon.getHomeTile());
					player.setNextAnimation(new Animation(-1));
					stop();
					DungeonPartyPlayer dp = dungeon.getDPlayer(player);
					if (dp != null)
						dp.increaseDeaths();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		if (dungeon == null || !player.getCombatDefinitions().isDungeonneringSpellBook() || !dungeon.hasStarted())
			return false;
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (dungeon == null || !dungeon.hasStarted())
			return false;
		if (DungeonUtils.isDoor(object.getId())) {
			openDoor(object);
			return false;
		} else if (DungeonUtils.isExit(object.getId())) {
			player.getDialogueManager().startDialogue("DungeonExit", this);
			return false;
		}
		return true;
	}

	public void openDoor(WorldObject object) {
		RoomReference roomReference = dungeon.getCurrentRoomReference(player);
		if (object.getRotation() == 3)
			dungeon.enterRoom(player, roomReference.getX(), roomReference.getY() - 1);
		else if (object.getRotation() == 1)
			dungeon.enterRoom(player, roomReference.getX(), roomReference.getY() + 1);
		else if (object.getRotation() == 2)
			dungeon.enterRoom(player, roomReference.getX() + 1, roomReference.getY());
		else
			dungeon.enterRoom(player, roomReference.getX() - 1, roomReference.getY());
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {
		if (dungeon == null || !dungeon.hasStarted())
			return false;
		return true;
	}

	/*
	 * return process normaly
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (dungeon == null || !dungeon.hasStarted())
			return false;
		if (interfaceId == 950 && componentId == 24) {
			Magic.sendNormalTeleportSpell(player, 0, 0, dungeon.getHomeTile());
			return false;
		}
		return true;
	}

	@Override
	public void forceClose() {
		// TODO HERE
	}

	@Override
	public boolean logout() {
		// TODO HERE
		return true;
	}

}
