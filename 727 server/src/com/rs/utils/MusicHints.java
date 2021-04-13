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

public class MusicHints {

	private final static HashMap<Integer, String> musicHints = new HashMap<Integer, String>();
	private final static String PACKED_PATH = "data/musics/packedMusicHints.mh";
	private final static String UNPACKED_PATH = "data/musics/unpackedMusicHints.txt";

	public static final void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedItemExamines();
		else
			loadUnpackedItemExamines();
	}

	public static final String getHint(int musicId) {
		String hint = musicHints.get(musicId);
		if (hint == null)
			return "somewhere.";
		return hint;
	}

	private static void loadPackedItemExamines() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining())
				musicHints.put(buffer.getShort() & 0xffff, readAlexString(buffer));
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static void loadUnpackedItemExamines() {
		Logger.log("MusicHints", "Packing music hints...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(UNPACKED_PATH));
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length < 2)
					throw new RuntimeException("Invalid list for music hints line: " + line);
				int musicId = Integer.valueOf(splitedLine[0]);
				if (splitedLine[1].length() > 255)
					continue;
				out.writeShort(musicId);
				writeAlexString(out, splitedLine[1]);
				musicHints.put(musicId, splitedLine[1]);
				in.close();
				out.flush();
				out.close();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String readAlexString(ByteBuffer buffer) {
		int count = buffer.get() & 0xff;
		byte[] bytes = new byte[count];
		buffer.get(bytes, 0, count);
		return new String(bytes);
	}

	public static void writeAlexString(DataOutputStream out, String string) throws IOException {
		byte[] bytes = string.getBytes();
		out.writeByte(bytes.length);
		out.write(bytes);
	}
}
