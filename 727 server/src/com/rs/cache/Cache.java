package com.rs.cache;

import java.io.IOException;

import com.alex.io.OutputStream;
import com.alex.store.Store;
import com.alex.util.whirlpool.Whirlpool;
import com.rs.Settings;
import com.rs.utils.Utils;

public final class Cache {

	public static Store STORE;

	private Cache() {

	}

	public static void init() throws IOException {
		STORE = new Store("data/cache/");
	}

	public static final byte[] generateUkeysFile() {
		OutputStream stream = new OutputStream();
		stream.writeByte(STORE.getIndexes().length);
		for (int index = 0; index < STORE.getIndexes().length; index++) {
			if (STORE.getIndexes()[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				stream.writeBytes(new byte[64]);
				continue;
			}
			stream.writeInt(STORE.getIndexes()[index].getCRC());
			stream.writeInt(STORE.getIndexes()[index].getTable().getRevision());
			stream.writeBytes(STORE.getIndexes()[index].getWhirlpool());
		}
		byte[] archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archive, 0, archive.length);
		OutputStream hashStream = new OutputStream(65);
		hashStream.writeByte(0);
		hashStream.writeBytes(Whirlpool.getHash(archive, 0, archive.length));
		byte[] hash = new byte[hashStream.getOffset()];
		hashStream.setOffset(0);
		hashStream.getBytes(hash, 0, hash.length);
		hash = Utils.cryptRSA(hash, Settings.GRAB_SERVER_PRIVATE_EXPONENT, Settings.GRAB_SERVER_MODULUS);
		stream.writeBytes(hash);
		archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archive, 0, archive.length);
		return archive;
	}

}
