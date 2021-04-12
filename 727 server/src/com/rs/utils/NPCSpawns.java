package com.rs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

public final class NPCSpawns {

	private static final Object lock = new Object();

	public static boolean addSpawn(String username, int id, WorldTile tile) throws Throwable {
		synchronized (lock) {
			File file = new File("data/npcs/spawns.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write("// " + NPCDefinitions.getNPCDefinitions(id).name + ", "
					+ NPCDefinitions.getNPCDefinitions(id).combatLevel + ", added by: " + username);
			writer.newLine();
			writer.flush();
			writer.write(id + " - " + tile.getX() + " " + tile.getY() + " " + tile.getPlane());
			writer.newLine();
			writer.flush();
			World.spawnNPC(id, tile, -1, true);
			return true;
		}

	}

	public static boolean removeSpawn(NPC npc) throws Throwable {
		synchronized (lock) {
			List<String> page = new ArrayList<>();
			File file = new File("data/npcs/spawns.txt");
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			boolean removed = false;
			int id = npc.getId();
			WorldTile tile = npc.getRespawnTile();
			while ((line = in.readLine()) != null) {
				if (line.equals(id + " - " + tile.getX() + " " + tile.getY() + " " + tile.getPlane())) {
					page.remove(page.get(page.size() - 1)); // description
					removed = true;
					continue;
				}
				page.add(line);
			}
			if (!removed)
				return false;
			file.delete();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (String l : page) {
				writer.write(l);
				writer.newLine();
				writer.flush();
			}
			npc.finish();
			return true;
		}
	}

	public static final void init() {
		if (!new File("data/npcs/packedSpawns").exists())
			packNPCSpawns();
	}

	private static final void packNPCSpawns() {
		Logger.log("NPCSpawns", "Packing npc spawns...");
		if (!new File("data/npcs/packedSpawns").mkdir())
			throw new RuntimeException("Couldn't create packedSpawns directory.");
		try {
			BufferedReader in = new BufferedReader(new FileReader("data/npcs/unpackedSpawnsList.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid NPC Spawn line: " + line);
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 5);
				if (splitedLine2.length != 3 && splitedLine2.length != 5)
					throw new RuntimeException("Invalid NPC Spawn line: " + line);
				WorldTile tile = new WorldTile(Integer.parseInt(splitedLine2[0]), Integer.parseInt(splitedLine2[1]),
						Integer.parseInt(splitedLine2[2]));
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				if (splitedLine2.length == 5) {
					mapAreaNameHash = Utils.getNameHash(splitedLine2[3]);
					canBeAttackFromOutOfArea = Boolean.parseBoolean(splitedLine2[4]);
				}
				addNPCSpawn(npcId, tile.getRegionId(), tile, mapAreaNameHash, canBeAttackFromOutOfArea);
			}
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadNPCSpawns(int regionId) {
		File file = new File("data/npcs/packedSpawns/" + regionId + ".ns");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int npcId = buffer.getShort() & 0xffff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				boolean hashExtraInformation = buffer.get() == 1;
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				if (hashExtraInformation) {
					mapAreaNameHash = buffer.getInt();
					canBeAttackFromOutOfArea = buffer.get() == 1;
				}
				World.spawnNPC(npcId, new WorldTile(x, y, plane), mapAreaNameHash, canBeAttackFromOutOfArea);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static final void addNPCSpawn(int npcId, int regionId, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		try {
			DataOutputStream out = new DataOutputStream(
					new FileOutputStream("data/npcs/packedSpawns/" + regionId + ".ns", true));
			out.writeShort(npcId);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(mapAreaNameHash != -1);
			if (mapAreaNameHash != -1) {
				out.writeInt(mapAreaNameHash);
				out.writeBoolean(canBeAttackFromOutOfArea);
			}
			out.flush();
			out.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private NPCSpawns() {
	}
}
