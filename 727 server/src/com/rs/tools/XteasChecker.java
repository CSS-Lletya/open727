package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.utils.MapArchiveKeys;

public class XteasChecker {

	public static final void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MapArchiveKeys.loadUnpackedKeys();
		for (int i = 0; i < 25000; i++) {
			checkMapDataIfWorks(i);
		}
	}

	/*
	 * public static int[] checkMapData(int regionId) { int absX = (regionId >> 8) * 64; int absY = (regionId & 0xff) * 64; int containerId = Cache.getCacheFileManagers()[5].getContainerId("l"+ ((absX >> 3) / 8) + "_" + ((absY >> 3) / 8)); if(containerId == -1) { return new int[] {0,0,0,0}; } byte[] containerData = Cache.getCacheFileManagers()[5].getCacheFile ().getContainerData(containerId); if(containerData == null) return null; byte[] originalData = new byte[containerData.length]; System.arraycopy(containerData, 0, originalData, 0, originalData.length); Stream stream = new Stream(originalData); int compression = stream.getUByte(); if(compression == 2) { int containerSize = stream.getInt(); if(containerSize > 0 && containerSize < 5000000) { int decompressedSize = stream.getInt(); if(decompressedSize > 0 && decompressedSize < 5000000) { if (~stream.payload[stream.offset] == -32 && stream.payload[stream.offset + 1] == -117) { return new int[] {0,0,0,0}; }
	 * 
	 * } } }
	 * 
	 * int tryId = 0; for(int key1 = Integer.MIN_VALUE; key1 < Integer.MAX_VALUE; key1++) { for(int key2 = Integer.MIN_VALUE; key2 < Integer.MAX_VALUE; key2++) { for(int key3 = Integer.MIN_VALUE; key3 < Integer.MAX_VALUE; key3++) { for(int key4 = Integer.MIN_VALUE; key4 < Integer.MAX_VALUE; key4++) { System.arraycopy(originalData, 0, containerData, 0, originalData.length); stream = new Stream(containerData); int[] xteas = new int[] {key1, key2, key3, key4}; tryId++; if(tryId == 1000000) { System.out.println("try: "+Arrays.toString(xteas)); tryId = 0; } stream.decodeXTEA(xteas , 5, stream.payload.length); compression = stream.getUByte(); int containerSize = stream.getInt(); if(containerSize <= 0 || containerSize >= 5000000) continue; int decompressedSize = stream.getInt(); if(decompressedSize <= 0 || decompressedSize >= 5000000) continue; if (~stream.payload[stream.offset] != -32 || stream.payload[stream.offset + 1] != -117) continue; return xteas; } } } } return null;
	 * 
	 * }
	 */

	@SuppressWarnings("unused")
	public static void checkMapDataIfWorks(int id) {
		int[] xtea_keys = MapArchiveKeys.getMapKeys(id);
		if (xtea_keys == null || xtea_keys[0] == 0 && xtea_keys[1] == 0 && xtea_keys[2] == 0 && xtea_keys[3] == 0)
			return;
		int absX = (id >> 8) * 64;
		int absY = (id & 0xff) * 64;

		int containerId = Cache.STORE.getIndexes()[5].getArchiveId("l" + ((absX >> 3) / 8) + "_" + ((absY >> 3) / 8));
		if (containerId == -1) {
			return;
		}
		byte[] data = Cache.STORE.getIndexes()[5].getFile(containerId, 0, xtea_keys);
		if (data == null) {
			System.out.println("null. ." + absX + ", " + absY);
			return;
		}
		// System.out.println(absX+", "+absY+" "+id);
		if (xtea_keys == null)
			xtea_keys = new int[4];
		try {
			File file = new File("working/" + id + ".txt");
			if (file.exists())
				file.delete();
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
			bf.write(String.valueOf(xtea_keys[0]));
			bf.newLine();
			bf.flush();
			bf.write(String.valueOf(xtea_keys[1]));
			bf.newLine();
			bf.flush();
			bf.write(String.valueOf(xtea_keys[2]));
			bf.newLine();
			bf.flush();
			bf.write(String.valueOf(xtea_keys[3]));
			bf.newLine();
			bf.flush();
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
