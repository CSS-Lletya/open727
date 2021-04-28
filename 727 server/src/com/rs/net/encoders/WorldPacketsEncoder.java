package com.rs.net.encoders;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.Settings;
import com.rs.cache.io.OutputStream;
import com.rs.game.Animation;
import com.rs.game.DynamicRegion;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.HintIcon;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.npc.NPC;
import com.rs.game.player.AccountCreation;
import com.rs.game.player.FriendChatsManager;
import com.rs.game.player.Player;
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.Rights;
import com.rs.net.Session;
import com.rs.utils.Huffman;
import com.rs.utils.MapArchiveKeys;
import com.rs.utils.Utils;

public class WorldPacketsEncoder extends Encoder {

	private Player player;

	public WorldPacketsEncoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	/*   Credits to Sir Tom   */
	public void sendPlayerUnderNPCPriority(boolean priority) {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 151);
		stream.writeByteC(priority ? 1 : 0);
		session.write(stream);
	}

	public void sendHintIcon(HintIcon icon) {
		OutputStream stream = new OutputStream(15);
		stream.writePacket(player, 79);
		stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		if (icon.getTargetType() == 0)
			stream.skip(13);
		else {
			stream.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				stream.writeShort(icon.getTargetIndex());
				stream.writeShort(2500);
				stream.skip(4);
			} else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
				stream.writeByte(icon.getPlane()); // unknown
				stream.writeShort(icon.getCoordX());
				stream.writeShort(icon.getCoordY());
				stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				stream.writeShort(-1);
			}
			stream.writeInt(icon.getModelId());
		}
		session.write(stream);
	}

	public void sendCameraShake(int slotId, int b, int c, int d, int e) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 71);
		stream.write128Byte(b);
		stream.writeShort(d);
		stream.writeByteC(slotId);
		stream.writeByteC(c);
		stream.write128Byte(e);
		session.write(stream);
	}

	public void sendStopCameraShake() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 89);
		session.write(stream);
	}

	public void sendIComponentModel(int interfaceId, int componentId, int modelId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 135);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeIntV2(modelId);
		session.write(stream);
	}

	public void sendHideIComponent(int interfaceId, int componentId, boolean hidden) {
		OutputStream stream = new OutputStream(6);
		stream.writePacket(player, 33);
		stream.writeByte128(hidden ? 1 : 0);
		stream.writeInt(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendRemoveGroundItem(FloorItem item) {
		OutputStream stream = createWorldTileStream(item.getTile());
		int localX = item.getTile().getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = item.getTile().getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 125);
		stream.writeByteC((offsetX << 4) | offsetY);
		stream.writeShortLE128(item.getId());
		session.write(stream);
	}

	public void sendGroundItem(FloorItem item) {
		OutputStream stream = createWorldTileStream(item.getTile());
		int localX = item.getTile().getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = item.getTile().getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 27);
		stream.writeShortLE128(item.getAmount());
		stream.writeShort128(item.getId());
		stream.writeByteC((offsetX << 4) | offsetY);
		session.write(stream);
	}

	public void sendProjectile(Entity receiver, WorldTile startTile, WorldTile endTile, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset, int creatorSize) {
		OutputStream stream = createWorldTileStream(startTile);
		stream.writePacket(player, 143);
		int localX = startTile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = startTile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 3) | offsetY);
		stream.writeByte(endTile.getX() - startTile.getX());
		stream.writeByte(endTile.getY() - startTile.getY());
		stream.writeShort(receiver == null ? 0 : (receiver instanceof Player ? -(receiver.getIndex() + 1) : receiver.getIndex() + 1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		int duration = (Utils.getDistance(startTile.getX(), startTile.getY(), endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1 : (speed / 10))) + delay;
		stream.writeShort(duration);
		stream.writeByte(curve);
		stream.writeShort(creatorSize * 64 + startDistanceOffset * 64);
		session.write(stream);

	}

	/**
	 * Makes the interface and component clickable. Clicking the component ID or slot allows for a packet to be sent incoming
	 * @param interfaceId
	 * @param componentId
	 * @param fromSlot
	 * @param toSlot
	 * @param optionsSlots
	 */
	public void sendAccessMask(int interfaceId, int componentId, int fromSlot, int toSlot, int... optionsSlots) {
		int settingsHash = 0;
		for (int slot : optionsSlots)
			settingsHash |= 2 << slot;
		sendAccessMask(interfaceId, componentId, fromSlot, toSlot, settingsHash);
	}

	/**
	 * Makes the interface and component clickable. Clicking the component ID or slot allows for a packet to be sent incoming
	 * @param interfaceId
	 * @param componentId
	 * @param fromSlot
	 * @param toSlot
	 * @param settingsHash
	 */
	public void sendAccessMask(int interfaceId, int componentId, int fromSlot, int toSlot, int settingsHash) {
		OutputStream stream = new OutputStream(13);
		stream.writePacket(player, 121);
		stream.writeShortLE128(toSlot);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.writeShort(fromSlot);
		stream.writeIntLE(settingsHash);
		session.write(stream);
	}

	public void sendInterFlashScript(int interfaceId, int componentId, int width, int height, int slot) {
		Object[] parameters = new Object[4];
		int index = 0;
		parameters[index++] = slot;
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(143, parameters);
	}

	public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, int width, int height, String... options) {
		sendInterSetItemsOptionsScript(interfaceId, componentId, key, false, width, height, options);
	}

	public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, boolean negativeKey, int width, int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = -1;
		parameters[index++] = 0;
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(negativeKey ? 695 : 150, parameters);
	}

	public void sendInputNameScript(String message) {
		sendRunScript(109, new Object[] { message });
	}

	public void sendInputIntegerScript(String message) {
		sendRunScript(108, new Object[] { message });
	}

	public void sendInputLongTextScript(String message) {
		sendRunScript(110, new Object[] { message });
	}

	public void sendRunScript(int scriptId, Object... params) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 99);
		String parameterTypes = "";
		if (params != null) {
			for (int count = params.length - 1; count >= 0; count--) {
				if (params[count] instanceof String)
					parameterTypes += "s"; // string
				else
					parameterTypes += "i"; // integer
			}
		}
		stream.writeString(parameterTypes);
		if (params != null) {
			int index = 0;
			for (int count = parameterTypes.length() - 1; count >= 0; count--) {
				if (parameterTypes.charAt(count) == 's')
					stream.writeString((String) params[index++]);
				else
					stream.writeInt((Integer) params[index++]);
			}
		}
		stream.writeInt(scriptId);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendGlobalConfig(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendGlobalConfig2(id, value);
		else
			sendGlobalConfig1(id, value);
	}

	public void sendGlobalConfig1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 116);
		stream.write128Byte(value);
		stream.writeShortLE(id);
		session.write(stream);
	}

	public void sendGlobalConfig2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 12);
		stream.writeShortLE(id);
		stream.writeIntV2(value);
		session.write(stream);
	}

	public void sendConfig(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendConfig2(id, value);
		else
			sendConfig1(id, value);
	}

	public void sendConfigByFile(int fileId, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendConfigByFile2(fileId, value);
		else
			sendConfigByFile1(fileId, value);
	}

	public void sendConfig1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 115);
		stream.writeByte(value);
		stream.writeShortLE128(id);
		session.write(stream);
	}

	public void sendConfig2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 8);
		stream.writeIntV2(value);
		stream.writeShortLE128(id);
		session.write(stream);
	}

	public void sendConfigByFile1(int fileId, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 68);
		stream.writeShort(fileId);
		stream.writeByte128(value);
		session.write(stream);
	}

	public void sendConfigByFile2(int fileId, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 108);
		stream.writeIntV2(value);
		stream.writeShort128(fileId);
		session.write(stream);
	}

	public void sendRunEnergy() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 64);
		stream.writeByte((int) player.getRunEnergy());
		session.write(stream);
	}

	public void sendIComponentText(int interfaceId, int componentId, String text) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 124);
		stream.writeString(text);
		stream.writeIntV1(interfaceId << 16 | componentId);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendIComponentAnimation(int emoteId, int interfaceId, int componentId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 82);
		stream.writeIntV2(emoteId);
		stream.writeInt(interfaceId << 16 | componentId);
		session.write(stream);
	}

	public void sendItemOnIComponent(int interfaceid, int componentId, int id, int amount) {
		OutputStream stream = new OutputStream(11);
		stream.writePacket(player, 112);
		stream.writeShort(id);
		stream.writeInt(interfaceid << 16 | componentId);
		stream.writeIntV2(amount);
		session.write(stream);
	}

	public void sendEntityOnIComponent(boolean isPlayer, int entityId, int interfaceId, int componentId) {
		if (isPlayer)
			sendPlayerOnIComponent(interfaceId, componentId);
		else
			sendNPCOnIComponent(interfaceId, componentId, entityId);
	}

	public void sendWorldTile(WorldTile tile) {
		session.write(createWorldTileStream(tile));
	}

	public OutputStream createWorldTileStream(WorldTile tile) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 41);
		stream.writeByte128(tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()) >> 3);
		stream.writeByte128(tile.getHeight());
		stream.writeByte128(tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()) >> 3);
		return stream;
	}

	public void sendObjectAnimation(WorldObject object, Animation animation) {
		OutputStream stream = new OutputStream(10);
		stream.writePacket(player, 80);
		stream.writeIntV2(object.getTileHash());
		stream.writeIntLE(animation.getIds()[0]);
		stream.write128Byte((object.getType() << 2) + (object.getRotation() & 0x3));
		session.write(stream);
	}

	public void sendTileMessage(String message, WorldTile tile, int color) {
		sendTileMessage(message, tile, 5000, 255, color);
	}

	public void sendTileMessage(String message, WorldTile tile, int delay, int height, int color) {
		OutputStream stream = createWorldTileStream(tile);
		stream.writePacketVarByte(player, 114);
		stream.skip(1);
		int localX = tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 4) | offsetY);
		stream.writeShort(delay / 30);
		stream.writeByte(height);
		stream.write24BitInteger(color);
		stream.writeString(message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendSpawnedObject(WorldObject object) {
		OutputStream stream = createWorldTileStream(object);
		int localX = object.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = object.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 117);
		stream.writeInt(object.getId());
		stream.write128Byte((offsetX << 4) | offsetY);
		stream.write128Byte((object.getType() << 2) + (object.getRotation() & 0x3));
		session.write(stream);
	}

	public void sendDestroyObject(WorldObject object) {
		OutputStream stream = createWorldTileStream(object);
		int localX = object.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int localY = object.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(player, 69);
		stream.writeByte128((object.getType() << 2) + (object.getRotation() & 0x3));
		stream.writeByte((offsetX << 4) | offsetY);
		session.write(stream);
	}

	public void sendPlayerOnIComponent(int interfaceId, int componentId) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 0);
		stream.writeIntLE(interfaceId << 16 | componentId);
		session.write(stream);

	}

	public void sendNPCOnIComponent(int interfaceId, int componentId, int npcId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 109);
		stream.writeIntLE(interfaceId << 16 | componentId);
		stream.writeIntV1(npcId);
		session.write(stream);

	}

	public void sendFriendsChatChannel() {
		FriendChatsManager manager = player.getCurrentFriendChat();
		OutputStream stream = new OutputStream(manager == null ? 3 : manager.getDataBlock().length + 3);
		stream.writePacketVarShort(player, 127);
		if (manager != null)
			stream.writeBytes(manager.getDataBlock());
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendFriends() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 74);
		for (String username : player.getFriendsIgnores().getFriends()) {
			String displayName;
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 != null)
				displayName = p2.getDisplayName();
			else
				displayName = Utils.formatPlayerNameForDisplay(username);
			player.getPackets().sendFriend(Utils.formatPlayerNameForDisplay(username), displayName, 177, p2 != null && player.getFriendsIgnores().isOnline(p2), false, stream);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendFriend(String username, String displayName, int world, boolean putOnline, boolean warnMessage) {
		Player friend = World.getPlayerByDisplayName(username);
		if (friend == null)
			friend = AccountCreation.loadPlayer(username);

		if (friend == null) {
			return;
		}

		putOnline = putOnline & player.getFriendsIgnores().isOnline(friend);

		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 74);
		sendFriend(username, displayName, world, putOnline, warnMessage, stream);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendFriend(String username, String displayName, int world, boolean putOnline, boolean warnMessage, OutputStream stream) {
		stream.writeByte(warnMessage ? 0 : 1);
		stream.writeString(displayName);
		stream.writeString(displayName.equals(username) ? "" : username);
		stream.writeShort(putOnline ? world : 0);
		stream.writeByte(player.getFriendsIgnores().getRank(Utils.formatPlayerNameForProtocol(username)));
		stream.writeByte(0);// 1 = recruit a friend highlight
		if (putOnline) {
			stream.writeString(Settings.SERVER_NAME);
			stream.writeByte(world == 1118 ? 1 : 0);
			stream.writeInt(0);
		}
	}

	public void sendIgnores() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 97);
		stream.writeByte(player.getFriendsIgnores().getIgnores().size());
		for (String username : player.getFriendsIgnores().getIgnores()) {
			String display;
			Player p2 = World.getPlayerByDisplayName(username);
			if (username != null) {
				if (p2 != null)
					display = p2.getDisplayName();
				else
					display = Utils.formatPlayerNameForDisplay(username);
				String name = Utils.formatPlayerNameForDisplay(username);

				stream.writeString(display.equals(name) ? name : display);
				stream.writeString(display.equals(name) ? "" : name);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendIgnore(String name, String display, boolean updateName) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 138);
		if (name != null) {
			stream.writeByte(0x2);
			stream.writeString(display.equals(name) ? name : display);
			stream.writeString(display.equals(name) ? "" : name);
			stream.endPacketVarByte();
		}
		session.write(stream);
	}

	public void sendPrivateMessage(String username, String message) {
		Player p2 = World.getPlayerByDisplayName(username);
		if (!player.getFriendsIgnores().isOnline(p2)) {
			player.getPackets().sendGameMessage("This player has their privacy mode disabled or is offline.");
			return;
		}

		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 92);
		stream.writeString(username);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarShort();
		session.write(stream);
	}
	
	public void sendGameBarStages() {
		sendConfig(1054, player.getClanStatus());
//		sendConfig(1055, player.getAssistStatus());
		sendConfig(1056, player.getPlayerDetails().isFilterGame() ? 1 : 0);
		sendConfig(2159, player.getFriendsIgnores().getFriendsChatStatus());
		sendOtherGameBarStages();
		sendPrivateGameBarStage();
	}

	public void sendOtherGameBarStages() {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 30);
		stream.writeByteC(player.getTradeStatus());
		stream.writeByte128(player.getPublicStatus());
		session.write(stream);
	}

	public void sendPrivateGameBarStage() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 67);
		stream.writeByte(player.getFriendsIgnores().getPrivateStatus());
		session.write(stream);
	}

	public void receivePrivateMessage(String name, String display, int rights, String message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 10);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display))
			stream.writeString(name);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandom(255));
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void receiveFriendChatMessage(String name, String display, int rights, String chatName, String message) {

		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 25);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display))
			stream.writeString(name);
		stream.writeLong(Utils.stringToLong(chatName));
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandom(255));
		stream.writeByte(rights);
