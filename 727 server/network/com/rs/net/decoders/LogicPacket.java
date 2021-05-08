package com.rs.net.decoders;

import com.rs.cache.io.InputStream;

public class LogicPacket {

	private byte id;
	byte[] data;

	public LogicPacket(byte id, int size, InputStream stream) {
		this.id = id;
		data = new byte[size];
		stream.getBytes(data, 0, size);
	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

}
