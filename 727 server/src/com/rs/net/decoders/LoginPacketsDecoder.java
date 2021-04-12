package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.Session;
import com.rs.utils.AntiFlood;
import com.rs.utils.Encrypt;
import com.rs.utils.IsaacKeyPair;
import com.rs.utils.Logger;
import com.rs.utils.MachineInformation;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public final class LoginPacketsDecoder extends Decoder {

	private static final boolean CHANGED = true;

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
		int displayMode = stream.readUnsignedByte();
		int screenWidth = stream.readUnsignedShort();
		int screenHeight = stream.readUnsignedShort();
		int unknown2 = stream.readUnsignedByte();
		stream.skip(24); // 24bytes directly from a file, no idea whats there
		String settings = stream.readString();
		int affid = stream.readInt();
		stream.skip(stream.readUnsignedByte()); // useless settings
		/*
		 * if (stream.readUnsignedByte() != 6) { //personal data start session.getLoginPackets().sendClientPacket(10); return; } int os =
		 * stream.readUnsignedByte(); boolean x64Arch = stream.readUnsignedByte() == 1; int osVersion = stream.readUnsignedByte(); int osVendor =
		 * stream.readUnsignedByte(); int javaVersion = stream.readUnsignedByte(); int javaVersionBuild = stream.readUnsignedByte(); int javaVersionBuild2 =
		 * stream.readUnsignedByte(); boolean hasApplet = stream.readUnsignedByte() == 1; int heap = stream.readUnsignedShort(); int availableProcessors =
		 * stream.readUnsignedByte(); int ram = stream.read24BitInt(); int cpuClockFrequency = stream.readUnsignedShort(); int cpuInfo3 =
		 * stream.readUnsignedByte(); int cpuInfo4 = stream.readUnsignedByte(); int cpuInfo5 = stream.readUnsignedByte(); String empty1 =
		 * stream.readJagString(); String empty2 = stream.readJagString(); String empty3 = stream.readJagString(); String empty4 = stream.readJagString(); int
		 * unused1 = stream.readUnsignedByte(); int unused2 = stream.readUnsignedShort(); MachineInformation mInformation = new MachineInformation(os,
		 * x64Arch, osVersion, osVendor, javaVersion, javaVersionBuild, javaVersionBuild2, hasApplet, heap, availableProcessors, ram, cpuClockFrequency,
		 * cpuInfo3, cpuInfo4, cpuInfo5);
		 */
		MachineInformation mInformation = null;
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
		// for (int index = 0; index < Cache.STORE.getIndexes().length; index++) {
		// int crc = Cache.STORE.getIndexes()[index] == null ? -1011863738 : Cache.STORE.getIndexes()[index].getCRC();
		// int receivedCRC = stream.readInt();
		// if (crc != receivedCRC && index < 32) {
		//
		// Logger.log(this, "Invalid CRC at index: "+index+", "+receivedCRC+", "+crc);
		//
		// session.getLoginPackets().sendClientPacket(6);
		// return;
		// }
		// }
		if (Utils.invalidAccountName(username)) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if (World.getPlayers().size() >= Settings.PLAYERS_LIMIT - 10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if (World.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		if (AntiFlood.getSessionsIP(session.getIP()) > 3) {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}
		Player player;
		if (CHANGED || !SerializableFilesManager.containsPlayer(username))
			player = new Player(password);
		else {
			player = SerializableFilesManager.loadPlayer(username);
			if (player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!SerializableFilesManager.createBackup(username)) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!password.equals(player.getPassword())) {
				session.getLoginPackets().sendClientPacket(3);
				return;
			}
		}
		if (player.isPermBanned() || player.getBanned() > Utils.currentTimeMillis()) {
			session.getLoginPackets().sendClientPacket(4);
			return;
		}

		player.init(session, username, displayMode, screenWidth, screenHeight, mInformation, new IsaacKeyPair(isaacKeys));
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.start();
	}

}