//		if (!player.hasDisabledProfanity())
//			message = Profanity.filter(message);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendDynamicMapRegion(boolean sendLswp) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 51);
		if (sendLswp)
			player.getLocalPlayerUpdate().init(stream);
		int regionX = player.getChunkX();
		int regionY = player.getChunkY();
		stream.writeByteC(player.isForceNextMapLoadRefresh() ? 1 : 0);
		stream.write128Byte(2);
		stream.writeByte128(player.getMapSize());
		stream.writeShort128(regionY);
		stream.writeShort(regionX);
		stream.initBitAccess();
		int mapHash = Settings.MAP_SIZES[player.getMapSize()] >> 4;
		int[] realRegionIds = new int[4 * mapHash * mapHash];
		int realRegionIdsCount = 0;
		for (int plane = 0; plane < 4; plane++) {
			for (int thisRegionX = (regionX - mapHash); thisRegionX <= ((regionX + mapHash)); thisRegionX++) {
				for (int thisRegionY = (regionY - mapHash); thisRegionY <= ((regionY + mapHash)); thisRegionY++) {
					int regionId = (((thisRegionX / 8) << 8) + (thisRegionY / 8));
					Region region = World.getRegions().get(regionId);
					int realRegionX;
					int realRegionY;
					int realPlane;
					int rotation;
					if (region instanceof DynamicRegion) {
						DynamicRegion dynamicRegion = (DynamicRegion) region;
						int[] regionCoords = dynamicRegion.getRegionCoords()[plane][thisRegionX - ((thisRegionX / 8) * 8)][thisRegionY - ((thisRegionY / 8) * 8)];
						realRegionX = regionCoords[0];
						realRegionY = regionCoords[1];
						realPlane = regionCoords[2];
						rotation = regionCoords[3];
					} else {
						realRegionX = thisRegionX;
						realRegionY = thisRegionY;
						realPlane = plane;
						rotation = 0;
					}
					if (realRegionX == 0 || realRegionY == 0)
						stream.writeBits(1, 0);
					else {
						stream.writeBits(1, 1);
						stream.writeBits(26, (rotation << 1) | (realPlane << 24) | (realRegionX << 14) | (realRegionY << 3));
						int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
						boolean found = false;
						for (int index = 0; index < realRegionIdsCount; index++)
							if (realRegionIds[index] == realRegionId) {
								found = true;
								break;
							}
						if (!found)
							realRegionIds[realRegionIdsCount++] = realRegionId;
					}

				}
			}
		}
		stream.finishBitAccess();
		for (int index = 0; index < realRegionIdsCount; index++) {
			int[] xteas = MapArchiveKeys.getMapKeys(realRegionIds[index]);
			if (xteas == null)
				xteas = new int[4];
			for (int keyIndex = 0; keyIndex < 4; keyIndex++)
				stream.writeInt(xteas[keyIndex]);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendMapRegion(boolean sendLswp) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 85);
		if (sendLswp)
			player.getLocalPlayerUpdate().init(stream);
		stream.writeByte(player.getMapSize());
		stream.writeShort(player.getChunkX());
		stream.writeShort(player.getChunkY());
		stream.writeByte(player.isForceNextMapLoadRefresh() ? 1 : 0);
		for (int regionId : player.getMapRegionsIds()) {
			int[] xteas = MapArchiveKeys.getMapKeys(regionId);
			if (xteas == null)
				xteas = new int[4];
			for (int index = 0; index < 4; index++)
				stream.writeInt(xteas[index]);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendCutscene(int id) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 110);
		stream.writeShort(id);
		stream.writeShort(20);
		for (int count = 0; count < 20; count++)
			for (int i = 0; i < 4; i++)
				stream.writeInt(0);
		byte[] appearence = player.getAppearance().getAppeareanceBlocks();
		stream.writeByte(appearence.length);
		stream.writeBytes(appearence);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendWindowsPane(int id, int type) {
		player.getInterfaceManager().setWindowsPane(id);
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 37);
		stream.writeIntV1(0);
		stream.writeIntV1(0);
		stream.writeShort128(id);
		stream.writeByte128(type);
		stream.writeIntLE(0);
		stream.writeIntLE(0);
		session.write(stream);
	}

	public void sendPlayerOption(String option, int slot, boolean top) {
		sendPlayerOption(option, slot, top, -1);
	}

	public void sendPublicMessage(Player p, PublicChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 152);
		stream.writeShort(p.getIndex());
		stream.writeShort(message.getEffects());
		stream.writeByte(p.getMessageIcon());
		String filtered = message.getMessage();

		byte[] chatStr = new byte[250];
		chatStr[0] = (byte) filtered.length();
		int offset = 1 + Huffman.encryptMessage(1, filtered.length(), chatStr, 0, filtered.getBytes());
		stream.writeBytes(chatStr, 0, offset);
		
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendPlayerOption(String option, int slot, boolean top, int cursor) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 111);
		stream.writeString(option);
		stream.writeByte128(slot);
		stream.writeShortLE128(cursor);
		stream.writeByteC(top ? 1 : 0);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendLocalPlayersUpdate() {
		session.write(player.getLocalPlayerUpdate().createPacketAndProcess());
	}

	public void sendLocalNPCsUpdate() {
		session.write(player.getLocalNPCUpdate().createPacketAndProcess());
	}

	public void sendGraphics(Graphics graphics, Object target) {
		OutputStream stream = new OutputStream(13);
		int hash = 0;
		if (target instanceof Player) {
			Player p = (Player) target;
			hash = p.getIndex() & 0xffff | 1 << 28;
		} else if (target instanceof NPC) {
			NPC n = (NPC) target;
			hash = n.getIndex() & 0xffff | 1 << 29;
		} else {
			WorldTile tile = (WorldTile) target;
			hash = tile.getHeight() << 28 | tile.getX() << 14 | tile.getY() & 0x3fff | 1 << 30;
		}
		stream.writePacket(player, 126);
		stream.writeByteC(0);
		stream.writeShort128(graphics.getId());
		stream.writeByteC(graphics.getSettings2Hash());
		stream.writeShort128(graphics.getSpeed());
		stream.writeIntLE(hash);
		stream.writeShortLE(graphics.getHeight());
		session.write(stream);
	}

	public void closeInterface(int windowComponentId) {
		closeInterface(player.getInterfaceManager().getTabWindow(windowComponentId), windowComponentId);
		player.getInterfaceManager().removeTab(windowComponentId);
	}

	public void closeInterface(int windowId, int windowComponentId) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 78);
		stream.writeInt(windowId << 16 | windowComponentId);
		session.write(stream);
	}

	public void sendInterface(boolean nocliped, int windowId, int windowComponentId, int interfaceId) {
		if (!(windowId == 752 && (windowComponentId == 9 || windowComponentId == 12))) {
			if (player.getInterfaceManager().containsInterface(windowComponentId, interfaceId))
				closeInterface(windowComponentId);
			if (!player.getInterfaceManager().addInterface(windowId, windowComponentId, interfaceId)) {
				return;
			}
		}
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(24);
		stream.writePacket(player, 38);
		stream.writeInt(xteas[0]);
		stream.writeIntV2(xteas[2]);
		stream.writeIntV2(xteas[1]);
		stream.writeShortLE128(interfaceId);
		stream.writeIntLE(xteas[3]);
		stream.writeInt(windowId << 16 | windowComponentId);
		stream.writeByteC(nocliped ? 1 : 0);
		// TODO: interface noclip/walkable
		session.write(stream);
	}

	public void sendSystemUpdate(int delay) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(player, 102);
		stream.writeShort((int) (delay * 1.6));
		session.write(stream);
	}

	public void sendUpdateItems(int key, ItemsContainer<Item> items, int... slots) {
		sendUpdateItems(key, items.getItems(), slots);
	}

	public void sendUpdateItems(int key, Item[] items, int... slots) {
		sendUpdateItems(key, key < 0, items, slots);
	}

	public void sendUpdateItems(int key, boolean negativeKey, Item[] items, int... slots) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 4);
		stream.writeShort(key);
		stream.writeByte(negativeKey ? 1 : 0);
		for (int slotId : slots) {
			if (slotId >= items.length)
				continue;
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items[slotId];
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			if (id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if (amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendGlobalString(int id, String string) {
		OutputStream stream = new OutputStream();
		if (string.length() > 253) {
			stream.writePacketVarShort(player, 119);
			stream.writeShortLE(id);
			stream.writeString(string);
			stream.endPacketVarShort();
		} else {
			stream.writePacketVarByte(player, 54);
			stream.writeString(string);
			stream.writeShortLE128(id);
			stream.endPacketVarByte();
		}
		session.write(stream);
	}

	public void sendItems(int key, ItemsContainer<Item> items) {
		sendItems(key, key < 0, items);
	}

	public void sendItems(int key, boolean negativeKey, ItemsContainer<Item> items) {
		sendItems(key, negativeKey, items.getItems());
	}

	public void sendItems(int key, Item[] items) {
		sendItems(key, key < 0, items);
	}

	public void resetItems(int key, boolean negativeKey, int size) {
		sendItems(key, negativeKey, new Item[size]);
	}

	public void sendItems(int key, boolean negativeKey, Item[] items) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 5);
		stream.writeShort(key);
		stream.writeByte(negativeKey ? 1 : 0);
		stream.writeShort(items.length);
		for (int index = 0; index < items.length; index++) {
			Item item = items[index];
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.write128Byte(amount >= 255 ? 255 : amount);
			if (amount >= 255)
				stream.writeIntV2(amount);
			stream.writeShortLE128(id + 1);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendLogout(boolean lobby) {
		OutputStream stream = new OutputStream();
		stream.writePacket(player, lobby ? 9 : 62);
		ChannelFuture future = session.write(stream);
		if (future != null)
			future.addListener(ChannelFutureListener.CLOSE);
		else
			session.getChannel().close();
	}

	public void sendInventoryMessage(int border, int slotId, String message) {
		sendGameMessage(message);
		sendRunScript(948, border, slotId, message);
	}

	public void sendNPCMessage(int border, NPC npc, String message) {
		sendGameMessage(message);
	}

	public void sendGameMessage(String text) {
		sendGameMessage(text, true);
	}

	public void sendGameMessage(String text, boolean filter) {
		sendMessage(filter ? 109 : 0, text, null);
	}

	public void sendPanelBoxMessage(String text) {
		sendMessage(player.getRights() == Rights.ADMINISTRATOR ? 99 : 0, text, null);
	}

	public void sendTradeRequestMessage(Player p) {
		if (player.getFriendsIgnores().getIgnores().contains(p.getUsername())) {
			return;
		}
		sendMessage(100, "wishes to trade with you.", p);
	}

	public void sendClanWarsRequestMessage(Player p) {
		if (player.getFriendsIgnores().getIgnores().contains(p.getUsername())) {
			return;
		}
		sendMessage(101, "wishes to challenge your clan to a clan war.", p);
	}

	public void sendDuelChallengeRequestMessage(Player p, boolean friendly) {
		if (player.getFriendsIgnores().getIgnores().contains(p.getUsername())) {
			return;
		}
		sendMessage(101, "wishes to duel with you(" + (friendly ? "friendly" : "stake") + ").", p);
	}

	public void sendMessage(int type, String text, Player p) {
		int maskData = 0;
		// this is the legit max, go to 249 rip client
		if (text.length() >= 248)
			text = text.substring(0, 248);

		if (p != null) {
			maskData |= 0x1;
			if (p.hasDisplayName())
				maskData |= 0x2;
		}
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 160);
		stream.writeSmart(type);
		stream.writeInt(player.getTileHash()); // junk, not used by client
		stream.writeByte(maskData);
		if ((maskData & 0x1) != 0) {
			stream.writeString(p.getDisplayName());
			if (p.hasDisplayName())
				stream.writeString(Utils.formatPlayerNameForDisplay(p.getUsername()));
		}
		stream.writeString(text);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendSound(int id, int delay, int effectType) {
		if (effectType == 1)
			sendIndex14Sound(id, delay);
		else if (effectType == 2)
			sendIndex15Sound(id, delay);
	}

	public void sendVoice(int id) {
		resetSounds();
		sendSound(id, 0, 2);
	}

	public void resetSounds() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 120);
		session.write(stream);
	}

	public void sendIndex14Sound(int id, int delay) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(player, 136);
		stream.writeShort(id);
		stream.writeByte(1);
		stream.writeShort(delay);
		stream.writeByte(255);
		stream.writeShort(256);
		session.write(stream);
	}

	public void sendIndex15Sound(int id, int delay) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 104);
		stream.writeShort(id);
		stream.writeByte(1);
		stream.writeShort(delay);
		stream.writeByte(255);
		session.write(stream);
	}

	public void sendMusicEffect(int id) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 60);
		stream.write24BitIntegerV2(0);
		stream.write128Byte(255);
		stream.writeShortLE(id);
		session.write(stream);
	}

	public void sendMusic(int id) {
		sendMusic(id, 100, 255);
	}

	public void sendMusic(int id, int delay, int volume) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(player, 63);
		stream.writeByte128(delay);
		stream.writeByte128(volume);
		stream.writeShort128(id);
		session.write(stream);
	}

	public void sendSkillLevel(int skill) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 140);
		stream.writeInt((int) player.getSkills().getXp(skill));
		stream.writeByte(skill);
		stream.writeByte(player.getSkills().getLevel(skill));
		session.write(stream);
	}

	public void sendBlackOut(int area) {
		OutputStream out = new OutputStream(2);
		out.writePacket(player, 154);
		out.writeByte(area);
		session.write(out);
	}

	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ) {
		sendCameraLook(viewLocalX, viewLocalY, viewZ, -1, -1);
	}

	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ, int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 24);
		stream.writeByte(viewLocalX);
		stream.writeShort128(viewZ >> 2);
		stream.writeByte128(viewLocalY);
		stream.writeByteC(speed1);
		stream.write128Byte(speed2);
		session.write(stream);
	}

	public void sendResetCamera() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(player, 66);
		session.write(stream);
	}

	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ) {
		sendCameraPos(moveLocalX, moveLocalY, moveZ, -1, -1);
	}

	public void sendClientConsoleCommand(String command) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 61);
		stream.writeString(command);
		stream.endPacketVarByte();
	}

	public void sendOpenURL(String url) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 16);
		stream.writeByte(0);
		stream.writeString(url);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendSetMouse(String walkHereReplace, int cursor) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 153);
		stream.writeString(walkHereReplace);
		stream.writeShort(cursor);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ, int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 39);
		stream.writeByte(moveLocalY);
		stream.writeByte(moveLocalX);
		stream.write128Byte(speed1);
		stream.writeShortLE(moveZ >> 2);
		stream.writeByte128(speed2);
		session.write(stream);
	}

	public void sendAccessMask(Player player, int min, int max, int interfaceId, int childId, int hash) {
		OutputStream stream = new OutputStream(13);
		stream.writePacket(player, 121);
		stream.writeShortLE128(min);
		stream.writeIntV2(interfaceId << 16 | childId);
		stream.writeShort(max);
		stream.writeIntLE(hash);
		session.write(stream);
	}

	public void sendBlankRunScript(int scriptId) {
		sendRunScript(scriptId, new Object[] {});
	}

	@Deprecated
	public void sendVar(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendVar2(id, value);
		else
			sendVar1(id, value);
	}

	public void sendVar1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(player, 115);
		stream.writeByte(value);
		stream.writeShortLE128(id);
		session.write(stream);
	}

	public void sendVar2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(player, 8);
		stream.writeIntV2(value);
		stream.writeShortLE128(id);
		session.write(stream);
	}

	public void sendUnlockIComponentOptionSlots(int interfaceId, int componentId, int fromSlot, int toSlot, boolean unlockEvent, int... optionsSlots) {
		int settingsHash = unlockEvent ? 1 : 0;
		for (int slot : optionsSlots)
			settingsHash |= 2 << slot;
		sendAccessMask(interfaceId, componentId, fromSlot, toSlot, settingsHash);
	}

	public void sendRunScriptBlank(int scriptId) {
		sendRunScript(scriptId, new Object[] {});
	}

