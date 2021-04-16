package com.rs.cache.loaders;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.cache.io.InputStream;

public final class ConfigDefinitions {

	private static final ConcurrentHashMap<Integer, ConfigDefinitions> configDefs = new ConcurrentHashMap<Integer, ConfigDefinitions>();

	public int configId;
	public int anInt2021;
	public int anInt2024;

	public static final void main(String[] args) throws IOException {
		Cache.init();
		int SEARCHING_FILE_FOR_CONFIG = 1363;// place it here
		for (int i = 0; i < 20000; i++) {
			ConfigDefinitions cd = getConfigDefinitions(i);
			if (cd.configId == SEARCHING_FILE_FOR_CONFIG)
				System.out.println("file: " + i + ", from bitshift:" + cd.anInt2024 + ", till bitshift: " + cd.anInt2021
						+ ", " + cd.configId);
		}

		ConfigDefinitions cd = getConfigDefinitions(1549);
		System.out.println(" from bitshift:" + cd.anInt2024 + ", till bitshift: " + cd.anInt2021 + ", " + cd.configId);

	}

	public static final ConfigDefinitions getConfigDefinitions(int id) {
		ConfigDefinitions script = configDefs.get(id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.STORE.getIndexes()[22].getFile(id >>> 1416501898, id & 0x3ff);
		script = new ConfigDefinitions();
		if (data != null)
			script.readValueLoop(new InputStream(data));
		configDefs.put(id, script);
		return script;

	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			configId = stream.readUnsignedShort();
			anInt2024 = stream.readUnsignedByte();
			anInt2021 = stream.readUnsignedByte();
		}
	}

	private ConfigDefinitions() {

	}
}
