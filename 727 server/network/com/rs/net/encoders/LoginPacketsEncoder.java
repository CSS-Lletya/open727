package com.rs.net.encoders;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.cache.io.OutputStream;
import com.rs.game.player.Player;
import com.rs.net.Session;

public final class LoginPacketsEncoder extends Encoder {

	public LoginPacketsEncoder(Session connection) {
		super(connection);
	}

	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(0);
		session.write(stream);
	}

	public final void sendClientPacket(int opcode) {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(opcode);
		ChannelFuture future = session.write(stream);
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			session.getChannel().close();
		}
	}

	public final void sendLoginDetails(Player player) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(null, 2);
		stream.writeByte(player.getRights().getValue());
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(1);
		stream.writeByte(0);
		stream.writeShort(player.getIndex());
		stream.writeByte(1);
		stream.write24BitInteger(0);
		stream.writeByte(1); // is member world
		stream.writeString(player.getDisplayName());
		stream.endPacketVarByte();
		session.write(stream);
	}
}
