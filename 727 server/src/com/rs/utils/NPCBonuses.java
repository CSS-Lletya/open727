package com.rs.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public final class NPCBonuses {
	private final static HashMap<Integer, int[]> npcBonuses = new HashMap<Integer, int[]>();
	private static final String PACKED_PATH = "data/npcs/packedBonuses.nb";

	public static void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedNPCBonuses();
		else
			loadUnpackedNPCBonuses();
	}

	public static int[] getBonuses(int id) {
		return npcBonuses.get(id);
	}

	private static void loadUnpackedNPCBonuses() {
		Logger.log("NPCBonuses", "Packing npc bonuses...");
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			BufferedReader in = new BufferedReader(new FileReader("data/npcs/unpackedBonuses.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid NPC Bonuses line: " + line);
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 10);
				if (splitedLine2.length != 10)
					throw new RuntimeException("Invalid NPC Bonuses line: " + line);
				int[] bonuses = new int[10];
				out.writeShort(npcId);
				for (int i = 0; i < bonuses.length; i++) {
					bonuses[i] = Integer.parseInt(splitedLine2[i]);
					out.writeShort(bonuses[i]);
				}
				npcBonuses.put(npcId, bonuses);
				in.close();
				out.close();
			}
			
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static void loadPackedNPCBonuses() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int npcId = buffer.getShort() & 0xffff;
				int[] bonuses = new int[10];
				for (int i = 0; i < bonuses.length; i++)
					bonuses[i] = buffer.getShort();
				npcBonuses.put(npcId, bonuses);
			}
			channel.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private NPCBonuses() {

	}
}
