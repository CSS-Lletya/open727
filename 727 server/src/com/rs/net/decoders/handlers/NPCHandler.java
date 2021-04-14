package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.dialogue.container.Test_D;
import com.rs.game.event.EventListener.ClickOption;
import com.rs.game.event.EventManager;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.route.strategy.RouteEvent;
import com.rs.io.InputStream;
import com.rs.utils.Logger;

public class NPCHandler {

	public static void handleExamine(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		if (forceRun)
			player.setRun(forceRun);
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		if (player.getRights().isStaff()) {
			player.getPackets().sendGameMessage("NPC - [id=" + npc.getId() + ", loc=[" + npc.getX() + ", " + npc.getY()
					+ ", " + npc.getPlane() + "]].");
		}
		player.getPackets().sendNPCMessage(0, npc, "It's a " + npc.getDefinitions().name + ".");
		
		if (Settings.DEBUG)
			Logger.log("NPCHandler", "examined npc: " + npcIndex + ", " + npc.getId());
	}

	public static void handleOption1(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		final int npcId = npc.getId();
		player.setRouteEvent(new RouteEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				
				if (npcId == 6537) { // This is a custom Dialogue system. Needs to be finished though.
					player.dialog(new Test_D(player, npc));
					return;
				}
				if (EventManager.get().handleNPCClick(player, npc, ClickOption.FIRST)) {
					return;
				}
				// Anything we dont want to rotate towards us goes above
				player.faceEntity(npc);
				if (Settings.DEBUG)
					System.out.println("cliked 1 at npc id : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY()
							+ ", " + npc.getPlane());
			}

		}, npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")));
	}

	public static void handleOption2(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		
		player.setRouteEvent(new RouteEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				// Anything we dont want to rotate towards us goes above
				player.faceEntity(npc);
				if (EventManager.get().handleNPCClick(player, npc, ClickOption.SECOND)) {
					return;
				}
			}
		}, npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")));
	}

	public static void handleOption3(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setRun(forceRun);
		player.setRouteEvent(new RouteEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (EventManager.get().handleNPCClick(player, npc, ClickOption.THIRD)) {
					return;
				}
				// Anything we dont want to rotate towards us goes above
				player.faceEntity(npc);

			}
		}, npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")));
		if (Settings.DEBUG)
			System.out.println("cliked 3 at npc id : " + npc.getId() + ", " + npc.getX() + ", " + npc.getY() + ", "
					+ npc.getPlane());
	}
}
