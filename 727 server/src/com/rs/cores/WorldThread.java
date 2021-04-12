package com.rs.cores;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class WorldThread extends Thread {

	protected WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}

	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			long currentTime = Utils.currentTimeMillis();
			try {
				// long debug = Utils.currentTimeMillis();
				WorldTasksManager.processTasks();
				// System.out.print("TASKS: "+(Utils.currentTimeMillis()-debug));
				// debug = Utils.currentTimeMillis();
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished())
						continue;
					if (currentTime - player.getPacketsDecoderPing() > Settings.MAX_PACKETS_DECODER_PING_DELAY
							&& player.getSession().getChannel().isOpen())
						player.getSession().getChannel().close();
					player.processEntity();
				}
				// System.out.print(" ,PLAYERS PROCESS: "+(Utils.currentTimeMillis()-debug));
				// debug = Utils.currentTimeMillis();
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished())
						continue;
					npc.processEntity();
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
			try {
				// System.out.print(" ,NPCS PROCESS: "+(Utils.currentTimeMillis()-debug));
				// debug = Utils.currentTimeMillis();

				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished())
						continue;
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
				}
				// System.out.print(" ,PLAYER UPDATE: "+(Utils.currentTimeMillis()-debug)+", "+World.getPlayers().size()+", "+World.getNPCs().size());
				// debug = Utils.currentTimeMillis();
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished())
						continue;
					player.resetMasks();
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished())
						continue;
					npc.resetMasks();
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
			// System.out.println(" ,TOTAL: "+(Utils.currentTimeMillis()-currentTime));
			LAST_CYCLE_CTM = Utils.currentTimeMillis();
			long sleepTime = Settings.WORLD_CYCLE_TIME + currentTime - LAST_CYCLE_CTM;
			if (sleepTime <= 0)
				continue;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				Logger.handle(e);
			}
		}
	}

	public static long LAST_CYCLE_CTM;

}
