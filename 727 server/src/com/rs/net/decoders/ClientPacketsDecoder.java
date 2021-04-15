package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.net.Session;
import com.rs.utils.Logger;

public final class ClientPacketsDecoder extends Decoder {

	public ClientPacketsDecoder(Session connection) {
		super(connection);
	}

	@Override
	public final void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		switch (packetId) {
		case 14:
			decodeLogin(stream);
			break;
		case 15:
			decodeGrab(stream);
			break;
		default:
			if (Settings.DEBUG)
				Logger.log(this, "PacketId " + packetId);
			session.getChannel().close();
			break;
		}
	}

	private final void decodeLogin(InputStream stream) {
		if (stream.getRemaining() != 0) {
			session.getChannel().close();
			return;
		}
		session.setDecoder(2);
		session.setEncoder(1);
		session.getLoginPackets().sendStartUpPacket();
	}

	private final void decodeGrab(InputStream stream) {
		int size = stream.readUnsignedByte();
		if (stream.getRemaining() < size) {
			session.getChannel().close();
			return;
		}
		session.setEncoder(0);

		int major = stream.readInt();
		int minor = stream.readInt();

		if (major != Settings.CLIENT_BUILD || minor != Settings.CUSTOM_CLIENT_BUILD) {
			session.setDecoder(-1);
			session.getGrabPackets().sendOutdatedClientPacket();
			return;
		}
		
		String token = stream.readString();
		
		if (!token.equals(Settings.GRAB_SERVER_TOKEN)) {
			session.getChannel().close();
			return;
		}
		session.setDecoder(1);
		session.getGrabPackets().sendStartUpPacket();
	}
}