//	public void receiveClanChatMessage(boolean myClan, String display, int rights, ChatMessage message) {
//		OutputStream stream = new OutputStream();
//		stream.writePacketVarByte(player, 81);
//		stream.writeByte(myClan ? 1 : 0);
//		stream.writeString(display);
//		for (int i = 0; i < 5; i++)
//			stream.writeByte(Utils.getRandom(255));
//		stream.writeByte(rights);
//		String message1 = message.getMessage(false);
//		if (!player.hasDisabledProfanity()) {
//			message1 = Profanity.filter(message1);
//		}
//		Huffman.sendEncryptMessage(stream, message1);
//		stream.endPacketVarByte();
//		session.write(stream);
//	}
//
//	public void sendClanChannel(ClansManager manager, boolean myClan) {
//		OutputStream stream = new OutputStream(manager == null ? 4 : manager.getClanChannelDataBlock().length + 4);
//		stream.writePacketVarShort(player, 49);
//		stream.writeByte(myClan ? 1 : 0); // 123
//		if (manager != null)
//			stream.writeBytes(manager.getClanChannelDataBlock());
//		stream.endPacketVarShort();
//		session.write(stream);
//	}
//
//	public void sendClanSettings(ClansManager manager, boolean myClan) {
//		OutputStream stream = new OutputStream(manager == null ? 4 : manager.getClanSettingsDataBlock().length + 4);
//		stream.writePacketVarShort(player, 137); // 1
//		stream.writeByte(myClan ? 1 : 0);
//		if (manager != null)
//			stream.writeBytes(manager.getClanSettingsDataBlock());
//		stream.endPacketVarShort();
//		session.write(stream);
//	}

	public void sendIComponentSprite(int interfaceId, int componentId, int spriteId) {
		OutputStream stream = new OutputStream(8);
		stream.writePacket(player, 135);
		stream.writeInt(spriteId);
		stream.writeIntV2(interfaceId << 16 | componentId);
		//session.write(stream);
	}

	public void sendClanInviteMessage(Player p) {
		sendMessage(117, p.getDisplayName() + " is inviting you to join their clan.", p);
	}

	public void sendAssistanceMessage(Player p) {
		sendMessage(102, "is requesting your assistance.", p);
	}

	public void sendResetMinimapFlag() {
		OutputStream stream = new OutputStream();
		stream.writePacket(player, 70);
		stream.write128Byte(255);
		stream.write128Byte(255);
		session.write(stream);
	}

	public void sendMinimapFlag(int localX, int localY) {
		OutputStream stream = new OutputStream();
		stream.writePacket(player, 70);
		stream.write128Byte(localX);
		stream.write128Byte(localY);
		session.write(stream);
	}

