package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.game.World;
import com.rs.game.player.AccountCreation;
import com.rs.game.player.Player;
import com.rs.net.Session;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.utils.AntiFlood;
import com.rs.utils.Encrypt;
import com.rs.utils.IsaacKeyPair;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class LoginPacketsDecoder extends Decoder {
	
	public LoginPacketsDecoder(Session session) {
		super(session);
	}

	@Override
	public void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		if (World.exiting_start != 0) {
			session.getLoginPackets().sendClientPacket(14);
			return;
		}
		int packetSize = stream.readUnsignedShort();
		if (packetSize != stream.getRemaining()) {
			session.getChannel().close();
			return;
		}

		int major = stream.readInt();

		if (major != Settings.CLIENT_BUILD) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}

		if (packetId == 16 || packetId == 18) // 16 world login
			decodeWorldLogin(stream);
		else {
			if (Settings.DEBUG)
				Logger.log(this, "PacketId " + packetId);
			session.getChannel().close();
		}
	}

	@SuppressWarnings("unused")
	public void decodeWorldLogin(InputStream stream) {
		int minor = stream.readInt();
		if (minor != Settings.CUSTOM_CLIENT_BUILD) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		boolean unknownEquals14 = stream.readUnsignedByte() == 1;
		int rsaBlockSize = stream.readUnsignedShort();
		if (rsaBlockSize > stream.getRemaining()) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		byte[] data = new byte[rsaBlockSize];
		stream.readBytes(data, 0, rsaBlockSize);
		InputStream rsaStream = new InputStream(Utils.cryptRSA(data, Settings.PRIVATE_EXPONENT, Settings.MODULUS));
		int ratio = rsaStream.readUnsignedByte();
		if (ratio != 10) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		int[] isaacKeys = new int[4];
		for (int i = 0; i < isaacKeys.length; i++)
			isaacKeys[i] = rsaStream.readInt();
		if (rsaStream.readLong() != 0L) { // rsa block check, pass part
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		String password = rsaStream.readString();
		if (password.length() > 30 || password.length() < 3) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		password = Encrypt.encryptSHA1(password);
		String unknown = Utils.longToString(rsaStream.readLong());
		rsaStream.readLong(); // random value
		rsaStream.readLong(); // random value
		stream.decodeXTEA(isaacKeys, stream.getOffset(), stream.getLength());
		boolean stringUsername = stream.readUnsignedByte() == 1; // unknown
		String username = Utils.formatPlayerNameForProtocol(stringUsername ? stream.readString() : Utils.longToString(stream.readLong()));
		byte displayMode = (byte) stream.readUnsignedByte();
		short screenWidth = (short) stream.readUnsignedShort();
		short screenHeight = (short) stream.readUnsignedShort();
		int unknown2 = stream.readUnsignedByte();
		stream.skip(24); // 24bytes directly from a file, no idea whats there
		String settings = stream.readString();
		int affid = stream.readInt();
		stream.skip(stream.readUnsignedByte()); // useless settings
		
		int unknown3 = stream.readInt();
		long userFlow = stream.readLong();
		boolean hasAditionalInformation = stream.readUnsignedByte() == 1;
		if (hasAditionalInformation)
			stream.readString(); // aditionalInformation
		boolean hasJagtheora = stream.readUnsignedByte() == 1;
		boolean js = stream.readUnsignedByte() == 1;
		boolean hc = stream.readUnsignedByte() == 1;
		int unknown4 = stream.readByte();
		int unknown5 = stream.readInt();
		String unknown6 = stream.readString();
		boolean unknown7 = stream.readUnsignedByte() == 1;

		if (Utils.invalidAccountName(username)) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if (World.getPlayers().size() >= Settings.PLAYERS_LIMIT - 10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if (World.containsPlayer(username).isPresent()) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		if (AntiFlood.getSessionsIP(session.getIP()) > 3) {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}
		Player player;
		if (!AccountCreation.exists(username)) {
			player = new Player(password);
		} else {
			player = AccountCreation.loadPlayer(username);
			if (player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!password.equals(player.getPassword())) {
				session.getLoginPackets().sendClientPacket(3);
				return;
			}
		}
		if(HostManager.contains(player.getPlayerDetails().getLastIP(), HostListType.BANNED_IP)) {
			session.getLoginPackets().sendClientPacket(4);
			return;
		}
		
		player.init(session, username, displayMode, screenWidth, screenHeight, new IsaacKeyPair(isaacKeys));
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.start();
		AccountCreation.savePlayer(player);
	}
}