//	public void sendGrandExchangeOffer(Offer offer) {
//		OutputStream stream = new OutputStream(21);
//		stream.writePacket(player, 57);
//		stream.writeByte(offer.getSlot());
//		stream.writeByte(offer.getStage());
//		if (offer.forceRemove())
//			stream.skip(18);
//		else {
//			stream.writeShort(offer.getId());
//			stream.writeInt(offer.getPrice());
//			stream.writeInt(offer.getAmount());
//			stream.writeInt(offer.getTotalAmount());
//			stream.writeInt(offer.getPriceTotal());
//		}
//		session.write(stream);
//	}

	public void sendObjectMessage(int border, int color, WorldObject object, String message) {
		sendGameMessage(message);
		sendGlobalString(306, message);
		sendGlobalConfig(1699, color);
		sendGlobalConfig(1700, border);
		sendGlobalConfig(1695, 1);
		sendObjectInterface(object, true, 746, 0, 1177);
	}

	public void sendObjectInterface(WorldObject object, boolean nocliped, int windowId, int windowComponentId, int interfaceId) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(33);
		stream.writePacket(player, 143);
		stream.writeIntV2(xteas[1]);
		stream.writeByte(nocliped ? 1 : 0);
		stream.writeIntLE(xteas[2]);
		stream.writeIntV1(object.getId());
		stream.writeByte128((object.getType() << 2) | (object.getRotation() & 0x3));
		stream.writeInt((object.getHeight() << 28) | (object.getX() << 14) | object.getY()); // the
		stream.writeIntV2((windowId << 16) | windowComponentId);
		stream.writeShort(interfaceId);
		stream.writeInt(xteas[3]);
		stream.writeInt(xteas[0]);
		session.write(stream);
	}

	public void sendItemsLook() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(player, 160);
		stream.writeByte(player.getPlayerDetails().isOldItemsLook() ? 1 : 0);
		// session.write(stream);
	}
}